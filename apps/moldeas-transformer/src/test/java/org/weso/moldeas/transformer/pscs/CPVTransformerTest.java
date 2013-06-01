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
package org.weso.moldeas.transformer.pscs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Assert;
import org.junit.Test;
import org.weso.moldeas.transformer.pscs.cpv.CPVTransformer;
import org.weso.moldeas.transformer.pscs.cpv.CPVTransformerBroader;
import org.weso.moldeas.transformer.pscs.cpv.CPVTransformerInit;
import org.weso.moldeas.transformer.pscs.cpv.CPVTransformerProvenance;
import org.weso.pscs.utils.PSCConstants;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;


public class CPVTransformerTest {


	public void testLoad(){
	 try {
		CPVTransformer transformer =new CPVTransformer();
		transformer.execute();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	@Test
	public void testFormatId() {
		String id = "03000000-1";
		String expectedId = PSCConstants.HTTP_PURL_ORG_WESO_CPV_2008+id;
		Assert.assertEquals(expectedId, PSCConstants.formatId(id));
	}
	@Test
	public void testGetType(){
		CPVTransformer transformer =new CPVTransformer();
	    Assert.assertEquals(PSCConstants.HTTP_PURL_ORG_WESO_CPV_2008+"cpv-category", 
	    		transformer.getType("03111000-2"));
	    Assert.assertEquals(PSCConstants.HTTP_PURL_ORG_WESO_CPV_2008+"cpv-category", 
	    		transformer.getType("03111100-3"));
	    Assert.assertEquals(PSCConstants.HTTP_PURL_ORG_WESO_CPV_2008+"cpv-category", 
	    		transformer.getType("03115110-4"));
	    Assert.assertEquals(PSCConstants.HTTP_PURL_ORG_WESO_CPV_2008+"cpv-category", 
	    		transformer.getType("03221111-7"));
	    Assert.assertEquals(PSCConstants.HTTP_PURL_ORG_WESO_CPV_2008+"cpv-class", 
	    		transformer.getType("03110000-5"));
	    Assert.assertEquals(PSCConstants.HTTP_PURL_ORG_WESO_CPV_2008+"cpv-group", 
	    		transformer.getType("03100000-2"));
	    Assert.assertEquals(PSCConstants.HTTP_PURL_ORG_WESO_CPV_2008+"cpv-division", 
	    		transformer.getType("03000000-1"));
	}
	@Test
	public void testTransform() throws FileNotFoundException{
		ChainTransformer transformer =new CPVTransformer();
		ChainTransformer broaderTransformer = new CPVTransformerBroader();
		ChainTransformer provenanceTransformer = new CPVTransformerProvenance();
		broaderTransformer.setSuccessor(provenanceTransformer);
		transformer.setSuccessor(broaderTransformer);	
		Model model = transformer.transform(ModelFactory.createDefaultModel());
		model.write(new PrintWriter("cpv-full.txt"));
		
	}
}
