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

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "nutsTO")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nutsTO", propOrder = {
    "code",
    "uri",
    "label",
    "kml",
    "containedBy",    
    "languages"
})
public class NUTSTO {

	private String code;
	private String uri;
	private String label;
	private String kml;
	private String containedBy;
	private Set<LanguageTO> languages;

	public String getCode() {
		return code;
	}

	public void setCode(String id) {
		this.code = id;
	}
	public NUTSTO(String uri,String id) {
		super();
		this.uri = uri;
		this.code = id;
	}


	public NUTSTO(String uri) {
		super();
		this.uri = uri;
	}

	public NUTSTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Set<LanguageTO> getLanguages() {
		return languages;
	}

	public void setLanguages(Set<LanguageTO> languages) {
		this.languages = languages;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getKml() {
		return kml;
	}

	public void setKml(String kml) {
		this.kml = kml;
	}

	public String getContainedBy() {
		return containedBy;
	}

	public void setContainedBy(String containedBy) {
		this.containedBy = containedBy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((containedBy == null) ? 0 : containedBy.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((kml == null) ? 0 : kml.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result
				+ ((languages == null) ? 0 : languages.hashCode());
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
		NUTSTO other = (NUTSTO) obj;
		if (containedBy == null) {
			if (other.containedBy != null)
				return false;
		} else if (!containedBy.equals(other.containedBy))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (kml == null) {
			if (other.kml != null)
				return false;
		} else if (!kml.equals(other.kml))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (languages == null) {
			if (other.languages != null)
				return false;
		} else if (!languages.equals(other.languages))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NUTSTO [id=" + code + ", uri=" + uri + ", label=" + label
				+ ", kml=" + kml + ", containedBy=" + containedBy
				+ ", languages=" + languages + "]";
	}

	
	
	
}
