package org.weso.moldeas.enhancers.psc;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.weso.moldeas.dao.PSCDAO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.ScoredPSCTO;

public class SolrNLPPSCCodesEnhancer extends SolrPSCCodesEnhancer{

	public SolrNLPPSCCodesEnhancer() throws IOException {
		super();
	}

	public SolrNLPPSCCodesEnhancer(PSCDAO dao) throws IOException{
		super(dao);
	}

	@Override
	protected void execute() throws Exception {
		String query = this.request.getStringQuery();
		IndexSearcher indexSearcher = new IndexSearcher(idx);		
		ScoreDoc[] scoreDocs = fetchSearchResults(
				createQueryFromString(cleanString(query)), indexSearcher, this.request.getMaxResults());	
		for(ScoreDoc scoredoc: scoreDocs){
			Document doc = indexSearcher.doc(scoredoc.doc);
			PSCTO code = new PSCTO(doc.getField(SolrPSCCodesEnhancer.URI_INDEX_FIELD).stringValue());	
			code.setPrefLabel(doc.getField(LABEL_INDEX_FIELD).stringValue());
			String uri = code.getUri(); //FIXME: hack
			code.setId(uri.substring(uri.lastIndexOf("/")+1,uri.length()));
			ScoredPSCTO scoredPSCTO = new ScoredPSCTO();
			scoredPSCTO.setScore(scoredoc.score);	
			scoredPSCTO.setPscTO(code);
			this.enhancedRequest.getScoredPSCCodes().add(scoredPSCTO );

		}
	}

}



