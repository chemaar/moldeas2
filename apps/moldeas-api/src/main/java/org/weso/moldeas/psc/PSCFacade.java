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

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.weso.moldeas.dao.NUTSDAO;
import org.weso.moldeas.dao.PSCDAO;
import org.weso.moldeas.dao.impl.CPV2008DAOImpl;
import org.weso.moldeas.exceptions.EmptySyntacticQueryException;
import org.weso.moldeas.exceptions.InvalidQueryException;
import org.weso.moldeas.to.NUTSTO;
import org.weso.moldeas.to.PSCSearchResultTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.utils.ApplicationContextLocator;
//TODO: Index of the concepts in a file
//TODO: Index of the ppns in a file
//TODO: files of Mahout prebuilt
//TODO: Use of SPring to create objects
public class PSCFacade {


	private static final Logger logger = Logger.getLogger(PSCFacade.class);
	private PSCSearch pscSearch;
	private PSCDAO pscDAO;
	private NUTSDAO nutsDAO;
	

	
	public PSCFacade(PSCSearch pscSearch, PSCDAO pscDAO) {
		super();
		this.pscSearch = pscSearch;
		this.pscDAO = pscDAO;
	}

	public PSCFacade(PSCSearch pscSearch, PSCDAO pscDAO, NUTSDAO nutsDAO) {
		super();
		this.pscSearch = pscSearch;
		this.pscDAO = pscDAO;
		this.nutsDAO = nutsDAO;
	}

	public PSCFacade() {
		super();
		this.pscSearch = (PSCSearch) 
		ApplicationContextLocator.getApplicationContext().getBean(PSCSearch.class.getSimpleName());
		this.pscDAO = (PSCDAO) 
		ApplicationContextLocator.getApplicationContext().getBean(PSCDAO.class.getSimpleName());
		this.nutsDAO = (NUTSDAO) 
		ApplicationContextLocator.getApplicationContext().getBean(NUTSDAO.class.getSimpleName());
	
	}

	public PSCSearchResultTO search(String query)
	throws EmptySyntacticQueryException, InvalidQueryException, NoResultsException {
		logger.debug("Searching psc: " + query);
		assertConceptIndexUpdated();
		return pscSearch.search(query);
	}

	/**
	 * Perform a syntactic search in the concept database
	 * 
	 * @param query Search term
	 * @return A concept search result transfer object
	 * @throws InvalidQueryException
	 * @throws EmptySyntacticQueryException
	 */
	public PSCSearchResultTO searchPSC(String query)
	throws EmptySyntacticQueryException, InvalidQueryException {
		logger.debug("Searching psc: " + query);
		assertConceptIndexUpdated();
		return pscSearch.searchPSCs(query);
	}

	/**
	 * Perform a syntactic search in the concept database
	 * 
	 * @param query Search term
	 * @return A concept search result transfer object
	 * @throws InvalidQueryException
	 * @throws EmptySyntacticQueryException
	 */
	public PSCSearchResultTO searchPSCByCode(String code)
	throws EmptySyntacticQueryException, InvalidQueryException {
		logger.debug("Searching code psc: " + code);
		assertConceptIndexUpdated();
		return pscSearch.searchPSCsByCode(code);
	}


	private void assertConceptIndexUpdated() {
		Date lastIndexUpdate = pscSearch.getLastIndexUpdate();
		if (lastIndexUpdate == null) {
			logger.debug("Indexing concepts as a result of a model update");
			pscSearch.indexConcepts(pscDAO.getPSCTOs());
		} else {
			logger.debug("Index does not need updating");
		}
	}
	
	public PSCTO describe(PSCTO pscTO){
		return this.pscDAO.describe(pscTO);			
	}
	public List<PSCTO> getPSCTOs(){
		return this.pscDAO.getPSCTOs();
	}
	
	public List<PSCTO> getNarrowersOf(PSCTO pscTO) {
		return this.pscDAO.getNarrowersOf(pscTO);
	}
	public List<PSCTO> getNarrowersTransitiveOf(PSCTO pscTO) {
		return this.pscDAO.getNarrowersTransitiveOf(pscTO);
	}
	public NUTSTO describe(NUTSTO nutsTO){
		return this.nutsDAO.describe(nutsTO);
	}
	public List<NUTSTO> getNUTSTOs(){
		return this.nutsDAO.getNUTSTOs();
	}
}
