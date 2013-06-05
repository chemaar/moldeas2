package org.weso.moldeas.enhancers.mahout.standalone;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.AveragingPreferenceInferrer;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.weso.moldeas.dao.WrapperDataModel;
import org.weso.moldeas.dao.WrapperDataModelFileImpl;
import org.weso.moldeas.dao.impl.DataSource;
import org.weso.moldeas.enhancers.EnhancerAdapter;
import org.weso.moldeas.to.EnhancedRequestSearchTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.ScoredDurationTO;
import org.weso.moldeas.to.ScoredLanguageTO;
import org.weso.moldeas.to.ScoredNUTSTO;
import org.weso.moldeas.to.ScoredPSCTO;
import org.weso.moldeas.to.ScoredYearsTO;
import org.weso.moldeas.utils.ApplicationContextLocator;
import org.weso.moldeas.utils.CPVEncoderCodes;
import org.weso.moldeas.utils.PPNVariableFactory;
import org.weso.pscs.utils.PSCConstants;

/**
 * Recommendations when the userID is a CPV code.
 * @author chema
 *
 */
public class PSCMahoutEnhancerStandalone extends EnhancerAdapter {

	private static final String PSC_WRAPPER_DATA_MODEL_BEAN = "pscWrapperDataModel";
	public static final int MAX_RECOMMENDATIONS = 20;
	protected static Logger logger = Logger.getLogger(PSCMahoutEnhancerStandalone.class);
	DataModel model;
	
	//Data from files 1* and 2.1.2*
	public PSCMahoutEnhancerStandalone(){
		this.model = ((WrapperDataModel) ApplicationContextLocator.getApplicationContext().
			getBean(PSC_WRAPPER_DATA_MODEL_BEAN)).getDataModel();
	}

	public PSCMahoutEnhancerStandalone(WrapperDataModel wrapperDataModel){
		this.model = wrapperDataModel.getDataModel();
	}
	
	@Override
	protected void execute() throws Exception {
		/**To experiment**/
		Recommender cachingRecommender = createRecommender();
		/**To experiment**/
			
		Set<PSCTO> codes = this.request.getPscCodes();
		for(PSCTO pscTO: codes){
			List<RecommendedItem> recommendations =
				cachingRecommender.recommend(Integer.valueOf(pscTO.getId()), MAX_RECOMMENDATIONS);
			for (RecommendedItem recommendation : recommendations) {	
				addScoredTO(this.enhancedRequest,createScored(recommendation));
			}
			//Keep previous results
			this.enhancedRequest.getScoredPSCCodes().add((ScoredPSCTO) this.request.getSwap().get(pscTO.getUri()));
		}
	}

	private Recommender createRecommender() throws TasteException {
		UserSimilarity similarity = new PearsonCorrelationSimilarity(this.model);		
		UserNeighborhood neighborhood =
		new NearestNUserNeighborhood(2, similarity, this.model);
		similarity.setPreferenceInferrer(new AveragingPreferenceInferrer(this.model));		
		Recommender recommender = new GenericUserBasedRecommender(
		this.model, neighborhood, similarity); 
		Recommender cachingRecommender = new CachingRecommender(recommender);
		return cachingRecommender;
	}
	

	@Override
	protected void preExecute() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void postExecute() throws Exception {
		// TODO Auto-generated method stub

	}

	
//FIXME: Extract to util Class
	protected static  Object createScored(RecommendedItem recommendation) {
		return PPNVariableFactory.createScoreTO(
				recommendation.getItemID(), recommendation.getValue());
	}

	
	
	protected static void addScoredTO(EnhancedRequestSearchTO enhancedRequest, Object scored){
		if(scored != null){
			if(scored instanceof ScoredPSCTO){
				//FIXME: hack to keep values
				ScoredPSCTO scoredPSCTO = (ScoredPSCTO) scored;
				//if (enhancedRequest.getRequest().getSwap().
			    //	containsKey(scoredPSCTO.getPscTO().getUri())){
				//	double value = ((ScoredPSCTO) enhancedRequest.getRequest().getSwap().get(scoredPSCTO.getPscTO().getUri())).getScore();
				//	scoredPSCTO.setScore(scoredPSCTO.getScore()+value);
				//}
				enhancedRequest.getScoredPSCCodes().add(scoredPSCTO);
			}else if (scored instanceof ScoredNUTSTO){
				enhancedRequest.getScoredNUTSTO().add((ScoredNUTSTO) scored);
			}else if (scored instanceof ScoredYearsTO){
				enhancedRequest.getScoredYearsTOs().add((ScoredYearsTO) scored);
			}else if (scored instanceof ScoredLanguageTO){
				enhancedRequest.getScoredLanguageTOs().add((ScoredLanguageTO) scored);
			} else if (scored instanceof ScoredDurationTO){
				enhancedRequest.getScoredDurationTOs().add((ScoredDurationTO) scored);
			}else{
				logger.debug("Skipping to add "+scored.toString()+" to enhanced request.");
			}
		} else logger.debug("Skipping to add null recommendation.");
		
	}
}
