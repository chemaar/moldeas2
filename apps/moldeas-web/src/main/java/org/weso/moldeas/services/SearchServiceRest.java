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
package org.weso.moldeas.services;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.ProduceMime;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.weso.moldeas.dao.DAOSPARQLService;
import org.weso.moldeas.to.DurationTO;
import org.weso.moldeas.to.NUTSTO;
import org.weso.moldeas.to.PPNResultTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.RequestSearchTO;
import org.weso.moldeas.to.TotalCostTO;
import org.weso.moldeas.to.YearsTO;
import org.weso.pscs.utils.PSCConstants;



@Path("/moldeas")
public class SearchServiceRest {
	private static final int MAX_RESULTS = 1000;

	protected static Logger logger = Logger.getLogger(SearchServiceRest.class);

	private MoldeasServiceFacade facade;
	public SearchServiceRest(){
		this.facade = new MoldeasServiceFacadeImpl();
	}
	@GET
	@Path("search/code/{code}")
	@ProduceMime({"text/plain", "application/xml", "application/json"})
	public PPNResultTO searchByCode(@PathParam("code") String code){	  
		try{
			logger.info("Searching by code "+code);
			PSCTO pscTO = new PSCTO();
			pscTO.setId(code);
			pscTO.setUri(PSCConstants.formatId(code));		   
			RequestSearchTO request = new RequestSearchTO();
			request.getPscCodes().add(pscTO);
			request.setMaxResults(MAX_RESULTS);
			return this.facade.search(request);
		}catch (Exception e){
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}

	}
	@GET
	@Path("searchByUri")
	@ProduceMime({"text/plain", "application/xml", "application/json"})
	public PPNResultTO searchByUri(@QueryParam("uri") String uri){	  
		try{
			PSCTO pscTO = new PSCTO();
			pscTO.setUri(uri);		   
			RequestSearchTO request = new RequestSearchTO();
			request.getPscCodes().add(pscTO);
			request.setMaxResults(MAX_RESULTS);
			return this.facade.search(request);
		}catch (Exception e){
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
	@GET
	@Path("search/enhanced/{code}")
	@ProduceMime({"text/plain", "application/xml", "application/json"})
	public PPNResultTO enhancedSearchByCode(@PathParam("code") String code){	  
		try{
			PSCTO pscTO = new PSCTO();
			pscTO.setId(code);
			pscTO.setUri(PSCConstants.formatId(code));		   
			RequestSearchTO request = new RequestSearchTO();
			request.getPscCodes().add(pscTO);
			request.setMaxResults(MAX_RESULTS);
			return this.facade.enhancedSearch(request);
		}catch (Exception e){
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}

	}
	
	@GET
	@Path("search")
	@ProduceMime({"text/plain", "application/xml", "application/json"})
	public PPNResultTO search(@QueryParam("cpvCodes") String cpvCodes,
			@QueryParam("nutsCodes") String nutsCodes,
			@QueryParam("minYear") String minYear,
			@QueryParam("maxYear") String maxYear,
			@QueryParam("minCost") String minCost,
			@QueryParam("maxCost") String maxCost,
			@QueryParam("minDuration") String minDuration,
			@QueryParam("maxDuration") String maxDuration,
			@QueryParam("typeEnterprise") String typeEnterprise
			){	  
		try{
			System.out.println(cpvCodes+" "+nutsCodes+" "+minYear+" "+maxYear+" "+minCost+" "+maxCost+" "+minDuration+" "+maxDuration+" "+typeEnterprise);
			RequestSearchTO request = new RequestSearchTO();
			String []cpvRawCodes = cpvCodes.split(",");
			for(int i = 0; i<cpvRawCodes.length;i++){
				PSCTO pscTO = new PSCTO();
				pscTO.setId(cpvRawCodes[i]);
				pscTO.setUri(PSCConstants.formatId(cpvRawCodes[i]));	
				request.getPscCodes().add(pscTO);
			}
			String []nutsRawCodes = nutsCodes.split(",");
			for(int i = 0; !nutsCodes.equals("") && i<nutsRawCodes.length;i++){
				NUTSTO nutsTO = new NUTSTO();
				nutsTO.setCode(nutsRawCodes[i]);
				nutsTO.setUri(PSCConstants.formatNUTSTO((nutsRawCodes[i])));	
				request.getNutsCodes().add(nutsTO);
			}
			DurationTO duration = new DurationTO();
			duration.setMin(Long.valueOf(minDuration));
			duration.setMax(Long.valueOf(maxDuration));
			request.setDuration(duration);
			TotalCostTO totalCost = new TotalCostTO();
			totalCost.setMin(Long.valueOf(minCost));
			totalCost.setMax(Long.valueOf(maxCost));
			request.setTotalCost(totalCost );
			YearsTO years = new YearsTO();
			years.setMin(Long.valueOf(minYear));
			years.setMax(Long.valueOf(maxYear));
			request.setYears(years);
			//return new PPNResultTO();
			//request.setMaxResults(10000);
			request.setMaxResults(MAX_RESULTS);
			return this.facade.enhancedSearch(request); //FIXME: Check parameters			
		}catch (Exception e){			
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}

	}
	
	
	@GET
	@Path("search/createQuery/code/{code}")
	@ProduceMime({"text/plain", "application/xml", "application/json"})
	public QueryTO createQuery(@PathParam("code") String code){	  
		try{
			PSCTO pscTO = new PSCTO();
			pscTO.setId(code);
			pscTO.setUri(PSCConstants.formatId(code));		   
			RequestSearchTO request = new RequestSearchTO();
			request.getPscCodes().add(pscTO);
			request.setMaxResults(MAX_RESULTS);
			return new QueryTO(this.facade.createSimpleSPARQLQuery(request));
		}catch (Exception e){
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}

	}
	
	@GET
	@Path("search/createQuery/enhanced/{code}")
	@ProduceMime({"text/plain", "application/xml", "application/json"})
	public QueryTO createQueryEnhanced(@PathParam("code") String code){	  
		try{
			PSCTO pscTO = new PSCTO();
			pscTO.setId(code);
			pscTO.setUri(PSCConstants.formatId(code));		   
			RequestSearchTO request = new RequestSearchTO();
			request.getPscCodes().add(pscTO);
			request.setMaxResults(MAX_RESULTS);
			return new QueryTO(this.facade.createEnhancedSPARQLQuery(request));
		}catch (Exception e){
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}

	}
}
