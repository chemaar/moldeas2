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
package org.weso.moldeas.to;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "enhancedRequestSearchTO")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "enhancedRequestSearchTO", propOrder = {
    "scoredPSCCodes",
    "scoredNUTSTO",
    "scoredDurationTOs",
    "scoredLanguageTOs",
    "scoredTotalCostTOs",
    "scoredYearsTOs",
    "maxResults",
    "request"
})
public class EnhancedRequestSearchTO {

	private Set<ScoredDurationTO> scoredDurationTOs;
	private Set<ScoredLanguageTO> scoredLanguageTOs;
	private Set<ScoredNUTSTO> scoredNUTSTO;
	private Set<ScoredTotalCostTO> scoredTotalCostTOs;
	private Set<ScoredYearsTO> scoredYearsTOs;

	private Set<ScoredPSCTO> scoredPSCCodes;	
	
	private RequestSearchTO request;
	private int maxResults;
	
	
	public Set<ScoredNUTSTO> getScoredNUTSTO() {
		if(this.scoredNUTSTO==null){
			this.scoredNUTSTO = new HashSet<ScoredNUTSTO>();
		}
		return scoredNUTSTO;
	}

	public void setScoredNUTSTO(Set<ScoredNUTSTO> scoredNUTSTO) {
		this.scoredNUTSTO = scoredNUTSTO;
	}

	public Set<ScoredPSCTO> getScoredPSCCodes() {
		if(this.scoredPSCCodes == null){
			this.scoredPSCCodes = new HashSet<ScoredPSCTO>();
		}
		return scoredPSCCodes;
	}

	public void setScoredPSCCodes(Set<ScoredPSCTO> scoredPSCCodes) {
		this.scoredPSCCodes = scoredPSCCodes;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public RequestSearchTO getRequest() {
		return request;
	}

	public void setRequest(RequestSearchTO request) {
		this.request = request;
	}

	public Set<ScoredDurationTO> getScoredDurationTOs() {
		if(this.scoredDurationTOs==null){
			this.scoredDurationTOs = new HashSet<ScoredDurationTO>();
		}
		return scoredDurationTOs;
	}

	public void setScoredDurationTOs(Set<ScoredDurationTO> scoredDurationTOs) {
		this.scoredDurationTOs = scoredDurationTOs;
	}

	public Set<ScoredLanguageTO> getScoredLanguageTOs() {
		if(this.scoredLanguageTOs==null){
			this.scoredLanguageTOs = new HashSet<ScoredLanguageTO>();
		}
		return scoredLanguageTOs;
	}

	public void setScoredLanguageTOs(Set<ScoredLanguageTO> scoredLanguageTOs) {
		this.scoredLanguageTOs = scoredLanguageTOs;
	}

	public Set<ScoredTotalCostTO> getScoredTotalCostTOs() {
		if(this.scoredTotalCostTOs==null){
			this.scoredTotalCostTOs = new HashSet<ScoredTotalCostTO>();
		}
		return scoredTotalCostTOs;
	}

	public void setScoredTotalCostTOs(Set<ScoredTotalCostTO> scoredTotalCostTOs) {
		this.scoredTotalCostTOs = scoredTotalCostTOs;
	}

	public Set<ScoredYearsTO> getScoredYearsTOs() {
		if(this.scoredYearsTOs==null){
			this.scoredYearsTOs = new HashSet<ScoredYearsTO>();
		}
		return scoredYearsTOs;
	}

	public void setScoredYearsTOs(Set<ScoredYearsTO> scoredYearsTOs) {
		this.scoredYearsTOs = scoredYearsTOs;
	}




	
}
