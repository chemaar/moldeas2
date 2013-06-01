package org.weso.transformer.filters;

import junit.framework.Assert;

import org.junit.Test;


public class PluralFilterTest {

	@Test
	public void testPlural(){
		String str = "services";
		Assert.assertEquals("service", PluralFilter.findSingular(str));
	}
}
