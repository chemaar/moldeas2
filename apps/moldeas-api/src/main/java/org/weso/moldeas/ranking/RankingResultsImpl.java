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
package org.weso.moldeas.ranking;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.weso.moldeas.to.PPNResultTO;
import org.weso.moldeas.to.PPNTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.ScoredPPNTO;
import org.weso.moldeas.to.ScoredPSCTO;

public class RankingResultsImpl implements RankingResults {

	@Override
	public PPNResultTO rank(PPNResultTO rawResult) {
		Map<String, Double> values = createTable(rawResult.getEnhancedRequest().getScoredPSCCodes());
		for(ScoredPPNTO scored:rawResult.getScoredPnnsTO()){
			double score = getScoredOf(scored.getPpnTO(),values);
			scored.setScore(score );
		}
		return rawResult;
	}

	private Map<String, Double> createTable(Set<ScoredPSCTO> scoredPSCCodes) {
		Map<String, Double> values = new HashMap<String, Double>();
		for(ScoredPSCTO scored:scoredPSCCodes){
			values.put(scored.getPscTO().getUri(), scored.getScore());
		}
		return values;
	}

	private double getScoredOf(PPNTO ppnTO, Map<String, Double> values) {
		double value = 0;
		for(PSCTO pscTO: ppnTO.getPscCodes()){
			double score = (values.get(pscTO.getUri())!=null?values.get(pscTO.getUri()):0);
			value += score;
		}
		return value;
	}

	private double searchScore(PSCTO pscTO, Set<ScoredPSCTO> scoredPSCCodes) {
		for(ScoredPSCTO scored:scoredPSCCodes){
			if(scored.getPscTO().equals(pscTO)){
				return scored.getScore();
			}
		}
		return 0;
	}

}
