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

import java.util.Enumeration;
import java.util.ResourceBundle;

import org.weso.moldeas.utils.URISchemeManager;

public class DAOSPARQLService {
	public static String WESO_SPARQL_SERVICE = "http://localhost/sparql/"; 
	
	public static String MAX_LIMIT = "LIMIT 1000"; //FIXME: ONLY FOR GOOGLE APPENGIN
	public static String MAX_LIMIT_PPN = "LIMIT 2000"; //FIXME: ONLY FOR GOOGLE APPENGIN
	
	//FIXME: Extract to database or consult via SPARQL and VOID vocabulary
	
	public static final String CPV_2008_GRAPH_URI = "http://purl.org/weso/pscs/";
	public static final String DEFAULT_GRAPH_URI = "http://purl.org/weso/moldeas/";
	public static final String ORGANIZATIONS_GRAPH_URI = "http://purl.org/weso/moldeas/organization/";
	
	public static String NS = "";

	public static final String GRAPH_SEPARATOR = "#";
	static{
		
		WESO_SPARQL_SERVICE = ResourceBundle.getBundle(DAOSPARQLService.class.getName().toString()).getString("endpoint.uri");
		
		ResourceBundle bundle = URISchemeManager.getResourceBundle();
		StringBuffer ns = new StringBuffer();
		Enumeration<String> keys = bundle.getKeys();
		while(keys.hasMoreElements()){
			String prefix = keys.nextElement();
			String uri = bundle.getString(prefix);
			ns.append("PREFIX "+prefix+": <"+uri+"> \n");
		}
		NS = ns.toString();
	}
	  
	  
}
