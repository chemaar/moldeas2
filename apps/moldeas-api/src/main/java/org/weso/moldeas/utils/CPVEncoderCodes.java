package org.weso.moldeas.utils;

public class CPVEncoderCodes {

	public static String encode(String inCompleteCode){
		String sb = new String(inCompleteCode);
		for (int i = sb.length(); i<8;i++){
			sb = "0"+sb;
		}	
		return sb;
	}
	public static String decode(String completeCode){
		int k = 0;
		for(k = 0; k < completeCode.length() && completeCode.charAt(k)=='0'; k++);
		return completeCode.substring(k, completeCode.length());		
	}
}
