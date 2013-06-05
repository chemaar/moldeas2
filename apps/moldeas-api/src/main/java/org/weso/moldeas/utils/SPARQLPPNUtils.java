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
package org.weso.moldeas.utils;

import java.util.Set;

import org.weso.moldeas.dao.DAOSPARQLService;
import org.weso.moldeas.to.NUTSTO;
import org.weso.moldeas.to.PPNTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.ScoredPPNTO;
import org.weso.moldeas.to.ScoredPSCTO;
import org.weso.moldeas.to.YearsTO;

import com.hp.hpl.jena.query.QuerySolution;

public class SPARQLPPNUtils {
	
	public static PPNTO fetchPPNTO (QuerySolution soln){
		String uri = resourceValue(soln,  "ppn"); //FIXME: Extract constans
		String id = fetchStringValue(soln,  "id"); 
		String date = fetchStringValue(soln,  "date"); 
		PSCTO pscTO = extractedPSCTO(soln,"cpvCode");
		NUTSTO nutsTO = extractedNUTSTO(soln,"nutsCode");
		PPNTO ppnTO = new PPNTO();
		ppnTO.setId(id);
		ppnTO.setYear(date);
		ppnTO.setUri(uri);		
		ppnTO.getPscCodes().add(pscTO);
		ppnTO.getNutsCodes().add(nutsTO);
		return ppnTO;
	}
	
	public static ScoredPPNTO fetchScoredPPNTO(QuerySolution soln) {	
		ScoredPPNTO result = new ScoredPPNTO();
		result.setPpnTO(fetchPPNTO(soln));
		result.setScore(1);//FIXME
		return result;
	}

	public static NUTSTO extractedNUTSTO(QuerySolution soln, String field) {
		String nutsCode = resourceValue(soln,  field); ;
		NUTSTO nutsTO = new NUTSTO(nutsCode);
		return nutsTO;
	}

	public static PSCTO extractedPSCTO(QuerySolution soln, String field) {
		String cpvCode = resourceValue(soln, field); 
		PSCTO pscTO = new PSCTO(cpvCode);
		return pscTO;
	}

	public static String fetchStringValue (QuerySolution soln, String field){
		return (soln==null  || soln.get(field)==null)?"": 
        	soln.get(field).isLiteral() ?  soln.getLiteral(field).getString():soln.toString();
	}
	
	public static String resourceValue (QuerySolution soln, String field){
		return (soln==null || soln.get(field)==null)?"": 
        	soln.get(field).isURIResource() ?  soln.getResource(field).getURI():soln.toString();
	}
	
	
	
	public static String createFilterNUTSCodes(Set<NUTSTO> set) {
		StringBuffer sb = new StringBuffer();
		if(set.size() > 0 ){
			sb.append("FILTER ( ");
			int i = 0;
			for(NUTSTO nutsto:set){
				sb.append(" (?nutsCode = <"+nutsto.getUri()+"> ) ");
				i++;
				if(i< set.size()){
					sb.append (" || ");
				}
			}
			sb.append("). ");
		}	
		return sb.toString();
	}
	

	public static String createFilter(Set<PSCTO> set) {
		StringBuffer sb = new StringBuffer();
		if(set.size() > 0 ){
			sb.append("FILTER ( ");
			int i = 0;
			for(PSCTO pscTO:set){
				sb.append(" (?cpvCode = <"+pscTO.getUri()+"> ) ");
				i++;
				if(i< set.size()){
					sb.append (" || ");
				}
			}
			sb.append("). ");
		}	
		return sb.toString();
	}
	
	
	public static String createFilterScoredCodes(Set<ScoredPSCTO> scoredPSCCodes) {
		StringBuffer sb = new StringBuffer();
		if(scoredPSCCodes.size() > 0 ){
			sb.append("FILTER ( ");
			int i = 0;
			for(ScoredPSCTO scoredPscTO:scoredPSCCodes){
				sb.append(" (?cpvCode = <"+scoredPscTO.getPscTO().getUri()+"> ) ");
				i++;
				if(i< scoredPSCCodes.size()){
					sb.append (" || ");
				}
			}
			sb.append("). ");
		}	
		return sb.toString();
	}
	
	
	
	public static String createFilterYears(YearsTO years) {
		StringBuffer sb = new StringBuffer();
		if(years != null){
			sb.append("FILTER ( ");
			sb.append(" (xsd:long(?date) >=  xsd:long("+years.getMin()+")) && (xsd:long(?date) <=  xsd:long("+years.getMax()+"))");
			sb.append("). ");
		}
		return sb.toString();
	}
	
	public static String createRewritingQuery(String filterCPV, String filterNuts, int max, String filterYears){
		return DAOSPARQLService.NS+" " +  
		"SELECT ?ppn ?id  ?date ?cpvCode ?nutsCode  WHERE{ " +
		"?ppn rdf:type moldeas-onto:Notice. " +
		"?ppn moldeas-onto:located-in ?nutsCode. "+
		filterNuts+
		"?ppn dcterms:date ?date. " +
		filterYears+
		"?ppn moldeas-onto:topic ?cpvCode. " +
		filterCPV+
		"?ppn dcterms:identifier ?id. " +
		"} "+"LIMIT "+max ;
		
	}
	
	public static String createRewritingQuery(String filterCPV, String filterNuts, int max){
		return DAOSPARQLService.NS+" " +  
		"SELECT ?ppn ?id  ?date ?cpvCode ?nutsCode  WHERE{ " +
		"?ppn rdf:type moldeas-onto:Notice. " +
		"?ppn moldeas-onto:located-in ?nutsCode. "+
		filterNuts+
		"?ppn moldeas-onto:topic ?cpvCode. " +
		filterCPV+
		"?ppn dcterms:identifier ?id. " +
		"?ppn dcterms:date ?date. " +
		"} "+"LIMIT "+max ;
		
	}
	
	
	public static String createRawQuery(String filterCPV, String filterNuts, int max, String filterYears){
		return DAOSPARQLService.NS+" " +  
		"SELECT ?ppn ?id  ?date ?cpvCode ?nutsCode  WHERE{ " +
		"?ppn rdf:type moldeas-onto:Notice. " +
		"?ppn dcterms:identifier ?id. " +	
		"?ppn moldeas-onto:topic ?cpvCode. " +
		"?ppn moldeas-onto:located-in ?nutsCode. "+
		"?ppn dcterms:date ?date. " +
		filterCPV+
		filterNuts+
		filterYears+
		"} "+((max == -1)?"":"LIMIT "+max) ;
		
	}
		
	
}
