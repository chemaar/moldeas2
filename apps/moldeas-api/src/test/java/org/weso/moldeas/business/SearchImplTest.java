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
package org.weso.moldeas.business;

import junit.framework.Assert;

import org.junit.Test;
import org.weso.moldeas.appserv.SearchAppServ;
import org.weso.moldeas.appserv.SearchAppServTest;
import org.weso.moldeas.psc.PSCFacade;
import org.weso.moldeas.psc.PSCFacadeTest;
import org.weso.moldeas.ranking.DummyRankingImpl;
import org.weso.moldeas.searchers.SPARQLSearchEngine;
import org.weso.moldeas.to.PPNResultTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.RequestSearchTO;

public class SearchImplTest {

	@Test
	public void testEnhancedSearch() {
		RequestSearchTO requestSearch = new RequestSearchTO();
		PSCTO pstTO = new PSCTO();
		pstTO.setUri("http://purl.org/weso/cpv/2008/60161000");
		requestSearch.getPscCodes().add(pstTO);
		requestSearch.setMaxResults(1000);
		Search searcher = new SearchImpl(createAppServ());
		PPNResultTO result = searcher.enhancedSearch(requestSearch);
		Assert.assertEquals(1821, result.getScoredPnnsTO().size());
	}

	private SearchAppServ createAppServ() {
		PSCFacade pscFacade = PSCFacadeTest.createPSCFacade(); 
		SearchAppServ appServ = new SearchAppServ(
				new SPARQLSearchEngine(),
				new DummyRankingImpl(),
				SearchAppServTest.createEnhancers(pscFacade));
		return appServ;
	}

	@Test
	public void testSearch() {
		RequestSearchTO requestSearch = new RequestSearchTO();
		PSCTO pstTO = new PSCTO();
		pstTO.setUri("http://purl.org/weso/cpv/2008/60161000");
		requestSearch.getPscCodes().add(pstTO);
		requestSearch.setMaxResults(1000);
		Search searcher = new SearchImpl(createAppServ());
		PPNResultTO result = searcher.search(requestSearch);
		Assert.assertEquals(692, result.getScoredPnnsTO().size());
	}
	@Test
	public void testSearchSpring() {
		RequestSearchTO requestSearch = new RequestSearchTO();
		PSCTO pstTO = new PSCTO();
		pstTO.setUri("http://purl.org/weso/cpv/2008/60161000");
		requestSearch.getPscCodes().add(pstTO);
		requestSearch.setMaxResults(1000);
		Search searcher = new SearchImpl();
		PPNResultTO result = searcher.search(requestSearch);
		Assert.assertEquals(692, result.getScoredPnnsTO().size());
	}

}
