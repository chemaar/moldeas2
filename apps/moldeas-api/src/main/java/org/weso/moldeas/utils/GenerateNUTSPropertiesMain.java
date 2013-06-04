package org.weso.moldeas.utils;

import java.io.FileNotFoundException;

public class GenerateNUTSPropertiesMain {

	public static void main(String []args) throws FileNotFoundException{
		GenerateNUTSProperties generator = new GenerateNUTSProperties();
		generator.save("src/main/resources/org/weso/moldeas/utils/"+NUTSCodesEncoder.class.getSimpleName()+".properties", 0);
	}
}
