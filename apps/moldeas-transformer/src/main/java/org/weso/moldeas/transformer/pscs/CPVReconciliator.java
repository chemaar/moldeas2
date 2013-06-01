package org.weso.moldeas.transformer.pscs;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexDeletionPolicy;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.KeepOnlyLastCommitDeletionPolicy;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.RAMDirectory;
import org.weso.moldeas.loader.JenaRDFModelWrapper;
import org.weso.moldeas.loader.resources.ExternalizeFilesResourceLoader;
import org.weso.moldeas.loader.resources.ResourceLoader;
import org.weso.pscs.utils.PSCConstants;
import org.weso.transformer.filters.PSCAnalyzer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class CPVReconciliator {

	public static void main(String []args) throws IOException, ParseException{
		String  source = "full-generation-1/cpv/2008/cpv-2008.ttl";
		String [] targetFiles = new String []{
								"full-generation-1/cn/2012/cn-2012.ttl",				
								"full-generation-1/cpa/2008/cpa-2008.ttl",
								"full-generation-1/cpc/2008/cpc-2008.ttl",
								"full-generation-1/isic/v4/isic-v4.ttl",
								"full-generation-1/naics/2007/naics-2007.ttl",
								"full-generation-1/naics/2012/naics-2012.ttl",
								"full-generation-1/sitc/v4/sitc-v4.ttl"
		};

		String [] outputFiles = new String []{
								"full-generation-2/cn/2012/cn-2012.ttl",				
								"full-generation-2/cpa/2008/cpa-2008.ttl",
								"full-generation-2/cpc/2008/cpc-2008.ttl",
								"full-generation-2/isic/v4/isic-v4.ttl",
								"full-generation-2/naics/2007/naics-2007.ttl",
								"full-generation-2/naics/2012/naics-2012.ttl",
								"full-generation-2/sitc/v4/sitc-v4.ttl"
		};


		//Create Lucene Index		
		RAMDirectory idx = new RAMDirectory();
		Analyzer standardAnalyzer = new PSCAnalyzer();
		boolean create = true;
		IndexDeletionPolicy deletionPolicy = 
			new KeepOnlyLastCommitDeletionPolicy(); 
		IndexWriter indexWriter = 
			new IndexWriter(idx,standardAnalyzer,create,
					deletionPolicy,IndexWriter.MaxFieldLength.UNLIMITED);

		//Load labels in lucene index
		ResourceLoader loader = new ExternalizeFilesResourceLoader(new String[]{source});		
		JenaRDFModelWrapper rdfModel = new JenaRDFModelWrapper(loader,"TURTLE");
		Model model = (Model) rdfModel.getModel();		
		ResIterator it = model.listResourcesWithProperty(model.getProperty(PSCConstants.SKOS_prefLabel));
		while (it.hasNext()){
			Resource r = it.next();
			StmtIterator iter = model.listStatements(
					new SimpleSelector(r, model.getProperty(PSCConstants.SKOS_prefLabel), (RDFNode) null) {
						public boolean selects(Statement s)
						{return s.getLiteral().getLanguage().equalsIgnoreCase("en");}
					});	
			while (iter.hasNext()){
				//Index Documents
				Field uriField =
					new Field("uri",r.getURI(),Field.Store.YES,Field.Index.NOT_ANALYZED);
				Field subjectField = 
					new Field("prefLabel",iter.next().getString(),Field.Store.YES,Field.Index.ANALYZED);
				Document doc = new Document();
				doc.add(uriField);
				doc.add(subjectField);
				indexWriter.addDocument(doc);
			}
		}//Resource			

		indexWriter.optimize();
		indexWriter.close();

		//Search in index
		IndexSearcher indexSearcher = new IndexSearcher(idx);		
		//		//4-Create maps: skos:closeMatch
		for(int i = 0; i<outputFiles.length;i++){
			System.out.println("Processing "+targetFiles[i]);
			ResourceLoader targetLoader = new ExternalizeFilesResourceLoader(new String[]{targetFiles[i]});		
			JenaRDFModelWrapper targetModel = new JenaRDFModelWrapper(targetLoader,"TURTLE");
			Model targetRdfModel = (Model) targetModel.getModel();
			targetRdfModel.createProperty(PSCConstants.SKOS_CLOSE_MATCH);
			ResIterator itTarget = targetRdfModel.listResourcesWithProperty(targetRdfModel.getProperty(PSCConstants.SKOS_prefLabel));
			while (itTarget.hasNext()){
				Resource r = itTarget.next();
				StmtIterator iter = targetRdfModel.listStatements(
						new SimpleSelector(r, targetRdfModel.getProperty(PSCConstants.SKOS_prefLabel), (RDFNode) null) {
							public boolean selects(Statement s)
							{return s.getLiteral().getLanguage().equalsIgnoreCase("en");}
						});	
				List<ScoreDoc> mappings = new LinkedList<ScoreDoc>();
				while (iter.hasNext()){
					String prefLabel = iter.next().getString();
					ScoreDoc[] scoreDocs = fetchSearchResults(
							createQueryFromString(CPVMapper.cleanPrefLabel(prefLabel)), indexSearcher, 3);	
					mappings.addAll(Arrays.asList(scoreDocs));
				}
				for(ScoreDoc scoredoc: mappings){
					Document doc = indexSearcher.doc(scoredoc.doc);
					r.addProperty(
							targetRdfModel.getProperty(PSCConstants.SKOS_CLOSE_MATCH),
									targetRdfModel.createResource(doc.getField("uri").stringValue()));
				}
				mappings.clear();
				mappings = null;
			}
			targetRdfModel.write(new PrintWriter(outputFiles[i]),"TURTLE");	
			System.out.println("End Processing "+targetFiles[i]);
		}



	}


	public static Query createQueryFromString(String q) throws ParseException {		
		QueryParser parser = new QueryParser("prefLabel",
				new PSCAnalyzer());
		parser.setDefaultOperator(QueryParser.Operator.OR);
		return parser.parse(q);
	}

	private static ScoreDoc[] fetchSearchResults(Query query, Searcher indexSearcher, int n ){
		try{
			TopScoreDocCollector collector = TopScoreDocCollector.create(n, true);
			indexSearcher.search(query, collector);
			ScoreDoc[] scoreDocs = collector.topDocs().scoreDocs;
			return scoreDocs;
		}catch(IOException e){
			e.printStackTrace();
		}
		return new ScoreDoc[0];

	}

}
