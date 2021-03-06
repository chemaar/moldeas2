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
package org.weso.moldeas.appserv;

import junit.framework.Assert;

import org.junit.Test;
import org.weso.moldeas.enhancers.Enhancer;
import org.weso.moldeas.enhancers.psc.LucenePSCCodesEnhancer;
import org.weso.moldeas.psc.PSCFacade;
import org.weso.moldeas.psc.PSCFacadeTest;
import org.weso.moldeas.ranking.DummyRankingImpl;
import org.weso.moldeas.searchers.SPARQLSearchEngine;
import org.weso.moldeas.to.DurationTO;
import org.weso.moldeas.to.NUTSTO;
import org.weso.moldeas.to.PPNResultTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.RequestSearchTO;
import org.weso.moldeas.to.TotalCostTO;
import org.weso.moldeas.to.YearsTO;


public class SearchAppServTest {

	//@Test
	public void testSimpleSearchByCode(){
		RequestSearchTO requestSearch = new RequestSearchTO();
		PSCTO pstTO = new PSCTO();
		pstTO.setUri("http://localhost/pscs/cpv/2008/resource/60161000");
		requestSearch.getPscCodes().add(pstTO);
		requestSearch.setMaxResults(1000);
		PSCFacade pscFacade = PSCFacadeTest.createPSCFacade(); 
		SearchAppServ appServ = new SearchAppServ(
				new SPARQLSearchEngine(),
				new DummyRankingImpl(),
				createEnhancers(pscFacade));//Skip the use of spring		
		PPNResultTO result = appServ.search(requestSearch);
		Assert.assertEquals(692, result.getScoredPnnsTO().size());
	}
	//@Test
	public void testEnhancedSearch(){
		RequestSearchTO requestSearch = new RequestSearchTO();
		PSCTO pstTO = new PSCTO();
		pstTO.setUri("http://localhost/pscs/cpv/2008/resource/60161000");
		requestSearch.getPscCodes().add(pstTO);
		requestSearch.setMaxResults(2000);
		PSCFacade pscFacade = PSCFacadeTest.createPSCFacade(); 
		SearchAppServ appServ = new SearchAppServ(
				new SPARQLSearchEngine(),
				new DummyRankingImpl(),
				createEnhancers(pscFacade));//Skip the use of spring		
		PPNResultTO result = appServ.enhancedSearch(requestSearch);
		Assert.assertEquals(1821, result.getScoredPnnsTO().size());
	}
	
	@Test
	public void testFailSearch(){
		RequestSearchTO requestSearch = new RequestSearchTO();
		PSCTO pstTO = new PSCTO();
		pstTO.setUri("http://localhost/pscs/cpv/2008/resource/98510000");
		requestSearch.getPscCodes().add(pstTO);
		requestSearch.getNutsCodes().add(new NUTSTO("http://localhost/moldeas/nuts/resource/DE"));
		DurationTO duration = new DurationTO();
		duration.setMin(Long.valueOf(1));
		duration.setMax(Long.valueOf(3));
		requestSearch.setDuration(duration);
		TotalCostTO totalCost = new TotalCostTO();
		totalCost.setMin(Long.valueOf(75));
		totalCost.setMax(Long.valueOf(300));
		requestSearch.setTotalCost(totalCost );
		YearsTO years = new YearsTO();
		years.setMin(Long.valueOf(2009));
		years.setMax(Long.valueOf(2010));
		requestSearch.setYears(years);
		//return new PPNResultTO();
		//request.setMaxResults(10000);
		requestSearch.setMaxResults(1000);		
		
//		RequestSearchTO requestSearch = new RequestSearchTO();
//		PSCTO pstTO = new PSCTO();
//		pstTO.setUri("http://localhost/pscs/cpv/2008/resource/98510000");
//		requestSearch.getPscCodes().add(pstTO);
//		requestSearch.setMaxResults(2000);
//		requestSearch.getNutsCodes().add(new NUTSTO("http://localhost/moldeas/nuts/resource/DE"));
		PSCFacade pscFacade = PSCFacadeTest.createPSCFacade(); 
		SearchAppServ appServ = new SearchAppServ(
				new SPARQLSearchEngine(),
				new DummyRankingImpl(),
				createEnhancers(pscFacade));//Skip the use of spring		
		PPNResultTO result = appServ.enhancedSearch(requestSearch);
		Assert.assertEquals(16, result.getScoredPnnsTO().size());
	}
	
	
	public static  Enhancer createEnhancers(PSCFacade facade){
		return new LucenePSCCodesEnhancer(facade);
	}
	
	//@Test
	public void testCreateQuery(){
		RequestSearchTO requestSearch = new RequestSearchTO();
		PSCTO pstTO = new PSCTO();
		pstTO.setUri("http://purl.org/weso/cpv/2008/60161000");
		requestSearch.getPscCodes().add(pstTO);
		requestSearch.setMaxResults(2000);
		PSCFacade pscFacade = PSCFacadeTest.createPSCFacade(); 
		SearchAppServ appServ = new SearchAppServ(
				new SPARQLSearchEngine(),
				new DummyRankingImpl(),
				createEnhancers(pscFacade));//Skip the use of spring		
		String result = appServ.createSimpleSPARQLQuery(requestSearch);
		System.out.println(result);
	}
	
}
