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

import static org.junit.Assert.*;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.weso.moldeas.to.NUTSTO;

public class NUTSDAOImplTest {

	@Test
	public void testDescribe() {
		NUTSDAOImpl dao = new NUTSDAOImpl();
		NUTSTO nutsTO = new NUTSTO();
		nutsTO.setUri("http://nuts.psi.enakting.org/id/UKL14");
		Assert.assertEquals(nutsTO.getUri(),dao.describe(nutsTO).getUri());
	}

	@Test
	public void testDescribeCountry() {
		NUTSDAOImpl dao = new NUTSDAOImpl();
		NUTSTO nutsTO = new NUTSTO();
		nutsTO.setUri("http://nuts.psi.enakting.org/id/ES");
		NUTSTO result =  dao.describe(nutsTO);
		Assert.assertEquals(nutsTO.getUri(),result.getUri());
	}
	
	@Test
	public void testDescribeCountryFR() {
		NUTSDAOImpl dao = new NUTSDAOImpl();
		NUTSTO nutsTO = new NUTSTO();
		nutsTO.setUri("http://nuts.psi.enakting.org/id/FR");
		NUTSTO result =  dao.describe(nutsTO);
		Assert.assertEquals(nutsTO.getUri(),result.getUri());
	}
	
	@Test
	public void testDescribeRegion() {
		NUTSDAOImpl dao = new NUTSDAOImpl();
		NUTSTO nutsTO = new NUTSTO();
		nutsTO.setUri("http://nuts.psi.enakting.org/id/ES12");//FIXME: Two labels: Asturias and Principado de Asturias, 2 codes repeat
		NUTSTO result =  dao.describe(nutsTO);
		Assert.assertEquals(nutsTO.getUri(),result.getUri());
	}
	
	
	@Test
	public void testGetNUTSTOs() {
		NUTSDAOImpl dao = new NUTSDAOImpl();
		List<NUTSTO> result = dao.getNUTSTOs();
		for(NUTSTO nutsTO:result){
			System.out.println(nutsTO);
		}
		Assert.assertEquals(1973, result.size());
	}

}
