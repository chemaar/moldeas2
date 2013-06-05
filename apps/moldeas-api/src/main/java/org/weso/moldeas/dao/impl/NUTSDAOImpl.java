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

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.weso.moldeas.dao.DAOSPARQLService;
import org.weso.moldeas.dao.NUTSDAO;
import org.weso.moldeas.to.NUTSTO;
import org.weso.moldeas.utils.SPARQLPPNUtils;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class NUTSDAOImpl implements NUTSDAO{

	protected static Logger logger = Logger.getLogger(NUTSDAOImpl.class);
	@Override
	public NUTSTO describe(NUTSTO nutsTO) {
		String query = DAOSPARQLService.NS+" "+
		"SELECT DISTINCT * WHERE{" +
			"?region rdf:type moldeas-onto:NUTSRegion. " + //FIXME: nuts:
			"?region rdfs:label ?label. " +
			"?region <http://nuts.psi.enakting.org/def/shapeKML> ?kml. " +
			"?region <http://nuts.psi.enakting.org/def/code> ?code. "+
			"OPTIONAL { ?region <http://data.ordnancesurvey.co.uk/ontology/spatialrelations/containedBy> ?parent. }."+
			"FILTER (?region=<"+nutsTO.getUri()+">)"+
		"}"	;
		logger.debug("Executing query: "+query+" service: "+DAOSPARQLService.WESO_SPARQL_SERVICE);
		QueryExecution qExec = QueryExecutionFactory.sparqlService(
				DAOSPARQLService.WESO_SPARQL_SERVICE, query);
		ResultSet resultSet = qExec.execSelect();
		for ( ; resultSet.hasNext() ; ){
			return (createNUTSTO(resultSet.next()));
		}
		return new NUTSTO();
	}

	@Override
	public List<NUTSTO> getNUTSTOs() {
		String query = DAOSPARQLService.NS+" "+
		"SELECT DISTINCT * WHERE{" +
			"?region rdf:type moldeas-onto:NUTSRegion. " +
			"?region rdfs:label ?label. " +		
			"?region <http://nuts.psi.enakting.org/def/shapeKML> ?kml. " +
			"?region <http://nuts.psi.enakting.org/def/code> ?code. "+
			"OPTIONAL { ?region <http://data.ordnancesurvey.co.uk/ontology/spatialrelations/containedBy> ?parent. }."+
		"}"	;
		logger.debug("Executing query: "+query+" service: "+DAOSPARQLService.WESO_SPARQL_SERVICE);
		QueryExecution qExec = QueryExecutionFactory.sparqlService(
				DAOSPARQLService.WESO_SPARQL_SERVICE, query);
		ResultSet resultSet = qExec.execSelect();
		List<NUTSTO> nutsTO = new LinkedList<NUTSTO>();
		for ( ; resultSet.hasNext() ; ){
			nutsTO.add(createNUTSTO(resultSet.next()));
		}
		return nutsTO;
	}

	protected static NUTSTO createNUTSTO(QuerySolution soln) {
		String uri = SPARQLPPNUtils.resourceValue(soln, "region");
		String code= SPARQLPPNUtils.fetchStringValue(soln, "code");   
		String label = SPARQLPPNUtils.fetchStringValue(soln, "label");   
		String kml = SPARQLPPNUtils.resourceValue(soln, "kml");
		String containedBy = SPARQLPPNUtils.resourceValue(soln, "parent");
		NUTSTO result = new NUTSTO();		
		result.setUri(uri);
		result.setCode(code);
		result.setLabel(label);
		result.setKml(kml);
		result.setContainedBy(containedBy);
		return result;
	}
	
}
