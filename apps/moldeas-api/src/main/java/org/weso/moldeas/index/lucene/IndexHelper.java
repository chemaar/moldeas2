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
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.weso.moldeas.exceptions.IndexException;
import org.weso.moldeas.query.lucene.QueryAnalyzer;


public class IndexHelper {
	
	protected static Logger logger = Logger.getLogger(IndexHelper.class);
	
	/**
     *  Ensure that directory exists and is an index. If don't exists create a new 
     * one.
     * 
     * @param directory that what to assert
     * @return if a new index was created over directory
     * @throws IndexException Some error occurs accesing to index 
     */
    public static boolean ensureIndex(Directory directory) throws IndexException {
        try {
            if (!IndexReader.indexExists(directory)) {
                logger.debug("Lucene index " + directory.toString()
                        + " does not exist, creating it");
                createIndex(directory);
                return true;
            }
            return false;
        } catch (IOException e) {
            throw new IndexException(e);
        }
    }
    
    /**
     * Create a new index in the (Lucene) directory. If the index already
     * exists, this method overwrites all its contents
     * 
     * @throws IndexException Failed to create the index
     */
    public static void createIndex(Directory directory) throws IndexException {
        boolean createIndexFlag = true;
        IndexWriter writer = null;
        try {
			writer = new IndexWriter(directory,  new QueryAnalyzer(Version.LUCENE_29), createIndexFlag, MaxFieldLength.UNLIMITED );
            logger.info("Created document index");
        } catch (IOException e) {
            throw new IndexException(e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new IndexException(e);
                }
            }
        }
    }
}
