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

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.weso.moldeas.exceptions.IndexException;
import org.weso.moldeas.exceptions.MoldeasModelException;
import org.weso.moldeas.lucene.searcher.MoldeasSearchersManager;
import org.weso.moldeas.query.lucene.QueryAnalyzer;

/**
 * 
 * Adaptation from Iván Frade's (ivan.frade@gmail.com) version in project BOPA.
 * @author chema
 *
 */
public class LuceneDAOFactory {

	    private static final Logger logger = Logger.getLogger(LuceneDAOFactory.class);
	    
	    private static QueryAnalyzer analyzer = null;
	    
	    private static LuceneDirectory lds;
	    
	    public LuceneDAOFactory() {
	     	if ( lds == null ) {
	     		try {
					lds = LuceneDirectorySingleton.getInstance();
				} catch (IndexException e) {
					logger.error("Check permissions in Lucene directories");
				}
	     	}
	    }
	    
	    private IndexReader createReader(Directory directory) throws IndexException {
	        try {
	            IndexHelper.ensureIndex(directory);
	            return IndexReader.open(directory);
	        } catch (IOException io) {
	            throw new IndexException(io);
	        }
	    }

	 

	    public IndexWriter createWriter(Directory directory) throws IndexException {
	        try {
	        	IndexHelper.ensureIndex(directory);
	            return new IndexWriter(directory, getQueryAnalyzer(), false, MaxFieldLength.UNLIMITED);
	        } catch (IOException e) {
	            throw new IndexException(e);
	        }
	    }

	  private Analyzer getQueryAnalyzer() {
			if ( analyzer == null )
				analyzer = new QueryAnalyzer(Version.LUCENE_29);
			return analyzer;
		}

		public IndexSearcher createSearcher(Directory directory) throws IndexException {
	        try {
	            IndexHelper.ensureIndex(directory);
	            return MoldeasSearchersManager.getSearcher(directory); 
	        } catch (IOException io) {
	            throw new IndexException(io);
	        }
	    }

	        
	    public PSCIndexInsertionDAO createPSCIndexInsertionDAO() {
	        try {
	            Directory index = lds.getPSCIndexDirectory();
	            IndexWriter writer = createWriter(index);
	            return new PSCIndexInsertionDAO(writer);
	        } catch (IndexException e) {
	            throw new MoldeasModelException ("Index DAO Factory: creating concept index insertion DAO", e);
	        }            
	    }
	    
	    public PSCIndexSearchDAO createPSCIndexSearchDAO() {
	        try {
	            Directory index = lds.getPSCIndexDirectory();
	            IndexSearcher searcher = createSearcher(index);
	            return new PSCIndexSearchDAO(searcher);
	        } catch (IndexException e) {
	        	 throw new MoldeasModelException ("Index DAO Factory: creating concept index search DAO", e);
	        }
	    }
	    


	
}
