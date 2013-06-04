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

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.collections.map.HashedMap;

@XmlRootElement(name = "requestSearchTO")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "requestSearchTO", propOrder = {
    "pscCodes",
    "totalCost",
    "duration",
    "years",
   // "enterpriseType",
    "nutsCodes",
    "state",
    "maxResults",
    "stringQuery"
})
public class RequestSearchTO {

	private Set<PSCTO> pscCodes;
	private TotalCostTO totalCost;
	private DurationTO duration;
	private YearsTO years;
	//private EnterpriseTypeTO enterpriseType;
	private Set<NUTSTO> nutsCodes;
	private StateTO state;
	private int maxResults = 1;
	private String stringQuery;
	private Map swap;	//FIXME: do not serialize
	
	public int getMaxResults() {
		return maxResults;
	}
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}
	public Set<PSCTO> getPscCodes() {
		if (this.pscCodes == null){
			this.pscCodes = new HashSet<PSCTO>();
		}
		return pscCodes;
	}
	public void setPscCodes(Set<PSCTO> pscCodes) {
		this.pscCodes = pscCodes;
	}
	public TotalCostTO getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(TotalCostTO totalCost) {
		this.totalCost = totalCost;
	}
	public DurationTO getDuration() {
		return duration;
	}
	public void setDuration(DurationTO duration) {
		this.duration = duration;
	}
//	
	public Set<NUTSTO> getNutsCodes() {
		if (this.nutsCodes == null){
			this.nutsCodes = new HashSet<NUTSTO>();
		}
		return nutsCodes;
	}
	public void setNutsCodes(Set<NUTSTO> nutsIds) {
		this.nutsCodes = nutsIds;
	}
	public StateTO getState() {
		return state;
	}
	public void setState(StateTO state) {
		this.state = state;
	}
	public YearsTO getYears() {
		return years;
	}
	public void setYears(YearsTO years) {
		this.years = years;
	}
	public String getStringQuery() {
		return this.stringQuery;
	}
	public void setStringQuery(String stringQuery) {
		this.stringQuery = stringQuery;
	}
	public Map getSwap() {
		if(this.swap == null){
			this.swap = new HashMap();
		}
		return swap;
	}
	public void setSwap(Map swap) {
		this.swap = swap;
	}
	
}
