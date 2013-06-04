package org.weso.moldeas.enhancers.psc;

import java.io.IOException;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.weso.moldeas.dao.impl.CPV2008FileDAOImpl;
import org.weso.moldeas.to.EnhancedRequestSearchTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.RequestSearchTO;
import org.weso.moldeas.to.ScoredPSCTO;
import org.weso.moldeas.utils.ApplicationContextLocator;


public class SolrPSCCodesEnhancerTest {
	@Test
	public void testEnhancePSCCodes() throws IOException{
		SolrPSCCodesEnhancer enhancer = new SolrPSCCodesEnhancer(new CPV2008FileDAOImpl());		
		executeTest(enhancer);
	}

	private void executeTest(SolrPSCCodesEnhancer enhancer) {
		PSCTO pstTO = new PSCTO();
		pstTO.setPrefLabel("construction");		
		RequestSearchTO request = new RequestSearchTO();
		request.getPscCodes().add(pstTO);
		EnhancedRequestSearchTO result = enhancer.enhance(request);
		Set<ScoredPSCTO> scoredPSCs = result.getScoredPSCCodes();
		Assert.assertEquals(5, result.getScoredPSCCodes().size());
		for(ScoredPSCTO pscTO: scoredPSCs){
			System.out.println(pscTO);
		}
	}
	
	@Test
	public void testFromBean() throws IOException{
		SolrPSCCodesEnhancer enhancer = new SolrPSCCodesEnhancer();
		executeTest(enhancer);
	}
	
	@Test
	public void testCompleteBean() throws IOException{
		SolrPSCCodesEnhancer enhancer = 
			(SolrPSCCodesEnhancer) ApplicationContextLocator.getApplicationContext().getBean("SolrPSCCodesEnhancer");
		executeTest(enhancer);
	}
	
	
}
