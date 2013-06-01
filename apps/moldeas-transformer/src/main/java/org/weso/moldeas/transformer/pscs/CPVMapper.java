package org.weso.moldeas.transformer.pscs;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;
import org.weso.moldeas.loader.JenaRDFModelWrapper;
import org.weso.moldeas.loader.resources.FilesResourceLoader;
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

public class CPVMapper {

	public static void main(String []args) throws IOException{
		String [] pscFiles = new String []{				
				"pscs/rdf/CN-2012.ttl",
				"pscs/rdf/CPA-2008.ttl",
				"pscs/rdf/CPC-2008.ttl",
				"pscs/rdf/ISIC-V4.ttl",
				"pscs/rdf/NAICS-2007.ttl",
				"pscs/rdf/NAICS-2012.ttl",
				"pscs/rdf/SITC-V4.ttl",
				"pscs/rdf/CPV-2003.ttl",
				"pscs/rdf/CPV-2008.ttl"
		};
		String [] outputFiles = new String []{
				"cn/2012/cn-2012.ttl",				
				"cpa/2008/cpa-2008.ttl",
				"cpc/2008/cpc-2008.ttl",
				"isic/v4/isic-v4.ttl",
				"naics/2007/naics-2007.ttl",
				"naics/2012/naics-2012.ttl",
				"sitc/v4/sitc-v4.ttl",
				"cpv/2003/cpv-2003.ttl",
				"cpv/2008/cpv-2008.ttl"
		};
	
		
		for(int i = 0; i<outputFiles.length;i++){
			ResourceLoader loader = new FilesResourceLoader(new String[]{pscFiles[i]});
			System.out.println("Processing "+pscFiles[i]);
			JenaRDFModelWrapper rdfModel = new JenaRDFModelWrapper(loader,"TURTLE");
			Model model = (Model) rdfModel.getModel();		
			ResIterator it = model.listResourcesWithProperty(model.getProperty(PSCConstants.SKOS_prefLabel));
			while (it.hasNext()){
				Resource r = it.next();
				List<String> tokens = new LinkedList<String>();
				StmtIterator iter = model.listStatements(
					    new SimpleSelector(r, model.getProperty(PSCConstants.SKOS_prefLabel), (RDFNode) null) {
					        public boolean selects(Statement s)
					            {return s.getLiteral().getLanguage().equalsIgnoreCase("en");}
					    });	
				while (iter.hasNext()){
					String value = iter.next().getString();
					tokens.addAll(analyze(cleanPrefLabel(value), System.out));					
				} //Labels
				
				for (String token : tokens) {
					r.addProperty(model.createProperty(PSCConstants.HTTP_PURL_ORG_WESO_PSCS_DEF_RELATED_MATCH), 
					model.createResource("http://www.productontology.org/id/"+token));
				}
				tokens.clear(); 
				tokens = null;
			}//Resource
			model.write(new PrintWriter("full-generation-1/"+outputFiles[i]),"TURTLE");	
		}
		
	}
	
	   private static final Analyzer[] analyzers = new Analyzer[]{
	        //new WhitespaceAnalyzer(),
	       // new SimpleAnalyzer(),
	       // new StopAnalyzer(),
		   // new  ClassicAnalyzer()
	       //new StandardAnalyzer(Version.LUCENE_29)
		   new PSCAnalyzer()
		   
	      //  new SnowballAnalyzer("English")
	    };

	
	  private static List<String> analyze(String line, PrintStream out) throws IOException {
	       //out.println("Analzying \"" + line + "\"");
	       List <String> tokens = new LinkedList<String>();
	        for (int i = 0; i < analyzers.length; i++) {
	            Analyzer analyzer = analyzers[i];
	          //  pw.println("\t" + analyzer.getClass().getName() + ":");
	           // pw.print("\t\t");	            
	            TokenStream stream = analyzer.tokenStream("contents", new StringReader(line));
	            while (true) {
	                Token token = stream.next();
	                if (token == null) break;
	                tokens.add(token.term().replaceAll("\"", ""));
	              // out.println(analyzer.getClass().getSimpleName()+";" + token.term());
	            }

	        }
	        return tokens;
	  }
	  public static String cleanPrefLabel(String q){
		  String value = q.replaceAll("-", "");
			value = value.replaceAll("á", "a");
			value = value.replaceAll("é", "e");
			value = value.replaceAll("í", "i");
			value = value.replaceAll("ó", "o");
			value = value.replaceAll("ú", "u");
			value = value.replaceAll("\\W", " ").replaceAll("\\d", "");
		return value;
	  }
}
