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
package org.weso.moldeas.searchers;

import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.weso.moldeas.to.EnhancedRequestSearchTO;
import org.weso.moldeas.to.NUTSTO;
import org.weso.moldeas.to.PPNResultTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.RequestSearchTO;
import org.weso.moldeas.to.ScoredPSCTO;
import org.weso.moldeas.utils.SPARQLPPNUtils;

public class SPARQLSearchEngineTest {

	@Test
	public void testEnhancedSearch() {
		fail("Not yet implemented");
	}

	//@Test
	public void testSearch() {
		RequestSearchTO request = new RequestSearchTO();
		PSCTO pstTO = new PSCTO();
		pstTO.setUri("http://purl.org/weso/cpv/2008/60161000");
		request.getPscCodes().add(pstTO);
		SPARQLSearchEngine engine = new SPARQLSearchEngine();
		PPNResultTO result = engine.search(request);
		Assert.assertEquals(692, result.getScoredPnnsTO().size());
		//System.out.println(result.getPpns().iterator().next());
	}
	//@Test
	public void testCreateFilter(){
		Set<PSCTO> pscTOs = new HashSet<PSCTO>();
		PSCTO pscTO = new PSCTO();
		pscTO.setUri("http://purl.org/weso/cpv/2008/60161000");
		pscTOs.add(pscTO);
		//System.out.println(SPARQLSearchEngine.createFilter(pscTOs));
		Assert.assertEquals("FILTER (  (?cpvCode = <http://purl.org/weso/cpv/2008/60161000> ) ). ", 
				SPARQLPPNUtils.createFilter(pscTOs));
		PSCTO pscTO1 = new PSCTO();
		pscTO1.setUri("http://purl.org/weso/cpv/2008/60161001");
		pscTOs.add(pscTO);
//		Assert.assertEquals(
//				"FILTER (  (?cpvCode = <http://purl.org/weso/cpv/2008/60161000> )  &&  (?cpvCode = <http://purl.org/weso/cpv/2008/60161000> ) ).", 
//				SPARQLSearchEngine.createFilter(pscTOs));
		
	}

	@Test
	public void testCreateRawSimpleQuery(){
		SPARQLSearchEngine engine = new SPARQLSearchEngine();
		Set<PSCTO> pscTOs = new HashSet<PSCTO>();
		PSCTO pscTO = new PSCTO();
		pscTO.setUri("http://purl.org/weso/cpv/2008/15841400");
		pscTOs.add(pscTO);
		RequestSearchTO request = new RequestSearchTO();
		request.getPscCodes().add(pscTO);
		System.out.println(createQuery(request,1));
		System.out.println(createQuery(request,2));
		System.out.println(createQuery(request,3));
	}
	
	@Test
	public void testCreateRawEnhancedQuery(){
		SPARQLSearchEngine engine = new SPARQLSearchEngine();
		Set<PSCTO> pscTOs = new HashSet<PSCTO>();
		PSCTO pscTO = new PSCTO();
		pscTO.setUri("http://purl.org/weso/cpv/2008/15841400");
		pscTOs.add(pscTO);
		NUTSTO nutsTO = new NUTSTO("http://nuts.psi.enakting.org/id/SE");
		RequestSearchTO request = new RequestSearchTO();
		request.getPscCodes().add(pscTO);
		request.getNutsCodes().add(nutsTO);
		EnhancedRequestSearchTO enhancedRequest = new EnhancedRequestSearchTO();
		enhancedRequest.setRequest(request);
		ScoredPSCTO scoredPSCTO = new ScoredPSCTO(pscTO, 1.0);
		enhancedRequest.getScoredPSCCodes().add(scoredPSCTO );
		System.out.println(createEnhancedQuery(enhancedRequest,1));
		System.out.println(createEnhancedQuery(enhancedRequest,2));
		System.out.println(createEnhancedQuery(enhancedRequest,3));
	}
	
	
	//Helper methods
	public static String createQuery(RequestSearchTO request, int type) {
		String filterCPVCodes = SPARQLPPNUtils.createFilter (request.getPscCodes());
		String filterNutsCodes = SPARQLPPNUtils.createFilterNUTSCodes(request.getNutsCodes());
		String filterYears = SPARQLPPNUtils.createFilterYears(request.getYears());
		String query;
		if(type==1){ //RAW
			query = SPARQLPPNUtils.createRawQuery(filterCPVCodes, 
					filterNutsCodes, 
					request.getMaxResults(),
					filterYears);
		}else if (type==2) { //Rewrite
			query = SPARQLPPNUtils.createRewritingQuery(filterCPVCodes, 
					filterNutsCodes, 
					request.getMaxResults(),
					filterYears);
		}else {//Rewrite withouth filter years
			query = SPARQLPPNUtils.createRewritingQuery(filterCPVCodes, 
					filterNutsCodes, 
					request.getMaxResults());
		}
		return query;
	}

	public static String createEnhancedQuery(EnhancedRequestSearchTO request, int type) {
		String filterCPVCodes = SPARQLPPNUtils.createFilterScoredCodes(request.getScoredPSCCodes());
		String filterNutsCodes = SPARQLPPNUtils.createFilterNUTSCodes(request.getRequest().getNutsCodes());
		String filterYears = SPARQLPPNUtils.createFilterYears(request.getRequest().getYears());
		String query;
		if(type==1){ //RAW
			query = SPARQLPPNUtils.createRawQuery(filterCPVCodes, 
					filterNutsCodes, 
					request.getMaxResults(),
					filterYears);
		}else if (type==2) { //Rewrite
			query = SPARQLPPNUtils.createRewritingQuery(filterCPVCodes, 
					filterNutsCodes, 
					request.getMaxResults(),
					filterYears);
		}else {//Rewrite withouth filter years+graphUris for years
			query = SPARQLPPNUtils.createRewritingQuery(filterCPVCodes, 
					filterNutsCodes, 
					request.getMaxResults());
		}
		return query;
	}

}
