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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.weso.moldeas.enhancers.EnhancerAdapter;
import org.weso.moldeas.psc.PSCFacade;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.utils.ApplicationContextLocator;
import org.weso.moldeas.to.ScoredPSCTO;
import org.weso.pscs.utils.PSCConstants;

public class PSCNarrowerEnhancer extends EnhancerAdapter {

	public static final float INITIAL_SCORE = 1;

	
	protected static Logger logger = Logger.getLogger(PSCNarrowerEnhancer.class);
	private PSCFacade facade;

	public PSCNarrowerEnhancer(){
		this.facade = (PSCFacade) ApplicationContextLocator.getApplicationContext().
		getBean(PSCFacade.class.getSimpleName());
	}
	
	public PSCNarrowerEnhancer(PSCFacade facade){
		this.facade = facade;
	}
	
	@Override
	protected void execute() throws Exception {
		// For each concept in the request
		// collect the narrowers
		// Assign value to each concept in scored concepts
		// Let be Q the set of request concepts with a cardinality of Nq
		// Let be Q' the set of narrower concepts of a concept C. U Q' i=1..J is the complete set of narrower concepts
		// The score  Sc of a concept C E a Q' = Sc0 + Vl / card(Q') where Vl = 1/d, d is the level in the hierarchy, with broader is = 2.
		// Sc0 = 0 or K depending of the level in the hierarchy
		// Questions?
		// card(Q') or card (U Q') 
		// The card (Q') depends for the reward
		// Set value depending on the level in the hierarchy: Division, Group, Class, Category L1, Category L2, Category L3, Category L4		
		//List<PSCTO> pscTOs = this.facade.getNarrowersOf(pscTO);
		Map<String,ScoredPSCTO> scoredConcepts = new HashMap<String, ScoredPSCTO>();
		double Vl = 1; //FIXME 10,38 with 10,68 but 3 zeros
		for(PSCTO pscTO:this.request.getPscCodes()){
			double initialScore = (Double) (this.request.getSwap().containsKey((pscTO.getUri()))?
					this.request.getSwap().get(pscTO.getUri()):
						INITIAL_SCORE);
			ScoredPSCTO scoredPSCTO = new ScoredPSCTO(pscTO, initialScore);
			scoredConcepts.put(scoredPSCTO.getPscTO().getUri(), scoredPSCTO);
			List<PSCTO> narrowers = this.facade.getNarrowersOf(pscTO);
			int Nnarrowers = narrowers.size(); //card (Q')
			for(PSCTO narrower:narrowers){
				ScoredPSCTO narrowerScored = scoredConcepts.get(narrower.getUri());
				if(narrowerScored == null){					
					double score = Vl/(Nnarrowers+getTypeValue(narrower.getType()));
					narrowerScored = new ScoredPSCTO(narrower, score);
					scoredConcepts.put(narrowerScored.getPscTO().getUri(), narrowerScored);
				}else{
					double score = narrowerScored.getScore()+Vl/Nnarrowers;
					narrowerScored.setScore(score);
				}
			}
		}
		this.enhancedRequest.getScoredPSCCodes().addAll(scoredConcepts.values());
	}

	public static double getTypeValue(String type) {
		if(type.equals(PSCConstants.HTTP_PURL_ORG_WESO_CPV_DEF_DIVISION)){
			return 0.25;
		}else if (type.equals(PSCConstants.HTTP_PURL_ORG_WESO_CPV_DEF_GROUP)){
			return 0.5;
		}else if (type.equals(PSCConstants.HTTP_PURL_ORG_WESO_CPV_DEF_CLASS)){
			return 0.75;
		}			
		return 1;
	}

	@Override
	protected void preExecute() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void postExecute() throws Exception {
		// TODO Auto-generated method stub

	}

}
