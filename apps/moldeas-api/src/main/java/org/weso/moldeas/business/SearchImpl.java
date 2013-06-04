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
package org.weso.moldeas.business;

import org.weso.moldeas.appserv.SearchAppServ;
import org.weso.moldeas.to.EnhancedRequestSearchTO;
import org.weso.moldeas.to.PPNResultTO;
import org.weso.moldeas.to.RequestSearchTO;

public class SearchImpl implements Search {

	protected SearchAppServ searchAppServ;
	
	public SearchImpl(){
		this.searchAppServ = new SearchAppServ();
	}
	
	public SearchImpl(SearchAppServ searchAppServ){
		this.searchAppServ = searchAppServ;
	}

	@Override
	public PPNResultTO enhancedSearch(RequestSearchTO request) {
		return this.searchAppServ.enhancedSearch(request);
	}

	@Override
	public PPNResultTO search(RequestSearchTO request) {
		return this.searchAppServ.search(request);
	}

	
	public String createEnhancedSPARQLQuery(RequestSearchTO request){
		return this.searchAppServ.createEnhancedSPARQLQuery(request);
	}

	@Override
	public String createSimpleSPARQLQuery(RequestSearchTO request) {
		return this.searchAppServ.createSimpleSPARQLQuery(request);
	}

}