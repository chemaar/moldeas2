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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.weso.moldeas.to.PSCTO;
import org.weso.pscs.utils.PSCConstants;

public class CPV2008DAOImplTest {

	CPVDAOImpl dao = new CPV2008DAOImpl();
	
	protected PSCTO getPSCTO(String id){
		PSCTO pscTO = new PSCTO();
		pscTO.setId(id);
		pscTO.setUri(PSCConstants.formatId(pscTO.getId()));		
		return dao.describe(pscTO);
	}
//	@Test
//	public void testDescribe() {
//		PSCTO pscTO = new PSCTO();
//		pscTO.setId("34513100");
//		pscTO.setPrefLabel("Fishing vessels");
//		pscTO.setUri(PSCConstants.formatId(pscTO.getId()));
//		CPVDAOImpl dao = new CPV2008DAOImpl();
//		PSCTO result = dao.describe(pscTO);
//		assertEquals(result.getUri(),pscTO.getUri());
//		assertEquals(result.getId(), pscTO.getId());
//		assertEquals(result.getPrefLabel(), pscTO.getPrefLabel());
//		assertEquals(result.getType(),PSCConstants.HTTP_PURL_ORG_WESO_CPV_DEF_CATEGORY);
//
//	}
	
	public void testDescribeAll() {
		Set<String> fullCodes = new HashSet<String>();
		String[] ids = new String[]{
				"45211000",
				"45233000",
				"45215140",
				"45252126",
				"77231600",
				"90712200"				
		};		
		for (int i = 0; i<ids.length;i++){
			fullCodes.addAll(getFullNarrowers(ids[i]));					
		}	
		
	}
	
	protected Set<String> getFullNarrowers(String id){
		Set<String> fullCodes = new HashSet<String>();		
		PSCTO current = this.getPSCTO(id);
		fullCodes.add(id);
		if (current.getNarrowers().size() != 0){
			for(PSCTO narrower:current.getNarrowers()){
				fullCodes.addAll(getFullNarrowers(narrower.getId()));	
			}			
		}
		return fullCodes;
	}
	protected Set<String> generateExpectedQuery(String []ids){
		Set<String> fullCodes = new HashSet<String>();
		for (int i = 0; i<ids.length;i++){
			fullCodes.addAll(getFullNarrowers(ids[i]));					
		}
		return fullCodes;
	}
	
	@Test
	public void generateExpectedQuery_1(){
		String[] ids = new String[]{
				"03416000",
				"09330000",
				"14630000",
				"15832000",
				"19600000",
				"31121300",
				"34144500",
				"34928480",
				"41000000",
				"42122220",
				"42320000",
				"42996000",
				"44131000",
				"44613700",
				"44613800",
				"44616200",
				"45222100",
				"45231300",
				"45232400",
				"45252000",
				"45259100",
				"45262640",
				"51135110",
				"71313000",
				"71314300",
				"71800000",
				"79723000",
				"80540000",
				"90000000",
				"98342000"		
		};	
		try {
			serialize("q1-expected",generateExpectedQuery(ids));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
		
	@Test
	public void generateExpectedQuery_2(){
		String[] ids = new String[]{
				"45211000",
				"45233000",
				"45215140",
				"45252126",
				"77231600",
				"90712200"				
		};	
		try {
			serialize("q2-expected",generateExpectedQuery(ids));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
	@Test
	public void generateExpectedQuery_3(){
		String[] ids = new String[]{
				"44211000"				
		};	
		try {
			serialize("q3-expected",generateExpectedQuery(ids));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
	
	@Test
	public void generateExpectedQuery_4(){
		String[] ids = new String[]{
				"43325000",
				"45112723",
				"37530000",
				"37535100",
				"37535200",
				"50870000"
		};	
		try {
			serialize("q4-expected",generateExpectedQuery(ids));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}

	//(cpv:33124100 OR cpv:33124110 OR cpv:33195100 OR cpv:33195000 OR (cpv:331* AND (“constantes vitales” OR “monitor constantes”))) AND nut:ES*
	@Test
	public void generateExpectedQuery_5(){
		String[] ids = new String[]{
				"33124100",
				"33124110",
				"33195100",
				"33195000",
				"33100000"
		};	
		try {
			serialize("q5-expected",generateExpectedQuery(ids));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
	@Test
	public void generateExpectedQuery_6(){
		String[] ids = new String[]{
				"92521100"
		};	
		try {
			serialize("q6-expected",generateExpectedQuery(ids));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
	
	@Test
	public void generateExpectedQuery_7(){
		String[] ids = new String[]{
				"38341300",
				"38400000",
				"31342000",
				"31224800",
				"44512000"
		};	
		try {
			serialize("q7-expected",generateExpectedQuery(ids));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
	//(cpv:45235111 OR cpv:45235210 OR cpv:45235311 OR cpv:452332* OR cpv:452333* OR cpv:45221119 OR cpv:45221248
	@Test
	public void generateExpectedQuery_8(){
		String[] ids = new String[]{
				"45235111",
				"45235210",
				"45235311",
				"45233200",
				"45221119",
				"45221248"
		};	
		try {
			serialize("q8-expected",generateExpectedQuery(ids));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
	
	//(cpv:442122* OR cpv:02181* OR cpv:34947* OR cpv:20111*)
	@Test
	public void generateExpectedQuery_9(){
		String[] ids = new String[]{
				"44212200",
				//"02181000",
				"34947000"
				//"20111000"
		};	
		try {
			serialize("q9-expected",generateExpectedQuery(ids));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
	
	@Test
	public void generateExpectedQuery_10(){
		String[] ids = new String[]{
				"71000000"
		};	
		try {
			serialize("q10-expected",generateExpectedQuery(ids));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
	
	@Test
	public void generateExpectedQuery_11(){
		String[] ids = new String[]{
				"85120000"
		};	
		try {
			serialize("q11-expected",generateExpectedQuery(ids));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
//HELPER
	
	protected void serialize(String filename, Set<String> codes) throws FileNotFoundException{
		PrintWriter pw = new PrintWriter(new File("/home/chema/tmp/queries/"+filename));
		System.out.println("Serializing to "+filename);
		for(String code:codes){
			pw.println(code);
		}
		pw.close();
	}
	
//	@Test
//	public void testGetPSCTOs() {
//		CPVDAOImpl dao = new CPV2008DAOImpl();
//		assertEquals(10000,dao.getPSCTOs().size());
//	}
//
//	@Test
//	public void testGetBroaderOf() {
//		CPVDAOImpl dao = new CPV2008DAOImpl();
//		PSCTO pscTO = new PSCTO();
//		pscTO.setId("34513100");
//		pscTO.setPrefLabel("Barcos pesqueros");
//		pscTO.setUri(PSCConstants.formatId(pscTO.getId()));
//		List<PSCTO> result = dao.getNarrowersOf(pscTO);
//		assertEquals(1, result.size());
//		assertEquals(result.get(0).getType(),PSCConstants.HTTP_PURL_ORG_WESO_CPV_DEF_CATEGORY);
//
//	}
	
}
