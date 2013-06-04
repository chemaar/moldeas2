package org.weso.moldeas.enhancers.mahout.standalone;

import java.util.List;
import java.util.Set;

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
import org.weso.moldeas.enhancers.EnhancerAdapter;
import org.weso.moldeas.to.NUTSTO;
import org.weso.moldeas.utils.ApplicationContextLocator;
import org.weso.moldeas.utils.NUTSCodesEncoder;

/**
 * Recommendations when the userID is a NUTS code.
 * @author chema
 *
 */
public class NUTSMahoutEnhancerStandalone extends EnhancerAdapter {

	public static final int MAX_RECOMMENDATIONS = 3;
	public static final String NUTS_WRAPPER_DATA_MODEL_BEAN = "nutsWrapperDataModel";
	DataModel model;
	
	//Data from files 1* and 2.1.2*
	public NUTSMahoutEnhancerStandalone(){
		this.model = ((WrapperDataModel) ApplicationContextLocator.getApplicationContext().
				getBean(NUTS_WRAPPER_DATA_MODEL_BEAN)).getDataModel();
	}

	public NUTSMahoutEnhancerStandalone(WrapperDataModel wrapperDataModel){
		this.model = wrapperDataModel.getDataModel();
	}
	
	@Override
	protected void execute() throws Exception {
		Recommender cachingRecommender = createRecommender();
		
		Set<NUTSTO> codes = this.request.getNutsCodes();
		for(NUTSTO nutsTO: codes){	
			List<RecommendedItem> recommendations =
				cachingRecommender.recommend(Integer.valueOf(
						NUTSCodesEncoder.getNumberCode(nutsTO.getCode())), 
						MAX_RECOMMENDATIONS);
			for (RecommendedItem recommendation : recommendations) {
				PSCMahoutEnhancerStandalone.addScoredTO(this.enhancedRequest,
						PSCMahoutEnhancerStandalone.createScored(recommendation));
			}
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

}
