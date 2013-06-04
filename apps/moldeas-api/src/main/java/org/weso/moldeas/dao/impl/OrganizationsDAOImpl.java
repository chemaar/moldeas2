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

import org.weso.moldeas.dao.DAOSPARQLService;
import org.weso.moldeas.dao.OrganizationsDAO;
import org.weso.moldeas.to.OrganizationTO;
import org.weso.moldeas.to.PSCTO;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.vocabulary.DC;

public class OrganizationsDAOImpl implements OrganizationsDAO {

	@Override
	public OrganizationTO describe(OrganizationTO organizationTO) {
		String query = "DESCRIBE ?psc WHERE { ?psc dc:identifier \""+organizationTO.getId()+"\". }";
		QueryExecution qExec = QueryExecutionFactory.sparqlService(
				DAOSPARQLService.WESO_SPARQL_SERVICE, query, DAOSPARQLService.ORGANIZATIONS_GRAPH_URI);
		Model results = qExec.execDescribe();
		OrganizationTO result = new OrganizationTO();
		result.setId(results.getResource(DAOSPARQLService.ORGANIZATIONS_GRAPH_URI+"#"+organizationTO.getId()).
				getProperty(DC.identifier).getString());
		return result;
	}

	@Override
	public List<OrganizationTO> getOrganizationTOs() {
		String query = "SELECT ?id WHERE { " +
		"?x dc:identifier ?id . " +
		"?x rdf:type ?type. " +
		"FILTER (?type = <http://purl.org/weso/cpv/def#category>)"+
		"}";
		QueryExecution qExec = QueryExecutionFactory.sparqlService(
				DAOSPARQLService.WESO_SPARQL_SERVICE, query, DAOSPARQLService.ORGANIZATIONS_GRAPH_URI);
		ResultSet resultsSet = qExec.execSelect();
		List<OrganizationTO> organizationsTOs = new LinkedList<OrganizationTO>();
		for ( ; resultsSet.hasNext() ; ){
			QuerySolution soln = resultsSet.nextSolution() ;                    
			RDFNode node = soln.get("id");
            String value= (node==null)?"": 
            	node.isLiteral() ?  soln.getLiteral("id").getString(): 
            		node.toString();    
            OrganizationTO result = new OrganizationTO();
        	result.setId(value);
			organizationsTOs.add(result);
		}
		return organizationsTOs;
	}

}
