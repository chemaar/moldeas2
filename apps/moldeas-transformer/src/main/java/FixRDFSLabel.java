import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import org.weso.moldeas.loader.JenaRDFModelWrapper;
import org.weso.moldeas.loader.resources.ExternalizeFilesResourceLoader;
import org.weso.moldeas.loader.resources.FilesResourceLoader;
import org.weso.moldeas.loader.resources.ResourceLoader;
import org.weso.moldeas.utils.TransformerConstants;
import org.weso.pscs.utils.PSCConstants;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDFS;


@SuppressWarnings("unused")
public class FixRDFSLabel {

	
	public static void main(String []args) throws FileNotFoundException{
		String outputFilename = "cpa-2008.ttl";
		String inputFilename = "full-generation-2/cpa/2008/cpa-2008.ttl";
		ResourceLoader loader = new ExternalizeFilesResourceLoader(new String[]{inputFilename,});
		JenaRDFModelWrapper rdfModel = new JenaRDFModelWrapper(loader,TransformerConstants.TURTLE_SYNTAX);
		Model model = (Model) rdfModel.getModel();		
		ResIterator it = model.listResourcesWithProperty(model.getProperty(PSCConstants.SKOS_prefLabel));
		while(it.hasNext()){
			Resource r = it.next();
			List<String> prefLabels = new LinkedList<String>();
			StmtIterator iter = model.listStatements(
				    new SimpleSelector(r, model.getProperty(PSCConstants.SKOS_prefLabel), (RDFNode) null) {
				        public boolean selects(Statement s)
				            {return s.getLiteral().getLanguage().equalsIgnoreCase("en");}
				    });	
			while(iter.hasNext()){
				prefLabels.add(iter.next().getString());
			}
			for(String label:prefLabels){
				Literal prefLabel = model.createLiteral(label,"en");
				r.addLiteral(RDFS.label,prefLabel);
			}	
		}
		model.write(new PrintWriter(outputFilename),TransformerConstants.TURTLE_SYNTAX);
	}
}
