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
import org.weso.moldeas.to.NUTSTO;
import org.weso.moldeas.to.RequestSearchTO;
import org.weso.moldeas.to.ScoredNUTSTO;
import org.weso.moldeas.to.ScoredPSCTO;
import org.weso.moldeas.utils.ShowUtils;


public class NUTSMahoutEnhancerStandaloneTest {


	protected static  RequestSearchTO createRequest(){
		RequestSearchTO request = new RequestSearchTO();
		NUTSTO nutsTO = new NUTSTO("","UK");
		request.getNutsCodes().add(nutsTO);
		return request;
	}
	

	private EnhancedRequestSearchTO executeTest(WrapperDataModel wrapperModel) {
		NUTSMahoutEnhancerStandalone recommender = new NUTSMahoutEnhancerStandalone(wrapperModel);		
		EnhancedRequestSearchTO result = recommender.enhance(createRequest() );
		ShowUtils.showResults(result);
		Assert.assertEquals(PSCMahoutEnhancerStandalone.MAX_RECOMMENDATIONS, 
				result.getScoredPSCCodes().size());
		return result;
	}
	
	
	@Test
	public void testRecommendations_2_2_2_2008() throws IOException{
		WrapperDataModel model = new WrapperDataModelFileImpl("/home/chema/tesis/test/generated/stats_2_2_2_2_2008.mht");
		executeTest(model);			
	}
	
	
	@Test
	public void testBean() throws IOException{
		NUTSMahoutEnhancerStandalone recommender = new NUTSMahoutEnhancerStandalone();	
		EnhancedRequestSearchTO result = recommender.enhance(createRequest() );
	}
}
