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
package org.weso.moldeas.index.lucene;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.util.Version;
import org.weso.moldeas.exceptions.EmptySyntacticQueryException;
import org.weso.moldeas.exceptions.IndexException;
import org.weso.moldeas.exceptions.InvalidQueryException;
import org.weso.moldeas.query.lucene.QueryAnalyzer;
import org.weso.moldeas.to.ScoredPSCTO;

public class PSCIndexSearchDAO {

	protected static Logger logger = Logger.getLogger(PSCIndexSearchDAO.class);
	private IndexSearcher connection;

	/**
	 * 
	 */
	public PSCIndexSearchDAO(IndexSearcher searcher) {
		super();
		connection = searcher;
	}

	/**
	 * 
	 *  Search a concept in Concept Index, returning ConceptTO collection 
	 * with results
	 * @param queryTerms Terms of the query
	 * @return Array of ConceptTO
	 * @throws IndexException Some error occured accessing to index
	 * @throws InvalidQueryException
	 * @throws EmptySyntacticQueryException
	 */
	public List<ScoredPSCTO> searchConcept(String queryTerms, int n) throws IndexException, InvalidQueryException, EmptySyntacticQueryException {
		if (queryTerms.length() == 0) {
			throw new EmptySyntacticQueryException(queryTerms);
		}
		try {
			logger.debug("Searching concept: "+queryTerms);
			QueryParser parser = new QueryParser(PSCIndexFields.PSC_FIELDS.PREF_LABEL.toString(), 
					new QueryAnalyzer(Version.LUCENE_29));
			Query query = parser.parse(queryTerms); 
			return runQuery(query, n);
		} catch (IOException e) {
			throw new IndexException(e);
		} catch (ParseException e) {
			throw new InvalidQueryException(queryTerms, e);
		} finally {
			try {
				connection.close();
			} catch (IOException e1) {
				throw new IndexException(e1);
			}
		}

	}


	/**
	 *  Search query using a fuzzy search
	 * 
	 * @param publicName query text
	 * @return Array of conceptTO
	 * @throws IndexException Some error occurs accessing index
	 */
	public List<ScoredPSCTO> searchConceptFuzzy(String publicName) throws IndexException {
		Query query;
		try {
			QueryParser parser = new QueryParser(PSCIndexFields.PSC_FIELDS.PREF_LABEL.toString(), 
					new QueryAnalyzer(Version.LUCENE_29));
			parser.setDefaultOperator(QueryParser.Operator.OR);

			//FIXME Improve this code!
			String fuzzylized = publicName.replaceAll(" ","~ ");
			fuzzylized = fuzzylized.concat("~");

			query = parser.parse(fuzzylized);
			return runQuery(query, 100);
		} catch (IOException e) {
			throw new IndexException(e);
		} catch (ParseException e) {
			return new LinkedList<ScoredPSCTO>();
		} finally {
			try {
				connection.close();
			} catch (IOException e1) {
				throw new IndexException(e1);
			}
		}
	}

	public List<ScoredPSCTO> runQuery(Query query, int n) throws IOException {
		List<ScoredPSCTO> result = new LinkedList<ScoredPSCTO>();
		try {
			TopDocs hits = connection.search(query, n);	
			ScoreDoc[] scoreDocs = hits.scoreDocs;
			for (int i = 0; i < scoreDocs.length; i++) {
				Document retrieved = connection.doc(scoreDocs[i].doc);
				float score = scoreDocs[i].score;
				ScoredPSCTO scoredPSCTO = PSCDocumentFactory.buildPSCTO(retrieved, score);
				result.add(scoredPSCTO);
			}
		} finally {
			connection.close();
		}
		return result;
	}
	
	public List<ScoredPSCTO> runSimpleQuery(Query query, int n) throws IOException {
		List<ScoredPSCTO> result = new LinkedList<ScoredPSCTO>();
	    TopScoreDocCollector collector = TopScoreDocCollector.create(n, true);
	    connection.search(query, collector);
	    ScoreDoc[] scoreDocs = collector.topDocs().scoreDocs;
		try {
			for (int i = 0; i < scoreDocs.length; i++) {
				Document retrieved = connection.doc(scoreDocs[i].doc);
				float score = scoreDocs[i].score;
				ScoredPSCTO scoredPSCTO = PSCDocumentFactory.buildPSCTO(retrieved, score);
				result.add(scoredPSCTO);
			}
		} finally {
			connection.close();
		}
		return result;
	}
}
