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

import org.weso.moldeas.dao.DAOSPARQLService;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

public class CPVDataSourceRemoteEndpoint implements CPVDataSource{

	protected String graph = DAOSPARQLService.CPV_2008_GRAPH_URI;
	protected String uriEndpoint = DAOSPARQLService.WESO_SPARQL_SERVICE;
	
	
	public CPVDataSourceRemoteEndpoint() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CPVDataSourceRemoteEndpoint(String graph, String uriEndpoint) {
		super();
		this.graph = graph;
		this.uriEndpoint = uriEndpoint;
	}

	@Override
	public Model execDescribe(String query) {
		QueryExecution qExec = QueryExecutionFactory.sparqlService(
					this.uriEndpoint, query, this.graph);
		return  qExec.execDescribe();
	}

	@Override
	public ResultSet execSelect(String query) {
		QueryExecution qExec = QueryExecutionFactory.sparqlService(
				this.uriEndpoint, query, this.graph);
		return qExec.execSelect();
	}

	public String getGraph() {
		return graph;
	}

	public void setGraph(String graph) {
		this.graph = graph;
	}

	public String getUriEndpoint() {
		return uriEndpoint;
	}

	public void setUriEndpoint(String uriEndpoint) {
		this.uriEndpoint = uriEndpoint;
	}

	@Override
	public String toString() {
		return "CPVDataSourceRemoteEndpoint [graph=" + graph + ", uriEndpoint="
				+ uriEndpoint + "]";
	}

}
