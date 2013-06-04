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
package org.weso.moldeas.enhancers.psc;

import java.util.Set;

import org.apache.log4j.Logger;
import org.weso.moldeas.enhancers.EnhancerAdapter;
import org.weso.moldeas.psc.PSCFacade;
import org.weso.moldeas.to.PSCSearchResultTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.utils.ApplicationContextLocator;

public class LucenePSCCodesEnhancer extends EnhancerAdapter {

	protected static Logger logger = Logger.getLogger(LucenePSCCodesEnhancer.class);
	private PSCFacade facade;

	public LucenePSCCodesEnhancer(){
		this.facade = (PSCFacade) ApplicationContextLocator.getApplicationContext().
		getBean(PSCFacade.class.getSimpleName());
	}
	
	public LucenePSCCodesEnhancer(PSCFacade facade){
		this.facade = facade;
	}
	@Override
	protected void execute() throws Exception {
		String query = buildQuery(this.request.getPscCodes());
		logger.debug("LucenePSCCodesEnhancer query: "+query);
		PSCSearchResultTO results = this.facade.search(query);
	    this.enhancedRequest.setScoredPSCCodes(results.getScoredscTOs());
	}

	private String buildQuery(Set<PSCTO> set) {
		StringBuffer buffer = new StringBuffer();
		for(PSCTO code: set){			
			buffer.append(code.getPrefLabel());
			if(set.size()>1){
				buffer.append(" ");
			}
		}
		return buffer.toString();
	}
	@Override
	protected void preExecute() throws Exception {
		//FIXME: the psctos has the prefLabel or not? in which language?
		for(PSCTO code: this.request.getPscCodes()){
			if (code.getPrefLabel()==null || code.getPrefLabel().equals("")){
				code.setPrefLabel(this.facade.describe(code).getPrefLabel());
			}
		}

	}

	@Override
	protected void postExecute() throws Exception {
		// TODO Auto-generated method stub

	}

}
