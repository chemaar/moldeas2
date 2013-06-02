package org.weso.moldeas.utils;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.weso.moldeas.loader.JenaRDFModelWrapper;
import org.weso.moldeas.loader.resources.FilesResourceLoader;
import org.weso.moldeas.loader.resources.ResourceLoader;

import com.hp.hpl.jena.rdf.model.Model;

public class SyntaxConverter {

	public static void main(String []args) throws FileNotFoundException, IOException{
		ResourceLoader loaderRdf = new FilesResourceLoader(new String[]{"nuts/nuts-2008-full-decorated.rdf"});
		JenaRDFModelWrapper rdfModel = new JenaRDFModelWrapper(loaderRdf);
		Model model = (Model) rdfModel.getModel();
		PrettyPrinter.serializeModel(model, 
				PrefixManager.getResourceBundle(), 
				"nuts-2008-full-decorated.ttl",
				TransformerConstants.TURTLE_SYNTAX);
	}
}
