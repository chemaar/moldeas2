package org.weso.moldeas.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class NUTSCodesEncoder {

	static Map<String,Integer> id2number;
	static Map<Integer,String> number2Id;
	
	
	static{
		ResourceBundle nutsCodes = ResourceBundle.getBundle(
				NUTSCodesEncoder.class.getName().toString());	
		id2number = new HashMap<String, Integer>();
		number2Id = new HashMap<Integer, String>();
		Enumeration<String> keys = nutsCodes.getKeys();
		while(keys.hasMoreElements()){
			String key = keys.nextElement();
			id2number.put(key, Integer.valueOf(nutsCodes.getString(key)));
			number2Id.put(Integer.valueOf(nutsCodes.getString(key)),key);
		}
		
	}
	
	public static Integer getNumberCode(String code){
		try{
			return  id2number.get(code);
		}catch(MissingResourceException e){
			return -1;
		}
	}
	
	public static String getStringCode(Integer number){
		try{
			return  number2Id.get(number);
		}catch(MissingResourceException e){
			return "";
		}
	}
}
