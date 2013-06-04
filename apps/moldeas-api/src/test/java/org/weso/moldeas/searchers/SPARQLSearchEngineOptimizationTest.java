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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.weso.moldeas.to.EnhancedRequestSearchTO;
import org.weso.moldeas.to.NUTSTO;
import org.weso.moldeas.to.PPNResultTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.RequestSearchTO;
import org.weso.moldeas.to.ScoredPSCTO;
import org.weso.moldeas.to.YearsTO;


public class SPARQLSearchEngineOptimizationTest {

	private static final int NUMBER_OF_REPLIES = 3;
	protected static Logger logger = Logger.getLogger(SPARQLSearchEngineOptimizationTest.class);
	/**
	 * Simple query: one CPV code, simple request
	 * Time of select query: 3250 msec.
	 * Time for fetching results 2 msec.
	 */
	public void exec(RequestSearchTO request){
		String query = SPARQLSearchEngineTest.createQuery(request, 1);
		for (int i = 0; i<NUMBER_OF_REPLIES; i++){
			logger.debug("REPLY RAW "+i);
			SPARQLSearchEngine.fetchResults(query);
		}

	    query = SPARQLSearchEngineTest.createQuery(request, 2);
		for (int i = 0; i<NUMBER_OF_REPLIES; i++){
			logger.debug("REPLY REWRITE "+i);
			SPARQLSearchEngine.fetchResults(query);
		}
	}
	
	public void execEnhanced(EnhancedRequestSearchTO request){
		String query = SPARQLSearchEngineTest.createEnhancedQuery(request, 1);
		for (int i = 0; i<NUMBER_OF_REPLIES; i++){
			logger.debug("REPLY RAW "+i);
			SPARQLSearchEngine.fetchResults(query);
		}

	    query = SPARQLSearchEngineTest.createEnhancedQuery(request, 2);
		for (int i = 0; i<NUMBER_OF_REPLIES; i++){
			logger.debug("REPLY REWRITE "+i);
			SPARQLSearchEngine.fetchResults(query);
		}
	}
	
	
	public void execEnhancedRewrite(EnhancedRequestSearchTO request){
		String query = SPARQLSearchEngineTest.createEnhancedQuery(request, 2);
		for (int i = 0; i<NUMBER_OF_REPLIES; i++){
			logger.debug("REPLY REWRITE "+i);
			SPARQLSearchEngine.fetchResults(query);
		}
	}
	
	
	public void execEnhancedNamed(EnhancedRequestSearchTO request){
		PPNResultTO result = new PPNResultTO();
	    String query = SPARQLSearchEngineTest.createEnhancedQuery(request, 3);
		for (int i = 0; i<NUMBER_OF_REPLIES; i++){			
			for(long j=request.getRequest().getYears().getMin();j<=request.getRequest().getYears().getMax();j++){
				logger.debug("REPLY REWRITE "+i+" NAMED "+j);
				List<String> graphUris = new LinkedList<String>();
				graphUris.add("http://purl.org/weso/ppn/"+j);
				result.getScoredPnnsTO().addAll(SPARQLSearchEngine.fetchResults(query, graphUris).getScoredPnnsTO());	
			}			
		}
	} 
	@Test
	public void test_1_1(){
		logger.debug("Executing TEST 1-1: Simple Query: one CPV code, no NUTS, raw query, no Named Graph");
		logger.debug("----------------------------------------------");
		Set<PSCTO> pscTOs = new HashSet<PSCTO>();
		PSCTO pscTO = new PSCTO("http://purl.org/weso/cpv/2008/15841400");
		pscTOs.add(pscTO);
		RequestSearchTO request = new RequestSearchTO();
		request.getPscCodes().add(pscTO);
		YearsTO years = new YearsTO(2008, 2011);
		request.setYears(years);
		request.setMaxResults(10000);
		exec(request);
		logger.debug("----------END TEST 1-1------------------------");
	}
	//http://nuts.psi.enakting.org/id/ES
	
	@Test
	public void test_1_2(){
		logger.debug("Executing TEST 1-2: Simple Query: one CPV code, one NUTS, raw query, no Named Graph");
		logger.debug("----------------------------------------------");
		Set<PSCTO> pscTOs = new HashSet<PSCTO>();
		PSCTO pscTO = new PSCTO("http://purl.org/weso/cpv/2008/15841400");
		pscTOs.add(pscTO);
		RequestSearchTO request = new RequestSearchTO();
		request.getPscCodes().add(pscTO);
		NUTSTO nuts1 = new NUTSTO("http://nuts.psi.enakting.org/id/ES");
		request.getNutsCodes().add(nuts1);
		YearsTO years = new YearsTO(2008, 2011);
		request.setYears(years);
		request.setMaxResults(10000);
		exec(request);
		logger.debug("----------END TEST 1-2------------------------");
	}
	
	//http://nuts.psi.enakting.org/id/FR
	
	@Test
	public void test_1_3(){
		logger.debug("Executing TEST 1-3: Simple Query: one CPV code, some NUTS (2), raw query, no Named Graph");
		logger.debug("----------------------------------------------");
		Set<PSCTO> pscTOs = new HashSet<PSCTO>();
		PSCTO pscTO = new PSCTO("http://purl.org/weso/cpv/2008/15841400");;
		pscTOs.add(pscTO);
		RequestSearchTO request = new RequestSearchTO();
		request.getPscCodes().add(pscTO);
		NUTSTO nuts1 = new NUTSTO("http://nuts.psi.enakting.org/id/ES");
		NUTSTO nuts2 = new NUTSTO("http://nuts.psi.enakting.org/id/FR");
		request.getNutsCodes().add(nuts1);
		request.getNutsCodes().add(nuts2);
		YearsTO years = new YearsTO(2008, 2011);
		request.setYears(years);
		request.setMaxResults(10000);
		exec(request);
		logger.debug("----------END TEST 1-3-----------------------");
	}
	
	//	http://nuts.psi.enakting.org/id/DK
	
	@Test
	public void test_1_4(){
		logger.debug("Executing TEST 1-4: Simple Query: one CPV code, some NUTS (3), raw query, no Named Graph");
		logger.debug("----------------------------------------------");
		Set<PSCTO> pscTOs = new HashSet<PSCTO>();
		PSCTO pscTO = new PSCTO("http://purl.org/weso/cpv/2008/15841400");
		pscTOs.add(pscTO);
		RequestSearchTO request = new RequestSearchTO();
		request.getPscCodes().add(pscTO);
		NUTSTO nuts1 = new NUTSTO("http://nuts.psi.enakting.org/id/ES");
		NUTSTO nuts2 = new NUTSTO("http://nuts.psi.enakting.org/id/FR");
		NUTSTO nuts3 = new NUTSTO("http://nuts.psi.enakting.org/id/DK");
		request.getNutsCodes().add(nuts1);
		request.getNutsCodes().add(nuts2);
		request.getNutsCodes().add(nuts3);
		YearsTO years = new YearsTO(2008, 2011);
		request.setYears(years);
		request.setMaxResults(10000);
		exec(request);
		logger.debug("----------END TEST 1-4-----------------------");
	}
	
	@Test
	public void test_1_5(){
		logger.debug("Executing TEST 1-5: Enhanced Query: some CPV code, some NUTS (3), raw query, no Named Graph");
		logger.debug("----------------------------------------------");
		PSCTO pscTO = new PSCTO("http://purl.org/weso/cpv/2008/15841400");
		PSCTO pscTO1 = new PSCTO("http://purl.org/weso/cpv/2008/15841300");
		PSCTO pscTO2 = new PSCTO("http://purl.org/weso/cpv/2008/44921210");
		PSCTO pscTO3 = new PSCTO("http://purl.org/weso/cpv/2008/15511700");
		PSCTO pscTO4 = new PSCTO("http://purl.org/weso/cpv/2008/03131400");	
		EnhancedRequestSearchTO request = new EnhancedRequestSearchTO();
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO, 1.0));
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO1, 1.0));
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO2, 1.0));
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO3, 1.0));
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO4, 1.0));
		RequestSearchTO simpleRequest = new RequestSearchTO();
		NUTSTO nuts1 = new NUTSTO("http://nuts.psi.enakting.org/id/ES");
		NUTSTO nuts2 = new NUTSTO("http://nuts.psi.enakting.org/id/FR");
		NUTSTO nuts3 = new NUTSTO("http://nuts.psi.enakting.org/id/DK");
		simpleRequest.getNutsCodes().add(nuts1);
		simpleRequest.getNutsCodes().add(nuts2);
		simpleRequest.getNutsCodes().add(nuts3);
		YearsTO years = new YearsTO(2008, 2011);
		simpleRequest.setYears(years);
		request.setMaxResults(10000);
		request.setRequest(simpleRequest);
		execEnhanced(request);
		logger.debug("----------END TEST 1-5-----------------------");
	}
	
	@Test
	public void test_1_6(){
		logger.debug("Executing TEST 1-6: Enhanced Query: some CPV code, some NUTS (3), raw query, no Named Graph");
		logger.debug("----------------------------------------------");
		PSCTO pscTO = new PSCTO("http://purl.org/weso/cpv/2008/15841400");
		PSCTO pscTO1 = new PSCTO("http://purl.org/weso/cpv/2008/15841300");
		PSCTO pscTO2 = new PSCTO("http://purl.org/weso/cpv/2008/44921210");
		PSCTO pscTO3 = new PSCTO("http://purl.org/weso/cpv/2008/15511700");
		PSCTO pscTO4 = new PSCTO("http://purl.org/weso/cpv/2008/03131400");	
		EnhancedRequestSearchTO request = new EnhancedRequestSearchTO();
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO, 1.0));
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO1, 1.0));
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO2, 1.0));
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO3, 1.0));
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO4, 1.0));
		RequestSearchTO simpleRequest = new RequestSearchTO();
		NUTSTO nuts1 = new NUTSTO("http://nuts.psi.enakting.org/id/ES");
		NUTSTO nuts2 = new NUTSTO("http://nuts.psi.enakting.org/id/FR");
		NUTSTO nuts3 = new NUTSTO("http://nuts.psi.enakting.org/id/DK");
		simpleRequest.getNutsCodes().add(nuts1);
		simpleRequest.getNutsCodes().add(nuts2);
		simpleRequest.getNutsCodes().add(nuts3);
		YearsTO years = new YearsTO(2008, 2008);
		simpleRequest.setYears(years);
		request.setMaxResults(10000);
		request.setRequest(simpleRequest);
		execEnhanced(request);
		logger.debug("----------END TEST 1-6-----------------------");
	}
	
	@Test
	public void test_1_7(){
		logger.debug("Executing TEST 1-7: Enhanced Query: some CPV code, some NUTS (3), raw query,  no Named Graph");
		logger.debug("----------------------------------------------");
		PSCTO pscTO = new PSCTO("http://purl.org/weso/cpv/2008/15841400");
		PSCTO pscTO1 = new PSCTO("http://purl.org/weso/cpv/2008/15841300");
		PSCTO pscTO2 = new PSCTO("http://purl.org/weso/cpv/2008/44921210");
		PSCTO pscTO3 = new PSCTO("http://purl.org/weso/cpv/2008/15511700");
		PSCTO pscTO4 = new PSCTO("http://purl.org/weso/cpv/2008/03131400");	
		EnhancedRequestSearchTO request = new EnhancedRequestSearchTO();
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO, 1.0));
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO1, 1.0));
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO2, 1.0));
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO3, 1.0));
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO4, 1.0));
		RequestSearchTO simpleRequest = new RequestSearchTO();
		NUTSTO nuts1 = new NUTSTO("http://nuts.psi.enakting.org/id/ES");
		NUTSTO nuts2 = new NUTSTO("http://nuts.psi.enakting.org/id/FR");
		NUTSTO nuts3 = new NUTSTO("http://nuts.psi.enakting.org/id/DK");
		simpleRequest.getNutsCodes().add(nuts1);
		simpleRequest.getNutsCodes().add(nuts2);
		simpleRequest.getNutsCodes().add(nuts3);
		YearsTO years = new YearsTO(2008, 2011);
		simpleRequest.setYears(years);
		request.setMaxResults(10000);
		request.setRequest(simpleRequest);
		execEnhanced(request);
		logger.debug("----------END TEST 1-7-----------------------");
	}
	
	@Test
	public void test_1_8(){
		logger.debug("Executing TEST 1-8: Enhanced Query: some CPV code, some NUTS (3), raw query,  Named Graph");
		logger.debug("----------------------------------------------");
		PSCTO pscTO = new PSCTO("http://purl.org/weso/cpv/2008/15841400");
		PSCTO pscTO1 = new PSCTO("http://purl.org/weso/cpv/2008/15841300");
		PSCTO pscTO2 = new PSCTO("http://purl.org/weso/cpv/2008/44921210");
		PSCTO pscTO3 = new PSCTO("http://purl.org/weso/cpv/2008/15511700");
		PSCTO pscTO4 = new PSCTO("http://purl.org/weso/cpv/2008/03131400");	
		EnhancedRequestSearchTO request = new EnhancedRequestSearchTO();
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO, 1.0));
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO1, 1.0));
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO2, 1.0));
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO3, 1.0));
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO4, 1.0));
		RequestSearchTO simpleRequest = new RequestSearchTO();
		NUTSTO nuts1 = new NUTSTO("http://nuts.psi.enakting.org/id/ES");
		NUTSTO nuts2 = new NUTSTO("http://nuts.psi.enakting.org/id/FR");
		NUTSTO nuts3 = new NUTSTO("http://nuts.psi.enakting.org/id/DK");
		simpleRequest.getNutsCodes().add(nuts1);
		simpleRequest.getNutsCodes().add(nuts2);
		simpleRequest.getNutsCodes().add(nuts3);
		YearsTO years = new YearsTO(2008, 2011);
		simpleRequest.setYears(years);
		request.setMaxResults(10000);
		request.setRequest(simpleRequest);
		execEnhancedNamed(request);
		logger.debug("----------END TEST 1-8-----------------------");
	}
	
	@Test
	public void test_1_9(){
		logger.debug("Executing TEST 1-9: Enhanced Query: some CPV code (queries splitted), some NUTS (3), rewrite query,  No Named Graph");
		logger.debug("----------------------------------------------");
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
		for(PSCTO itemPSCTO: pscTOs){
			logger.debug("SPLITTING FOR "+itemPSCTO.getUri());			
			EnhancedRequestSearchTO request = new EnhancedRequestSearchTO();
			request.getScoredPSCCodes().add(new ScoredPSCTO(itemPSCTO, 1.0));
			request.setRequest(simpleRequest);
			request.setMaxResults(10000);
			execEnhancedRewrite(request);
		}
			
		logger.debug("----------END TEST 1-9-----------------------");
	}
	
	@Test
	public void test_1_10(){
		logger.debug("Executing TEST 1-10: Enhanced Query: some CPV code (queries splitted), some NUTS (3), rewrite query, Named Graph");
		logger.debug("----------------------------------------------");
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
		for(PSCTO itemPSCTO: pscTOs){
			logger.debug("SPLITTING FOR "+itemPSCTO.getUri());			
			EnhancedRequestSearchTO request = new EnhancedRequestSearchTO();
			request.getScoredPSCCodes().add(new ScoredPSCTO(itemPSCTO, 1.0));
			request.setRequest(simpleRequest);
			request.setMaxResults(10000);
			execEnhancedNamed(request);
			//execEnhancedRewrite(request);
		}
			
		logger.debug("----------END TEST 1-10-----------------------");
	}
	
	@Test
	public void test_1_11(){
		logger.debug("Executing TEST 1-11: Enhanced Query: some CPV code (queries splitted), some NUTS (nuts splitted), rewrite query,  No Named Graph");
		logger.debug("----------------------------------------------");
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
		List<NUTSTO> nutsTOs = new LinkedList<NUTSTO>();
		nutsTOs.add(nuts1);
		nutsTOs.add(nuts2);
		nutsTOs.add(nuts3);
		YearsTO years = new YearsTO(2008, 2011);
		simpleRequest.setYears(years);
		for(PSCTO itemPSCTO: pscTOs){
			for(NUTSTO nutsTO:nutsTOs){
				logger.debug("SPLITTING FOR "+itemPSCTO.getUri()+" NUTS "+nutsTO.getUri());			
				EnhancedRequestSearchTO request = new EnhancedRequestSearchTO();
				request.getScoredPSCCodes().add(new ScoredPSCTO(itemPSCTO, 1.0));
				request.setRequest(simpleRequest);
				request.setMaxResults(10000);
				request.getRequest().getNutsCodes().clear();//Clean the request
				request.getRequest().getNutsCodes().add(nutsTO);
				execEnhancedRewrite(request);
			}
			
		}
			
		logger.debug("----------END TEST 1-11-----------------------");
	}
	
	
}
