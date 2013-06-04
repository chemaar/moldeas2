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
import org.weso.moldeas.loader.resources.ResourceLoader;
import org.weso.moldeas.to.StatsPairTO;
import org.weso.moldeas.utils.NUTSCodesEncoder;

import au.com.bytecode.opencsv.CSVReader;

public class PPNCreateDataModelYears  {

	public static final String TESIS_TEST_SOURCES = "/home/chema/tesis/test/sources";
	protected static Logger logger = Logger.getLogger(PPNCreateDataModelYears.class);
	private static final int MAX_RESOURCES = 100000;


	private Map<StatsPairTO, Integer> pair;
	private String  year = "";

	public PPNCreateDataModelYears(){
		this.pair = new HashMap<StatsPairTO, Integer>(MAX_RESOURCES);
	}

	public PPNCreateDataModelYears(String year){
		this.pair = new HashMap<StatsPairTO, Integer>(MAX_RESOURCES);
		this.year = year;
	}


	public void execute() throws Exception{
		ResourceLoader loaderNuts = new ExternalizeFilesResourceLoader(
				new String[]{TESIS_TEST_SOURCES+"/tender-nut-"+year+".csv"});
		InputStream dataNuts =loaderNuts.getKnowledgeResources()[0].getKnowledgeSourceData();
		CSVReader readerNuts = new CSVReader(new InputStreamReader(dataNuts),';');
		Map<String,String[]> ppnNuts = createTable(readerNuts);			
		String tendYearFile = TESIS_TEST_SOURCES+"/tender-cpv-"+year+".csv";
		loadAndTransform(tendYearFile,ppnNuts, year);

	}

	private void loadAndTransform(String tendYear, Map<String, String[]> ppnNuts, String year) throws FileNotFoundException, IOException {
		ResourceLoader loader = new ExternalizeFilesResourceLoader(new String[]{tendYear});
		InputStream data =loader.getKnowledgeResources()[0].getKnowledgeSourceData();
		CSVReader reader = new CSVReader(new InputStreamReader(data),';');		
		//code ppn; cpv_code, cpv_code_1
		String [] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			processingLine(nextLine, ppnNuts);			
		}
	}


	private Map<String, String[]> createTable(CSVReader readerNuts) throws IOException {
		//code ppn; code_nuts
		String [] nextLine;
		Map<String,String[]> ppnNuts = new HashMap<String, String[]>(); 
		while ((nextLine = readerNuts.readNext()) != null) {
			String id = nextLine[0];
			List<String> l = new LinkedList<String>();
			for(int i = 1; i<nextLine.length;i++){
				if(nextLine[i] != null && !nextLine[i].equals("") ){
					l.add(nextLine[i]);
				}
			}
			String []nutsCodes = l.toArray(new String[l.size()]);
			ppnNuts.put(id, nutsCodes);
		}
		return ppnNuts;
	}

	public void processingLine(String []line, Map<String, String[]> ppnNuts){
		String rawCode = line[0]; //ID of PPN
		String []cpvCodes = line[1].split(",");
		for(int i = 0; i<cpvCodes.length;i++){
			if (cpvCodes[i]!=null && !cpvCodes[i].equals("")){
				addPair(new StatsPairTO(this.year, cpvCodes[i]));				
				//Tender-nut: 000001-2011;;RS
				//Tender-cpv: 1-2011;75242000
				//Needs to fill with 0s
				for (int k = rawCode.length(); k<11;k++){
					rawCode = "0"+rawCode;
				}
				String []nutsCodes = ppnNuts.get(rawCode);
				if(nutsCodes != null){
					for(int l = 0; l< nutsCodes.length;l++){	
						addPair(new StatsPairTO(this.year, 
								String.valueOf(NUTSCodesEncoder.getNumberCode(nutsCodes[l]))));						
					}
				}
			}
		}

	}

	protected void addPair(StatsPairTO stats){
		if(!stats.getUserId().equalsIgnoreCase(stats.getItemId())){
			Integer hits = this.pair.get(stats);
			if(hits != null){
				hits++;
				this.pair.put(stats, hits);
			}else{
				this.pair.put(stats, 1);
			}						
		}
		
	}

	public Map<StatsPairTO, Integer> getPair() {
		return pair;
	}

	public void setPair(Map<StatsPairTO, Integer> pair) {
		this.pair = pair;
	}



}
