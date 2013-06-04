package org.weso.moldeas.utils;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;

public class TransformerConstants {

	public static final String TURTLE_SYNTAX = "TURTLE";
	public static final String N3_SYNTAX = "N3";
	public static final String HTTP_WWW_PRODUCTONTOLOGY_ORG_ID = "http://www.productontology.org/id/";
	public static final String PREF_LABEL_FIELD = "prefLabel";
	public static final String URI_FIELD = "uri";
	public static final String HTTP_WWW_W3_ORG_2003_01_GEO_WGS84_POS = 
			PrefixManager.getURIPrefix("geo");
	public static final String HTTP_WWW_W3_ORG_2003_01_GEO_WGS84_POS_LONG = HTTP_WWW_W3_ORG_2003_01_GEO_WGS84_POS+"long";
	public static final String HTTP_WWW_W3_ORG_2003_01_GEO_WGS84_POS_LAT = HTTP_WWW_W3_ORG_2003_01_GEO_WGS84_POS+"lat";
	public static final String SPATIALRELATIONS_PREFIX = "spatialrelations";
	public static final String NUTS_PREFIX = "moldeas-nuts";
	public static final String GEO_PREFIX = "geo";
	public static final String HTTP_NUTS_PSI_ENAKTING_ORG_ID = "http://nuts.psi.enakting.org/id/";
	static final String MOLDEAS_PREFIX = "moldeas";
	
	public static final int INDEX_ID_CONTRACT = 0;
	public static final int INDEX_NUTS_CODE= 1;
	public static final int INDEX_TYPE= 6;
	public static final int INDEX_LABEL= 7;
	public static final int INDEX_FULL_ADDRESS= 10;
	public static final int INDEX_STREET= 11;
	public static final int INDEX_POSTAL_CODE= 12;
	public static final int INDEX_REGION= 13;
	public static final int INDEX_EMAIL= 17;
	public static final int INDEX_TELEPHONE= 18;
	public static final int INDEX_FAX= 19;
	public static final int INDEX_HOMEPAGE= 20;
	public static Literal literalLang(Model m, String value, String lang){
		return m.createLiteral(value, lang);
	}
	public static final String DEFAULT_LANGUAGE = "en";
	public static final String DEFAULT_PPN_LABEL = "Public Procurement Notice ";
	public static final String DEFAULT_PPN_COMMENT = "This is an announcement of a public contract. The original id is comprised of: identifier-year.";
	public static final String WESO_ORG_URI = PrefixManager.getURIPrefix("moldeas-org")+"WESO";
	
}
