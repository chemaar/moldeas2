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
package org.weso.moldeas.appserv;

import static org.junit.Assert.*;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.weso.moldeas.dao.impl.PPNDAOImpl;
import org.weso.moldeas.to.PPNTO;
import org.weso.pscs.utils.PSCConstants;

public class PPNAppServTest {

	@Test
	public void testDescribe() {
		PPNAppServ appServ = new PPNAppServ(new PPNDAOImpl());
		PPNTO ppnTO = new PPNTO();
		ppnTO.setId("339002");
		ppnTO.setYear("2008");
		ppnTO.setUri(PSCConstants.formatURIId(ppnTO.getId(), ppnTO.getYear()));
		PPNTO result = appServ.describe(ppnTO);
		Assert.assertEquals(result.getId(), ppnTO.getId());
		Assert.assertEquals(result.getNutsCodes().size(), 2);
		Assert.assertEquals(result.getPscCodes().size(), 1);
	}

	@Test
	public void testGetPPNTOsByYear() {
		PPNAppServ appServ = new PPNAppServ(new PPNDAOImpl());
		PPNTO ppnTO = new PPNTO();
		ppnTO.setYear("2008");
		List<PPNTO> result = appServ.getPPNTOsByYear(ppnTO);
		Assert.assertEquals(357, result.size());
	}

}
