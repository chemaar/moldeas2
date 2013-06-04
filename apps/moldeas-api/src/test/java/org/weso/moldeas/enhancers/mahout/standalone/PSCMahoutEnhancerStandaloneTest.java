package org.weso.moldeas.enhancers.mahout.standalone;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import junit.framework.Assert;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.junit.Test;
import org.weso.moldeas.dao.WrapperDataModel;
import org.weso.moldeas.dao.WrapperDataModelFileImpl;
import org.weso.moldeas.to.EnhancedRequestSearchTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.RequestSearchTO;
import org.weso.moldeas.to.ScoredPSCTO;
import org.weso.moldeas.utils.ShowUtils;
import org.weso.pscs.utils.PSCConstants;


public class PSCMahoutEnhancerStandaloneTest {

	protected static  RequestSearchTO createRequest(){
		RequestSearchTO request = new RequestSearchTO();
		PSCTO pscTO = new PSCTO();
		pscTO.setId("72222300");
		pscTO.setUri(PSCConstants.formatId(pscTO.getId()));
		request.getPscCodes().add(pscTO);
		return request;
	}

	
	private EnhancedRequestSearchTO executeTest(WrapperDataModel model) {
		PSCMahoutEnhancerStandalone recommender = new PSCMahoutEnhancerStandalone(model);		
		EnhancedRequestSearchTO result = recommender.enhance(createRequest() );
		ShowUtils.showResults(result);
		return result;
	}
	
	@Test
	public void testRecommendations_1_2008() throws IOException{
		WrapperDataModel model = new WrapperDataModelFileImpl("/home/chema/tesis/test/generated/stats_1_2008.mht");
		EnhancedRequestSearchTO result = executeTest(model);		
		Assert.assertEquals(1, result.getScoredPSCCodes().size());
		Assert.assertEquals(2, result.getScoredNUTSTO().size());
	}
	
	
	@Test
	public void testRecommendations_1_2009() throws IOException{
		WrapperDataModel model = new WrapperDataModelFileImpl("/home/chema/tesis/test/generated/stats_1_2009.mht");
		EnhancedRequestSearchTO result = executeTest(model);		
		Assert.assertEquals(2, result.getScoredPSCCodes().size());
		Assert.assertEquals(1, result.getScoredNUTSTO().size());
	}
	
	
	@Test
	public void testRecommendations_1_2010() throws IOException{
		WrapperDataModel model = new WrapperDataModelFileImpl("/home/chema/tesis/test/generated/stats_1_2010.mht");
		EnhancedRequestSearchTO result = executeTest(model);		
		Assert.assertEquals(1, result.getScoredPSCCodes().size());
	}
	
	@Test
	public void testRecommendations_1_2011() throws IOException{
		WrapperDataModel model = new WrapperDataModelFileImpl("/home/chema/tesis/test/generated/stats_1_2011.mht");
		EnhancedRequestSearchTO result = executeTest(model);		
		Assert.assertEquals(1, result.getScoredPSCCodes().size());
	}
	
	@Test
	public void testRecommendations_1() throws IOException{
		WrapperDataModel model = new WrapperDataModelFileImpl("/home/chema/tesis/test/generated/stats_1.mht");
		EnhancedRequestSearchTO result = executeTest(model);		
		Assert.assertEquals(3, result.getScoredPSCCodes().size());
	}
	
	@Test
	public void testRecommendations_2_1_2_2008() throws IOException{
		WrapperDataModel model = new WrapperDataModelFileImpl("/home/chema/tesis/test/generated/stats_2_1_2_2008.mht");
		EnhancedRequestSearchTO result = executeTest(model);		
		Assert.assertEquals(3, result.getScoredPSCCodes().size());
	}

	@Test
	public void testRecommendations_2_1_2_2009() throws IOException{
		WrapperDataModel model = new WrapperDataModelFileImpl("/home/chema/tesis/test/generated/stats_2_1_2_2009.mht");
		EnhancedRequestSearchTO result = executeTest(model);		
		Assert.assertEquals(3, result.getScoredPSCCodes().size());
	}

	@Test
	public void testRecommendations_2_1_2_2010() throws IOException{
		WrapperDataModel model = new WrapperDataModelFileImpl("/home/chema/tesis/test/generated/stats_2_1_2_2010.mht");
		EnhancedRequestSearchTO result = executeTest(model);		
		Assert.assertEquals(3, result.getScoredPSCCodes().size());
	}
	
	@Test
	public void testRecommendations_2_1_2_2011() throws IOException{
		WrapperDataModel model = new WrapperDataModelFileImpl("/home/chema/tesis/test/generated/stats_2_1_2_2011.mht");
		EnhancedRequestSearchTO result = executeTest(model);		
		Assert.assertEquals(3, result.getScoredPSCCodes().size());
	}
	
	
	@Test
	public void testRecommendations_2_1_2() throws IOException{
		WrapperDataModel model = new WrapperDataModelFileImpl("/home/chema/tesis/test/generated/stats_2_1_2.mht");
		EnhancedRequestSearchTO result = executeTest(model);		
		Assert.assertEquals(3, result.getScoredPSCCodes().size());
	}
	
	@Test
	public void testBean(){
		PSCMahoutEnhancerStandalone recommender = new PSCMahoutEnhancerStandalone();		
		EnhancedRequestSearchTO result = recommender.enhance(createRequest() );
	}
}
