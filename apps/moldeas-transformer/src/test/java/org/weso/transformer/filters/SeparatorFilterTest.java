package org.weso.transformer.filters;

import java.io.StringReader;

import org.apache.lucene.queryParser.ParseException;
import org.apache.solr.analysis.WordDelimiterFilterFactory;
import org.junit.Test;
import org.weso.moldeas.transformer.pscs.CPVReconciliator;

import com.hp.hpl.jena.sparql.util.StringUtils;


public class SeparatorFilterTest {

	@Test
	public void testStrangeChars() throws ParseException{
		String q = "Reservoirs, tanks, vats and similar containers (other than for compressed or liquefied gas), of iron, steel or aluminium, of a capacity > 300 litres, not fitted with mechanical or thermal equipment)";
		System.out.println(q.replaceAll("\\W", " ").replaceAll("\\d", ""));
				
	}
}
