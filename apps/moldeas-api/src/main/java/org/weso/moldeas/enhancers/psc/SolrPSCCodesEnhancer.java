package org.weso.moldeas.enhancers.psc;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
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
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.weso.moldeas.dao.PSCDAO;
import org.weso.moldeas.enhancers.EnhancerAdapter;
import org.weso.moldeas.psc.PSCFacade;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.ScoredPSCTO;
import org.weso.moldeas.utils.ApplicationContextLocator;
import org.weso.transformer.filters.PSCAnalyzer;

public class SolrPSCCodesEnhancer extends EnhancerAdapter{

	public static final String LABEL_INDEX_FIELD = "prefLabel";
	public static final String URI_INDEX_FIELD = "uri";
	public static final int MAX_RESULTS = 1; //Using request.getMaxResults()
	protected PSCDAO dao;
	protected RAMDirectory idx;
	
	public SolrPSCCodesEnhancer(PSCDAO dao) throws IOException{
		this.dao = dao;
		createIndex();
	}

	public SolrPSCCodesEnhancer() throws IOException{
		this.dao = (PSCDAO) ApplicationContextLocator.getApplicationContext().getBean("CPVFileDAO");
		createIndex();
	}

	private void createIndex() throws IOException {
		try{
			this.idx = new RAMDirectory();
			Analyzer standardAnalyzer = new PSCAnalyzer();
			boolean create = true;
			IndexDeletionPolicy deletionPolicy = 
				new KeepOnlyLastCommitDeletionPolicy(); 
			IndexWriter indexWriter = 
				new IndexWriter(idx,standardAnalyzer,create,
						deletionPolicy,IndexWriter.MaxFieldLength.UNLIMITED);
			createIndex(indexWriter);
		}catch (CorruptIndexException e){
			throw new IOException(e);
		}catch (LockObtainFailedException e){
			throw new IOException(e);
		}
	}

	private void createIndex(IndexWriter indexWriter)
	throws CorruptIndexException, IOException {		
		List<PSCTO> pscTOs = dao.getPSCTOs();
		for(PSCTO pscTO:pscTOs){
			Field uriField =
				new Field(URI_INDEX_FIELD,pscTO.getUri(),Field.Store.YES,Field.Index.NOT_ANALYZED);
			Field subjectField = 
				new Field(LABEL_INDEX_FIELD,pscTO.getPrefLabel(),Field.Store.YES,Field.Index.ANALYZED);
			Document doc = new Document();
			doc.add(uriField);
			doc.add(subjectField);
			indexWriter.addDocument(doc);
		}
		indexWriter.optimize();
		indexWriter.close();
	}

	@Override
	protected void execute() throws Exception {
		Set<PSCTO> pscTOs = this.request.getPscCodes();
		IndexSearcher indexSearcher = new IndexSearcher(idx);		
		for(PSCTO pscTO:pscTOs){
			String prefLabel = pscTO.getPrefLabel();
			ScoreDoc[] scoreDocs = fetchSearchResults(
					createQueryFromString(cleanString(prefLabel)), indexSearcher, this.request.getMaxResults());	
			for(ScoreDoc scoredoc: scoreDocs){
				Document doc = indexSearcher.doc(scoredoc.doc);
				PSCTO code = new PSCTO(doc.getField(URI_INDEX_FIELD).stringValue());
				code.setPrefLabel(doc.getField(LABEL_INDEX_FIELD).stringValue());
				ScoredPSCTO scoredPSCTO = new ScoredPSCTO();
				scoredPSCTO.setScore(scoredoc.score);	
				scoredPSCTO.setPscTO(code);
				this.enhancedRequest.getScoredPSCCodes().add(scoredPSCTO );
				
			}
		}

	}

	@Override
	protected void preExecute() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void postExecute() throws Exception {
		// TODO Auto-generated method stub

	}

	public static String cleanString(String q){
		String value = q.replaceAll("-", "");
		value = value.replaceAll("á", "a");
		value = value.replaceAll("é", "e");
		value = value.replaceAll("í", "i");
		value = value.replaceAll("ó", "o");
		value = value.replaceAll("ú", "u");
		value = value.replaceAll("\\W", " ").replaceAll("\\d", "");
		return value;
	}

	public static Query createQueryFromString(String q) throws ParseException {		
		QueryParser parser = new QueryParser(LABEL_INDEX_FIELD, new PSCAnalyzer());
		//new PSCAnalyzer());
		parser.setDefaultOperator(QueryParser.Operator.OR);
		return parser.parse(q);
	}

	protected static ScoreDoc[] fetchSearchResults(Query query, Searcher indexSearcher, int n ){
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
