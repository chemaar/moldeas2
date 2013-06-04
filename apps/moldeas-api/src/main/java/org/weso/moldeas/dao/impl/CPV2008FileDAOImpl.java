package org.weso.moldeas.dao.impl;

import java.util.LinkedList;
import java.util.List;

import org.weso.moldeas.dao.PSCDAO;
import org.weso.moldeas.exceptions.MoldeasModelException;
import org.weso.moldeas.loader.JenaRDFModelWrapper;
import org.weso.moldeas.loader.MoldeasModelWrapper;
import org.weso.moldeas.loader.resources.FilesResourceLoader;
import org.weso.moldeas.loader.resources.ResourceLoader;
import org.weso.moldeas.to.PSCTO;
import org.weso.pscs.utils.PSCConstants;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;

public class CPV2008FileDAOImpl implements PSCDAO {
	
	Model model;
	String lang =  "en";
	public CPV2008FileDAOImpl(){
		ResourceLoader loader = new FilesResourceLoader(new String[]{"cpv/cpv-2008.ttl"});
		JenaRDFModelWrapper rdfModel = new JenaRDFModelWrapper(loader,"TURTLE");
		this.model = (Model) rdfModel.getModel();		
	}

	public CPV2008FileDAOImpl(MoldeasModelWrapper wrapper){		
		this.model = (Model) wrapper.getModel();		
	}
	
	public CPV2008FileDAOImpl(MoldeasModelWrapper wrapper, String lang){		
		this.model = (Model) wrapper.getModel();	
		this.lang = lang;
	}
	
	
	@Override
	public PSCTO describe(PSCTO pscTO) {
		throw new MoldeasModelException("Not yet implemented");
	}

	@Override
	public List<PSCTO> getPSCTOs() {
		ResIterator it = model.listResourcesWithProperty(model.getProperty(PSCConstants.SKOS_IN_SCHEME));
		List<PSCTO> pscTOs = new LinkedList<PSCTO>();
		while(it.hasNext()){
			Resource r = it.next();
			PSCTO pscTO = new PSCTO();
			pscTO.setUri(r.getURI());
			pscTO.setId(r.getProperty(DC.identifier).getString());
			pscTO.setSubject(r.getProperty(DC.subject).getString());
			pscTO.setInScheme(model.getProperty(PSCConstants.SKOS_IN_SCHEME).getURI());
			//pscTO.setType(r.getProperty(RDF.type).getResource().getURI());
			StmtIterator iter = model.listStatements(
				    new SimpleSelector(r, model.getProperty(PSCConstants.SKOS_prefLabel), (RDFNode) null) {
				        public boolean selects(Statement s)
				            {return s.getLiteral().getLanguage().equalsIgnoreCase(lang);}
				    });	
			while(iter.hasNext()){
				pscTO.setPrefLabel(iter.next().getString());
			}
			pscTOs.add(pscTO);
		}
		return pscTOs;
	}

	@Override
	public List<PSCTO> getNarrowersOf(PSCTO pscTO) {
		throw new MoldeasModelException("Not yet implemented");
		
	}

	@Override
	public List<PSCTO> getNarrowersTransitiveOf(PSCTO pscTO) {
		throw new MoldeasModelException("Not yet implemented");		
	}


	
}
