/**
 * 
 *   (c) Copyright 2011 WESO, Computer Science Department,
 *   Facultad de Ciencias, University of Oviedo, Oviedo, Asturias, Spain, 33007
 *   All rights reserved.
 *  
 *   Redistribution and use in source and binary forms, with or without
 *   modification, are permitted provided that the following conditions
 *   are met:
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *   3. The name of the author may not be used to endorse or promote products
 *      derived from this software without specific prior written permission.
 *  
 *   THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 *   IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *   OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 *   IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 *   INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 *   NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *   DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *   THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 *   THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 */
package org.weso.moldeas.business;

import static org.junit.Assert.assertEquals;
import junit.framework.Assert;

import org.junit.Test;
import org.weso.moldeas.appserv.PSCAppServ;
import org.weso.moldeas.psc.PSCFacade;
import org.weso.moldeas.psc.PSCFacadeTest;
import org.weso.moldeas.to.PSCTO;


public class PSCImplTest {
	@Test
	public void testDescribe() {
		PSCFacade pscFacade = PSCFacadeTest.createPSCFacade(); 
		PSCAppServ appServ = new PSCAppServ(pscFacade);
		PSC psc = new PSCImpl(appServ);
		PSCTO pscTO = new PSCTO("http://purl.org/weso/cpv/2008/14780000");
		PSCTO result = psc.describe(pscTO);
		Assert.assertEquals(result.getUri(), pscTO.getUri());
	}

	@Test
	public void testGetPSCTOs() {
		PSCFacade pscFacade = PSCFacadeTest.createPSCFacade(); 
		PSCAppServ appServ = new PSCAppServ(pscFacade);
		PSC psc = new PSCImpl(appServ);
		assertEquals(10357,psc.getPSCTOs().size());
	}
}
