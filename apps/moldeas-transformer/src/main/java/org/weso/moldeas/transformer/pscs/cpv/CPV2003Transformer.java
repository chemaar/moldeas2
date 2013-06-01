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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.weso.moldeas.loader.JenaRDFModelWrapper;
import org.weso.moldeas.loader.resources.FilesResourceLoader;
import org.weso.moldeas.loader.resources.ResourceLoader;
import org.weso.moldeas.transformer.pscs.ChainTransformerAdapter;
import org.weso.pscs.utils.PSCConstants;

import au.com.bytecode.opencsv.CSVReader;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;


public class CPV2003Transformer extends ChainTransformerAdapter{


	protected Logger logger = Logger.getLogger(CPV2003Transformer.class);
	public CPV2003Transformer (){
		
	}

	@Override
	protected void execute() throws Exception {
		ResourceLoader loader = new FilesResourceLoader(new String[]{"cpv/cpv-2003-clean.rdf"});
		JenaRDFModelWrapper rdfModel = new JenaRDFModelWrapper(loader);
		this.model = (Model) rdfModel.getModel();
		//Create table codes
		List<String> codes = createTableCodes();		
		ResIterator it = this.model.listResourcesWithProperty(RDF.type);
		this.model.createProperty("http://purl.org/goodrelations/v1#description");
		this.model.createProperty(PSCConstants.SKOS_IN_SCHEME);
		
		int i = 0;
		while(it.hasNext()){
			Resource r = it.next();
			//Adding type
			r.addProperty(RDF.type, model.createResource("http://purl.org/goodrelations/v1#ProductOrServiceModel"));
			//Fixing ids
			Statement id = r.getProperty(DC.identifier);
			Statement subject = r.getProperty(DC.subject);
			String subjectValue = subject.getLiteral().getString();
			String value =id.getLiteral().getString();
			r.removeAll(DC.identifier);
			r.removeAll(DC.subject);
			String newValue = getCode(value,codes);
			r.addProperty(DC.identifier, subjectValue);
			r.addProperty(DC.subject, newValue);
			r.addProperty(this.model.getProperty(PSCConstants.SKOS_IN_SCHEME), "http://purl.org/weso/pscs/cpv/2003/ds");
			
			//Adding labels
			StmtIterator iterPrefLabels = model.listStatements(
					    new SimpleSelector(r, model.getProperty(PSCConstants.SKOS_prefLabel), (RDFNode) null));
			List<Literal> literals = new LinkedList<Literal>();
			while(iterPrefLabels.hasNext()){
				Statement prefLabel = iterPrefLabels.next();
				Literal newPrefLabel = model.createLiteral( prefLabel.getString(),prefLabel.getLanguage());
				literals.add(newPrefLabel);	
			}
			for(Literal l:literals){
				r.addLiteral(RDFS.label,l);		
				r.addLiteral(model.getProperty("http://purl.org/goodrelations/v1#description"),l);
			}

			literals.clear();
			literals = null;
			logger.debug("Processing "+r.getURI()+" with subject "+newValue+" total "+i);
			i++;
			
		}
		
		
		
	}

	private List<String> createTableCodes() throws IOException {
		ResourceLoader loaderCsv = new FilesResourceLoader(new String[]{"cpv/cpv-codes-2003.txt"});
		InputStream dataCsv =loaderCsv.getKnowledgeResources()[0].getKnowledgeSourceData();
		CSVReader reader = new CSVReader(new InputStreamReader(dataCsv),' ');
		String [] nextLine;
		List<String> codes = new LinkedList<String>();
		while ((nextLine = reader.readNext()) != null) {
			codes.add(nextLine[0]);
		}
		return codes;
	}

	private String getCode(String code, List<String> codes) {
		int i = 0;
		for (i = 0; i<codes.size() && !codes.get(i).startsWith(code) 
		;i++){
		}
		return (codes.get(i).startsWith(code)?codes.get(i):code);
	}

}
