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

import org.weso.moldeas.loader.resources.FilesResourceLoader;
import org.weso.moldeas.loader.resources.ResourceLoader;
import org.weso.moldeas.transformer.pscs.ChainTransformerAdapter;
import org.weso.moldeas.utils.PrefixManager;
import org.weso.pscs.utils.PSCConstants;

import au.com.bytecode.opencsv.CSVReader;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;


public class CPVTransformer extends ChainTransformerAdapter{
	public static final String HTTP_PURL_ORG_WESO_PSCS_CPV_2008_DS = PrefixManager.getURIPrefix("cpv-2008-ds");

	public CPVTransformer (){
		
	}


	public void execute() throws Exception{
		//1-Load CPV file
		ResourceLoader loader = new FilesResourceLoader(new String[]{"cpv/cpv-2008-csv.csv"});
		InputStream data =loader.getKnowledgeResources()[0].getKnowledgeSourceData();
		CSVReader reader = new CSVReader(new InputStreamReader(data),';');
		String [] nextLine;
		String []languages = null;
		while ((nextLine = reader.readNext()) != null) {
			// nextLine[] is an array of values from the line
			if (languages == null){
				languages = nextLine;
			}else {
				processingLine(nextLine,languages);
			}
		}
	}
	
	public Resource processingLine(String []line, String[] languages){
		String rawId = line[0].replace("\"","");	
		String id = (rawId.indexOf('-')>0)?rawId.substring(0,rawId.indexOf('-')):rawId;
		String uri = PSCConstants.formatId(id);
		String type = getType(id);
	
		Resource cpvConceptResource = model.createResource(uri);
		Resource typeResource = model.createResource(type);
		cpvConceptResource.addProperty(RDF.type, typeResource);
		cpvConceptResource.addProperty(RDF.type, model.createResource("http://purl.org/goodrelations/v1#ProductOrServiceModel"));
		cpvConceptResource.addLiteral(DC.identifier,id);
		cpvConceptResource.addLiteral(DC.subject,rawId);
		cpvConceptResource.addProperty(model.getProperty(PSCConstants.SKOS_IN_SCHEME),
				model.createResource(HTTP_PURL_ORG_WESO_PSCS_CPV_2008_DS));
		for(int i = 1; i<line.length;i++){
			Literal prefLabel = (i < languages.length)?
					model.createLiteral(line[i],languages[i]):
					model.createLiteral(line[i]) ;		
			cpvConceptResource.addLiteral(model.getProperty(PSCConstants.SKOS_prefLabel),prefLabel);
			cpvConceptResource.addLiteral(RDFS.label,prefLabel);
			cpvConceptResource.addLiteral(model.getProperty(CPVTransformerInit.HTTP_PURL_ORG_GOODRELATIONS_V1_DESCRIPTION),prefLabel);
		}
		return cpvConceptResource;
	}


	
	 //4   f(Id)=RDF-type -->
	public String getType(String id){
		String type = PSCConstants.HTTP_PURL_ORG_WESO_CPV_DEF_CATEGORY;
		if(id.length()==8){
			if(id.substring(2, 8).equalsIgnoreCase("000000")){
				type = PSCConstants.HTTP_PURL_ORG_WESO_CPV_DEF_DIVISION;
			}else if (id.substring(3, 8).equalsIgnoreCase("00000")){
				type =PSCConstants.HTTP_PURL_ORG_WESO_CPV_DEF_GROUP;
			}else if (id.substring(4, 8).equalsIgnoreCase("0000")){
				type =PSCConstants.HTTP_PURL_ORG_WESO_CPV_DEF_CLASS;
			}
		}else {
			//AA01-1 
		}
			
		return type;
	}

}
