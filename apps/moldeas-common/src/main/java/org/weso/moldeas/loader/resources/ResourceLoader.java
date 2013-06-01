package org.weso.moldeas.loader.resources;

import org.w3c.dom.Document;
import org.weso.moldeas.exceptions.ResourceNotFoundException;
import org.weso.moldeas.to.KnowledgeResourcesTO;


/**
 * This interface indicates the set of operations to be implemented
 * for a loader of differente kind of sources (Files, Local files, String, etc.).
 */
public interface ResourceLoader {
    
    public KnowledgeResourcesTO [] getKnowledgeResources() throws ResourceNotFoundException;
    public Document getKnowledgeResourceAsDocument(String filename) throws ResourceNotFoundException;
    
}
