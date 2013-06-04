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
package org.weso.moldeas.psc;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.weso.moldeas.exceptions.EmptySyntacticQueryException;
import org.weso.moldeas.exceptions.IndexException;
import org.weso.moldeas.exceptions.InvalidQueryException;
import org.weso.moldeas.exceptions.MoldeasModelException;
import org.weso.moldeas.index.lucene.LuceneDAOFactory;
import org.weso.moldeas.index.lucene.PSCIndexFields;
import org.weso.moldeas.index.lucene.PSCIndexInsertionDAO;
import org.weso.moldeas.index.lucene.PSCIndexSearchDAO;
import org.weso.moldeas.lucene.MatchingResults;
import org.weso.moldeas.query.lucene.QueryAnalyzer;
import org.weso.moldeas.query.lucene.QueryFactory;
import org.weso.moldeas.to.PSCSearchResultTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.ScoredPSCTO;
import org.weso.moldeas.utils.ApplicationContextLocator;


public class PSCSearch {

	private static int MAX_HITS = 5;//FIXME: Extract Important! how to change and obtain results?
	private LuceneDAOFactory luceneDAOFactory;
	private static Logger logger = Logger.getLogger(PSCSearch.class);
	private Date lastIndexUpdate = null;

	public PSCSearch() {
		this.luceneDAOFactory = (LuceneDAOFactory) ApplicationContextLocator.getApplicationContext().getBean(LuceneDAOFactory.class.getSimpleName());        
	}

	public PSCSearch(LuceneDAOFactory luceneDAOFactory) {
		super();
		this.luceneDAOFactory = luceneDAOFactory;
	}
	public PSCSearch(LuceneDAOFactory luceneDAOFactory, int max) {
		super();
		this.luceneDAOFactory = luceneDAOFactory;
		MAX_HITS = max;
	}
	
	private PSCIndexInsertionDAO createPSCIndexInsertionDAO() {
		return luceneDAOFactory.createPSCIndexInsertionDAO();
	}

	private PSCIndexSearchDAO createPSCIndexSearchDAO() {
		return luceneDAOFactory.createPSCIndexSearchDAO();
	}

	public void indexConcepts(List<PSCTO> pscTOs) {
		try {
			createPSCIndexInsertionDAO().insertPSCTOs(pscTOs);
			lastIndexUpdate = new Date();
		} catch (IndexException e) {
			throw new MoldeasModelException(e);
		}            
	}
	
	//FIXME:review
	public PSCSearchResultTO search(String query) throws EmptySyntacticQueryException, NoResultsException{
		if ( query != null && !"".equals(query)) {
			MatchingResults result  = simpleSearch(query);
			return new PSCSearchResultTO(result.getActivatedPSCs(), 
					result.isFuzzy());
		}else {
			throw new EmptySyntacticQueryException(query);
		}
	}
	
	private MatchingResults simpleSearch(String q) throws NoResultsException {
		try {
			Query query = QueryFactory.createQueryFromString(q);
			List<ScoredPSCTO> pscTOs = createPSCIndexSearchDAO().runSimpleQuery(query,MAX_HITS);
			logger.debug("Results: "+pscTOs.size());
			if(pscTOs.size()==0){
				throw new NoResultsException();
			}
			MatchingResults m = new MatchingResults(q);
			return m.addActivatedPSCs(pscTOs);
		} catch (IOException e) {
			throw new MoldeasModelException("Searching psc by " + q,e);
		} catch (ParseException e) {
			throw new MoldeasModelException("Searching psc by " + q+" parsing exception.",e);
		}			

	}

	/**
	 * Try to match all query to only one concept. If it don't present good result,
	 *  try to match various concepts. If it also don't work, then perform the
	 *  old good search (syntactic and fuzzy)
	 * 
	 * @param query Search term(s)
	 * @return A transfer object with the results
	 * @throws EmptySyntacticQueryException The query contained no significative terms 
	 * @throws InvalidQueryException Error parsing the query 
	 */
	public PSCSearchResultTO searchPSCs(String query)
	throws EmptySyntacticQueryException, InvalidQueryException {
		if ( query != null && !"".equals(query)) {
			MatchingResults q = new MatchingResults(query);
			MatchingResults result  = enhancedSearch(q);
			return new PSCSearchResultTO(result.getActivatedPSCs(), result.isFuzzy());
		} else {
			throw new EmptySyntacticQueryException(query);
		}
	}

	
	public PSCSearchResultTO searchPSCsByCode(String code)
	throws EmptySyntacticQueryException, InvalidQueryException {
		if ( code != null && !"".equals(code)) {
			MatchingResults q = new MatchingResults(code);
			MatchingResults result;
			try {
				result = exactPSCCodeMatch(q);
			} catch (IndexException e) {
				throw new MoldeasModelException("Searching psc by " + q.getQuery());
			}
			return new PSCSearchResultTO(result.getActivatedPSCs(), result.isFuzzy());
		} else {
			throw new EmptySyntacticQueryException(code);
		}
	}
	
	
	private MatchingResults enhancedSearch(MatchingResults m) throws EmptySyntacticQueryException, InvalidQueryException {
		try {
			m = exactPSCMatch(m);
			if(!"".equals(m.getQuery())){
				if ( m.getActivatedPSCs().size() == 0) {
					throw new NoResultsException();
				}
			}
			//FIXME:
//			if ( !"".equals(m.getQuery()) && 
//					m.getActivatedPSCs().size()>1) {
//				logger.debug("Unable to match exact concept, trying mixing concepts");
//				m = multipleConceptMatch(m);
//				if ( m.getActivatedPSCs().size() == 0 && "".equals(m.getQuery())) {
//					throw new NoResultsException();
//				}
//				return m;
//			}
		} catch (NoResultsException e1) {
			logger.debug("Unable to match concepts, trying old good query");
			PSCSearchResultTO classicResult = classicSearchPSCs(m.getQuery());
			return new MatchingResults(m.getQuery(), 
					classicResult.getScoredscTOs(), 
					classicResult.isFuzzy());
		} catch (IndexException e1) {
			throw new MoldeasModelException("Searching psc by " + m.getQuery());
		} catch (InvalidQueryException e) {
			throw new MoldeasModelException("Searching psc by " + m.getQuery());
		}
		return m;
	}

	
	private MatchingResults exactPSCCodeMatch(MatchingResults m) 
	throws IndexException {
		try {
			PSCTO pscTO = new PSCTO();
			pscTO.setId(m.getQuery());
			Query query = QueryFactory.createQueryFromPSCTO(pscTO);
			List<ScoredPSCTO> pscTOs = createPSCIndexSearchDAO().runQuery(query,MAX_HITS);
			// Founded perfect match!
			if (pscTOs.size() == 1 ) {
				m.setQuery("");
				return m.addScoredPSCTO(pscTOs.get(0));
			} 
			return m.addActivatedPSCs(pscTOs);
		} catch (IOException e) {
			throw new IndexException(e);
		}
	}
	
	
	/**
	 *  AND stage. Try to find one and only one concept that match
	 *  with complete query. A special case is one correct concept
	 *  that is present in 2 context:
	 * 
	 *  typed: Pilar
	 *  return: Pilar (A)
	 *          Pilar (A)
	 *  
	 * @param queryTyped query typed by user
	 * @return Search result TO with ScoredConceptsCtxTO
	 * @throws InvalidQueryException Error parsing query
	 * @throws IndexException Error accessing index
	 * @throws NoResultsException Unable to activate any concept!
	 */
	private MatchingResults exactPSCMatch(MatchingResults m) 
	throws InvalidQueryException, IndexException {

		try {
			QueryParser parser = new QueryParser(
					PSCIndexFields.PSC_FIELDS.PREF_LABEL.toString(), 
					new QueryAnalyzer(Version.LUCENE_29));
			parser.setDefaultOperator(QueryParser.Operator.AND);
			Query query = parser.parse(m.getQuery());
			List<ScoredPSCTO> pscTOs = createPSCIndexSearchDAO().runQuery(query,MAX_HITS);
			// Founded perfect match!
			if (pscTOs.size() == 1 ) {
				m.setQuery("");
				return m.addScoredPSCTO(pscTOs.get(0));
			} 
			return m.addActivatedPSCs(pscTOs);
		} catch (ParseException e) {
			throw new InvalidQueryException(m.getQuery(), e);
		} catch (IOException e) {
			throw new IndexException(e);
		}
	}

	/**
	 * Syntactically search of a concept in the index
	 * 
	 * @param query Search term(s)
	 * @return A transfer object with the results
	 * @throws EmptySyntacticQueryException The query contained no significative terms 
	 * @throws InvalidQueryException Error parsing the query 
	 */
	public PSCSearchResultTO classicSearchPSCs(String query)
	throws EmptySyntacticQueryException, InvalidQueryException {

		try {
			// try a literal-search
			boolean fuzzy = false;
			List<ScoredPSCTO> searchResults = createPSCIndexSearchDAO().searchConcept(query,MAX_HITS);
			// fallback to fuzzy search if the literal-search returned no results
			if (searchResults.size() == 0) {
				fuzzy = true;
				searchResults = createPSCIndexSearchDAO().searchConceptFuzzy(query);
			}
			return new PSCSearchResultTO(new HashSet(searchResults), fuzzy);
		} catch (IndexException e) {
			throw new MoldeasModelException("Searching concept by " + query, e);
		}        
	}


	public Date getLastIndexUpdate() {
		return lastIndexUpdate;
	}

	public void setLastIndexUpdate(Date lastIndexUpdate) {
		this.lastIndexUpdate = lastIndexUpdate;
	}

	/**
	 * 
	 *  OR Stage. Try to find concept with so much terms as possible in query.
	 *  It probably will not consume all terms. Prepare to recursive invocation.
	 * 
	 *  "Accesibilidad Universidad de Oviedo"
	 *  first invocation: "Universidad de Oviedo"
	 * 	second invocation: "Accesibilidad"
	 * 
	 * @param query
	 * @return
	 * @throws NoResultsException
	 * @throws IndexException
	 * @throws InvalidQueryException
	 */
	private MatchingResults multipleConceptMatch(MatchingResults r) throws  IndexException, InvalidQueryException, NoResultsException {

		QueryParser parser = new QueryParser(PSCIndexFields.PSC_FIELDS.PREF_LABEL.toString(), 
				new QueryAnalyzer(Version.LUCENE_29));
		parser.setDefaultOperator(QueryParser.Operator.OR);

		if ( "".equals(r.getQuery())) { return r; } //End recursivity (no more query)

		try {
			Query queryL = parser.parse(r.getQuery());
			List<ScoredPSCTO> searchResults = createPSCIndexSearchDAO().runQuery(queryL, MAX_HITS);
			logger.debug("Candidated with: " + r.getQuery() + " (" + searchResults.size() + ")");
			if ( searchResults.size()  == 0 ) {
				logger.debug("No candidated with: " + r.getQuery());
				if ( r.getActivatedPSCs().size() != 0 ) {
					return r; //End recursivity (no more activation candidate queries)
				} else {
					throw new NoResultsException(); //Unable to determine concept, try classic way
				}
			}
			// ONLY one possible concept!!!! We are lucky :) 
			//  (probably lot of no-concept related words)
			if ( searchResults.size() == 1 ) {
				r.addScoredPSCTO(searchResults.get(0));
				return r;
			}

			// If first result is not enought good...
			if ( searchResults.size() > 1 && searchResults.get(0).getScore() < 0.7f ) {
				logger.debug("Rejecting concept " + searchResults.get(0).getPscTO().getUri()
						+ " with " + searchResults.get(0).getScore());
				return r;
			}

			//logger.debug("Obtained " + ctxTOs.length + " results (Adding initial)");

			// Multiple concepts (Could be the same in different contexts and/or multiple with the same score)
			r.addScoredPSCTO(searchResults.get(0));
			for ( int i = 1; i < searchResults.size(); i++){
				if (Double.compare(searchResults.get(0).getScore(), searchResults.get(i).getScore()) == 0 ) {
					r.addScoredPSCTO(searchResults.get(i));
				}
			}
			// Do only once (don't required once for each context)
			String matchedWord = searchResults.get(0).getPscTO().getPrefLabel();
			r.removeFindedTermsFromQuery(matchedWord);
			return multipleConceptMatch(r);
		} catch (IOException e) {
			throw new IndexException(e);
		} catch (ParseException e) {
			throw new InvalidQueryException(r.getQuery(), e);
		}
	}

}
class NoResultsException extends Exception {}