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

/**
 * Public Procurement Notice TO.
 * 
 * It should be contain the same information as a request: language, region, cost, etc.
 * 
 * @author chema
 *
 */
@XmlRootElement(name = "ppnResultTO")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ppnResultTO", propOrder = {
	"totalResults",
    "request",
    "enhancedRequest",
    "scoredPnnsTO",
    "time"
})
public class PPNResultTO {

	private long totalResults = 0;
	private RequestSearchTO request;
	private EnhancedRequestSearchTO enhancedRequest;
    private Set<ScoredPPNTO> scoredPnnsTO;
    private long time;
	public RequestSearchTO getRequest() {
		return request;
	}
	public void setRequest(RequestSearchTO request) {
		this.request = request;
	}

	public PPNResultTO(RequestSearchTO request, Set<ScoredPPNTO> ppns) {
		super();
		this.request = request;
		this.scoredPnnsTO = ppns;
	}
	
	public Set<ScoredPPNTO> getScoredPnnsTO() {
		if(this.scoredPnnsTO==null){
			this.scoredPnnsTO = new HashSet<ScoredPPNTO>();
		}
		return scoredPnnsTO;
	}
	public void setScoredPnnsTO(Set<ScoredPPNTO> scoredPnnsTO) {
		this.scoredPnnsTO = scoredPnnsTO;
	}
	public PPNResultTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public EnhancedRequestSearchTO getEnhancedRequest() {
		return enhancedRequest;
	}
	public void setEnhancedRequest(EnhancedRequestSearchTO enhancedRequest) {
		this.enhancedRequest = enhancedRequest;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public long getTotalResults() {
		return this.totalResults;
	}
	public void setTotalResults(long totalResults) {
		this.totalResults = totalResults;
	}
	
}
