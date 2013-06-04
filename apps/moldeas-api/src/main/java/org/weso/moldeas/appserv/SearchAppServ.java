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
package org.weso.moldeas.appserv;

import org.apache.log4j.Logger;
import org.weso.moldeas.enhancers.Enhancer;
import org.weso.moldeas.ranking.RankingResults;
import org.weso.moldeas.searchers.SearchEngine;
import org.weso.moldeas.to.EnhancedRequestSearchTO;
import org.weso.moldeas.to.PPNResultTO;
import org.weso.moldeas.to.RequestSearchTO;
import org.weso.moldeas.utils.ApplicationContextLocator;

public class SearchAppServ {

	protected static Logger logger = Logger.getLogger(SearchAppServ.class);
	private SearchEngine searchEngine;
	private RankingResults rankingResults;
	private Enhancer enhancer;

	
	public SearchAppServ() {
		super();		
		this.searchEngine = (SearchEngine) ApplicationContextLocator.getApplicationContext().
		getBean(SearchEngine.class.getSimpleName());
		this.rankingResults = (RankingResults) ApplicationContextLocator.getApplicationContext().
		getBean(RankingResults.class.getSimpleName());
		this.enhancer = (Enhancer) ApplicationContextLocator.getApplicationContext().
		getBean(Enhancer.class.getSimpleName());
	}

	public SearchAppServ(
			SearchEngine sparqlSearchEngine, 
			RankingResults rankingResults,
			Enhancer enhancer) {
		super();
		this.searchEngine = sparqlSearchEngine;
		this.rankingResults = rankingResults;
		this.enhancer = enhancer;
	}

	/**
	 * Search enhancing the request.
	 * Lucene Enhancer seeks for the prefLabels of the CPV Codes if they do not exist it requests the information via SPARQL
	 * to retrive the prefLabels and create a search query over the Lucene CPV index.
	 * 
	 * @param request
	 * @return
	 */
	public PPNResultTO enhancedSearch(RequestSearchTO request){
		logger.debug("Enhanced search in appserv.");
		//Chain of responsability to enrich the query
		long time = System.currentTimeMillis();
		//1-PSCCode
		EnhancedRequestSearchTO enhancedRequest = enhancer.enhance(request);
		enhancedRequest.setMaxResults(request.getMaxResults());//FIXME: how to combine request and enhance request.
		//2-Duration 
		//3-Type of enterprise
		//4-Enterprise
		//5-NUTS
		//6-TotalCost
		//Query Builder: Lucene or SPARQL Service
		//Execute Search
		enhancedRequest.setRequest(request);
		PPNResultTO ppnResultTO = this.searchEngine.enhancedSearch(enhancedRequest);
		//Prepare output
		ppnResultTO.setRequest(request);
		ppnResultTO.setEnhancedRequest(enhancedRequest);
		ppnResultTO.setTime(System.currentTimeMillis()-time);
		ppnResultTO.setTotalResults(ppnResultTO.getScoredPnnsTO().size());
		//Ranking Search: limited to maxResults
		logger.debug("End Enhanced search in appserv.");
		return this.rankingResults.rank(ppnResultTO);
	}
	
	/**
	 * Plain search.
	 * Search directly with the CPV Codes and information of the request.
	 * @param request
	 * @return
	 */
	public PPNResultTO search(RequestSearchTO request){
		logger.debug("Simple search in appserv.");
		//Chain of responsability to enrich the query
		long time = System.currentTimeMillis();
		PPNResultTO ppnResultTO = this.searchEngine.search(request);
		//Ranking Search
		ppnResultTO.setRequest(request);
		ppnResultTO.setTime(System.currentTimeMillis()-time);
		ppnResultTO.setTotalResults(ppnResultTO.getScoredPnnsTO().size());
		logger.debug("End Simple search in appserv.");
		return ppnResultTO ;
	}
	
	public String createEnhancedSPARQLQuery(RequestSearchTO request){
		EnhancedRequestSearchTO enhancedRequest = enhancer.enhance(request);
		enhancedRequest.setMaxResults(request.getMaxResults());
		return this.searchEngine.createEnhancedQuery(enhancedRequest);
	}
	
	public String createSimpleSPARQLQuery(RequestSearchTO request){
		return this.searchEngine.createQuery(request);
	}
}
