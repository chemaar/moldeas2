/**
 * (c) Copyright 2011 WESO, Computer Science Department,
 * Facultad de Ciencias, University of Oviedo, Oviedo, Asturias, Spain, 33007
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.weso.moldeas.stats;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.weso.moldeas.loader.resources.ExternalizeFilesResourceLoader;
import org.weso.moldeas.loader.resources.FilesResourceLoader;
import org.weso.moldeas.loader.resources.ResourceLoader;
import org.weso.moldeas.to.StatsPairTO;
import org.weso.moldeas.utils.NUTSCodesEncoder;

import au.com.bytecode.opencsv.CSVReader;

public class PPNCreateDataModelNUTS  {

	
	protected static Logger logger = Logger.getLogger(PPNCreateDataModelNUTS.class);
	private static final int MAX_RESOURCES = 100000;


	private Map<StatsPairTO, Integer> pair;
	private String  file = "";

	public PPNCreateDataModelNUTS(){
		this.pair = new HashMap<StatsPairTO, Integer>(MAX_RESOURCES);
	}

	public PPNCreateDataModelNUTS(String file){
		this.pair = new HashMap<StatsPairTO, Integer>(MAX_RESOURCES);
		this.file = file;
	}


	public void execute() throws Exception{
		ResourceLoader loaderNuts = new ExternalizeFilesResourceLoader(
				new String[]{file});
		InputStream dataNuts =loaderNuts.getKnowledgeResources()[0].getKnowledgeSourceData();
		CSVReader readerNuts = new CSVReader(new InputStreamReader(dataNuts),';');
		createTable(readerNuts);	
		
	}


	private void createTable(CSVReader readerNuts) throws IOException {
		//code ppn; [code_nuts]
		String [] nextLine;
		while ((nextLine = readerNuts.readNext()) != null) {
			for(int i = 1; i<nextLine.length;i++){
				if(nextLine[i] != null && !nextLine[i].equals("") ){
					if (nextLine[i]!=null && !nextLine[i].equals("")){
						for(int j = 1; j<nextLine.length;j++){
							if (i != j 	&& nextLine[j]!=null 
									&& !nextLine[j].equals("")){	
								addPair(new StatsPairTO(
										String.valueOf(NUTSCodesEncoder.getNumberCode(nextLine[i])), 
										String.valueOf(NUTSCodesEncoder.getNumberCode(nextLine[j]))));					
							}else if(nextLine.length==3){
								//In case only one NUTS code, keep all information
								addPair(new StatsPairTO(
										String.valueOf(NUTSCodesEncoder.getNumberCode(nextLine[i])), 
										String.valueOf(NUTSCodesEncoder.getNumberCode(nextLine[i]))));
							}
						}
					}
				}		
			}	
		}
	}


	protected void addPair(StatsPairTO stats){
	//	if(!stats.getUserId().equalsIgnoreCase(stats.getItemId())){
			Integer hits = this.pair.get(stats);
			if(hits != null){
				hits++;
				this.pair.put(stats, hits);
			}else{
				this.pair.put(stats, 1);
			}						
	//	}
		
	}

	public Map<StatsPairTO, Integer> getPair() {
		return pair;
	}

	public void setPair(Map<StatsPairTO, Integer> pair) {
		this.pair = pair;
	}



}
