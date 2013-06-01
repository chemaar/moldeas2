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
package org.weso.moldeas.transformer.pscs.cpv;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.weso.moldeas.loader.resources.FilesResourceLoader;
import org.weso.moldeas.loader.resources.ResourceLoader;
import org.weso.moldeas.transformer.pscs.ChainTransformerAdapter;
import org.weso.pscs.utils.PSCConstants;

import au.com.bytecode.opencsv.CSVReader;

import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;

public class CPVTransformerProvenance extends ChainTransformerAdapter {



	protected CSVReader getCVSReader(String [] fileNames, char separator){
		ResourceLoader loader = new FilesResourceLoader(fileNames);
		InputStream data =loader.getKnowledgeResources()[0].getKnowledgeSourceData();
		return new CSVReader(new InputStreamReader(data),separator);
	}
	protected void execute() throws Exception{
		//1-Load codes of CPV 2003 file
		CSVReader codes2003 = getCVSReader(new String[]{"cpv/cpv-codes-2003.txt"},' ');
		CSVReader codesMap = getCVSReader(new String[]{"cpv/cpv-codes-2008-2003.csv"},';');
		Map<String, List<OldCPVConcept>> mappings = createMapTable(codesMap.readAll(),codes2003.readAll());
		ResIterator it = this.model.listResourcesWithProperty(RDF.type);
		//Speeding up
		while(it.hasNext()){
			Resource r = it.next();
			Statement property = r.getProperty(DC.identifier);
			if(property!=null){
				String id = property.getString().split("-")[0];	
				List<OldCPVConcept> mapCodes = mappings.get(id);
				if (mapCodes != null){
					for (OldCPVConcept oldCPVConcept: mapCodes){
						Resource cpv2003 = model.createResource(PSCConstants.formatId2003(oldCPVConcept.getId()));
					 	r.addProperty(model.getProperty(PSCConstants.SKOS_EXACT_MATCH),cpv2003);	
						}
				}
				}			
		}				
		
	}

	
	public Map<String, List<OldCPVConcept>> createMapTable(List<String[]> fmappings, List<String[]> oldCodes){
		String previousCode = "";
		Map<String,List<OldCPVConcept>> mappings = new HashMap<String, List<OldCPVConcept>>();
		for(String[] line: fmappings){
			String currentCode =line[0];
			if (currentCode.equals("")){
				currentCode = previousCode;
			}
			List<OldCPVConcept> list = mappings.get(currentCode);
			if (currentCode.length()<8){
				currentCode = "0"+currentCode;
				//System.out.println("Rewrite code "+currentCode);
			}
			String idOld = line[2];//without - //searchIdOld(oldCodes,line[2]); //with -
			if (idOld.length()<8){
				idOld = "0"+idOld;
				//System.out.println("Rewrite code "+currentCode);
			}
			String descOld = line[3];
			OldCPVConcept old = new OldCPVConcept(idOld, descOld);
			if (list == null){
				list = new LinkedList<CPVTransformerProvenance.OldCPVConcept>();
				list.add(old);
				mappings.put(currentCode, new LinkedList<OldCPVConcept>());								
			}else{
				list.add(old);
			}
			previousCode = currentCode;
		}
		return mappings;
	}

	private String searchIdOld(List<String[]> oldCodes, String code) {
		String tid = code;
		boolean match = false;
		String fullCode = "";
		for(int i = tid.length(); i< 8;i++){
			tid = "0"+tid;
		}
		for (int j = 0; !match && j<oldCodes.size();j++){
			if (oldCodes.get(j)[0].startsWith(tid)){
				match = true;
				fullCode = oldCodes.get(j)[0];
			}
		}
		return fullCode;
	}

	class OldCPVConcept{
		String id;
		String description;
		public OldCPVConcept(){
			
		}
		public OldCPVConcept(String id, String description) {
			super();
			this.id = id;
			this.description = description;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String toString(){
			return "CPV: "+id+" description: "+description;
		}
		
	}
	
}
