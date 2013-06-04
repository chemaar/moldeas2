/**
 * 
 *   (c) Copyright 2011 WESO, Computer Science Department,
 *   Facultad de Ciencias, University of Oviedo, Oviedo, Asturias, Spain, 33007
 *   All rights reserved.
 *  
 *   Redistribution and use in source and binary forms, with or without
 *   modification, are permitted provided that the following conditions
 *   are met:
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *   3. The name of the author may not be used to endorse or promote products
 *      derived from this software without specific prior written permission.
 *  
 *   THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 *   IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *   OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 *   IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 *   INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 *   NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *   DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *   THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 *   THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 */
package org.weso.moldeas.enhancers.psc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.weso.moldeas.enhancers.Enhancer;
import org.weso.moldeas.psc.PSCFacadeTest;
import org.weso.moldeas.to.EnhancedRequestSearchTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.RequestSearchTO;
import org.weso.moldeas.to.ScoredPSCTO;
import org.weso.pscs.utils.PSCConstants;

public class PSCNarrowerEnhancerTest {

	@Test
	public void testExecute() {
		Enhancer enhancer = new PSCNarrowerEnhancer(PSCFacadeTest.createPSCFacade());
		RequestSearchTO request = new RequestSearchTO();
		PSCTO pscTO = new PSCTO();
		pscTO.setId("34513100");
		pscTO.setUri(PSCConstants.formatId(pscTO.getId()));
		request.getPscCodes().add(pscTO);
		EnhancedRequestSearchTO result = enhancer.enhance(request );
		for(ScoredPSCTO scored: result.getScoredPSCCodes()){
			System.out.println(scored);
		}
		assertEquals(result.getScoredPSCCodes().size(), 2);
	}
	
	@Test
	public void testExecuteBean() {
		Enhancer enhancer = new PSCNarrowerEnhancer();
		RequestSearchTO request = new RequestSearchTO();
		PSCTO pscTO = new PSCTO();
		pscTO.setId("34513100");
		pscTO.setUri(PSCConstants.formatId(pscTO.getId()));
		request.getPscCodes().add(pscTO);
		EnhancedRequestSearchTO result = enhancer.enhance(request );
		for(ScoredPSCTO scored: result.getScoredPSCCodes()){
			System.out.println(scored);
		}
		assertEquals(result.getScoredPSCCodes().size(), 2);
	}

}
