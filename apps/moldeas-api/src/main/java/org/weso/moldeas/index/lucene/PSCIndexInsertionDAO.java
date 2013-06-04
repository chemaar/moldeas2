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
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.util.Version;
import org.weso.moldeas.exceptions.IndexException;
import org.weso.moldeas.query.lucene.QueryAnalyzer;
import org.weso.moldeas.to.PSCTO;

public class PSCIndexInsertionDAO {
	private IndexWriter connection;
	protected static Logger logger = Logger.getLogger(PSCIndexInsertionDAO.class);
	
	public PSCIndexInsertionDAO(IndexWriter writer) {
		super();
		connection = writer;
	}

	public void insertPSCTO(PSCTO pscTO) throws IndexException {
		Document[] docs = PSCDocumentFactory.buildDocument(pscTO);
		try {
			for ( int i = 0; i < docs.length; i++) {
				connection.addDocument(docs[i], new QueryAnalyzer(Version.LUCENE_29));
			}
		} catch (IOException e) {
			throw new IndexException(e);
		} finally {
			try {
				connection.close();
			} catch (IOException e1) {
				throw new IndexException(e1);
			}
		}
	}
	public void insertPSCTOs(List<PSCTO> pscTOs) throws IndexException{
		try{
			for(PSCTO pscTO:pscTOs){
				Document[] docs = PSCDocumentFactory.buildDocument(pscTO);
				for ( int i = 0; i < docs.length; i++) {					
					connection.addDocument(docs[i], 
							new QueryAnalyzer(Version.LUCENE_29));
				}
			}
		} catch (IOException e) {
			throw new IndexException(e);
		} finally {
			try {
				connection.close();
			} catch (IOException e1) {
				throw new IndexException(e1);
			}
		}
		
	}
		
		
	

}
