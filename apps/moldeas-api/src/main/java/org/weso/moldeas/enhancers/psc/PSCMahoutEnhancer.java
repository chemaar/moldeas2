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
package org.weso.moldeas.enhancers.psc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.weso.moldeas.enhancers.EnhancerAdapter;
import org.weso.moldeas.psc.PSCFacade;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.ScoredPSCTO;
import org.weso.moldeas.utils.ApplicationContextLocator;

public class PSCMahoutEnhancer extends EnhancerAdapter {

	protected static Logger logger = Logger.getLogger(PSCMahoutEnhancer.class);
	private PSCFacade facade;
	/**
	 * 1) Create the table CPV, CPVnarrower, Value. Direct narrower of transitive narrower?mv
	 * 2) Recommendation: a) Filtrado or User
	 * 3) Evaluation of the recommendation
	 * 4) Quality
	 * 5) Similarity: Euclidean, Pearson, Tanimoto, Likehood, Clustering
	 * 
	 * Collaborative filtering: producing recommendations based on, and only based on, knowledge of users’ relationships to items.
	 * Content-based recommendation: based on the attributes of items
	 */

	public PSCMahoutEnhancer(){
		this.facade = (PSCFacade) ApplicationContextLocator.getApplicationContext().
		getBean(PSCFacade.class.getSimpleName());
	}

	public PSCMahoutEnhancer(PSCFacade facade){
		this.facade = facade;
	}


	@Override
	protected void execute() throws Exception {		
		//Create only one time
		FastByIDMap<PreferenceArray> preferences = createPreferences();
		//FIXME: serialize preferences
		serialize(preferences);
		DataModel model = new GenericDataModel(preferences); 
		UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
		UserNeighborhood neighborhood =		new NearestNUserNeighborhood(2, similarity, model);
		Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity); 
		List<RecommendedItem> recommendations = 
			recommender.recommend(31000000,3); //(id concept, number of recomendations)
		logger.debug(recommendations.size());
		for (RecommendedItem recommendation : recommendations) {
			logger.debug(recommendation.getItemID()+"  "+recommendation.getValue());
		}

	}

	private void serialize(FastByIDMap<PreferenceArray> preferences) {
		// TODO Auto-generated method stub
		try {
			
			FileWriter fw = new FileWriter(new File("cpv-preferences.txt"));
			Set<Entry<Long, PreferenceArray>> values = preferences.entrySet();
			for(Entry<Long, PreferenceArray> entry: values){
				Long id = entry.getKey();
				PreferenceArray prefsPSCTO = entry.getValue();
				for (int i = 0; i<prefsPSCTO.getIDs().length;i++){
					//id, item, value
					fw.write(id+","+prefsPSCTO.getItemID(i)+","+prefsPSCTO.getValue(i)+"\n");
				}
			}
			fw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private FastByIDMap<PreferenceArray> createPreferences() {
		double Vl = 0.5; 
		FastByIDMap<PreferenceArray> preferences = new FastByIDMap<PreferenceArray>();
		int concept = 0;
		int Nnarrower = 0;
		Pattern id = Pattern.compile("^[A-Z]+[0-9]+$");
		for(PSCTO pscTO:this.facade.getPSCTOs()){
			if(!id.matcher(pscTO.getId()).matches()){
				List<PSCTO> narrowers = this.facade.getNarrowersTransitiveOf(pscTO);
				PreferenceArray prefsPSCTO = new GenericUserPreferenceArray(narrowers.size()); //FIXME number of narrowers
				prefsPSCTO.setUserID(concept++, Long.valueOf(pscTO.getId())); // (id internal, id concept)
				Nnarrower = 0;
				int Nnarrowers = narrowers.size();
				for(PSCTO narrower: narrowers){
					double score = Vl/Nnarrowers+PSCNarrowerEnhancer.getTypeValue(narrower.getType());
					prefsPSCTO.setItemID(Nnarrower, Long.valueOf(narrower.getId())); // (id internal, id narrower) 
					prefsPSCTO.setValue(Nnarrower, (float)score); // (id narrower, value)
					Nnarrower++;
				}
				System.out.println("Adding preference: "+prefsPSCTO);
				preferences.put(Long.valueOf(pscTO.getId()), prefsPSCTO);
			}else{
				//skipping [A-Z*]
				
			}
		

		}
		return preferences;
	}

	@Override
	protected void preExecute() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void postExecute() throws Exception {
		// TODO Auto-generated method stub

	}

}
