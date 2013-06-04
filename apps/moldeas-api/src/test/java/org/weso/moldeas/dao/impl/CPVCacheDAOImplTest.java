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

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.weso.moldeas.to.PSCTO;
import org.weso.pscs.utils.PSCConstants;

public class CPVCacheDAOImplTest {
	static CPVCacheDAOImpl dao ;
	@BeforeClass
     public static void setUp() {
		 dao = new CPVCacheDAOImpl();
     }
	@Test
	public void testDescribe() {
		PSCTO pscTO = new PSCTO();
		pscTO.setId("34513100");
		pscTO.setPrefLabel("Barcos pesqueros");
		pscTO.setUri(PSCConstants.formatId(pscTO.getId()));		
		PSCTO result = dao.describe(pscTO);
		assertEquals(result.getUri(),pscTO.getUri());
		assertEquals(result.getId(), pscTO.getId());
		assertEquals(result.getPrefLabel(), pscTO.getPrefLabel());
		assertEquals(result.getType(),PSCConstants.HTTP_PURL_ORG_WESO_CPV_DEF_CATEGORY);
	}

	@Test
	public void testGetPSCTOs() {
		dao.getPSCTOs();
	//	assertEquals(10000,dao.getPSCTOs().size());//FIXME
	}
	@Test
	public void testGetBroaderOf() {
		PSCTO pscTO = new PSCTO();
		pscTO.setId("34513100");
		pscTO.setPrefLabel("Barcos pesqueros");
		pscTO.setUri(PSCConstants.formatId(pscTO.getId()));
		List<PSCTO> result = dao.getNarrowersOf(pscTO);
		assertEquals(1, result.size());
		assertEquals(result.get(0).getType(),PSCConstants.HTTP_PURL_ORG_WESO_CPV_DEF_CATEGORY);
	}
	@Test
	public void testNarrowerCatLevel1Transitive() {
		PSCTO pscTO = new PSCTO();
		pscTO.setId("34513100");
		pscTO.setUri(PSCConstants.formatId(pscTO.getId()));
		List<PSCTO> result = dao.getNarrowersTransitiveOf(pscTO);
		assertEquals(1, result.size());	
	}
	@Test
	public void testNarrowerCatLevelTransitive() {
		PSCTO pscTO = new PSCTO();
		pscTO.setId("34513000");
		pscTO.setUri(PSCConstants.formatId(pscTO.getId()));
		List<PSCTO> result = dao.getNarrowersTransitiveOf(pscTO);
		assertEquals(14, result.size());	
	}
	@Test
	public void testNarrowerClassLevelTransitive() {
		PSCTO pscTO = new PSCTO();
		pscTO.setId("34510000");
		pscTO.setUri(PSCConstants.formatId(pscTO.getId()));
		List<PSCTO> result = dao.getNarrowersTransitiveOf(pscTO);
		assertEquals(40, result.size());	
	}
	
	@Test
	public void testNarrowerGroupLevelTransitive() {
		PSCTO pscTO = new PSCTO();
		pscTO.setId("34500000");
		pscTO.setUri(PSCConstants.formatId(pscTO.getId()));
		List<PSCTO> result = dao.getNarrowersTransitiveOf(pscTO);
		assertEquals(60, result.size());	
	}

	@Test
	public void testNarrowerDivisionLevelTransitive() {
		//Should be more quickly than first execution: 51seg.
		PSCTO pscTO = new PSCTO();
		pscTO.setId("34000000");
		pscTO.setUri(PSCConstants.formatId(pscTO.getId()));
		List<PSCTO> result = dao.getNarrowersTransitiveOf(pscTO);
		assertEquals(447, result.size());	
	}
	
	@Test
	public void testNarrowerDivision2LevelTransitive() {
		//Should be more quickly than first execution: 0,029
		PSCTO pscTO = new PSCTO();
		pscTO.setId("34000000");
		pscTO.setUri(PSCConstants.formatId(pscTO.getId()));
		List<PSCTO> result = dao.getNarrowersTransitiveOf(pscTO);
		assertEquals(447, result.size());	
	}

}
