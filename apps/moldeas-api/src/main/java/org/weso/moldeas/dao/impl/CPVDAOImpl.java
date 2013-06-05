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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.weso.moldeas.dao.DAOSPARQLService;
import org.weso.moldeas.dao.PSCDAO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.utils.SPARQLPPNUtils;
import org.weso.pscs.utils.PSCConstants;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDF;

public abstract class CPVDAOImpl implements PSCDAO {

	//FIXME: Use Command design pattern
	protected static Logger logger = Logger.getLogger(CPVDAOImpl.class);
	public static final String CPV_DATA_SOURCE_BEAN = "CPVDataSource";

	public CPVDAOImpl(){
		//FIXME: REquest dataSource
	}

	protected abstract DataSource getCVPDataSource();
	
	@Override
	public PSCTO describe(PSCTO pscTO) {
		String query = DAOSPARQLService.NS+" " +
		"DESCRIBE <"+pscTO.getUri()+">";
		DataSource cvpDataSource = getCVPDataSource();
		logger.debug("Executing describe query: "+query+" provider: "+ cvpDataSource.getClass().getSimpleName());
		Model results = cvpDataSource.execDescribe(query);
		PSCTO result = new PSCTO();
		Resource target = results.listResourcesWithProperty(RDF.type).next();//only one
		result.setId(target.getProperty(DC.identifier).getString());				
		result.setUri(target.getURI());
		result.setType(target.getProperty(RDF.type).getResource().getURI());
		NodeIterator it = results.listObjectsOfProperty(target, results.getProperty(PSCConstants.SKOS_prefLabel));
		while (it.hasNext()){
			Literal l = (Literal) it.next();
			if(l.getLanguage().equalsIgnoreCase("EN")) result.setPrefLabel(l.getString());//FIXME
		}
		result.setNarrowers(createListOfNarrowers(result)); //full implementation
		return result;
	}

	@Override
	public List<PSCTO> getPSCTOs() {
		//FIXME: Externalize uris and NameSpaces
		String query =DAOSPARQLService.NS+" " + 
		"SELECT * WHERE{ "+
		"?code dc:identifier ?id. "+ 
		"?code rdf:type ?type. " +
		"?code skos:prefLabel ?prefLabel. "+
		"FILTER (?type=<http://localhost/pscs/cpv/ontology/Category> " + 
		"|| ?type=<http://localhost/pscs/cpv/ontology/Division> " +
		"|| ?type=<http://localhost/pscs/cpv/ontology/Group> " +
		"|| ?type=<http://localhost/pscs/cpv/ontology/Class> ). " +
		"FILTER (lang(?prefLabel)=\"EN\")"+ //FIXME
		
//		"FILTER (?type=<http://purl.org/weso/pscs/cpv/ontology/Category> " + 
//		"|| ?type=<http://purl.org/weso/pscs/cpv/ontology/Division> " +
//		"|| ?type=<http://purl.org/weso/pscs/cpv/ontology/Group> " +
//		"|| ?type=<http://purl.org/weso/pscs/cpv/ontology/Class> ). " +
//		"FILTER (lang(?prefLabel)=\"EN\")"+ //FIXME
		"} "; 
		DataSource cvpDataSource = getCVPDataSource();
		logger.debug("Executing query: "+query+" provider: "+cvpDataSource);
		List<PSCTO> pscTOs = new LinkedList<PSCTO>();
		ResultSet resultsSet = cvpDataSource.execSelect(query);
		for ( ; resultsSet.hasNext() ; ){
			pscTOs.add(createPSCTO(resultsSet.nextSolution()));
		}
		logger.debug("Return CPV Concepts: "+pscTOs.size());
		return pscTOs;
	}

	@Override
	public List<PSCTO> getNarrowersOf(PSCTO pscTO) {
		return createListOfNarrowers(pscTO);
	}
	
//	@Override
//	public List<PSCTO> getNarrowersOfTransitive(PSCTO pscTO) {
//		return createListOfNarrowersTransitive(pscTO);
//	}

	private List<PSCTO> createListOfNarrowers(PSCTO pscTO) {
		logger.debug("Get narrowers of: "+pscTO);
		String query =DAOSPARQLService.NS+" " +
				"SELECT DISTINCT * WHERE{" +
				"?code rdf:type ?type . " +
				"?code skos:broaderTransitive <" +pscTO.getUri()+"> ." +
				"?code dc:identifier ?id. " +
				"?code skos:prefLabel ?prefLabel. "+
				"FILTER (?type=<http://localhost/pscs/cpv/ontology/Category> " + 
				"|| ?type=<http://localhost/pscs/cpv/ontology/Division> " +
				"|| ?type=<http://localhost/pscs/cpv/ontology/Group> " +
				"|| ?type=<http://localhost/pscs/cpv/ontology/Class> ). " +
				
//				"FILTER (?type=<http://purl.org/weso/pscs/cpv/ontology/Category> " + 
//				"|| ?type=<http://purl.org/weso/pscs/cpv/ontology/Division> " +
//				"|| ?type=<http://purl.org/weso/pscs/cpv/ontology/Group> " +
//				"|| ?type=<http://purl.org/weso/pscs/cpv/ontology/Class> ). " +
				"FILTER (lang(?prefLabel)=\"EN\")" +
				"}"; //FIXME";			
		DataSource cvpDataSource = getCVPDataSource();
		logger.debug("Executing query: "+query+" provider: "+cvpDataSource);
		ResultSet resultsSet = cvpDataSource.execSelect(query);
		List<PSCTO> pscTOs = new LinkedList<PSCTO>();
		for ( ; resultsSet.hasNext() ; ){			
			pscTOs.add(createPSCTO(resultsSet.nextSolution()));
		}
		logger.debug("Return CPV narrower Concepts: "+pscTOs.size());
		return pscTOs;
	}
	
//	private List<PSCTO> createListOfNarrowersTransitive(PSCTO pscTO) {
//		logger.debug("Get narrowers transitive of: "+pscTO);
//		String query =DAOSPARQLService.NS+" " +
//				"SELECT DISTINCT * WHERE{" +
//				"?code rdf:type ?type . " +
//				"?code skos:broaderTransitive <" +pscTO.getUri()+"> ." +
//				"?code dc:identifier ?id. " +
//				"?code skosxl:prefLabel ?prefLabel. "+
//				"FILTER (?type=<http://purl.org/weso/pscs/cpv/ontology/Category> " + //FIXME: Change by PSCConcept
//				"|| ?type=<http://purl.org/weso/pscs/cpv/ontology/Division> " +
//				"|| ?type=<http://purl.org/weso/pscs/cpv/ontology/Group> " +
//				"|| ?type=<http://purl.org/weso/pscs/cpv/ontology/Class> ). " +
//				"FILTER (lang(?prefLabel)=\"EN\")" +
//				"}"; //FIXME";			
//		CPVDataSource cvpDataSource = getCVPDataSource();
//		logger.debug("Executing query: "+query+" provider: "+cvpDataSource);
//		ResultSet resultsSet = cvpDataSource.execSelect(query);	
//		int i = 0;
//		List<PSCTO> pscTOs = new LinkedList<PSCTO>();
//		for ( ; resultsSet.hasNext() ; ){		
//			PSCTO narrower = createPSCTO(resultsSet.nextSolution());
//			System.out.println("Finding narrower "+narrower+" "+i);
//			pscTOs.add(narrower);
//			pscTOs.addAll(createListOfNarrowersTransitive(narrower));
//			i++;
//		}
//		logger.debug("Return CPV narrower Concepts: "+pscTOs.size());
//		return pscTOs;
//	}

	@Override
	public List<PSCTO> getNarrowersTransitiveOf(PSCTO pscTO) {
		logger.debug("Getting narrowers of "+pscTO.getUri());		
		List<PSCTO> aggregated = getNarrowersOf(pscTO);
		List<PSCTO> result = new LinkedList<PSCTO>();
		if(aggregated.size()!=0){
			result.addAll(aggregated);
			for(PSCTO aggregate:aggregated){
				result.addAll(this.getNarrowersTransitiveOf(aggregate));
			}
		}
		return result;
	}

	protected static PSCTO createPSCTO(QuerySolution soln) {
		String uri = SPARQLPPNUtils.resourceValue(soln, "code");
		String id= SPARQLPPNUtils.fetchStringValue(soln, "id");   
		String prefLabel = SPARQLPPNUtils.fetchStringValue(soln, "prefLabel");   
		String type = SPARQLPPNUtils.resourceValue(soln, "type");
		PSCTO result = new PSCTO();		
		result.setUri(uri);
		result.setId(id);
		result.setPrefLabel(prefLabel);	
		result.setType(type);
		return result;
	}

}
