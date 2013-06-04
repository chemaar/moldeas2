package org.weso.moldeas.searchers;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;
import org.weso.moldeas.to.EnhancedRequestSearchTO;
import org.weso.moldeas.to.NUTSTO;
import org.weso.moldeas.to.PPNResultTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.RequestSearchTO;
import org.weso.moldeas.to.ScoredPSCTO;
import org.weso.moldeas.to.YearsTO;

public class SPARQLMapReduceSearchEngineTest {

	@Test
	public void testEnhancedSearch() {
		PSCTO pscTO = new PSCTO("http://purl.org/weso/cpv/2008/15841400");
		PSCTO pscTO1 = new PSCTO("http://purl.org/weso/cpv/2008/15841300");
		PSCTO pscTO2 = new PSCTO("http://purl.org/weso/cpv/2008/44921210");
		PSCTO pscTO3 = new PSCTO("http://purl.org/weso/cpv/2008/15511700");
		PSCTO pscTO4 = new PSCTO("http://purl.org/weso/cpv/2008/03131400");	
		EnhancedRequestSearchTO request = new EnhancedRequestSearchTO();
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO, 1.0));
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO1, 1.0));
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO2, 1.0));
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO3, 1.0));
		request.getScoredPSCCodes().add(new ScoredPSCTO(pscTO4, 1.0));
		RequestSearchTO simpleRequest = new RequestSearchTO();
		NUTSTO nuts1 = new NUTSTO("http://nuts.psi.enakting.org/id/ES");
		NUTSTO nuts2 = new NUTSTO("http://nuts.psi.enakting.org/id/FR");
		NUTSTO nuts3 = new NUTSTO("http://nuts.psi.enakting.org/id/DK");
		simpleRequest.getNutsCodes().add(nuts1);
		simpleRequest.getNutsCodes().add(nuts2);
		simpleRequest.getNutsCodes().add(nuts3);
		YearsTO years = new YearsTO(2008, 2011);
		simpleRequest.setYears(years);
		request.setMaxResults(10000);
		request.setRequest(simpleRequest);
		for(int i = 0; i<10;i++){
			SPARQLMapReduceSearchEngine engine = new SPARQLMapReduceSearchEngine();
			PPNResultTO result = engine.enhancedSearch(request);
			Assert.assertEquals(69, result.getScoredPnnsTO().size());
		}
		
	}

}
