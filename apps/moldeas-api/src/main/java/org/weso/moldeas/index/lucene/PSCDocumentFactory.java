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

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.ScoredPSCTO;

public class PSCDocumentFactory {

	public static Document[] buildDocument(PSCTO pscTO) {
		Document[] indexMe = new Document[1];//FIXME: one for each prefLabel
		Field uriField = new Field(PSCIndexFields.PSC_FIELDS.URI.toString(), 
				pscTO.getUri(), 
				Field.Store.YES, 
				Field.Index.NOT_ANALYZED);
		Field idField = new Field(PSCIndexFields.PSC_FIELDS.ID.toString(), 
				pscTO.getId(), 
				Field.Store.YES, 
				Field.Index.NOT_ANALYZED);
        String []prefLabels = new String[]{pscTO.getPrefLabel()};//FIXME: one for each prefLabel
        Field [] prefLabelfields = new Field[prefLabels.length];
        for(int i = 0; i< prefLabels.length;i++ ){
            Field descriptionField = new Field(
            	   PSCIndexFields.PSC_FIELDS.PREF_LABEL.toString(), 
                   prefLabels[i], 
                   Field.Store.YES, 
                   Field.Index.ANALYZED);
            prefLabelfields[i] = descriptionField;
        }		
	
		for (int i = 0; i < prefLabels.length; i++) {	
			Document doc = new Document();
			doc.add(uriField);
		    doc.add(prefLabelfields[i]);
		    doc.add(idField);
			indexMe[i] = doc;
		}
		return indexMe; 
	}

	public static ScoredPSCTO buildPSCTO(Document doc, float score) {
		String uri = doc.get(PSCIndexFields.PSC_FIELDS.URI.toString());
		String prefLabel = doc.get(PSCIndexFields.PSC_FIELDS.PREF_LABEL.toString());
		String id = doc.get(PSCIndexFields.PSC_FIELDS.ID.toString());
		PSCTO result = new PSCTO();
		result.setUri(uri);
		result.setPrefLabel(prefLabel);
		result.setId(id);
		return new ScoredPSCTO(result, score);		
	}

}
