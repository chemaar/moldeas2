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

import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.weso.moldeas.to.EnhancedRequestSearchTO;
import org.weso.moldeas.to.NUTSTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.RequestSearchTO;
import org.weso.moldeas.to.ScoredPSCTO;
import org.weso.moldeas.to.YearsTO;

public class SPARQLScalaSearchEngine {

	protected static Logger logger = Logger.getLogger(SPARQLScalaSearchEngine.class);
	public List<String> createQueries(){
		PSCTO pscTO = new PSCTO("http://purl.org/weso/cpv/2008/15841400");
		PSCTO pscTO1 = new PSCTO("http://purl.org/weso/cpv/2008/15841300");
		PSCTO pscTO2 = new PSCTO("http://purl.org/weso/cpv/2008/44921210");
		PSCTO pscTO3 = new PSCTO("http://purl.org/weso/cpv/2008/15511700");
		PSCTO pscTO4 = new PSCTO("http://purl.org/weso/cpv/2008/03131400");	
		List<PSCTO> pscTOs = new LinkedList<PSCTO>();
		pscTOs.add(pscTO);
		pscTOs.add(pscTO1);
		pscTOs.add(pscTO2);
		pscTOs.add(pscTO3);
		pscTOs.add(pscTO4);
		//Create simple request
		RequestSearchTO simpleRequest = new RequestSearchTO();
		NUTSTO nuts1 = new NUTSTO("http://nuts.psi.enakting.org/id/ES");
		NUTSTO nuts2 = new NUTSTO("http://nuts.psi.enakting.org/id/FR");
		NUTSTO nuts3 = new NUTSTO("http://nuts.psi.enakting.org/id/DK");
		simpleRequest.getNutsCodes().add(nuts1);
		simpleRequest.getNutsCodes().add(nuts2);
		simpleRequest.getNutsCodes().add(nuts3);
		YearsTO years = new YearsTO(2008, 2011);
		simpleRequest.setYears(years);
		List<String> queries = new LinkedList<String>();
		for(PSCTO itemPSCTO: pscTOs){
			EnhancedRequestSearchTO request = new EnhancedRequestSearchTO();
			request.getScoredPSCCodes().add(new ScoredPSCTO(itemPSCTO, 1.0));
			request.setRequest(simpleRequest);
			request.setMaxResults(10000);
			queries.add(SPARQLSearchEngineTest.createEnhancedQuery(request, 2));			
		}
		return queries;
		}
	
	@Test
	public void testCreateQueries(){
		Assert.assertEquals(5, createQueries().size());
	}
}
