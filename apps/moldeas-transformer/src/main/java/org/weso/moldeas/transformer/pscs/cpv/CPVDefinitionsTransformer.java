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

import org.weso.moldeas.transformer.pscs.ChainTransformerAdapter;
import org.weso.pscs.utils.PSCConstants;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class CPVDefinitionsTransformer extends ChainTransformerAdapter {

	protected void execute(){	
		createTaxonomy("division", "Division", "01000000-7");
		createTaxonomy("group", "Group", "01100000-8");
		createTaxonomy("class", "Class", "01110000-1");
		createTaxonomy("category", "Category", "01111000-8");
		//FIXME: Define property cpv2003:codeIn
		Property p = this.model.createProperty(PSCConstants.CPV_codeIn);
		p.addProperty(RDFS.subPropertyOf, 
				model.createResource(PSCConstants.HTTP_WWW_W3_ORG_2004_02_SKOS_CORE+"mappingRelation"));
		Property p1 = this.model.createProperty(PSCConstants.CPV2003_codeIn);	
		p1.addProperty(RDFS.subPropertyOf, 
				model.createResource(PSCConstants.CPV_codeIn));
		
		
	}
	
	private void createTaxonomy (String level, String prefLabelValue, String exampleValue){
		Resource concept = model.createResource(PSCConstants.HTTP_PURL_ORG_WESO_CPV_DEF+level);		
		concept.addProperty(RDF.type, model.getProperty(PSCConstants.SKOS_Concept));
		
		Literal prefLabel = model.createLiteral(prefLabelValue) ;
		concept.addLiteral(model.getProperty(PSCConstants.SKOS_prefLabel), prefLabel);
		
		Literal example = model.createLiteral(exampleValue) ;
		concept.addLiteral(model.getProperty(PSCConstants.SKOS_example), example);
	}
	
}
