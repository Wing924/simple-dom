package com.nekplus.xml.query;

import com.nekplus.xml.XML;
import com.nekplus.xml.filters.AndFilter;
import com.nekplus.xml.filters.CompareFilter;
import com.nekplus.xml.filters.XMLFilter;
import com.nekplus.xml.query.QueryLexer.Token;
import com.nekplus.xml.query.QueryLexer.TokenType;

public class QueryParser {

	//@formatter:off
	/*
	 * EXPR   := TAGS EOF
	 * TAGS   := TAG [ Slash TAGS ] | Attr 
	 * TAG    := Elem [ FILTER ] 
	 * FILTER := INDEX | COND [ INDEX ]
	 * INDEX  := Lbr Num Rbr
	 * COND   := Lpar AND Rpar
	 * AND    := TERM { and TERM }
	 * TERM   := (Attr | Elem) Op ( Str | Num )
	 */
	//@formatter:on

	private QueryLexer lexer;
	private XML node;

	public XML eval(XML root, String expression) {
		lexer = new QueryLexer(expression);
		node = root;
		test(parseExpr());
		return node;
	}

	// EXPR := TAGS EOF
	private boolean parseExpr() {
		test(parseTags());
		test(predict(TokenType.EOF));
		return true;
	}

	private String getAttrName() {
		Token token = lexer.nextToken();
		if (token.type != TokenType.Attr) {
			lexer.back();
			return null;
		}
		return token.text.subSequence(1, token.text.length()).toString();
	}

	// TAGS := TAG [ Slash TAGS ] | Attr
	private boolean parseTags() {
		if (parseTag()) {
			if (predict(TokenType.Slash)) test(parseTags());
			return true;
		}
		String attr = getAttrName();
		if (attr == null) return false;
		node = node.getAttribute(attr);
		return true;
	}

	// TAG := Elem [ FILTER ]
	private boolean parseTag() {
		Token token = lexer.nextToken();
		if (token.type != TokenType.Elem) {
			lexer.back();
			return false;
		}
		node = node.getElement(token.text.toString());
		parseFilter();
		return true;
	}

	// FILTER := INDEX | COND [ INDEX ]
	private boolean parseFilter() {
		if (parseIndex()) return true;
		if (!parseCond()) return false;
		parseIndex();
		return true;
	}

	// INDEX := Lbr Num Rbr
	private boolean parseIndex() {
		if (lexer.nextToken().type != TokenType.Lbr) {
			lexer.back();
			return false;
		}
		Token token = lexer.nextToken();
		test(TokenType.Num, token);
		test(TokenType.Rbr, lexer.nextToken());
		int idx = Integer.parseInt(token.text.toString());
		node = node.get(idx);
		return true;
	}

	// COND := Lpar AND Rpar
	private boolean parseCond() {
		if (!predict(TokenType.Lpar)) return false;
		parseAnd();
		test(TokenType.Rpar, lexer.nextToken());
		return true;
	}

	private void parseAnd() {
		XMLFilter filter1 = parseTerm();
		for (;;) {
			if (!predict(TokenType.And)) break;
			XMLFilter filter2 = parseTerm();
			filter1 = new AndFilter(filter1, filter2);
		}
		node = node.filter(filter1);
	}

	private XMLFilter parseTerm() {
		// Attr | Elem
		Token token = lexer.nextToken();
		test(token.type == TokenType.Attr || token.type == TokenType.Elem);
		String name = token.text.toString();
		// Op
		token = lexer.nextToken();
		test(TokenType.Op, token);
		String op = token.text.toString();
		// Str | Num
		token = lexer.nextToken();
		test(token.type == TokenType.Str || token.type == TokenType.Num);
		XMLFilter filter;
		if (token.type == TokenType.Str) {
			// remove " or '
			String str = token.text.subSequence(1, token.text.length() - 1).toString();
			str = str.replaceAll("\\'", "'").replaceAll("\\\"", "\"");
			filter = new CompareFilter(name, op, str);
		} else {
			String num = token.text.toString();
			if (num.indexOf('.') >= 0) {// double
				filter = new CompareFilter(name, op, Double.parseDouble(num));
			} else {
				filter = new CompareFilter(name, op, Long.parseLong(num));
			}
		}
		return filter;
	}

	private void test(TokenType expect, Token actual) {
		if (expect != actual.type) {
			throw new QuerySyntaxException(
					String.format("Expect token: %s but actual: %s",
							expect.name(),
							actual.type.name()));
		}
	}

	private void test(boolean cond) {
		if (!cond) {
			throw new QuerySyntaxException();
		}
	}

	private boolean predict(TokenType type) {
		if (lexer.nextToken().type == type) return true;
		lexer.back();
		return false;
	}
}
