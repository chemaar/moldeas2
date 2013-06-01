/**
 * (c) Copyright 2011 WESO, Computer Science Department,
 * Facultad de Ciencias, University of Oviedo, Oviedo, Asturias, Spain, 33007
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.weso.moldeas.transformer.pscs;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

import org.geonames.Toponym;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;
import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;


public class GeonamesTestCase {

	//@Test
	public void testGeonamesService() throws Exception{
		ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
		searchCriteria.setQ("zurich");
		ToponymSearchResult searchResult = WebService.search(searchCriteria);
		for (Toponym toponym : searchResult.getToponyms()) {
			System.out.println(toponym.getName()+" "+ toponym.getCountryName()
					+toponym.getLatitude()+" "+toponym.getLongitude());
		}
	}
	@Test
	public void testNutsPlaces() throws Exception{
		PrintStream writer = new PrintStream(new File("salida.txt"));
		//1-Loading places and codes
		ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream in = classLoader.getResourceAsStream("nuts-places");
		CSVReader reader = new CSVReader(new InputStreamReader(in),';');
		//2-Searching
		List<String[]> strings = reader.readAll();
		for(String []nextLine:strings){
			String countryCode =(nextLine[0].length()>2?nextLine[0].substring(0,2):nextLine[0]); 
			String place = (nextLine.length>1?nextLine[1]:"WARNING "+nextLine[0]);
			//Common criteria
			searchCriteria.setQ(place);
			searchCriteria.setMaxRows(5);
			writer.println("Searching for: "+place+" in "+countryCode);
			//Search
			ToponymSearchResult searchResult = WebService.search(searchCriteria);
			List<Toponym> toponyms = searchResult.getToponyms();
			if(toponyms.size() == 0){
				//Change criteria, only search country and take coordinates
				searchCriteria.setQ(null);
				searchCriteria.setCountryCode(countryCode);
				searchResult = WebService.search(searchCriteria);
				writer.println(searchResult.getTotalResultsCount());
			}else{
				//Showing results
				for (int i = 0; i<1 && i<toponyms.size();i++){				  
					Toponym toponym = toponyms.get(i);
					//Check consistency in the same country at least
					writer.println("Results for: "+place+" "+nextLine[0]+" "+
							toponym .getName()+" "
							+ toponym.getCountryName()+ " "
							+ toponym.getCountryCode()+ "="+countryCode+"  "+
							(!toponym.getCountryCode().equals(countryCode)?"WARNING  ":" ")+
							+toponym.getLatitude()+" "+toponym.getLongitude());  
				}
			}
			writer.flush();
		
		}

		writer.close();
	}
}
