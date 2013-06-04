package org.weso.moldeas.experiment;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import org.junit.Test;
import org.weso.moldeas.enhancers.psc.SolrNLPPSCCodesEnhancer;
import org.weso.moldeas.to.EnhancedRequestSearchTO;
import org.weso.moldeas.to.RequestSearchTO;
import org.weso.moldeas.to.ScoredPSCTO;

public class NLPTest {
	
	public static EnhancedRequestSearchTO executeQuery(SolrNLPPSCCodesEnhancer enhancer, String query, int results) {
		RequestSearchTO request = new RequestSearchTO();
		request.setMaxResults(results);
		request.setStringQuery(query);
		return enhancer.enhance(request);
		
	}
	@Test
	public void testQueries() throws IOException{
		SolrNLPPSCCodesEnhancer enhancer = new SolrNLPPSCCodesEnhancer();
		String []queries = new String[]{
				"comprehensive overview over all environmental technologies renewable energy products", // query1
				"Tendering of public works: housing, hospitals, roads, housing developments, station drinking water treatment, reforestation", // q2
				"Prefabricated buildings",  // q3 
				"GAMES FOR CHILDREN (PARKS SWINGS, SLIDES, LAND OF PLAY EQUIPMENT IN THE PUBLIC SPHERE ", // q4
				"vital signs monitor", //q5
				"museum and exhibition and product launch services", //q6
				"Voltmeters, instruments measuring electrical quantities, Ammeters, Instruments for checking physical characteristics, hygrometers, thermometers, measuring equipment and control, leak detector, Analyzers, Cable Splicing insulated cable joints kits, screwdrivers, hand tools , screwdriver", //q7
				"conservation Maintenance of pavements for roads, airfields, bridges, tunnels",//q8
				"Wood poles, Wooden sleepers , Lattice towers", //q9
				"Architectural, construction, engineering and inspection services", //q10
				"Medical practice and related services" //q11
			};		
		int []results = new int[]{
				463,
				35,
				7,
				26,
				277,
				1,
				117,
				13,
				10,
				173,
				13
			};
		for(int i = 0; i<queries.length;i++){
			EnhancedRequestSearchTO result = executeQuery(enhancer, queries[i],results[i]);
			PrintWriter pw = new PrintWriter(new File("/home/chema/tmp/queries/result/q"+(i+1)+"-nlp"));			
			Set<ScoredPSCTO> scoredPSCs = result.getScoredPSCCodes();
			for(ScoredPSCTO pscTO: scoredPSCs){
				String uri = pscTO.getPscTO().getUri();
				String id = uri.substring(uri.lastIndexOf("/")+1,uri.length());
				pw.println(id);
			}
			scoredPSCs.clear();
			scoredPSCs = null;
			result = null;
			pw.close();
		}
		
	}
}
