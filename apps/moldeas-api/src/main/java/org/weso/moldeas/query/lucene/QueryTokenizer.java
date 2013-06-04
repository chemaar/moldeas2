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
package org.weso.moldeas.query.lucene;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;


/**
 * @author ivan
 *
 * Tokenize input using whitespaces considerating quotes. Dash is not take as separator.  
 *  " Thats "a quoted" query " => [Thats] [a quoted] [query]
 *  " Dirty-dash to disturb" => [Dirty-dash] [to] [disturb]
 * 
 * Token type can be "quoted" or "word" 
 *  
 */
public class QueryTokenizer extends Tokenizer{

	public enum TokenTypes { WORD, QUOTED}
	
	private boolean inQuotes = false;
	private boolean isQuotedToken = false;
	/**
	 * @param input Reader with info to tokenize
	 */
	public QueryTokenizer(Reader input) {
		super(input);
	}
	
	protected boolean isTokenChar(char readed) {
		
		if ( Character.isWhitespace(readed) && !inQuotes ) return false;
		
		if ( Character.toString(readed).equals("\"") ) {
			if ( inQuotes ) {
				inQuotes = false;
			}
			else { 
				inQuotes = true;
				isQuotedToken = true;
			}
			return false;
		}
		return true;
	}
	

	private int offset = 0, bufferIndex = 0, dataLen = 0;
	private static final int MAX_WORD_LEN = 255;
	private static final int IO_BUFFER_SIZE = 1024;
	private final char[] buffer = new char[MAX_WORD_LEN];
	private final char[] ioBuffer = new char[IO_BUFFER_SIZE];
	
	/** Called on each token character to normalize it before it is added to the
	 * token.  The default implementation does nothing.  Subclasses may use this
	 * to, e.g., lowercase tokens. */
	protected char normalize(char c) {
		return c;
	}
	
	/** Returns the next token in the stream, or null at EOS. */
	public final Token next() throws java.io.IOException {
		int length = 0;
		int start = offset;
		while (true) {
			final char c;
			
			offset++;
			if (bufferIndex >= dataLen) {
				dataLen = input.read(ioBuffer);
				bufferIndex = 0;
			}
			;
			if (dataLen == -1) {
				if (length > 0)
					break;
				else
					return null;
			} else
				c = ioBuffer[bufferIndex++];
			
			if (isTokenChar(c)) {               // if it's a token char
				
				if (length == 0)			           // start of token
					start = offset - 1;
				
				buffer[length++] = normalize(c); // buffer it, normalized
				
				if (length == MAX_WORD_LEN)		   // buffer overflow!
					break;
				
			} else if (length > 0)             // at non-Letter w/ chars
				break;                           // return 'em
			
		}
		if ( isQuotedToken ) {
			isQuotedToken = false;
			return new Token(new String(buffer, 0, length), start, start + length, TokenTypes.QUOTED.toString().toLowerCase()); 
		}
		
		return new Token(new String(buffer, 0, length), start, start + length);
	}

	@Override
	public boolean incrementToken() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}
}
