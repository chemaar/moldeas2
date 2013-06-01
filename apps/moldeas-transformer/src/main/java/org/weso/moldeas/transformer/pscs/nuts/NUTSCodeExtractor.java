package org.weso.moldeas.transformer.pscs.nuts;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.weso.moldeas.loader.resources.FilesResourceLoader;
import org.weso.moldeas.loader.resources.ResourceLoader;

import au.com.bytecode.opencsv.CSVReader;

public class NUTSCodeExtractor {

	public static void main(String []args) throws IOException{
		ResourceLoader loader = new FilesResourceLoader(new String[]{"ppn/tender-nut.csv"});
		InputStream data =loader.getKnowledgeResources()[0].getKnowledgeSourceData();
		CSVReader reader = new CSVReader(new InputStreamReader(data),';');
		String [] nextLine = reader.readNext();
		PrintWriter pw = new PrintWriter (new File("/home/chema/tesis/test/sources/NUTS/nuts-from-tender.properties"));
		//skip first line		
		while ((nextLine = reader.readNext()) != null) {
			// nextLine[] is an array of values from the line
			 for(int i = 1; i<nextLine.length;i++){
				if(nextLine[i]!= null && !nextLine[i].equalsIgnoreCase("")){
					pw.println(nextLine[i]+"=");	
				}
			}
		}
		pw.close();
	}
}
