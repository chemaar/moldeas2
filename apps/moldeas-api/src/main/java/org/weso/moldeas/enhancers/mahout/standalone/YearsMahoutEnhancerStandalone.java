package org.weso.moldeas.enhancers.mahout.standalone;

import java.util.List;

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
import org.weso.moldeas.to.YearsTO;
import org.weso.moldeas.utils.ApplicationContextLocator;

/**
 * Recommendations when the userID is a Year.
 * @author chema
 *
 */
public class YearsMahoutEnhancerStandalone extends EnhancerAdapter {

	public static final int MAX_RECOMMENDATIONS = 3;
	public static final String YEARS_WRAPPER_DATA_MODEL_BEAN = "yearsWrapperDataModel";
	DataModel model;
	
	//Data from files 1* and 2.1.2*
	public YearsMahoutEnhancerStandalone(){
		this.model = ((WrapperDataModel) ApplicationContextLocator.getApplicationContext().
				getBean(YEARS_WRAPPER_DATA_MODEL_BEAN)).getDataModel();
	}

	public YearsMahoutEnhancerStandalone(WrapperDataModel wrapperDataModel){
		this.model = wrapperDataModel.getDataModel();
	}
	
	@Override
	protected void execute() throws Exception {
		/**To experiment**/
		Recommender cachingRecommender = createRecommender();
		/**To experiment**/
		
		YearsTO yearsTO = this.request.getYears();
		for(long year = yearsTO.getMin(); year<=yearsTO.getMax();year++){
			List<RecommendedItem> recommendations =
				cachingRecommender.recommend(year,	MAX_RECOMMENDATIONS);
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
