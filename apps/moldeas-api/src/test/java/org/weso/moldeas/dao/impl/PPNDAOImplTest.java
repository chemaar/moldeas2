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
package org.weso.moldeas.dao.impl;

import static org.junit.Assert.fail;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.weso.moldeas.to.PPNTO;
import org.weso.pscs.utils.PSCConstants;

public class PPNDAOImplTest {

	@Test
	public void testDescribe() {
		PPNDAOImpl ppnDAO = new PPNDAOImpl();
		PPNTO ppnTO = new PPNTO();
		ppnTO.setId("339002");
		ppnTO.setYear("2008");
		ppnTO.setUri(PSCConstants.formatURIId(ppnTO.getId(), ppnTO.getYear()));
		PPNTO result = ppnDAO.describe(ppnTO);
		Assert.assertEquals(result.getId(), ppnTO.getId());
		Assert.assertEquals(result.getNutsCodes().size(), 2);
		Assert.assertEquals(result.getPscCodes().size(), 1);
	}

	@Test
	public void testGetPPNTOs() {
		PPNDAOImpl ppnDAO = new PPNDAOImpl();
		List<PPNTO> result = ppnDAO.getPPNTOs();
		Assert.assertEquals(357, result.size());
	}

	@Test
	public void testGetGraphUri() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPPNTOsByYear() {
		PPNDAOImpl ppnDAO = new PPNDAOImpl();
		PPNTO param = new PPNTO();
		param.setYear("2008");
		List<PPNTO> result = ppnDAO.getPPNTOsByYear(param);
		Assert.assertEquals(3213, result.size());
	}

}
