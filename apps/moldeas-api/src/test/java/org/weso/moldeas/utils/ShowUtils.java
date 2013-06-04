package org.weso.moldeas.utils;

import java.util.Set;

import org.weso.moldeas.to.EnhancedRequestSearchTO;

public class ShowUtils {

	
	public static void showResults(EnhancedRequestSearchTO enhancedRequest){
		showSet(enhancedRequest.getScoredPSCCodes());
		showSet(enhancedRequest.getScoredNUTSTO());
		showSet(enhancedRequest.getScoredYearsTOs());
	}
	protected static void showSet(Set set){
		for(Object scored: set){
			System.out.println(scored);
		}	
	}
	
}
