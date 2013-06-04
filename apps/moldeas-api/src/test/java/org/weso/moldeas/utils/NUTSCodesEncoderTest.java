package org.weso.moldeas.utils;

import junit.framework.Assert;

import org.junit.Test;


public class NUTSCodesEncoderTest {

	@Test
	public void testCode(){
		String key = "NLZZ";		
		Assert.assertEquals(1778, NUTSCodesEncoder.getNumberCode(key).intValue());
		Assert.assertEquals(-1, NUTSCodesEncoder.getNumberCode("NOEXISTS").intValue());
	}
	
}
