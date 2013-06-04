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

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.ProduceMime;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.weso.moldeas.to.NUTSTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.pscs.utils.PSCConstants;

@Path("/moldeas/psc")
public class PSCServiceRest {
	private MoldeasPSCServiceFacade facade;
	public PSCServiceRest(){
		this.facade = new MoldeasPSCServiceFacadeImpl();
	}
	@GET
	@Path("describe/{code}")
	@ProduceMime({"text/plain", "application/xml", "application/json"})
	public PSCTO describe(@PathParam("code") String code){	 
		try{
			PSCTO pscTO = new PSCTO();
			pscTO.setId(code);
			pscTO.setUri(PSCConstants.formatId(code));		   
			return this.facade.describe(pscTO);
		}catch(Exception e){
			 throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		
	}
	@GET
	@Path("listCodes")
	@ProduceMime({"text/plain", "application/xml", "application/json"})
	public PSCTOList listCodes(){	    
		try{
			return new PSCTOList(this.facade.getPSCTOs());
		}catch(Exception e){
			 throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
	
	@GET
	@Path("describe/nuts/{code}")
	@ProduceMime({"text/plain", "application/xml", "application/json"})
	public NUTSTO describeNUTSTO(@PathParam("code") String code){
		try{
			NUTSTO pscTO = new NUTSTO();
			pscTO.setCode(code);
			pscTO.setUri(PSCConstants.formatNUTSTO(code));		   
			return this.facade.describe(pscTO);
		}catch(Exception e){
			 throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
	@GET
	@Path("listNuts")
	@ProduceMime({"text/plain", "application/xml", "application/json"})
	public NUTSTOList getNUTSTOs(){
		try{
			return new NUTSTOList(this.facade.getNUTSTOs());
		}catch(Exception e){
			 throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
	
}
