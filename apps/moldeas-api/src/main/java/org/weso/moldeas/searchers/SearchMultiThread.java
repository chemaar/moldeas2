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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.weso.moldeas.to.EnhancedRequestSearchTO;
import org.weso.moldeas.to.PPNResultTO;
import org.weso.moldeas.to.RequestSearchTO;
import org.weso.moldeas.utils.SPARQLPPNUtils;

public class SearchMultiThread {
	
	protected static Logger logger = Logger.getLogger(SearchMultiThread.class);
	protected Boolean needFree = false;
	Thread [] consumers = new Thread[0];
	Thread [] producers = new Thread[0];	
	protected PPNResultTO result = new PPNResultTO();
	

	public PPNResultTO getResult() {
		return result;
	}

class Producer implements Runnable {
		protected BlockingQueue<PPNResultTO>  responses;
		protected BlockingQueue<EnhancedRequestSearchTO>  requests;


		Producer (BlockingQueue<PPNResultTO>  responses, BlockingQueue<EnhancedRequestSearchTO> requests){ 
			this.responses = responses;
			this.requests = requests;
		}

		public void run () {
			try {
				while (true) {
					EnhancedRequestSearchTO request = this.requests.take();
					if (request != null && request.getScoredPSCCodes().size()!=0) {					
						this.responses.put(getEnhancedRequestSearch(request));
					}
					//Si no hay mas peticiones finaliza					
					if (requests.size() == 0 ) {//Stop condition		
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
			return execEnhancedRewrite(request);
		}
	}
	
	
	class Consumer implements Runnable {
		protected BlockingQueue<PPNResultTO>  responses;
		protected BlockingQueue<EnhancedRequestSearchTO>  requests;
		int finished = 0;
		Consumer (BlockingQueue<PPNResultTO> responses, BlockingQueue<EnhancedRequestSearchTO> requests){ 
			this.responses = responses;
			this.requests = requests;
		}

		public void run () {
			try {;
				while (true) {
					PPNResultTO response = this.responses.take();
					EnhancedRequestSearchTO newRequest = process(response);
					//Condición de parada
					if (response.getTime() == -1) {	
						if(finished == producers.length){
							synchronized (needFree) {
								if(needFree){
									//Si los recursos no han sido liberados
									needFree = !freeResources();
								}
							}
							return ;
							}
						}else{
							finished++;
							this.requests.put(newRequest);	
						}					
				
				}

			} catch ( InterruptedException ex ) {
			}
		}

		private boolean freeResources() throws InterruptedException {
			//El hilo finaliza y libera los productores
			for ( int i= 0 ; i<producers.length; i++ ){
				if (producers[i].isAlive()){
					this.requests.put(new EnhancedRequestSearchTO());
				}
			}
			for ( int i= 0 ; i<consumers.length; i++ ){
				if (consumers[i].getId() != Thread.currentThread().getId() && consumers[i].isAlive()){
					PPNResultTO ppnResultTO = new PPNResultTO();
					ppnResultTO.setTime(-1);
					this.responses.put(ppnResultTO);
				}
			}
			return true;
		}

		synchronized EnhancedRequestSearchTO process ( PPNResultTO response ) {
			result.getScoredPnnsTO().addAll(response.getScoredPnnsTO());
			return new EnhancedRequestSearchTO(); //Vacía para que finalizen los productores
		}
	}

	

	public void exec(int nP, int nC, BlockingQueue<EnhancedRequestSearchTO> requests) throws InterruptedException{
		this.needFree = true;
		BlockingQueue<PPNResultTO>responses = new LinkedBlockingQueue<PPNResultTO> () ; 
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
	}

	



//	public static void main(String args[]) throws java.io.IOException,java.lang.NumberFormatException, InterruptedException{
//		//Productores y consumidores
//		int nP = 5;
//		int nC = 1 ;		
//		SearchMultiThread multiclient = new SearchMultiThread();
//    	BlockingQueue<EnhancedRequestSearchTO> createQueries = createQueries_8_1(i);
//		multiclient.exec(nP, nC,createQueries);		
//
//	}
//		
//
//	} 

	
	
	public static PPNResultTO execEnhancedRewrite(EnhancedRequestSearchTO request){
		String query = createEnhancedQuery(request, 2);
		return SPARQLSearchEngine.fetchResults(query);
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
