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
package org.weso.moldeas.transformer.ppn;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.weso.moldeas.loader.resources.FilesResourceLoader;
import org.weso.moldeas.loader.resources.ResourceLoader;
import org.weso.moldeas.transformer.pscs.ChainTransformerAdapter;
import org.weso.moldeas.utils.PrefixManager;
import org.weso.moldeas.utils.PrettyPrinter;
import org.weso.moldeas.utils.TransformerConstants;
import org.weso.pscs.utils.PSCConstants;

import au.com.bytecode.opencsv.CSVReader;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class PPNTransformer extends ChainTransformerAdapter {

	private static final int MAX_PPN_RESOURCES = 100000;
	
	protected static Logger logger = Logger.getLogger(PPNTransformer.class);
	public void execute() throws Exception{
		this.model.createProperty(PSCConstants.CPV_codeIn);
		this.model.createProperty(PSCConstants.IN_PROCESS);
		this.model.createResource(TransformerConstants.WESO_ORG_URI);
		//1-Load PPN,CPV file //FIXME: as parameters
		String [] years = new String []{
				"2008",
				"2009",
				"2010",
				"2011"
		};
		ResourceLoader loaderNuts = new FilesResourceLoader(new String[]{"ppn/tender-nut.csv"});
		InputStream dataNuts =loaderNuts.getKnowledgeResources()[0].getKnowledgeSourceData();
		CSVReader readerNuts = new CSVReader(new InputStreamReader(dataNuts),';');
		Map<String,String[]> ppnNuts = createTable(readerNuts);
		
		for(int i = 0; i<years.length;i++){
			String tendYearFile = "ppn/tender-cpv-"+years[i]+".csv";
			logger.info("Loading "+tendYearFile);
			loadAndTransform(tendYearFile,ppnNuts, years[i]);
		}
		
	}
	
	private void loadAndTransform(String tendYear, Map<String, String[]> ppnNuts, String year) throws FileNotFoundException, IOException {
		ResourceLoader loader = new FilesResourceLoader(new String[]{tendYear});
		InputStream data =loader.getKnowledgeResources()[0].getKnowledgeSourceData();
		CSVReader reader = new CSVReader(new InputStreamReader(data),';');		
		//code ppn; cpv_code, cpv_code_1
		String [] nextLine;
		int i = 0;
		int total = 0;
		Model aggregated = ModelFactory.createDefaultModel();
		while ((nextLine = reader.readNext()) != null) {
				Model resourceModel= processingLine(nextLine, ppnNuts);
				aggregated.add(resourceModel);
				resourceModel = null;
				if (i>MAX_PPN_RESOURCES){
					serialize(aggregated,total,year);
					total +=i;
					i = 0;
					aggregated = null;
					aggregated = ModelFactory.createDefaultModel();
				}				
				i++;			
			}
		if (i != 0 && aggregated!= null){
			serialize(aggregated,total, year);
		}
		
	}

	private void serialize(Model resourceModel, int i, String year) throws FileNotFoundException, IOException {
		logger.info("Serialize "+("generated/ppn-"+year+"-"+i+".ttl"));
		PrettyPrinter.serializeModel(resourceModel, PrefixManager.getResourceBundle(), 
				"generated/ppn-"+year+"-"+i+".ttl", TransformerConstants.TURTLE_SYNTAX);
	}

	private Map<String, String[]> createTable(CSVReader readerNuts) throws IOException {
		//code ppn; code_nuts
		String [] nextLine;
		Map<String,String[]> ppnNuts = new HashMap<String, String[]>(); 
		while ((nextLine = readerNuts.readNext()) != null) {
				String id = nextLine[0];
				List<String> l = new LinkedList<String>();
				for(int i = 1; i<nextLine.length;i++){
					if(nextLine[i] != null && !nextLine[i].equals("") ){
						l.add(nextLine[i]);
					}
				}
				String []nutsCodes = l.toArray(new String[l.size()]);
				ppnNuts.put(id, nutsCodes);
			}
		return ppnNuts;
	}

	public Model processingLine(String []line, Map<String, String[]> ppnNuts){
		String rawCode = line[0];
		String id = rawCode.split("-")[0];
		String date = rawCode.split("-")[1];
		String []cpvCodes = line[1].split(",");
		//FIXME: extract DAO
		Model resourceModel = createResourceModel();		
		Resource ppnResource = resourceModel.createResource(PSCConstants.formatURIId(id,date));
		Resource typeResource = resourceModel.createResource(getType());
		ppnResource.addProperty(RDF.type, typeResource);
		ppnResource.addProperty(RDFS.label, TransformerConstants.literalLang(resourceModel, TransformerConstants.DEFAULT_PPN_LABEL+id, TransformerConstants.DEFAULT_LANGUAGE));
		ppnResource.addProperty(RDFS.comment, TransformerConstants.literalLang(resourceModel, TransformerConstants.DEFAULT_PPN_COMMENT, TransformerConstants.DEFAULT_LANGUAGE));
		ppnResource.addLiteral(DCTerms.identifier,id);
		ppnResource.addLiteral(DCTerms.description,TransformerConstants.literalLang(resourceModel, TransformerConstants.DEFAULT_PPN_COMMENT, TransformerConstants.DEFAULT_LANGUAGE));
		//ppnResource.addLiteral(DCTerms.source,id);	
		//ppnResource.addLiteral(DCTerms.subject,id);
		ppnResource.addLiteral(DCTerms.date,date);		
		resourceModel.add(ppnResource,DCTerms.publisher,this.model.getResource(TransformerConstants.WESO_ORG_URI));
		resourceModel.add(ppnResource,this.model.getProperty(PSCConstants.IN_PROCESS),resourceModel.createResource(getAccess()));
		for(int i = 0; i<cpvCodes.length;i++){
			if(cpvCodes[i]!=null && !cpvCodes[i].equals("")){
				Resource cpv2008 = resourceModel.createResource(PSCConstants.formatId(cpvCodes[i]));
				ppnResource.addProperty(model.getProperty(PSCConstants.CPV_codeIn),cpv2008);	
			}
			
		}
		//Tender-nut: 000001-2011;;RS
		//Tender-cpv: 1-2011;75242000
		//Needs to fill with 0s
		for (int i = rawCode.length(); i<11;i++){
			rawCode = "0"+rawCode;
		}		
		String []nutsCodes = ppnNuts.get(rawCode);
		if(nutsCodes != null){
			for(int i = 0; i< nutsCodes.length;i++){
				Resource nutsResource = resourceModel.createResource(
						PrefixManager.getURIPrefix("moldeas-nuts")+"resource/"+nutsCodes[i]);
				ppnResource.addProperty(
						resourceModel.getProperty(PSCConstants.NUTS_CODE),
						nutsResource);	
			}
		}
		return resourceModel;
	}

	private Model createResourceModel() {
		Model resourceModel = ModelFactory.createDefaultModel();
		return resourceModel;
	}

	private String getType() {
		return PSCConstants.HTTP_PURL_ORG_WESO_PPN_DEF+"Notice";
	}

	private String getAccess() {
		return PSCConstants.HTTP_PURL_ORG_WESO_PPN_DEF+"Closed";
	}
}
