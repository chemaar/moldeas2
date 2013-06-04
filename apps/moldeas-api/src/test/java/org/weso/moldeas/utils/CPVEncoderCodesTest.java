package org.weso.moldeas.utils;

import junit.framework.Assert;

import org.junit.Test;


public class CPVEncoderCodesTest {

	@Test
	public void testEncoder(){
		Assert.assertEquals("00112233", CPVEncoderCodes.encode("112233")) ;
		Assert.assertEquals("00112233", CPVEncoderCodes.encode("0112233")) ;		
	}
	@Test
	public void testDecode(){
		Assert.assertEquals("112233", CPVEncoderCodes.decode("00112233")) ;

	}
}
