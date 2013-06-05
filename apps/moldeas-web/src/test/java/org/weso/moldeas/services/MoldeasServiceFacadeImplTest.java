package org.weso.moldeas.services;

import junit.framework.Assert;

import org.junit.Test;
import org.weso.moldeas.to.DurationTO;
import org.weso.moldeas.to.PPNResultTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.RequestSearchTO;
import org.weso.moldeas.to.TotalCostTO;
import org.weso.moldeas.to.YearsTO;

public class MoldeasServiceFacadeImplTest {

	@Test
	public void test() {
		MoldeasServiceFacadeImpl impl = new MoldeasServiceFacadeImpl();
		RequestSearchTO requestSearch = new RequestSearchTO();
		PSCTO pstTO = new PSCTO();
		pstTO.setUri("http://localhost/pscs/cpv/2008/resource/98510000");
		requestSearch.getPscCodes().add(pstTO);
		requestSearch.setMaxResults(1000);
		DurationTO duration = new DurationTO();
		duration.setMin(Long.valueOf(1));
		duration.setMax(Long.valueOf(3));
		requestSearch.setDuration(duration);
		TotalCostTO totalCost = new TotalCostTO();
		totalCost.setMin(Long.valueOf(75));
		totalCost.setMax(Long.valueOf(300));
		requestSearch.setTotalCost(totalCost );
		YearsTO years = new YearsTO();
		years.setMin(Long.valueOf(2009));
		years.setMax(Long.valueOf(2010));
		requestSearch.setYears(years);
		//return new PPNResultTO();
		//request.setMaxResults(10000);
		requestSearch.setMaxResults(1000);
		
		PPNResultTO result = impl.search(requestSearch);;
		Assert.assertEquals(18, result.getScoredPnnsTO().size());


	}

}
