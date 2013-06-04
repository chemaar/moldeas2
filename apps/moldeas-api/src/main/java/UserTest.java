/**
 * 
 *   (c) Copyright 2011 WESO, Computer Science Department,
 *   Facultad de Ciencias, University of Oviedo, Oviedo, Asturias, Spain, 33007
 *   All rights reserved.
 *  
 *   Redistribution and use in source and binary forms, with or without
 *   modification, are permitted provided that the following conditions
 *   are met:
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *   3. The name of the author may not be used to endorse or promote products
 *      derived from this software without specific prior written permission.
 *  
 *   THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 *   IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *   OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 *   IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 *   INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 *   NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *   DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *   THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 *   THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 */
import java.util.List;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.AveragingPreferenceInferrer;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;


public class UserTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception {
		DataModel model = new FileDataModel(
		// 		new File("/home/chema/weso/google-repo/moldeas/trunk/apps/moldeas-api/src/main/resources/org/weso/moldeas/enhancers/psc/cpv-preferences.txt")); 
		//new File("/home/chema/weso/google-repo/moldeas/trunk/apps/moldeas-api/src/main/resources/org/weso/moldeas/enhancers/psc/mahout-example.txt"));
		//		new File("/home/chema/weso/google-repo/moldeas/trunk/apps/moldeas-api/stats-2011.csv"));				
				new File("/home/chema/tesis/test/generated/stats_1_2011.mht"));
//		new File("/home/chema/tesis/test/generated/stats_2_3_1_2011.mht"));

		UserSimilarity similarity = new EuclideanDistanceSimilarity(model);
		
		UserNeighborhood neighborhood =
		new NearestNUserNeighborhood(3, similarity, model);
		similarity.setPreferenceInferrer(new AveragingPreferenceInferrer(model));
		
		Recommender recommender = new GenericUserBasedRecommender(
		model, neighborhood, similarity); 
		Recommender cachingRecommender = new CachingRecommender(recommender);
		List<RecommendedItem> recommendations =
			cachingRecommender.recommend(72222300, 3); 
		for (RecommendedItem recommendation : recommendations) {
			System.out.println(recommendation);
		}
//		RecommenderEvaluator evaluator =
//	          new AverageAbsoluteDifferenceRecommenderEvaluator();
//		RecommenderBuilder builder = new RecommenderBuilder() {
//			  public Recommender buildRecommender(DataModel model) {
//			    // build and return the Recommender to evaluate here
//					UserSimilarity similarity;
//					try {
//						similarity = new PearsonCorrelationSimilarity(model);
//					
//						UserNeighborhood neighborhood;
//						neighborhood = new NearestNUserNeighborhood(2, similarity, model);
//						  Recommender recommender = new GenericUserBasedRecommender(
//									model, neighborhood, similarity); 
//							return new CachingRecommender(recommender);
//					} catch (TasteException e) {
//						// TO0DO Auto-generated catch block
//						e.printStackTrace();
//					}
//					return null;				
//			  }
//			};
//		double evaluation = evaluator.evaluate(builder, null, model, 0.5, 1.0);
//		System.out.println(evaluation);

	}

}

