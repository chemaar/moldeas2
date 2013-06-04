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
package org.weso.moldeas.searchers;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.weso.moldeas.searchers.SPARQLSearchEngine;
import org.weso.moldeas.to.EnhancedRequestSearchTO;
import org.weso.moldeas.to.NUTSTO;
import org.weso.moldeas.to.PPNResultTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.RequestSearchTO;
import org.weso.moldeas.to.ScoredPSCTO;
import org.weso.moldeas.to.YearsTO;
import org.weso.moldeas.utils.SPARQLPPNUtils;

public class SearchMultiThreadTest {
	
	protected static Logger logger = Logger.getLogger(SearchMultiThreadTest.class);
	protected Boolean needFree = false;
	Thread [] consumers = new Thread[0];
	Thread [] producers = new Thread[0];

	class Producer implements Runnable {
		protected BlockingQueue<PPNResultTO>  responses;
		protected BlockingQueue<EnhancedRequestSearchTO>  requests;
		protected PPNResultTO result;

		Producer (BlockingQueue<PPNResultTO>  responses, BlockingQueue<EnhancedRequestSearchTO> requests){ 
			this.responses = responses;
			this.requests = requests;
		}

		public void run () {
			try {
				//System.out.println("Entrando en run de productor "+Thread.currentThread().getName()+" con "+this.requests.size());
				while (true) {
					EnhancedRequestSearchTO request = this.requests.take();
					if (request != null && request.getScoredPSCCodes().size()!=0) {					
						//System.out.println("Putting response "+enhancedRequestSearch.getScoredPnnsTO().size());
						this.responses.put(getEnhancedRequestSearch(request));
					}
					//Si no hay mas peticiones finaliza					
					if (requests.size() == 0 || request.getScoredPSCCodes().size() == 0) {//Stop condition		
						//System.out.println("Finalizando productor "+Thread.currentThread().getName());
						PPNResultTO ppnResultTO = new PPNResultTO();
						ppnResultTO.setTime(-1);
						this.responses.add(ppnResultTO);	//Response de parada al consumidor					
						return ;
					}else{
					
					}

				}
			}catch ( InterruptedException ex){
				ex.printStackTrace();
			}
		}

		synchronized PPNResultTO getEnhancedRequestSearch (EnhancedRequestSearchTO request) {
			//test 6_1		execEnhancedNamed(request);
			//return execEnhancedNamed(request);
			//tets 7-1, 9_1
			return execEnhancedRewrite(request);
			//return SPARQLSearchEngine.fetchResults(createEnhancedQuery(request, 2));
		}
	}
	
	
	class Consumer implements Runnable {
		protected BlockingQueue<PPNResultTO>  responses;
		protected BlockingQueue<EnhancedRequestSearchTO>  requests;
		Consumer (BlockingQueue<PPNResultTO> responses, BlockingQueue<EnhancedRequestSearchTO> requests){ 
			this.responses = responses;
			this.requests = requests;
		}

		public void run () {
			try {
				//System.out.println("Entrando en run de consumidor "+Thread.currentThread().getName());
				while (true) {
					PPNResultTO response = this.responses.take();
					EnhancedRequestSearchTO newRequest = process(response);
					//Condición de parada
					if (response.getTime() == -1) {				
						synchronized (needFree) {
							if(needFree){
								//Si los recursos no han sido liberados
								needFree = !freeResources();
							}
						}
					//	System.out.println("Finalizando consumidor "+Thread.currentThread().getName());
						return ;
					}
					//Siempre pone una request para que el productor no bloquee al final.
					this.requests.put(newRequest);	
				}

			} catch ( InterruptedException ex ) {
			//	System.out.println ( "Consumer interrupted" ) ;
			}
		}

		private boolean freeResources() throws InterruptedException {
			//El hilo finaliza y libera los productores
			for ( int i= 0 ; i<producers.length; i++ ){
				if (producers[i].isAlive()){
					//System.out.println("Enviando mensaje para que el productor "+producers[i].getName()+" finalice.");
					this.requests.put(new EnhancedRequestSearchTO());
				}
			}
			for ( int i= 0 ; i<consumers.length; i++ ){
				if (consumers[i].getId() != Thread.currentThread().getId() && consumers[i].isAlive()){
				//	System.out.println("Enviando mensaje para que el consumidor "+consumers[i].getName()+" finalice.");
					PPNResultTO ppnResultTO = new PPNResultTO();
					ppnResultTO.setTime(-1);
					this.responses.put(ppnResultTO);
				}
			}
			return true;
		}

		EnhancedRequestSearchTO process ( PPNResultTO response ) {
			//System.out.println ("Procesando respuesta con :"+response.getScoredPnnsTO().size()) ;
			return new EnhancedRequestSearchTO(); //Vacía para que finalizen los productores
		}
	}

	

	public void exec(int nP, int nC, BlockingQueue<EnhancedRequestSearchTO> requests) throws InterruptedException{
		this.needFree = true;
		BlockingQueue<PPNResultTO>responses = new LinkedBlockingQueue<PPNResultTO> () ; //Productor produce responses y consume requests
		//3-Iniciamos metiendo la primera peticion del fichero linea 0		
		//4-Lanzamos los hilos
		this.consumers = new Thread[nC];
		this.producers = new Thread[nP];

		for ( int i= 0 ; i<nC; i++ ){
			this.consumers[i] = new Thread ( new Consumer(responses, requests));
			this.consumers[i].start();
		}
		for ( int i= 0 ; i<nP; i++ ){
			this.producers[i] = new Thread ( new Producer (responses, requests));
			this.producers[i].start();
		}
		for ( int i= 0 ; i<nC; i++ ){
			try{this.consumers[i].join();}catch(InterruptedException E){}
		}
		for ( int i= 0 ; i<nP; i++ ){
			try{this.producers[i].join();}catch(InterruptedException E){}
		}
		//System.out.println("Fin execution.");
	}

	



	public static void main(String args[]) throws java.io.IOException,java.lang.NumberFormatException, InterruptedException{
		//Productores y consumidores
		int nP = 5;
		int nC = 1 ;		
		logger.debug("Executing TEST 8_1 ");
		logger.debug("----------------------------------------------");
		for(int i = 0; i<9;i++){		
			SearchMultiThreadTest multiclient = new SearchMultiThreadTest();
			for (int j = 0; j<SearchTest.NUMBER_OF_REPLIES; j++){				
				BlockingQueue<EnhancedRequestSearchTO> createQueries = createQueries_8_1(i);
				logger.debug("QUERY "+i+" REPLY "+j+" QUERIES "+createQueries.size());
				long antes = System.currentTimeMillis();		
				multiclient.exec(nP, nC,createQueries);		
				logger.debug("QUERY"+i+"  REPLY "+j+" TOTAL REPLY TIME "+(System.currentTimeMillis()-antes));
			}
		
		}
		

		logger.debug("----------END TEST 8_1------------------------");
	} 

	
	public static BlockingQueue<EnhancedRequestSearchTO> createQueries_6_1(int i ){		
		BlockingQueue<EnhancedRequestSearchTO> queries = new LinkedBlockingQueue<EnhancedRequestSearchTO>();
		EnhancedRequestSearchTO query = SearchTest.getQuery(i+1, SearchTest.MAX_RESULTS);
		for(int j=2008;	j<=2011;j++){
			YearsTO years = new YearsTO(j,j);
			EnhancedRequestSearchTO itemQuery = new EnhancedRequestSearchTO();
			itemQuery.setScoredPSCCodes(query.getScoredPSCCodes());			
			itemQuery.setMaxResults(SearchTest.MAX_RESULTS);
			RequestSearchTO itemSimpleRequest = new RequestSearchTO();
			itemSimpleRequest.setNutsCodes(query.getRequest().getNutsCodes());
			itemSimpleRequest.setPscCodes(query.getRequest().getPscCodes());
			itemSimpleRequest.setMaxResults(SearchTest.MAX_RESULTS);
			itemSimpleRequest.setYears(years);
			itemQuery.setRequest(itemSimpleRequest);
			queries.add(itemQuery);
		}
		return queries;

	}
	public static BlockingQueue<EnhancedRequestSearchTO> createQueries_8_1(int i ){		
		BlockingQueue<EnhancedRequestSearchTO> queries = new LinkedBlockingQueue<EnhancedRequestSearchTO>();
		EnhancedRequestSearchTO query = SearchTest.getQuery(i+1, SearchTest.MAX_RESULTS);		
		for(ScoredPSCTO scoredPSCTO: query.getScoredPSCCodes()){
			for(int j=2008;	j<=2011;j++){
				YearsTO years = new YearsTO(j,j);
				EnhancedRequestSearchTO itemQuery = new EnhancedRequestSearchTO();
				itemQuery.getScoredPSCCodes().add(scoredPSCTO);
				itemQuery.setMaxResults(SearchTest.MAX_RESULTS);
				RequestSearchTO itemSimpleRequest = new RequestSearchTO();
				itemSimpleRequest.setNutsCodes(query.getRequest().getNutsCodes());
			//	itemSimpleRequest.setPscCodes(query.getRequest().getPscCodes());
				itemSimpleRequest.setMaxResults(SearchTest.MAX_RESULTS);
				itemSimpleRequest.setYears(years);
				itemQuery.setRequest(itemSimpleRequest);
				queries.add(itemQuery);
			}
		}
		return queries;

	}
	
	
	public static BlockingQueue<EnhancedRequestSearchTO> createQueries_7_1(int i ){		
		BlockingQueue<EnhancedRequestSearchTO> queries = new LinkedBlockingQueue<EnhancedRequestSearchTO>();
		EnhancedRequestSearchTO fullRequest = SearchTest.getQuery(i+1, SearchTest.MAX_RESULTS);
		for(ScoredPSCTO scoredPSCTO: fullRequest.getScoredPSCCodes()){
			EnhancedRequestSearchTO itemRequest = new EnhancedRequestSearchTO();
			itemRequest.getScoredPSCCodes().add(scoredPSCTO);
			itemRequest.setRequest(fullRequest.getRequest());
			itemRequest.setMaxResults(SearchTest.MAX_RESULTS);
			queries.add(itemRequest);
		}
		return queries;

	}
	
	
	
	public static BlockingQueue<EnhancedRequestSearchTO> createQueries_9_1(int i ){		
		BlockingQueue<EnhancedRequestSearchTO> queries = new LinkedBlockingQueue<EnhancedRequestSearchTO>();
		EnhancedRequestSearchTO fullRequest = SearchTest.getQuery(i+1, SearchTest.MAX_RESULTS);
		for(ScoredPSCTO scoredPSCTO: fullRequest.getScoredPSCCodes()){
			for(NUTSTO nutsTO:fullRequest.getRequest().getNutsCodes()){
				RequestSearchTO itemSimpleRequest = new RequestSearchTO();
				itemSimpleRequest.getNutsCodes().add(nutsTO);
				EnhancedRequestSearchTO itemRequest = new EnhancedRequestSearchTO();
				itemRequest.getScoredPSCCodes().add(scoredPSCTO);
				itemRequest.setRequest(itemSimpleRequest);
				itemRequest.setMaxResults(SearchTest.MAX_RESULTS);
				queries.add(itemRequest);
			}
				
		}
		return queries;
		
	}

//
//	for(ScoredPSCTO scoredPSCTO: fullRequest.getScoredPSCCodes()){
//		for(NUTSTO nutsTO:fullRequest.getRequest().getNutsCodes()){
//			RequestSearchTO itemSimpleRequest = new RequestSearchTO();
//			itemSimpleRequest.getNutsCodes().add(nutsTO);
//			itemSimpleRequest.setYears(fullRequest.getRequest().getYears());
//			EnhancedRequestSearchTO itemRequest = new EnhancedRequestSearchTO();
//			itemRequest.getScoredPSCCodes().add(scoredPSCTO);
//			itemRequest.setRequest(itemSimpleRequest);
//			itemRequest.setMaxResults(MAX_RESULTS);
//			execEnhancedNamed(itemRequest);
//		}
//			
//	}
	
	public static BlockingQueue<EnhancedRequestSearchTO> createQueries_10_1(int i ){		
		BlockingQueue<EnhancedRequestSearchTO> queries = new LinkedBlockingQueue<EnhancedRequestSearchTO>();
		EnhancedRequestSearchTO query = SearchTest.getQuery(i+1, SearchTest.MAX_RESULTS);		
		for(ScoredPSCTO scoredPSCTO: query.getScoredPSCCodes()){
			for(NUTSTO nutsTO:query.getRequest().getNutsCodes()){
				for(int j=2008;	j<=2011;j++){
					YearsTO years = new YearsTO(j,j);
					EnhancedRequestSearchTO itemQuery = new EnhancedRequestSearchTO();
					itemQuery.getScoredPSCCodes().add(scoredPSCTO);
					itemQuery.setMaxResults(SearchTest.MAX_RESULTS);
					RequestSearchTO itemSimpleRequest = new RequestSearchTO();
					itemSimpleRequest.getNutsCodes().add(nutsTO);
					itemSimpleRequest.setPscCodes(query.getRequest().getPscCodes());
					itemSimpleRequest.setMaxResults(SearchTest.MAX_RESULTS);
					itemSimpleRequest.setYears(years);
					itemQuery.setRequest(itemSimpleRequest);
					queries.add(itemQuery);
				}
			}
		}
		return queries;

	}
	
	
	public static PPNResultTO execEnhancedRewrite(EnhancedRequestSearchTO request){
		String query = SPARQLSearchEngineTest.createEnhancedQuery(request, 2);
		return SPARQLSearchEngine.fetchResults(query);
	}
	
	public static PPNResultTO execEnhancedNamed(EnhancedRequestSearchTO request){
		PPNResultTO result = new PPNResultTO();
	    String query = SPARQLSearchEngineTest.createEnhancedQuery(request, 3);
			for(long j=request.getRequest().getYears().getMin();j<=request.getRequest().getYears().getMax();j++){
				List<String> graphUris = new LinkedList<String>();
				logger.debug("GRAPH URI "+j);
				graphUris.add("http://purl.org/weso/ppn/"+j);
				result.getScoredPnnsTO().addAll(SPARQLSearchEngine.fetchResults(query, graphUris).getScoredPnnsTO());		
			}
		return result;
	} 
	
	//Helper methods
	public static String createQuery(RequestSearchTO request, int type) {
		String filterCPVCodes = SPARQLPPNUtils.createFilter (request.getPscCodes());
		String filterNutsCodes = SPARQLPPNUtils.createFilterNUTSCodes(request.getNutsCodes());
		String filterYears = SPARQLPPNUtils.createFilterYears(request.getYears());
		String query;
		if(type==1){ //RAW
			query = SPARQLPPNUtils.createRawQuery(filterCPVCodes, 
					filterNutsCodes, 
					request.getMaxResults(),
					filterYears);
		}else if (type==2) { //Rewrite
			query = SPARQLPPNUtils.createRewritingQuery(filterCPVCodes, 
					filterNutsCodes, 
					request.getMaxResults(),
					filterYears);
		}else {//Rewrite withouth filter years
			query = SPARQLPPNUtils.createRewritingQuery(filterCPVCodes, 
					filterNutsCodes, 
					request.getMaxResults());
		}
		return query;
	}

	public static String createEnhancedQuery(EnhancedRequestSearchTO request, int type) {
		String filterCPVCodes = SPARQLPPNUtils.createFilterScoredCodes(request.getScoredPSCCodes());
		String filterNutsCodes = SPARQLPPNUtils.createFilterNUTSCodes(request.getRequest().getNutsCodes());
		String filterYears = SPARQLPPNUtils.createFilterYears(request.getRequest().getYears());
		String query;
		if(type==1){ //RAW
			query = SPARQLPPNUtils.createRawQuery(filterCPVCodes, 
					filterNutsCodes, 
					request.getMaxResults(),
					filterYears);
		}else if (type==2) { //Rewrite
			query = SPARQLPPNUtils.createRewritingQuery(filterCPVCodes, 
					filterNutsCodes, 
					request.getMaxResults(),
					filterYears);
		}else {//Rewrite withouth filter years+graphUris for years
			query = SPARQLPPNUtils.createRewritingQuery(filterCPVCodes, 
					filterNutsCodes, 
					request.getMaxResults());
		}
		return query;
	}
}
