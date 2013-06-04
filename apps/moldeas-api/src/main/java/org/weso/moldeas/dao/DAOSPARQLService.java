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
package org.weso.moldeas.dao;

public class DAOSPARQLService {
	public static final String WESO_SPARQL_SERVICE = "http://156.35.31.156/sparql"; //FIXME: EXTRACT PARAM IN SPRING-BEANS
	
	public static String MAX_LIMIT = "LIMIT 1000"; //FIXME: ONLY FOR GOOGLE APPENGIN
	public static String MAX_LIMIT_PPN = "LIMIT 2000"; //FIXME: ONLY FOR GOOGLE APPENGIN
	
	//FIXME: Extract to database or consult via SPARQL and VOID vocabulary
	
	public static final String CPV_2008_GRAPH_URI = "http://purl.org/weso/cpv/2008";
	public static final String CPV_2003_GRAPH_URI = "http://purl.org/weso/cpv/2003";
	public static final String NUTS_2008_GRAPH_URI = "ttp://nuts.psi.enakting.org";
	public static final String EUROVOC_GRAPH_URI = "http://eurovoc.europa.eu";
	public static final String PPN_GRAPH_URI = "http://purl.org/weso/ppn";
	public static final String PPN_GRAPH_URI_2008 = PPN_GRAPH_URI+"/2008";
	public static final String PPN_GRAPH_URI_2009 = PPN_GRAPH_URI+"/2009";
	public static final String PPN_GRAPH_URI_2010 = PPN_GRAPH_URI+"/2010";
	public static final String PPN_GRAPH_URI_2011 = PPN_GRAPH_URI+"/2011";
	
	public static final String ORGANIZATIONS_GRAPH_URI = "http://purl.org/weso/organizations";
	
	  public static String NS =(
			  	"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
				"PREFIX dc: <http://purl.org/dc/elements/1.1/> " +
				"PREFIX : <http://dbpedia.org/resource/> " +
				"PREFIX cpv-def: <http://purl.org/weso/cpv/def#> " +
				"PREFIX cpv: <http://purl.org/weso/cpv/2008/> " +
				"PREFIX cpv2003: <http://purl.org/weso/cpv/2003/> " +
				"PREFIX ppn-def: <http://purl.org/weso/ppn/def#> " +
				"PREFIX ppn2008: <http://purl.org/weso/ppn/2008/> " +
				"PREFIX ppn2009: <http://purl.org/weso/ppn/2009/> " +				
				"PREFIX ppn2010: <http://purl.org/weso/ppn/2010/> " +
				"PREFIX ppn2011: <http://purl.org/weso/ppn/2011/> " +
				"PREFIX eurovoc: <http://eurovoc.europa.eu#> " +
				"PREFIX dbpedia2: <http://dbpedia.org/property/> " +
				"PREFIX dbpedia: <http://dbpedia.org/> " +
				"PREFIX  wn20schema: <http://www.w3.org/2006/03/wn/wn20/schema/> "+
				"PREFIX  skosxl: <http://www.w3.org/2008/05/skos-xl#> "+
				"PREFIX skos: <http://www.w3.org/2004/02/skos/core#> ");

	public static final String GRAPH_SEPARATOR = "#";
	  
	  
}
