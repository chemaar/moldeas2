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

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
@XmlRootElement(name = "ppnTO")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ppnTO", propOrder = {
    "uri",
    "id",
    "year",
    "pscCodes",
    "totalCost",
    "duration",
   // "enterpriseType",
    "nutsCodes",
    "state"
})
public class PPNTO {


	private String uri;
	//The same variables as RequestSearchTO
	private String id; //dc:identifier
	private String year; //dc:date
	private Set<PSCTO> pscCodes; //codeIn2008
	private TotalCostTO totalCost; //not mapped
	private DurationTO duration; //not mapped
	//private EnterpriseTypeTO enterpriseType; //not mapped
	private Set<NUTSTO> nutsCodes; //nutsCode
	private StateTO state;
		public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	

	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public Set<PSCTO> getPscCodes() {
		if(this.pscCodes==null){
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


	public Set<NUTSTO> getNutsCodes() {
		if(this.nutsCodes==null){
			this.nutsCodes = new HashSet<NUTSTO>();
		}
		return nutsCodes;
	}
	public void setNutsCodes(Set<NUTSTO> nutsCodes) {
		this.nutsCodes = nutsCodes;
	}
	public StateTO getState() {
		return state;
	}
	public void setState(StateTO state) {
		this.state = state;
	}

	public PPNTO(String uri) {
		super();
		this.uri = uri;
	}
	public PPNTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public PPNTO(String id, String uri) {
		super();
		this.id = id;
		this.uri = uri;
	}
	@Override
	public String toString() {
		return "PPNTO [uri=" + uri + ", id=" + id + ", year=" + year
				+ ", pscCodes=" + pscCodes + ", totalCost=" + totalCost
				+ ", duration=" + duration + ", enterpriseType="
				+ "enterpriseType" + ", nutsCodes=" + nutsCodes + ", state="
				+ state + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PPNTO other = (PPNTO) obj;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}



}
