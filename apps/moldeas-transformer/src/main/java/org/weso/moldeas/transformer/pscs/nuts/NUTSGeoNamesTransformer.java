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
package org.weso.moldeas.transformer.pscs.nuts;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.geonames.Toponym;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;
import org.weso.moldeas.loader.JenaRDFModelWrapper;
import org.weso.moldeas.loader.resources.FilesResourceLoader;
import org.weso.moldeas.loader.resources.ResourceLoader;
import org.weso.moldeas.loader.resources.URLFilesResourceLoader;
import org.weso.moldeas.transformer.pscs.ChainTransformerAdapter;

import au.com.bytecode.opencsv.CSVReader;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public class NUTSGeoNamesTransformer extends ChainTransformerAdapter {

	private static final String HTTP_WWW_W3_ORG_2003_01_GEO_WGS84_POS = "http://www.w3.org/2003/01/geo/wgs84_pos#";
	private static final String HTTP_WWW_W3_ORG_2003_01_GEO_WGS84_POS_LONG = HTTP_WWW_W3_ORG_2003_01_GEO_WGS84_POS+"long";
	private static final String HTTP_WWW_W3_ORG_2003_01_GEO_WGS84_POS_LAT = HTTP_WWW_W3_ORG_2003_01_GEO_WGS84_POS+"lat";
	private static final int NUMBER_OF_PROCESSING_FILES = 19;
	
	protected static Logger logger = Logger.getLogger(NUTSGeoNamesTransformer.class);
	private Map<String,String[]> countryInfo = new HashMap<String, String[]>();
	
	
	@Override
	protected void execute() throws Exception {
		ResourceLoader loaderRdf = new FilesResourceLoader(new String[]{"nuts/nuts-2008-full.txt"});
		JenaRDFModelWrapper rdfModel = new JenaRDFModelWrapper(loaderRdf);
		this.model = (Model) rdfModel.getModel();
		//Init model
		this.model.setNsPrefix("geo", HTTP_WWW_W3_ORG_2003_01_GEO_WGS84_POS);
		this.model.setNsPrefix("nuts", "http://nuts.psi.enakting.org/def/");
		this.model.setNsPrefix("spatialrelations", "http://data.ordnancesurvey.co.uk/ontology/spatialrelations/");
		this.model.createProperty(HTTP_WWW_W3_ORG_2003_01_GEO_WGS84_POS_LAT);
		this.model.createProperty(HTTP_WWW_W3_ORG_2003_01_GEO_WGS84_POS_LONG);		
		//Init search
		ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
	
		for(int i = 0; i<NUMBER_OF_PROCESSING_FILES; i++){
			String fileName = "nuts/nuts-code-region-item-"+i+".txt";
			logger.debug("Processing file: "+fileName);
			ResourceLoader loader = new FilesResourceLoader(new String[]{fileName});
			InputStream data =loader.getKnowledgeResources()[0].getKnowledgeSourceData();
			CSVReader reader = new CSVReader(new InputStreamReader(data),';');
			List<String[]> strings = reader.readAll();
			for(String []nextLine:strings){
				String id = nextLine[0];
				String countryCode =(id.length()>2?id.substring(0,2):id); 
				String place = (nextLine.length>1?nextLine[1]:"WARNING "+nextLine[0]);
				if(!place.equalsIgnoreCase("EXTRA-REGIO") && 
						!place.equalsIgnoreCase("Extra-Regio")){
					//Get country info
					addCountryInfo(countryCode);
					//Common criteria
					searchCriteria.setQ(place);
					searchCriteria.setCountryCode(countryCode);
					searchCriteria.setMaxRows(5);
					logger.debug("Searching for: "+place+" in "+countryCode);
					//Search
					ToponymSearchResult searchResult = WebService.search(searchCriteria);
					List<Toponym> toponyms = searchResult.getToponyms();
					if(toponyms.size() == 0){					
						String[] codeInfo = this.countryInfo.get(countryCode);
						searchCriteria.setQ(codeInfo[4]+" "+codeInfo[5]);
						logger.debug("Second try with "+codeInfo[4]+" "+codeInfo[5]);
						searchResult = WebService.search(searchCriteria);		
						toponyms = searchResult.getToponyms();
					}
					//Showing results
					addCoordinates(toponyms, countryCode, id);
				}
				}
			
		}

	}

	protected void addCountryInfo(String countryCode) throws IOException {
		if(this.countryInfo.get(countryCode)==null && !countryCode.equals("")){
			String url = "http://api.geonames.org/countryInfoCSV?country="+countryCode+"&username=chema_ar";
			ResourceLoader loader = new URLFilesResourceLoader(new String[]{url});
			InputStream data =loader.getKnowledgeResources()[0].getKnowledgeSourceData();
			CSVReader reader = new CSVReader(new InputStreamReader(data),'\t');
			List<String[]> strings = reader.readAll();
			logger.debug("Country Code "+countryCode+" values "+
					strings.get(1)[4]+" "+strings.get(1)[5]+" "
					+strings.get(1)[9]);
			this.countryInfo.put(countryCode, strings.get(1));
		}

	}

	protected void addCoordinates(List<Toponym> toponyms, String countryCode, String id){
		//Take the first
		logger.debug("Proceed to add coordinates with "+toponyms.size()+" results.");
		Toponym toponym = toponyms.get(0);
		String countryCodeGeonames = toponym.getCountryCode();
		double lat = 0;		
		double longitude = 0;
		logger.debug("Code "+countryCodeGeonames+ " = "+countryCode+ " "+countryCodeGeonames.equals(countryCode));		
		if(countryCodeGeonames.equals(countryCode)){		
			lat = toponym.getLatitude();
			longitude = toponym.getLongitude();
		}else{
			//Search in the results
			boolean found = false;
			for(int j=1; !found && j<toponyms.size();j++){
				toponym = toponyms.get(j);
				if(countryCodeGeonames.equals(countryCode)){
					lat = toponym.getLatitude();
					longitude = toponym.getLongitude();
					found = true;
				}
			}
		}//else
		//addLanguages to region
		addValues(lat, longitude, id);
}

	private void addValues(Double lat, Double longitude, String id){
		if (Double.compare(lat, 0)!=0 && Double.compare(longitude, 0)!=0){
			//Get Resource from RDF Model
			String url = "http://nuts.psi.enakting.org/id/"+id;
			//FIXME: for testing we create the resource					
			//Resource r = this.model.createResource(url);
			//Get Resource
			Resource r = this.model.getResource(url);
			if (r != null){
				Literal latValue = model.createTypedLiteral(lat) ;
				Literal longValue = model.createTypedLiteral(longitude) ;
				//Add coordinates
				r.addProperty(this.model.getProperty(HTTP_WWW_W3_ORG_2003_01_GEO_WGS84_POS_LAT), latValue);
				r.addProperty(this.model.getProperty(HTTP_WWW_W3_ORG_2003_01_GEO_WGS84_POS_LONG), longValue);
				logger.debug("Adding coordinates lat= "+lat+" long= "+longitude+" to "+r.getURI());
			}		
		}
	}

}
