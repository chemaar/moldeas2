package org.weso.moldeas.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.ResourceBundle;

public class GenerateNUTSProperties {
    
	static ResourceBundle nutsCodes = null; 
	
	static{
		nutsCodes = ResourceBundle.getBundle(GenerateNUTSProperties.class.getName().toString());
	}
    public GenerateNUTSProperties(){
    	
    }
    
    public void save(String file, int offset) throws FileNotFoundException{
    	PrintWriter pw = new PrintWriter(new File(file));
    	Enumeration<String> keys = nutsCodes.getKeys();
    	int i = 0;
    	while(keys.hasMoreElements()){
    		String key = keys.nextElement();
    		pw.println(key+"="+(i+offset));
    		i++;
    	}
    	pw.close();
    }
}
