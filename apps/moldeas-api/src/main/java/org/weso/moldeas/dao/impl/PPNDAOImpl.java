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
package org.weso.moldeas.dao.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.weso.moldeas.dao.DAOSPARQLService;
import org.weso.moldeas.dao.PPNDAO;
import org.weso.moldeas.to.NUTSTO;
import org.weso.moldeas.to.PPNTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.utils.SPARQLPPNUtils;
import org.weso.pscs.utils.PSCConstants;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDF;

public class PPNDAOImpl implements PPNDAO{

	protected static Logger logger = Logger.getLogger(PPNDAOImpl.class);
	//FIXME: graph uri
	
	public PPNDAOImpl() {
		super();
	}
	
	@Override
	public PPNTO describe(PPNTO ppnTO) {
		String query = DAOSPARQLService.NS+" " +
		"DESCRIBE <"+ppnTO.getUri()+">";

		
		QueryExecution qExec = QueryExecutionFactory.sparqlService(
				DAOSPARQLService.WESO_SPARQL_SERVICE, query);
		Model results = qExec.execDescribe();

		PPNTO result = new PPNTO();
		Resource target = results.listResourcesWithProperty(RDF.type).next();//only one
		result.setUri(target.getURI());
		result.setId(target.getProperty(DCTerms.identifier).getString());
		result.setYear(target.getProperty(DCTerms.date).getString());
		
		NodeIterator it = results.listObjectsOfProperty(target, results.getProperty(PSCConstants.CPV_codeIn));
		while (it.hasNext()){
			Resource r = (Resource) it.next();			
			PSCTO code = new PSCTO();
			code.setUri(r.getURI());
			result.getPscCodes().add(code );
		}
		it = results.listObjectsOfProperty(target, results.getProperty(PSCConstants.NUTS_CODE));
		while (it.hasNext()){
			Resource r = (Resource) it.next();			
			NUTSTO code = new NUTSTO();
			code.setUri(r.getURI());
			result.getNutsCodes().add(code );
		}
		return result;
	}

	@Override
	public List<PPNTO> getPPNTOs() {
		String query = DAOSPARQLService.NS+"" +
				"SELECT * WHERE{" +
				"?ppn rdf:type moldeas-onto:Notice. " +
				"?ppn dcterms:identifier ?id. " +
				"?ppn dcterms:date ?date. " +
				"?ppn moldeas-onto:topic ?cpvCode." +
				"?ppn moldeas-onto:located-in ?nutsCode. " +
				"}  " +
				DAOSPARQLService.MAX_LIMIT_PPN;
		logger.debug("Executing query: "+query+" service: "+DAOSPARQLService.WESO_SPARQL_SERVICE);
		QueryExecution qExec = QueryExecutionFactory.sparqlService(
				DAOSPARQLService.WESO_SPARQL_SERVICE, query);
		ResultSet resultsSet = qExec.execSelect();
		return processingResults(resultsSet);
	}

	private List<PPNTO> processingResults(ResultSet resultsSet) {
		List<PPNTO> ppnTOs = new LinkedList<PPNTO>();	
		Map<String, PPNTO> resultMap = new HashMap<String, PPNTO>();
		for ( ; resultsSet.hasNext() ; ){
			QuerySolution soln = resultsSet.nextSolution() ;   
			String ppn = SPARQLPPNUtils.resourceValue(soln,  "ppn");
			//Search if exists, target result, uri is the key	
			PPNTO existingPPNTO = resultMap.get(ppn);
			if(existingPPNTO == null){
				existingPPNTO = SPARQLPPNUtils.fetchPPNTO(soln);				
				resultMap.put(ppn, existingPPNTO);
			}else{
				existingPPNTO.getPscCodes().add(SPARQLPPNUtils.extractedPSCTO(soln, "cpvCode"));
				existingPPNTO.getNutsCodes().add(SPARQLPPNUtils.extractedNUTSTO(soln, "nutsCode"));
			}					
		}
		ppnTOs.addAll(resultMap.values());
		return ppnTOs;
	}

	@Override
	public List<PPNTO> getPPNTOsByYear(PPNTO ppnTO) {
		String query =DAOSPARQLService.NS+" " + 
		"SELECT * WHERE{ " +
		"?ppn rdf:type moldeas-onto:Notice. " +
		"?ppn dcterms:identifier ?id. " +
		"?ppn dcterms:date ?date. " +
		"?ppn moldeas-onto:topic ?cpvCode." +
		"?ppn moldeas-onto:located-in ?nutsCode. " +
		"FILTER (str(?date) = \""+ppnTO.getYear() +"\")"+
		"} " +
		DAOSPARQLService.MAX_LIMIT_PPN;
		logger.debug("Query: "+query);
		QueryExecution qExec = QueryExecutionFactory.sparqlService(
				DAOSPARQLService.WESO_SPARQL_SERVICE, query);
		ResultSet resultsSet = qExec.execSelect();
		return processingResults(resultsSet);
	}

}
