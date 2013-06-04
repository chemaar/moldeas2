package org.weso.moldeas.searchers;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.weso.moldeas.exceptions.MoldeasModelException;
import org.weso.moldeas.to.EnhancedRequestSearchTO;
import org.weso.moldeas.to.PPNResultTO;
import org.weso.moldeas.to.ScoredPSCTO;

public class SPARQLMapReduceSearchEngine extends SPARQLSearchEngine{

	protected static Logger logger = Logger.getLogger(SPARQLMapReduceSearchEngine.class);
	@Override
	public PPNResultTO enhancedSearch(EnhancedRequestSearchTO request) {	
		try {
			BlockingQueue<EnhancedRequestSearchTO> queries = createQueries(request);
			SearchMultiThread sm = new SearchMultiThread();		
			sm.exec(5, 1,queries);
			return sm.getResult();
			
		} catch (InterruptedException e) {
			throw new MoldeasModelException(e);
		}	
	
	}
	
	public static BlockingQueue<EnhancedRequestSearchTO> createQueries(EnhancedRequestSearchTO fullRequest ){		
		BlockingQueue<EnhancedRequestSearchTO> queries = new LinkedBlockingQueue<EnhancedRequestSearchTO>();
		for(ScoredPSCTO scoredPSCTO: fullRequest.getScoredPSCCodes()){
			EnhancedRequestSearchTO itemRequest = new EnhancedRequestSearchTO();
			itemRequest.getScoredPSCCodes().add(scoredPSCTO);
			itemRequest.setRequest(fullRequest.getRequest());
			itemRequest.setMaxResults(fullRequest.getMaxResults());
			queries.add(itemRequest);
		}
		return queries;

	}

}
