package org.weso.moldeas.experiment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.weso.moldeas.enhancers.psc.PSCNarrowerEnhancer;
import org.weso.moldeas.enhancers.psc.SolrNLPPSCCodesEnhancer;
import org.weso.moldeas.to.EnhancedRequestSearchTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.RequestSearchTO;
import org.weso.moldeas.to.ScoredPSCTO;
import org.weso.moldeas.utils.ScoredPSCTOComparator;

public class PSCNarrowerEnhancerExpTest {
	ScoredPSCTOComparator mycomparator = new ScoredPSCTOComparator();

	@Test
	public void testQueries() throws IOException{
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
				"Medical practice and related services", //q11
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
		//1-Step
		SolrNLPPSCCodesEnhancer enhancer = new SolrNLPPSCCodesEnhancer();	
		PSCNarrowerEnhancer enhancerPSC = new PSCNarrowerEnhancer();	
		for(int i = 0; i<queries.length;i++){
			EnhancedRequestSearchTO result = NLPTest.executeQuery(enhancer, queries[i], results[i]);			
			Set<ScoredPSCTO> scoredPSCs = result.getScoredPSCCodes();
			RequestSearchTO request = new RequestSearchTO();				
			for(ScoredPSCTO pscTO: scoredPSCs){
				request.getPscCodes().add(pscTO.getPscTO());
				request.getSwap().put(pscTO.getPscTO().getUri(), pscTO.getScore());
			}
			//Clean 1st-step				
			result = null;
			//2-Step
			result = enhancerPSC.enhance(request);
			saveResults(result.getScoredPSCCodes(), "q"+(i+1)+"-narrower", mycomparator, results[i]);
			//Clean 2nd-step	
			scoredPSCs.clear();
			scoredPSCs = null;
			result = null;
		}	
		
	}
	public static void saveResults(Set<ScoredPSCTO> scoredPSCs, String filename, ScoredPSCTOComparator comparator, int max) throws FileNotFoundException{
		List<ScoredPSCTO> finals = new LinkedList<ScoredPSCTO>(scoredPSCs);
		Collections.sort(finals, comparator); //FIXME: no ordenar sino ponderar o que se acumule en 		
		PrintWriter pw = new PrintWriter(new File("/home/chema/tmp/queries/result/"+filename));		
		for(int i = 0; i<max && i<finals.size();i++){
			PSCTO pscTO = finals.get(i).getPscTO();
			String uri = pscTO.getUri();
			String id = uri.substring(uri.lastIndexOf("/")+1,uri.length());
			pw.println(id);
		}
		pw.close();
	}
}
