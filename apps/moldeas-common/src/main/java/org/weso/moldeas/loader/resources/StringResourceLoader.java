package org.weso.moldeas.loader.resources;

import java.io.ByteArrayInputStream;
import java.util.logging.Level;

import org.weso.moldeas.exceptions.DocumentBuilderException;
import org.weso.moldeas.exceptions.ResourceNotFoundException;

import java.io.InputStream;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.weso.moldeas.to.KnowledgeResourcesTO;
import org.weso.moldeas.utils.DocumentBuilderHelper;

/**
 *
 * This class implements the interface ResourceLoader loading the data
 * from a String.
 *
 */
public class StringResourceLoader  implements ResourceLoader {
    
    private static final Logger logger = Logger.getLogger(StringResourceLoader.class);

    private String content;
    
    public StringResourceLoader(String content) {
       this.content = content;
    }

    public Document getKnowledgeResourceAsDocument(String filename) throws ResourceNotFoundException {
        try {
            return DocumentBuilderHelper.getDocumentFromString(content);
        } catch (DocumentBuilderException ex) {
            java.util.logging.Logger.getLogger(StringResourceLoader.class.getName()).log(Level.SEVERE, null, ex);
            throw new ResourceNotFoundException("Can not parse content.");
        }
    }
    

  
    public KnowledgeResourcesTO[] getKnowledgeResources() {
        KnowledgeResourcesTO knowledgeResourcesTO = new KnowledgeResourcesTO();
        InputStream knowledgeSourceData = new ByteArrayInputStream(content.getBytes());
        knowledgeResourcesTO.setKnowledgeSourceData(knowledgeSourceData);
        return  new KnowledgeResourcesTO[]{knowledgeResourcesTO};
    }





}
