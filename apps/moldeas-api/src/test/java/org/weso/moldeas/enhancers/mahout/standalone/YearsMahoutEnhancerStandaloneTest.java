package org.weso.moldeas.enhancers.mahout.standalone;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.junit.Test;
import org.weso.moldeas.dao.WrapperDataModel;
import org.weso.moldeas.dao.WrapperDataModelFileImpl;
import org.weso.moldeas.to.EnhancedRequestSearchTO;
import org.weso.moldeas.to.RequestSearchTO;
import org.weso.moldeas.to.YearsTO;
import org.weso.moldeas.utils.ShowUtils;


public class YearsMahoutEnhancerStandaloneTest {
	protected static  RequestSearchTO createRequest(){
		RequestSearchTO request = new RequestSearchTO();
		YearsTO years = new YearsTO(2008);
		request.setYears(years);
		return request;
	}

	private EnhancedRequestSearchTO executeTest(WrapperDataModel model) {
		YearsMahoutEnhancerStandalone recommender = new YearsMahoutEnhancerStandalone(model);		
		EnhancedRequestSearchTO result = recommender.enhance(createRequest() );
		ShowUtils.showResults(result);
		Assert.assertEquals(PSCMahoutEnhancerStandalone.MAX_RECOMMENDATIONS, 
				result.getScoredYearsTOs());
		return result;
	}
	
	
	@Test
	public void testRecommendations_2_3_1_2008() throws IOException{
		//COMMENT: YOU CAN NOT RECOMMEND TO AN USER WITH ALL THE INFORMATION!
		WrapperDataModel model = new WrapperDataModelFileImpl("/home/chema/tesis/test/generated/stats_2_3_1_2008.mht");
		executeTest(model);			
	}
	
	@Test
	public void testRecommendations_2_3_1() throws IOException{
		WrapperDataModel model = new WrapperDataModelFileImpl("/home/chema/tesis/test/generated/stats_2_3_1.mht");
		executeTest(model);			
	}
	
	@Test
	public void testBean() throws IOException{
		YearsMahoutEnhancerStandalone recommender = new YearsMahoutEnhancerStandalone();	
		EnhancedRequestSearchTO result = recommender.enhance(createRequest() );
	}
}
