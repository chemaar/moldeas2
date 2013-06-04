/**
 * 
 *   (c) Copyright 2011 WESO, Computer Science Department,
 *   Facultad de Ciencias, University of Oviedo, Oviedo, Asturias, Spain, 33007
 *   All rights reserved.
 *  
 *   Redistribution and use in source and binary forms, with or without
 *   modification, are permitted provided that the following conditions
 *   are met:
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *   3. The name of the author may not be used to endorse or promote products
 *      derived from this software without specific prior written permission.
 *  
 *   THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 *   IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *   OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 *   IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 *   INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 *   NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *   DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *   THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 *   THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 */
package org.weso.moldeas.psc;

import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.weso.moldeas.dao.PSCDAO;
import org.weso.moldeas.dao.impl.CPV2008DAOImpl;
import org.weso.moldeas.dao.impl.CPVCacheDAOImpl;
import org.weso.moldeas.dao.impl.CPVDataSource;
import org.weso.moldeas.dao.impl.CPVDataSourceLocalEndpoint;
import org.weso.moldeas.exceptions.EmptySyntacticQueryException;
import org.weso.moldeas.exceptions.InvalidQueryException;
import org.weso.moldeas.index.lucene.LuceneDAOFactory;
import org.weso.moldeas.loader.JenaRDFModelWrapper;
import org.weso.moldeas.loader.resources.FilesResourceLoader;
import org.weso.moldeas.loader.resources.ResourceLoader;
import org.weso.moldeas.to.PSCSearchResultTO;
import org.weso.moldeas.to.ScoredPSCTO;

public class PSCFacadeTest {

	@Test
	public void testSearchPSC() throws EmptySyntacticQueryException, InvalidQueryException {
		String query = "Servicios de transporte de paquetes";
		//Skip the use of spring
		PSCFacade facade = createPSCFacade();
		PSCSearchResultTO results = facade.searchPSC(query);
		Set<ScoredPSCTO> scoredResults = results.getScoredscTOs();
		Assert.assertEquals(3, scoredResults.size());
	}

	public static PSCFacade createPSCFacade() {
		ResourceLoader loader = new FilesResourceLoader(new String[]{"cpv/cpv-2008.ttl"});
		JenaRDFModelWrapper rdfModel = new JenaRDFModelWrapper(loader,"TURTLE");
		CPVDataSource dataSource = new CPVDataSourceLocalEndpoint(rdfModel);
		PSCDAO pscDAO = new CPV2008DAOImpl(dataSource);
		LuceneDAOFactory luceneDAOFactory = new LuceneDAOFactory();
		PSCSearch pscSearch = new PSCSearch(luceneDAOFactory);
		return new PSCFacade(pscSearch,pscDAO);
	}

	public static PSCFacade createCachePSCFacade() {
		PSCDAO pscDAO = new CPVCacheDAOImpl();
		LuceneDAOFactory luceneDAOFactory = new LuceneDAOFactory();
		PSCSearch pscSearch = new PSCSearch(luceneDAOFactory);
		return new PSCFacade(pscSearch,pscDAO);
	}
	
	@Test
	public void testSearchPSCByCode() throws EmptySyntacticQueryException, InvalidQueryException {
		//FIXME: test cases related the index is not removed from RAM memory. Maybe due to singleton
		String code = "34513100";
		PSCFacade facade = createPSCFacade();
		PSCSearchResultTO results = facade.searchPSCByCode(code);
		Set<ScoredPSCTO> scoredResults = results.getScoredscTOs();
		Assert.assertEquals(1, scoredResults.size());
	}
}
