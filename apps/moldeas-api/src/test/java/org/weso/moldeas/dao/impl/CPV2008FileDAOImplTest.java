package org.weso.moldeas.dao.impl;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.weso.moldeas.dao.PSCDAO;
import org.weso.moldeas.to.PSCTO;
import org.weso.pscs.utils.PSCConstants;


public class CPV2008FileDAOImplTest {

//	@Test
//	public void testGetPSCTOs(){
//		List<PSCTO> pscTOs = new CPV2008FileDAOImpl().getPSCTOs();		
//		for(PSCTO pscTO: pscTOs){
//			System.out.println(pscTO);
//		}
//		Assert.assertEquals(10357,pscTOs.size());
//	}
//	
//	@Test
//	public void testGetPSCTOsLabels() throws FileNotFoundException{
//		List<PSCTO> pscTOs = new CPV2008FileDAOImpl().getPSCTOs();
//		PrintWriter pw = new PrintWriter(new File("cpv-codes-label.txt"));
//		for(PSCTO pscTO: pscTOs){
//			pw.println(pscTO.getId()+", "+pscTO.getUri()+", "+pscTO.getPrefLabel());
//		}
//		Assert.assertEquals(10357,pscTOs.size());
//		pw.close();
//	}
	
	@Test
	public void testDescribe() {
		PSCTO pscTO = new PSCTO();
		pscTO.setId("34513100");
		pscTO.setPrefLabel("Barcos pesqueros");
		pscTO.setUri(PSCConstants.formatId(pscTO.getId()));
		PSCDAO dao = new CPV2008FileDAOImpl();
		PSCTO result = dao.describe(pscTO);
		System.out.println(result);
		assertEquals(result.getUri(),pscTO.getUri());
		assertEquals(result.getId(), pscTO.getId());
		assertEquals(result.getPrefLabel(), pscTO.getPrefLabel());
		assertEquals(result.getType(),PSCConstants.HTTP_PURL_ORG_WESO_CPV_DEF_CATEGORY);

	}
}
