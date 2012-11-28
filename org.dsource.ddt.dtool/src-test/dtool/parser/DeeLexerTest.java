package dtool.parser;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.junit.Test;

import dtool.tests.CommonTestUtils;

public class DeeLexerTest extends CommonTestUtils {
	
	@Test
	public void basicLexerTest() throws Exception { basicLexerTest$(); }
	public void basicLexerTest$() throws Exception {
		testLexerTokenizing("  \t", array(DeeTokens.WHITESPACE));
		testLexerTokenizing("\n", array(DeeTokens.EOL));
		testLexerTokenizing("/*asd*/", array(DeeTokens.COMMENT_MULTI));
		testLexerTokenizing("/+as/+ sadf  +/ d+/", array(DeeTokens.COMMENT_NESTED));
		testLexerTokenizing("// asdfs", array(DeeTokens.COMMENT_LINE));
		
		testLexerTokenizing("`asdfsdaf`", array(DeeTokens.STRING_WYSIWYG));
		testLexerTokenizing("r\"asdfsdaf\"", array(DeeTokens.STRING_WYSIWYG));
		testLexerTokenizing("\"asdfsdaf\"d", array(DeeTokens.STRING_DQ));
		testLexerTokenizing("x\"A0 01 FF\"w", array(DeeTokens.STRING_HEX));
		
		testLexerTokenizing("q\"/foo(xxx)/\"", array(DeeTokens.STRING_DELIM));
		testLexerTokenizing("q\"(foo(xxx))\"", array(DeeTokens.STRING_DELIM));
		testLexerTokenizing("q\"foo\n(xxx)\nfoo\"", array(DeeTokens.STRING_DELIM));
		testLexerTokenizing("q{ asdf __TIME__  {nest \n braces} }", array(DeeTokens.STRING_TOKENS));
		
		
		testLexerTokenizing("(){}[]", array(DeeTokens.OPEN_PARENS, DeeTokens.CLOSE_PARENS, 
			DeeTokens.OPEN_BRACE, DeeTokens.CLOSE_BRACE, DeeTokens.OPEN_BRACKET, DeeTokens.CLOSE_BRACKET));

		testLexerTokenizing("'a'", array(DeeTokens.CHAR_LITERAL));
		
		testLexerTokenizing("123", array(DeeTokens.INTEGER));
		testLexerTokenizing("0b101", array(DeeTokens.INTEGER_BINARY));
		testLexerTokenizing("01234567", array(DeeTokens.INTEGER_OCTAL));
		testLexerTokenizing("0x0123456789ABDCEF", array(DeeTokens.INTEGER_HEX));
		
		testLexerTokenizing("1234567890.1234567890E123F", array(DeeTokens.FLOAT));
		testLexerTokenizing("0x0123456789ABDCEFP123f", array(DeeTokens.FLOAT_HEX));
		
		testLexerTokenizing("asdf", array(DeeTokens.IDENTIFIER));
		testLexerTokenizing("final", array(DeeTokens.KW_FINAL));
		testLexerTokenizing("finally", array(DeeTokens.KW_FINALLY));
		testLexerTokenizing("finallyx", array(DeeTokens.IDENTIFIER));
	}
	
	public static void testLexerTokenizing(String source, DeeTokens[] deeTokens) {
		DeeLexer deeLexer = new DeeLexer(source);
		int readSourceOffset = 0;
		
		StringBuilder constructedSource = new StringBuilder();
		for (int i = 0; i < deeTokens.length; i++) {
			DeeTokens expectedTokenCode = deeTokens[i];
			Token token = checkToken(deeLexer, expectedTokenCode, readSourceOffset);
			readSourceOffset = token.getEndPos();
			String sourceSoFar = source.substring(0, readSourceOffset);
			
			String tokenSourceValue = token.getSourceValue();
			// retest with just the token source to make sure boundaries are correct
			if(deeTokens.length != 1 && !tokenSourceValue.startsWith("#!")) {
				testLexerTokenizing(tokenSourceValue, array(expectedTokenCode));
			}
			
			constructedSource.append(tokenSourceValue);
			assertTrue(sourceSoFar.contentEquals(constructedSource));
		}
		assertTrue(deeLexer.tokenStartPos == source.length());
		checkToken(deeLexer, DeeTokens.EOF, readSourceOffset);
		assertEquals(source, constructedSource.toString());
	}
	
	public static Token checkToken(DeeLexer deeLexer, DeeTokens expectedTokenCode, int readOffset) {
		Token token = deeLexer.next();
		DeeTokens tokenCode = token.getTokenCode();
		if(expectedTokenCode != null) {
			assertTrue(tokenCode == expectedTokenCode);
		}
		assertTrue(token.getStartPos() == readOffset);
		assertEquals(deeLexer.source.subSequence(token.getStartPos(), token.getEndPos()), token.getSourceValue());
		if(tokenCode.getSourceValue() != null) {
			assertEquals(tokenCode.getSourceValue(), token.getSourceValue());
		}
		
		switch (tokenCode) {
		case EOF: assertTrue(token.getEndPos() >= token.getStartPos());
			break;
		default:
			assertTrue(token.getEndPos() > token.getStartPos());
		}
		return token;
	}
	
}