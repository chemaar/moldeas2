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

import java.util.HashMap;
import java.util.Map;

import org.weso.moldeas.transformer.pscs.ChainTransformerAdapter;
import org.weso.pscs.utils.PSCConstants;

import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;

public class CPVTransformerBroader extends ChainTransformerAdapter {

		
	public void execute(){
		ResIterator it = this.model.listResourcesWithProperty(RDF.type);
		//Speeding up
		Map<String, String> codes = createCodesTable();
		while(it.hasNext()){
			Resource r = it.next();
			Statement property = r.getProperty(DC.identifier);
			if(property != null){
				String id = property.getString();				
				String uriBroader = getBroaderUriClass(id, codes);				
				if(uriBroader != null){
					Resource broader = model.createResource(PSCConstants.formatId(uriBroader));
					r.addProperty(model.getProperty(PSCConstants.SKOS_Broader_Transitive), broader);			
				}
			}
			
		}		
	}
	public String getBroaderUriClass(String id, Map<String, String> codes){
		String broader = null; 
		if(id.length()==8){//8-without - //10 with X-
			if (id.substring(1,8).equals("0000000"))
				return null;
			if (id.substring(2,8).equals("000000")) 
				broader = generateBroader(id.substring(0,1) + "0000000", codes);
			else if (id.substring(3,8).equals("00000"))
				broader = generateBroader(id.substring(0,2) + "000000", codes);
			else if(id.substring(4,8).equals("0000"))
				broader = generateBroader(id.substring(0,3) + "00000", codes);
			else if (id.substring(5,8).equals("000"))
				broader = generateBroader(id.substring(0,4) + "0000", codes);
			else if (id.substring(6,8).equals("00"))
				broader = generateBroader(id.substring(0,5) + "000", codes);
			else if (id.substring(7,8).equals("0"))
				broader = generateBroader(id.substring(0,6) + "00", codes);
		}
		return broader;
	}

	private String generateBroader(String string, Map<String, String> codes) {
		return codes.get(string);
	}

	private Map<String, String> createCodesTable(){
		Map<String, String> codes = new HashMap<String, String>();
		ResIterator it = this.model.listResourcesWithProperty(RDF.type);
		while(it.hasNext()){
			Resource r = it.next();
			Statement p = r.getProperty(DC.identifier); 
			if (p != null) {
				String id = p.getString();
				codes.put(id,id);				
				}			
		}
		return codes;
	}
}
