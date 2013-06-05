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
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.utils.ApplicationContextLocator;
import org.weso.moldeas.utils.SPARQLPPNUtils;

import com.hp.hpl.jena.query.ResultSet;

public class CPVCacheDAOImpl extends CPVDAOImpl {

	protected static Logger logger = Logger.getLogger(CPVCacheDAOImpl.class);
	private DataSource dataSource = null;
	protected Map<String,PSCTO> cpvConcepts ;
	protected List<PSCTO> listConcepts;
	
	public CPVCacheDAOImpl(DataSource dataSource){
		this.dataSource = dataSource;
		this.cpvConcepts = createMapCPVConceptsV2();
		this.listConcepts = new LinkedList<PSCTO>(this.cpvConcepts.values());
	}

	public CPVCacheDAOImpl(){
		logger.debug("Default Constructor");
		this.dataSource = (DataSource) ApplicationContextLocator.getApplicationContext().
				getBean(CPVDAOImpl.CPV_DATA_SOURCE_BEAN);
		this.cpvConcepts = createMapCPVConceptsV2();
		this.listConcepts = new LinkedList<PSCTO>(this.cpvConcepts.values()); 
	
	}

//	private Map<String, PSCTO> createMapCPVConcepts() {
//		Map<String,PSCTO> pscTOs = new HashMap<String,PSCTO>();
//		String query =DAOSPARQLService.NS+" " + 
//		"SELECT * WHERE{ "+
//		"?code dc:identifier ?id. "+ 
//		"?code rdf:type ?type. " +
//		"?code skosxl:prefLabel ?prefLabel. "+
//		"FILTER (?type=<http://purl.org/weso/cpv/def#division> " + 
//		"|| ?type=<http://purl.org/weso/cpv/def#group> " +
//		"|| ?type=<http://purl.org/weso/cpv/def#class> " +
//		"|| ?type=<http://purl.org/weso/cpv/def#category> ). " +
//		"FILTER (lang(?prefLabel)=\"es\")"+ //FIXME label and retrieve broaders one time. Need to optimize the queries
//		"} "; 
//		logger.debug("Executing query to create Map CPV Concepts: "+query+" service: "+DAOSPARQLService.WESO_SPARQL_SERVICE+
//				" graph: "+this.graph);
//		QueryExecution qExec = QueryExecutionFactory.sparqlService(
//				DAOSPARQLService.WESO_SPARQL_SERVICE, query, this.graph);
//
//		ResultSet resultsSet = qExec.execSelect();
//		for ( ; resultsSet.hasNext() ; ){
//			PSCTO pscTO = CPVDAOImpl.createPSCTO(resultsSet.nextSolution()); 
//			pscTOs.put(pscTO.getUri(),pscTO);
//		}
//		return pscTOs;
//	}
	
	public List<PSCTO> createListPSCTO() {
		String query =DAOSPARQLService.NS+" " + 
		"SELECT * WHERE{ "+
		"?code dc:identifier ?id. "+ 
		"?code rdf:type ?type. " +
		"?code skos:prefLabel ?prefLabel. "+
		"FILTER (?type=<http://localhost/pscs/cpv/ontology/Division> " + 
		"|| ?type=<http://localhost/pscs/cpv/ontology/Group> " +
		"|| ?type=<http://localhost/pscs/cpv/ontology/Class> " +
		"|| ?type=<http://localhost/pscs/cpv/ontology/Category> ). " +
		"FILTER (lang(?prefLabel)=\"ES\")"+ //FIXME: SPARQL Jena does not admit "es" it works with "ES", Virtuoso works with "es" "ES" not yet tested 
		"} "; 
		DataSource cvpDataSource = getCVPDataSource();
		logger.debug("Executing query: "+query+" data source ");		
		List<PSCTO> pscTOs = new LinkedList<PSCTO>();
		ResultSet resultsSet = cvpDataSource.execSelect(query);
		for ( ; resultsSet.hasNext() ; ){
			pscTOs.add(new PSCTO(SPARQLPPNUtils.resourceValue(resultsSet.nextSolution(), "code")));
		}
		logger.debug("Return CPV Concepts: "+pscTOs.size());
		return pscTOs;
	}
		
		
	
	private Map<String, PSCTO> createMapCPVConceptsV2() {
		Map<String,PSCTO> pscTOs = new HashMap<String,PSCTO>();
		List<PSCTO> lisfOfpscTOs = createListPSCTO();
		for(PSCTO pscTO:lisfOfpscTOs){
			pscTOs.put(pscTO.getUri(), super.describe(pscTO));
		}
		return pscTOs;
	}

	@Override
	public PSCTO describe(PSCTO pscTO) {
		logger.debug("Describing cache concept "+pscTO.getUri());
		return this.cpvConcepts.get(pscTO.getUri());
	}

	@Override
	public List<PSCTO> getPSCTOs() {
		logger.debug("Returning cache list of concepts.");
		return this.listConcepts;
	}

	@Override
	public List<PSCTO> getNarrowersOf(PSCTO pscTO) {
		logger.debug("Getting cache narrowers of "+pscTO.getUri());
		return this.cpvConcepts.get(pscTO.getUri()).getNarrowers();
	}

	@Override
	protected DataSource getCVPDataSource() {	
		return this.dataSource;
	}


}
