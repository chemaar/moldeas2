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
package org.weso.moldeas.transformer.pscs.nuts;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.weso.moldeas.loader.resources.FilesResourceLoader;
import org.weso.moldeas.loader.resources.ResourceLoader;
import org.weso.moldeas.transformer.pscs.ChainTransformerAdapter;
import org.weso.moldeas.utils.TransformerConstants;

import au.com.bytecode.opencsv.CSVReader;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class NUTSTransformer extends ChainTransformerAdapter {

	@Override
	protected void execute() throws Exception {
		//http://nuts.psi.enakting.org/id/ES12
		//http://nuts.psi.enakting.org/id/PT183/rdf
		//1-Load CPV file
		ResourceLoader loader = new FilesResourceLoader(new String[]{"nuts/nuts_2008"});
		InputStream data =loader.getKnowledgeResources()[0].getKnowledgeSourceData();
		CSVReader reader = new CSVReader(new InputStreamReader(data),';');
		String [] nextLine = reader.readNext();
		//skip first line		
		while ((nextLine = reader.readNext()) != null) {
			// nextLine[] is an array of values from the line
			if (nextLine[0].length()!=0){	
				this.model.add(loadRDF(nextLine[0]));
//				for (int i = 0; i<nextLine.length;i++){
//					System.out.print(" pos["+i+"]="+nextLine[i]);
//				}
					
	
			}
		}
	}

	private Model loadRDF(String id) {
		String url = TransformerConstants.HTTP_NUTS_PSI_ENAKTING_ORG_ID+id+"/rdf";
		Model rmodel = ModelFactory.createDefaultModel();
		System.out.println("Loading from "+url);
		rmodel.read(url);
		return rmodel;
		//ResourceLoader loader = new URLFilesResourceLoader(new String[]{url});
		//return (Model) new JenaRDFModelWrapper(loader).getModel();
	}

}
