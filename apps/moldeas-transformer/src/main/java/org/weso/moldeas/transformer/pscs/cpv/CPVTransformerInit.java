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
package org.weso.moldeas.transformer.pscs.cpv;

import org.weso.moldeas.transformer.pscs.ChainTransformerAdapter;
import org.weso.moldeas.utils.PrefixManager;
import org.weso.pscs.utils.PSCConstants;


public class CPVTransformerInit extends ChainTransformerAdapter {

	public static final String HTTP_PURL_ORG_GOODRELATIONS_V1_DESCRIPTION = "http://purl.org/goodrelations/v1#description";

	protected void execute() {
		this.model.createProperty(PSCConstants.SKOS_Concept);
		this.model.createProperty(PSCConstants.SKOS_prefLabel);
		this.model.createProperty(PSCConstants.SKOS_example);
		this.model.createProperty(PSCConstants.SKOS_Broader);		
		this.model.createProperty(PSCConstants.CPV2003_codeIn);
		this.model.createProperty(HTTP_PURL_ORG_GOODRELATIONS_V1_DESCRIPTION);
		this.model.createProperty(PSCConstants.SKOS_EXACT_MATCH);
		this.model.createProperty(PSCConstants.HTTP_PURL_ORG_WESO_PSCS_DEF_RELATED_MATCH);
		
	//	this.model.setNsPrefix("cpv2003", PSCConstants.HTTP_PURL_ORG_WESO_CPV_2003);
		this.model.setNsPrefix("cpv-def", PSCConstants.HTTP_PURL_ORG_WESO_CPV_DEF);
		this.model.setNsPrefix("skos", PSCConstants.HTTP_WWW_W3_ORG_2004_02_SKOS_CORE);
		this.model.setNsPrefix("gr", PrefixManager.getURIPrefix("gr"));
		
	}
	
	

	
}
