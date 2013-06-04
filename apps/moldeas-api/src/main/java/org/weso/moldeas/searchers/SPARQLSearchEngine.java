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
package org.weso.moldeas.searchers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.weso.moldeas.dao.DAOSPARQLService;
import org.weso.moldeas.to.EnhancedRequestSearchTO;
import org.weso.moldeas.to.PPNResultTO;
import org.weso.moldeas.to.RequestSearchTO;
import org.weso.moldeas.to.ScoredPPNTO;
import org.weso.moldeas.utils.SPARQLPPNUtils;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class SPARQLSearchEngine implements SearchEngine {

	protected static Logger logger = Logger.getLogger(SPARQLSearchEngine.class);
	public SPARQLSearchEngine() {
		super();
	}

	@Override
	public PPNResultTO enhancedSearch(EnhancedRequestSearchTO request) {
		String query = createEnhancedQuery(request);
		//String limitedQuery = query+DAOSPARQLService.MAX_LIMIT_PPN;//FIXME: Only for app-engine
		logger.debug("Enhanced Search SPARQL query: "+query+".");
		return fetchResults(query);
	}

	@Override
	public PPNResultTO search(RequestSearchTO request) {
		String query = createQuery(request);
		logger.debug("Simple Search SPARQL query: "+query+".");
		return fetchResults(query);
	}
	
	
	public static PPNResultTO fetchResults(String query) {
		logger.debug("Fetching results for query: "+query);
		QueryExecution qExec = QueryExecutionFactory.sparqlService(
				DAOSPARQLService.WESO_SPARQL_SERVICE, query);
		long current = System.currentTimeMillis();
		logger.debug("Preparing to execute query ");
		ResultSet resultsSet = qExec.execSelect();	
		logger.debug("Time of select query: "+(System.currentTimeMillis()-current)+ " msec.");
		return fetchResultSet(resultsSet);
	}

	public static PPNResultTO fetchResults(String query, List<String> graphUris) {
		logger.debug("Fetching results for query "+query+" with graph uris: "+graphUris);
		QueryExecution qExec = QueryExecutionFactory.sparqlService(
				DAOSPARQLService.WESO_SPARQL_SERVICE, query,graphUris,graphUris);
		long current = System.currentTimeMillis();
		logger.debug("Preparing to execute query ");
		ResultSet resultsSet = qExec.execSelect();	
		logger.debug("Time of select query: "+(System.currentTimeMillis()-current)+ " msec.");
		return fetchResultSet(resultsSet);
	}
	
	public static PPNResultTO fetchResultSet (ResultSet resultsSet){
		PPNResultTO result = new PPNResultTO();
		long currentProcessing = System.currentTimeMillis();
		Map<String, ScoredPPNTO> resultMap = new HashMap<String, ScoredPPNTO>();
		for ( ; resultsSet.hasNext() ; ){
			QuerySolution soln = resultsSet.nextSolution() ;   
			String ppn = SPARQLPPNUtils.resourceValue(soln,  "ppn");
			//Search if exists, target result, uri is the key	
			ScoredPPNTO existingPPNTO = resultMap.get(ppn);
			if(existingPPNTO == null){
				existingPPNTO = SPARQLPPNUtils.fetchScoredPPNTO(soln);				
				resultMap.put(ppn, existingPPNTO);
			}else{
				existingPPNTO.getPpnTO().getPscCodes().add(SPARQLPPNUtils.extractedPSCTO(soln, "cpvCode"));
				existingPPNTO.getPpnTO().getNutsCodes().add(SPARQLPPNUtils.extractedNUTSTO(soln, "nutsCode"));
			}					
		}
		result.getScoredPnnsTO().addAll(resultMap.values());
		logger.debug("Time for fetching results "+(System.currentTimeMillis()-currentProcessing)+" msec.");
		logger.debug("Number of results: "+result.getScoredPnnsTO().size());
		return result;
	}
	@Override
	public String createQuery(RequestSearchTO request) {
		String filterCPVCodes = SPARQLPPNUtils.createFilter (request.getPscCodes());
		String filterNutsCodes = SPARQLPPNUtils.createFilterNUTSCodes(request.getNutsCodes());
		String filterYears = SPARQLPPNUtils.createFilterYears(request.getYears());
		return SPARQLPPNUtils.createRawQuery(filterCPVCodes, 
				filterNutsCodes, 
				request.getMaxResults(),
				filterYears);
	}

	public String createEnhancedQuery(EnhancedRequestSearchTO request) {
		String filterCPVCodes = SPARQLPPNUtils.createFilterScoredCodes (request.getScoredPSCCodes());
		String filterNUTSCodes = SPARQLPPNUtils.createFilterNUTSCodes (request.getRequest().getNutsCodes());
		String filterYears = SPARQLPPNUtils.createFilterYears(request.getRequest().getYears());
		return SPARQLPPNUtils.createRawQuery(filterCPVCodes, 
				filterNUTSCodes,
				request.getMaxResults(),
				filterYears);
	}

	

}
