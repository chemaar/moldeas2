package org.weso.moldeas.enhancers.psc;

import java.io.IOException;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.weso.moldeas.dao.PSCDAO;
import org.weso.moldeas.dao.impl.CPV2008FileDAOImpl;
import org.weso.moldeas.to.EnhancedRequestSearchTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.RequestSearchTO;
import org.weso.moldeas.to.ScoredPSCTO;
import org.weso.moldeas.utils.ApplicationContextLocator;


public class SolrNLPPSCCodesEnhancerTest {

	

	
	@Test
	public void testEnhancePSCCodesNLP() throws IOException{
		SolrNLPPSCCodesEnhancer enhancer = new SolrNLPPSCCodesEnhancer(new CPV2008FileDAOImpl());
		executeTest(enhancer);
	}

	private void executeTest(SolrNLPPSCCodesEnhancer enhancer) {
		RequestSearchTO request = new RequestSearchTO();
		request.setStringQuery("construction");
		EnhancedRequestSearchTO result = enhancer.enhance(request);
		Set<ScoredPSCTO> scoredPSCs = result.getScoredPSCCodes();
		Assert.assertEquals(5, result.getScoredPSCCodes().size());
		for(ScoredPSCTO pscTO: scoredPSCs){
			System.out.println(pscTO);
		}
	}
	
	@Test
	public void testFromBean() throws IOException{
		SolrNLPPSCCodesEnhancer enhancer = new SolrNLPPSCCodesEnhancer();
		executeTest(enhancer);
	}
	
	@Test
	public void testCompleteBean() throws IOException{
		SolrNLPPSCCodesEnhancer enhancer = 
			(SolrNLPPSCCodesEnhancer) ApplicationContextLocator.getApplicationContext().getBean("SolrNLPPSCCodesEnhancer");
		executeTest(enhancer);
	}
}

