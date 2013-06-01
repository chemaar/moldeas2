package org.weso.moldeas.transformer.pscs.cpv;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.weso.moldeas.loader.resources.FilesResourceLoader;
import org.weso.moldeas.loader.resources.ResourceLoader;

import au.com.bytecode.opencsv.CSVReader;

@Deprecated
public class CPVTransformerLucene {

	public static void main(String []args) throws IOException{
		//1-Load CPV file
		ResourceLoader loader = new FilesResourceLoader(new String[]{"cpv/cpv-2008-csv.csv"});
		InputStream data =loader.getKnowledgeResources()[0].getKnowledgeSourceData();
		CSVReader reader = new CSVReader(new InputStreamReader(data),';');
		String [] nextLine;
		String []languages = null;
		PrintWriter pw = new PrintWriter("cpv-tags-2008.txt");
		while ((nextLine = reader.readNext()) != null) {
			// nextLine[] is an array of values from the line
			if (languages == null){
				languages = nextLine;
			}else {
				processingLine(nextLine,languages,pw);
			}
		}
		pw.close();
	}
	
	
	public static void processingLine(String []line, String[] languages, PrintWriter pw) throws IOException{
		String rawId = line[0].replace("\"","");	
		String id = (rawId.indexOf('-')>0)?rawId.substring(0,rawId.indexOf('-')):rawId;
		for(int i = 1; i<line.length;i++){
			if(i< languages.length && languages[i].equals("EN")){
				//pw.println(id+";"+line[i]);
				analyze(id,line[i],pw);
			}
		}
			
	}
	
	   private static final Analyzer[] analyzers = new Analyzer[]{
	        //new WhitespaceAnalyzer(),
	       // new SimpleAnalyzer(),
	       // new StopAnalyzer(),
	      //  new StandardAnalyzer(Version.LUCENE_29),
		   
	       // new SnowballAnalyzer("English")
	    };

	
	  private static void analyze(String id, String line, PrintWriter pw) throws IOException {
	       System.out.println("Analzying \"" + line + "\"");
	       pw.println(id+";"+"FULL;" + line);
	        for (int i = 0; i < analyzers.length; i++) {
	            Analyzer analyzer = analyzers[i];
	          //  pw.println("\t" + analyzer.getClass().getName() + ":");
	           // pw.print("\t\t");	            
	            TokenStream stream = analyzer.tokenStream("contents", new StringReader(line));
	            while (true) {
	                Token token = stream.next();
	                if (token == null) break;

	                pw.println(id+";"+analyzer.getClass().getSimpleName()+";" + token.term());
	            }

	        }
	    }
	  
}
