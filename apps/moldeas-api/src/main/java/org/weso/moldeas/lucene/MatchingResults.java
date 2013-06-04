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
package org.weso.moldeas.lucene;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.util.Version;
import org.weso.moldeas.query.lucene.QueryAnalyzer;
import org.weso.moldeas.to.ScoredPSCTO;

public class MatchingResults {

	private static Logger logger = Logger.getLogger(MatchingResults.class);
	
	Set<ScoredPSCTO> activatedPSCs = new HashSet<ScoredPSCTO>();
	boolean fuzzy = false;
	String query;
	
	public MatchingResults(String query) {
		this.query = query;
	}
	
	/**
	 * @param query
	 * @param concepts
	 */
	public MatchingResults(String query, Set<ScoredPSCTO> set, boolean fuzzy) {
		this.query = query;
		this.activatedPSCs = set;
		this.fuzzy = fuzzy;
	}

	public String getQuery() {
		return query;
	}
	
	public MatchingResults setQuery(String query) {
		this.query = query;
		return this;
	}
	
	// Move as method of Matching Results!!! (work with his data!)
	public void removeFindedTermsFromQuery(String matchedWord) {
		logger.debug("RAW: Removing " + matchedWord + " from query: " + getQuery());
		String[] parts = matchedWord.split("( )+");
		query = analyze(query.toLowerCase());
		for ( int i = 0; i < parts.length; i++ ) {
			//logger.debug("Removing: -" + analyze(parts[i].toLowerCase()) + "- from query -" + query + "-");
			query = query.replaceFirst("(\\b)" + analyze(parts[i].toLowerCase()) + "(s|es|as)?(\\b)", " ");
			//logger.debug("Removing empty quotes and brackets");
			query = query.replaceAll("\"( )*\"" ,"").replaceAll("\\(( )*\\)","");
		}
		query = analyze(query.trim());
	}

	public String analyze(String chain) {
//		StringReader buffer = new StringReader(chain);
//		QueryAnalyzer qa = new QueryAnalyzer(Version.LUCENE_30);		
//		TokenStream ts =  qa.tokenStream("", buffer);
////		
////		
////		String result = "";
////		try {
////			//FIXME
////			
////			for (Token t = ts.getAttribute(Token.class);ts.incrementToken(); ) {
////				result += t.term() + " ";
////			}
////			return result.trim();
////		} catch (IOException e) {
////			return chain;
////		}
		//FIXME:
		return chain;
	}

	public boolean isFuzzy() {
		return fuzzy;
	}

	public Set<ScoredPSCTO> getActivatedPSCs() {
		return activatedPSCs;
	}

	public void setActivatedPSCs(Set<ScoredPSCTO> activatedPSCs) {
		this.activatedPSCs = activatedPSCs;
	}

	public MatchingResults addScoredPSCTO(ScoredPSCTO scoredPSCTO) {
		this.activatedPSCs.add(scoredPSCTO);
		return this;
	}

	public MatchingResults addActivatedPSCs(List<ScoredPSCTO> scoredPSCTO) {
		this.activatedPSCs.addAll(scoredPSCTO);	
		return this;
	}

	
}
