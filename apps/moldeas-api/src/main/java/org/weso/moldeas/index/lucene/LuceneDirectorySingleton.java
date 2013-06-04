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

import org.weso.moldeas.exceptions.IndexException;

public class LuceneDirectorySingleton extends LuceneDirectoryAbstract implements LuceneDirectory{

	/**
	 * @author ivan
	 *
	 */

	protected static String INDEXPATH = null; 
	protected static String DICTPATH = null; 
	protected static String SPELLPATH = null;

	private static LuceneDirectory instance;

//	static {
//		ResourceBundle bundle = ResourceBundle.
//		getBundle(LuceneDirectorySingleton.class.getName().toString());
//
//		INDEXPATH = bundle.getString("articles.index");
//		DICTPATH = bundle.getString("dictionary.index");
//		SPELLPATH = bundle.getString("spell.index");
//
//	}


	/**
	 *  Initialices index directories
	 * @throws IndexException Unable to create index directory!
	 */
	protected LuceneDirectorySingleton() throws IndexException {
		logger.debug("Using " + INDEXPATH + " as articles index");
		//articlesIndex = createFSDirectory(INDEXPATH);
		this.pscIndexDirectory = createRAMDirectory();
		//dictionaryIndex = createFSDirectory(DICTPATH);
		//spelledIndex = createFSDirectory(SPELLPATH);

	}

	public static LuceneDirectory getInstance() throws IndexException {
		if (instance == null) {
			instance = new LuceneDirectorySingleton();
		}
		return instance;
	}

	public static void setInstance(LuceneDirectory ld) {
		instance = ld;
	}


}

