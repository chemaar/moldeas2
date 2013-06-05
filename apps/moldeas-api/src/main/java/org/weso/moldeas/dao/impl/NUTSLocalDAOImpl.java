package org.weso.moldeas.dao.impl;

import java.util.LinkedList;
import java.util.List;

import org.weso.moldeas.dao.NUTSDAO;
import org.weso.moldeas.exceptions.MoldeasModelException;
import org.weso.moldeas.loader.JenaRDFModelWrapper;
import org.weso.moldeas.loader.MoldeasModelWrapper;
import org.weso.moldeas.loader.resources.FilesResourceLoader;
import org.weso.moldeas.loader.resources.ResourceLoader;
import org.weso.moldeas.to.NUTSTO;
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

public class NUTSLocalDAOImpl implements NUTSDAO{
	Model model;
	String lang =  "en";
	public NUTSLocalDAOImpl(){
		ResourceLoader loader = new FilesResourceLoader(new String[]{"nuts/nuts-2008.ttl"});
		JenaRDFModelWrapper rdfModel = new JenaRDFModelWrapper(loader,"TURTLE");
		this.model = (Model) rdfModel.getModel();		
	}

	public NUTSLocalDAOImpl(MoldeasModelWrapper wrapper){		
		this.model = (Model) wrapper.getModel();		
	}
	
	public NUTSLocalDAOImpl(MoldeasModelWrapper wrapper, String lang){		
		this.model = (Model) wrapper.getModel();	
		this.lang = lang;
	}

	@Override
	public NUTSTO describe(NUTSTO nutsTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NUTSTO> getNUTSTOs() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
