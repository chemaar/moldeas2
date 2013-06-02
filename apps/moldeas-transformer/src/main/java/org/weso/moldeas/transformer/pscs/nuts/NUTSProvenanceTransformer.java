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

import org.weso.moldeas.loader.JenaRDFModelWrapper;
import org.weso.moldeas.loader.resources.FilesResourceLoader;
import org.weso.moldeas.loader.resources.ResourceLoader;
import org.weso.moldeas.transformer.pscs.ChainTransformerAdapter;

import com.hp.hpl.jena.rdf.model.Model;

public class NUTSProvenanceTransformer extends ChainTransformerAdapter {

	@Override
	protected void execute() throws Exception {
		ResourceLoader loader = new FilesResourceLoader(new String[]{"nuts/nuts-full-2008.txt"});
		JenaRDFModelWrapper rdfModel = new JenaRDFModelWrapper(loader);
		this.model = (Model) rdfModel.getModel();	
		/**
		 * 0 "Code";
		 * 1 "Name";
		 * 2 "Level";
		 * 3 "country order";
		 * 4 "original sorting order";
		 * 5 "change 2006"; //Enrich
		 * 6 "sorting order 2003";
		 * 7 ;
		 * 8 "NUTS 2003 version >>>"; if there is code -> there is a change of code
		 * 9 ;
		 * 10 "Code 2003";
		 * 11 "Name";
		 * 12 "Level";
		 * 13 "country order";
		 * 14 "original sorting order"
		 */
	
	}

	

}