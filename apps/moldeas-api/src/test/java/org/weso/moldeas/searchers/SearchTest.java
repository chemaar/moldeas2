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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.weso.moldeas.to.EnhancedRequestSearchTO;
import org.weso.moldeas.to.NUTSTO;
import org.weso.moldeas.to.PPNResultTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.RequestSearchTO;
import org.weso.moldeas.to.ScoredPSCTO;
import org.weso.moldeas.to.YearsTO;

public class SearchTest {

	public static final int NUMBER_OF_REPLIES = 3;
	public static final int MAX_RESULTS = 10000;
	public static final int NO_RESULTS = -1;
	protected static Logger logger = Logger.getLogger(SearchTest.class);
	
	public static void exec(RequestSearchTO request){
		String query = SPARQLSearchEngineTest.createQuery(request, 1);
		for (int i = 0; i<NUMBER_OF_REPLIES; i++){
			logger.debug("REPLY RAW "+i);
			SPARQLSearchEngine.fetchResults(query);
		}

	 }	
	
	public static void execEnhanced(EnhancedRequestSearchTO request){
		String query = SPARQLSearchEngineTest.createEnhancedQuery(request, 1);
		for (int i = 0; i<NUMBER_OF_REPLIES; i++){
			logger.debug("REPLY RAW "+i);
			SPARQLSearchEngine.fetchResults(query);
		}
	}
	
	public static void execEnhancedRewrite(EnhancedRequestSearchTO request){
		String query = SPARQLSearchEngineTest.createEnhancedQuery(request, 2);
		for (int i = 0; i<NUMBER_OF_REPLIES; i++){
			logger.debug("REPLY REWRITE "+i);
			SPARQLSearchEngine.fetchResults(query);
		}
	}
	
	public static void execEnhancedNamed(EnhancedRequestSearchTO request){
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
	
	public static String getInitialCode(int pos){
		String [] codes = new String [] {
				"15331137",
				"50531510",
				"34144100",
				"64122000",
				"79320000",
				"44100000",
				"31000000",
				"50000000",
				"15841400"
		};
		return codes[pos];
	}
	
	@Test
	public void test_1(){
		logger.debug("Executing TEST 1");
		logger.debug("----------------------------------------------");
		for(int i = 0; i<9;i++){		
			Set<PSCTO> pscTOs = new HashSet<PSCTO>();
			PSCTO pscTO = new PSCTO("http://purl.org/weso/cpv/2008/"+getInitialCode(i));
			pscTOs.add(pscTO);
			logger.debug("CODE "+pscTO.getUri());
			RequestSearchTO request = new RequestSearchTO();
			request.getPscCodes().add(pscTO);
			YearsTO years = new YearsTO(2008, 2011);
			request.setYears(years);
			request.setMaxResults(NO_RESULTS);
			exec(request);	
			logger.debug("END CODE "+pscTO.getUri());
		}
		logger.debug("----------END TEST 1------------------------");
	
	}
	
	
	@Test
	public void test_2(){
		logger.debug("Executing TEST 2");
		logger.debug("----------------------------------------------");
		for(int i = 0; i<9;i++){	
			Set<PSCTO> pscTOs = new HashSet<PSCTO>();
			PSCTO pscTO = new PSCTO("http://purl.org/weso/cpv/2008/"+getInitialCode(i));
			pscTOs.add(pscTO);
			logger.debug("CODE "+pscTO.getUri());
			RequestSearchTO request = new RequestSearchTO();
			request.getPscCodes().add(pscTO);
			YearsTO years = new YearsTO(2008, 2011);
			request.setYears(years);
			request.setMaxResults(MAX_RESULTS);
			exec(request);
			logger.debug("END CODE "+pscTO.getUri());
		}
		logger.debug("----------END TEST 2------------------------");
	}
	
	
	@Test
	public void test_3(){
		logger.debug("Executing TEST 3");
		logger.debug("----------------------------------------------");
		for(int i = 0; i<9;i++){
			logger.debug("QUERY "+i);
			EnhancedRequestSearchTO request = getQuery(i+1, NO_RESULTS);
			execEnhanced(request);
			logger.debug("END QUERY "+i);
		}
	
		logger.debug("----------END TEST 3------------------------");
	}
	
	@Test
	public void test_4(){
		logger.debug("Executing TEST 4");
		logger.debug("----------------------------------------------");
		for(int i = 0; i<9;i++){
			logger.debug("QUERY "+i);
			EnhancedRequestSearchTO request = getQuery(i+1, MAX_RESULTS);
			execEnhanced(request);
			logger.debug("END QUERY "+i);
		}
	
		logger.debug("----------END TEST 4------------------------");
	}
	
	@Test
	public void test_5(){
		logger.debug("Executing TEST 5 ");
		logger.debug("----------------------------------------------");
		for(int i = 0; i<9;i++){
			logger.debug("QUERY "+i);
			EnhancedRequestSearchTO request = getQuery(i+1, MAX_RESULTS);
			execEnhancedRewrite(request);
			logger.debug("END QUERY "+i);
		}	
		logger.debug("----------END TEST 5------------------------");
	}
	
	@Test
	public void test_6_1(){
		logger.debug("Executing TEST 6_1 ");
		logger.debug("----------------------------------------------");
		for(int i = 0; i<9;i++){
			logger.debug("QUERY "+i);
			EnhancedRequestSearchTO request = getQuery(i+1, MAX_RESULTS);
			execEnhancedNamed(request);
			logger.debug("END QUERY "+i);
		}	
		logger.debug("----------END TEST 6_1------------------------");
	}
	
	
	@Test
	public void test_7_1(){
		logger.debug("Executing TEST 7_1 ");
		logger.debug("----------------------------------------------");
		for(int i = 0; i<9;i++){
			logger.debug("QUERY "+i);
			EnhancedRequestSearchTO fullRequest = getQuery(i+1, MAX_RESULTS);
			for(ScoredPSCTO scoredPSCTO: fullRequest.getScoredPSCCodes()){
				EnhancedRequestSearchTO itemRequest = new EnhancedRequestSearchTO();
				itemRequest.getScoredPSCCodes().add(scoredPSCTO);
				itemRequest.setRequest(fullRequest.getRequest());
				itemRequest.setMaxResults(MAX_RESULTS);
				execEnhancedRewrite(itemRequest);
			}
			logger.debug("END QUERY "+i);
		}
		logger.debug("----------END TEST 7_1------------------------");
	}
	
	@Test
	public void test_8_1(){
		logger.debug("Executing TEST 8_1 ");
		logger.debug("----------------------------------------------");
		for(int i = 0; i<9;i++){
			logger.debug("QUERY "+i);
			EnhancedRequestSearchTO fullRequest = getQuery(i+1, MAX_RESULTS);
			for(ScoredPSCTO scoredPSCTO: fullRequest.getScoredPSCCodes()){
				EnhancedRequestSearchTO itemRequest = new EnhancedRequestSearchTO();
				itemRequest.getScoredPSCCodes().add(scoredPSCTO);
				itemRequest.setRequest(fullRequest.getRequest());
				itemRequest.setMaxResults(MAX_RESULTS);
				execEnhancedNamed(itemRequest);
			}
			logger.debug("END QUERY "+i);
		}
		logger.debug("----------END TEST 8_1------------------------");
	}
	
	@Test
	public void test_9_1(){
		logger.debug("Executing TEST 9_1 ");
		logger.debug("----------------------------------------------");
		for(int i = 0; i<9;i++){
			logger.debug("QUERY "+i);
			EnhancedRequestSearchTO fullRequest = getQuery(i+1, MAX_RESULTS);
			for(ScoredPSCTO scoredPSCTO: fullRequest.getScoredPSCCodes()){
				for(NUTSTO nutsTO:fullRequest.getRequest().getNutsCodes()){
					RequestSearchTO itemSimpleRequest = new RequestSearchTO();
					itemSimpleRequest.getNutsCodes().add(nutsTO);
					EnhancedRequestSearchTO itemRequest = new EnhancedRequestSearchTO();
					itemRequest.getScoredPSCCodes().add(scoredPSCTO);
					itemRequest.setRequest(itemSimpleRequest);
					itemRequest.setMaxResults(MAX_RESULTS);
					execEnhancedRewrite(itemRequest);
				}
					
			}
			logger.debug("END QUERY "+i);
		}
		logger.debug("----------END TEST 9_1------------------------");
	}
	
	
	@Test
	public void test_10_1(){
		logger.debug("Executing TEST 10_1 ");
		logger.debug("----------------------------------------------");
		for(int i = 0; i<9;i++){
			logger.debug("QUERY "+i);
			EnhancedRequestSearchTO fullRequest = getQuery(i+1, MAX_RESULTS);
			for(ScoredPSCTO scoredPSCTO: fullRequest.getScoredPSCCodes()){
				for(NUTSTO nutsTO:fullRequest.getRequest().getNutsCodes()){
					RequestSearchTO itemSimpleRequest = new RequestSearchTO();
					itemSimpleRequest.getNutsCodes().add(nutsTO);
					itemSimpleRequest.setYears(fullRequest.getRequest().getYears());
					EnhancedRequestSearchTO itemRequest = new EnhancedRequestSearchTO();
					itemRequest.getScoredPSCCodes().add(scoredPSCTO);
					itemRequest.setRequest(itemSimpleRequest);
					itemRequest.setMaxResults(MAX_RESULTS);
					execEnhancedNamed(itemRequest);
				}
					
			}
			logger.debug("END QUERY "+i);
		}
		logger.debug("----------END TEST 10_1------------------------");
	}
	
	public static EnhancedRequestSearchTO getQuery(int n, int noResults){
		switch(n){
		case 1: return createQuery_1(noResults);
		case 2: return createQuery_2(noResults);
		case 3: return createQuery_3(noResults);
		case 4: return createQuery_4(noResults);
		case 5: return createQuery_5(noResults);
		case 6: return createQuery_6(noResults);
		case 7: return createQuery_7(noResults);
		case 8: return createQuery_8(noResults);
		case 9: return createQuery_9(noResults);
		default: return null;
			
		}
	}
	public static EnhancedRequestSearchTO createQuery_1(int results){
		String [] cpvCodes = new String[]{
				"48611000",
				"48611000",
				"50531510",
				"15871210",
				"15331137"
		};
		String [] nutsCodes = new String []{
				"UK",
				"PL",
				"RO"
		};
		return createQuery(cpvCodes, nutsCodes, results);
	}
	public static EnhancedRequestSearchTO createQuery_2(int results){
		String [] cpvCodes = new String[]{
				"50531510",
				"34144100",
				"44212211",
				"44212212",
				"50531510"
		};
		String [] nutsCodes = new String []{
				"ES",
				"FR",
				"DE"
		};
		return createQuery(cpvCodes, nutsCodes, results);
	}
	
	public static EnhancedRequestSearchTO createQuery_3(int results){
		String [] cpvCodes = new String[]{
				"44212211",
				"31140000",
				"50531510",
				"44212212",
				"34144100"
		};
		String [] nutsCodes = new String []{
				"PL",
				"CZ",
				"RO"
		};
		return createQuery(cpvCodes, nutsCodes, results);
	}
	
	public static EnhancedRequestSearchTO createQuery_4(int results){
		String [] cpvCodes = new String[]{
				"64216120",
				"79571000",
				"64122000",
				"15871210",
				"64121000"
		};
		String [] nutsCodes = new String []{
				"BE",
				"SE",
				"DE"
		};
		return createQuery(cpvCodes, nutsCodes, results);
	}
	
	public static EnhancedRequestSearchTO createQuery_5(int results){
		String [] cpvCodes = new String[]{
				"75241000",
				"79320000",
				"75100000",
				"75000000",
				"60112000"
		};
		String [] nutsCodes = new String []{
				"UK",
				"FR",
				"AT"
		};
		return createQuery(cpvCodes, nutsCodes, results);
	}
	
	public static EnhancedRequestSearchTO createQuery_6(int results){
		String [] cpvCodes = new String[]{
				"44100000",
				"44110000",
				"44170000",
				"44190000",
				"UB03"
		};
		String [] nutsCodes = new String []{
				"NL",
				"SE",
				"DE"
		};
		return createQuery(cpvCodes, nutsCodes, results);
	}
	
	
	public static EnhancedRequestSearchTO createQuery_7(int results){
		String [] cpvCodes = new String[]{
				"33141000",
				"39000000",
				"31000000",
				"44000000",
				"31600000"
		};
		String [] nutsCodes = new String []{
				"DE",
				"IT",
				"HU"
		};
		return createQuery(cpvCodes, nutsCodes, results);
	}
	
	public static EnhancedRequestSearchTO createQuery_8(int results){
		String [] cpvCodes = new String[]{
				"50512000",
				"50333100",
				"50000000",
				"50530000",
				"50532300"
		};
		String [] nutsCodes = new String []{
				"UK",
				"IT",
				"FR"
		};
		return createQuery(cpvCodes, nutsCodes, results);
	}
	
	
	public static EnhancedRequestSearchTO createQuery_9(int results){
		String [] cpvCodes = new String[]{
				"15841300",
				"15841400",
				"15511700",
				"44921210",
				"03131400"
		};
		String [] nutsCodes = new String []{
				"ES",
				"FR",
				"DK"
		};
		return createQuery(cpvCodes, nutsCodes, results);
	}
	
	public static EnhancedRequestSearchTO createQuery(String  [] cpvCodes, String[] nutsCodes, int results){
		String NS = "http://purl.org/weso/cpv/2008/";
		String NS_NUTS = "http://nuts.psi.enakting.org/id/";
		EnhancedRequestSearchTO request = new EnhancedRequestSearchTO();
		RequestSearchTO simpleRequest = new RequestSearchTO();
		YearsTO years = new YearsTO(2008, 2011);
		for(int i = 0; i<cpvCodes.length; i++){
			request.getScoredPSCCodes().add(new ScoredPSCTO(new PSCTO(NS+cpvCodes[i]), 1.0));
		}
		for(int i = 0; i<nutsCodes.length;i++){		
			simpleRequest.getNutsCodes().add(new NUTSTO(NS_NUTS+nutsCodes[i]));
		}
		simpleRequest.setYears(years);
		request.setMaxResults(results);
		request.setRequest(simpleRequest);
		return request;
	}
}
