package com.nekplus.xml.query;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class QueryLexer {

	//@formatter:off
	/*
	 * Num   := [-+]?(\d+)?\.?(\d+)?([eE][-+]?\d+)?
	 * Elem  := [a-zA-Z_][-a-zA-Z0-9_:.]*
	 * Str   := ('.*'|".*")
	 * Attr  := @[a-zA-Z_][-a-zA-Z0-9_:.]*
	 * Slash := '/'
	 * Op    := [><!]=?|==
	 * Lbr   := '['
	 * Rbr   := ']'
	 * Lpar  := '('
	 * Rpar  := ')'
	 */
	//@formatter:on

	private static final Pattern NUM_REGEX = Pattern.compile("^[-+]?(\\d+)?\\.?(\\d+)?([eE][-+]?\\d+)?");
	private static final Pattern IDENTIFIER_REGEX = Pattern.compile("^[a-zA-Z_][-a-zA-Z0-9_:.]*");
	private static final Pattern ATTR_REGEX = Pattern.compile("^@[a-zA-Z_][-a-zA-Z0-9_:.]*");
	private static final Pattern STR_REGEX = Pattern.compile("^('.*'|\".*\")");
	private static final Pattern OP_REGEX = Pattern.compile("^[><!]=?|==");

	public enum TokenType {
		Num, Elem, Str, Attr, Slash, Op, Lbr, Rbr, Lpar, Rpar, EOF, Unknown
	}

	public static class Token {
		TokenType type;
		CharSequence text;

		public static final Token SLASH = new Token(TokenType.Slash, "/");
		public static final Token LBR = new Token(TokenType.Lbr, "[");
		public static final Token RBR = new Token(TokenType.Rbr, "]");
		public static final Token LPAR = new Token(TokenType.Lpar, "(");
		public static final Token RPAR = new Token(TokenType.Rpar, ")");
		public static final Token EOF = new Token(TokenType.EOF, "\0");

		Token(TokenType type, CharSequence text) {
			this.type = type;
			this.text = text;
		}
	}

	private char[] expr;
	private int pos = 0;
	private int mark = 0;
	private List<Token> tokens = new ArrayList<QueryLexer.Token>();

	public QueryLexer(String expression) {
		expr = toCStyleCharArray(expression.trim());
		makeTokens();
		pos = 0;
	}

	public Token nextToken() {
		return tokens.get(pos++);
	}

	public Token peakToken() {
		return tokens.get(pos);
	}

	public void back() {
		pos--;
	}

	private void makeTokens() {
		TokenType tokenType;
		while ((tokenType = lex()) != TokenType.EOF) {
			switch (tokenType) {
				case Attr:
					tokens.add(new Token(TokenType.Attr, text()));
					break;
				case EOF:
					tokens.add(Token.EOF);
					break;
				case Elem:
					tokens.add(new Token(TokenType.Elem, text()));
					break;
				case Lbr:
					tokens.add(Token.LBR);
					break;
				case Lpar:
					tokens.add(Token.LPAR);
					break;
				case Num:
					tokens.add(new Token(TokenType.Num, text()));
					break;
				case Op:
					tokens.add(new Token(TokenType.Op, text()));
					break;
				case Rbr:
					tokens.add(Token.RBR);
					break;
				case Rpar:
					tokens.add(Token.RPAR);
					break;
				case Slash:
					tokens.add(Token.SLASH);
					break;
				case Str:
					tokens.add(new Token(TokenType.Str, text()));
					break;
				case Unknown:
					throw new QuerySyntaxException("Syntax Error near " + nearExpr());
			}
		}
		tokens.add(Token.EOF);
	}

	private TokenType lex() {
		skipSpace();
		char ch = read();
		switch (ch) {
			case '[':
				return TokenType.Lbr;
			case ']':
				return TokenType.Rbr;
			case '(':
				return TokenType.Lpar;
			case ')':
				return TokenType.Rpar;
			case '/':
				return TokenType.Slash;
			case '@':
				unread();
				return nextPattern(ATTR_REGEX, TokenType.Attr);
			case '\0':
				unread();
				return TokenType.EOF;
			case '=':
			case '>':
			case '<':
			case '!':
				unread();
				return nextPattern(OP_REGEX, TokenType.Op);
			case '\'':
			case '"':
				unread();
				return nextPattern(STR_REGEX, TokenType.Str);
		}
		unread();
		if (ch == '_' || between(ch, 'A', 'Z') || between(ch, 'a', 'z')) {
			return nextPattern(IDENTIFIER_REGEX, TokenType.Elem);
		}
		return nextPattern(NUM_REGEX, TokenType.Num);
	}

	private String text() {
		return new String(expr, mark, pos - mark);
	}

	private String nearExpr() {
		int begin = pos - 4;
		int end = pos + 4;
		if (begin < 0) begin = 0;
		if (end > expr.length) end = expr.length;
		StringBuilder sb = new StringBuilder(end - begin + 1);
		sb.append(expr, begin, pos);
		sb.append('^');
		sb.append(expr, pos, end);
		return sb.toString();
	}

	private TokenType nextPattern(Pattern pattern, TokenType token) {
		Matcher matcher = pattern.matcher(new CharSegment(expr, pos));
		if (matcher.lookingAt()) {
			mark = pos;
			pos += matcher.end();
			return token;
		}
		return TokenType.Unknown;
	}

	private char read() {
		return expr[pos++];
	}

	private char peak() {
		return expr[pos];
	}

	private void unread() {
		pos--;
	}

	private void skipSpace() {
		while (isSpace(peak()))
			pos++;
	}

	private boolean isSpace(char ch) {
		switch (ch) {
			case ' ':
			case '\t':
			case '\f':
			case 0x0B:
			case '\r':
			case '\n':
				return true;
			default:
				return false;
		}
	}

	private char[] toCStyleCharArray(String str) {
		return (str + '\0').toCharArray();
	}

	private boolean between(char ch, char first, char last) {
		return ch >= first && ch <= last;
	}
}
