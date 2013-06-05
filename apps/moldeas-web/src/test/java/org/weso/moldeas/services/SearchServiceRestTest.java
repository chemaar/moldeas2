package org.weso.moldeas.services;

import junit.framework.Assert;

import org.junit.Test;

public class SearchServiceRestTest {

	@Test
	public void test() {
		String cpvCodes="98510000";
		String nutsCodes="DE";
		SearchServiceRest rest = new SearchServiceRest();
		Assert.assertEquals(16,rest.search(cpvCodes, nutsCodes, "2009", "2010", "75", "100", "1", "3", "").getTotalResults());

	}

	@Test
	public void test2() {
		String cpvCodes="98510000";
		SearchServiceRest rest = new SearchServiceRest(new MoldeasServiceFacadeImpl());
		Assert.assertEquals(266,rest.search(cpvCodes, "", "2009", "2010", "75", "100", "1", "3", "").getTotalResults());

	}

	
}
