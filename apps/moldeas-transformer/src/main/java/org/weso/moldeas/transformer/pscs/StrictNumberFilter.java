package org.weso.moldeas.transformer.pscs;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

public class StrictNumberFilter extends TokenFilter {

		private static Pattern pattern = Pattern.compile(".*[123456789].*");
		
		public StrictNumberFilter(TokenStream stream) {
			super(stream);
		}
		/* (non-Javadoc)
		 * @see org.apache.lucene.analysis.TokenStream#next()
		 */
		public Token next() throws IOException {
			Matcher matcher;
			Token t = input.next();
			while ( true && t != null ) {
				matcher = pattern.matcher(t.termText());
				if ( !matcher.matches() ){
					return t;
				}
				t = input.next();
			}
			return null;
		}

		
}
