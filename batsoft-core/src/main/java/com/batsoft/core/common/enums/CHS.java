package com.batsoft.core.common.enums;

import java.math.BigInteger;

/**
 * 符号枚举
 * 
 * @author Administrator
 */
public enum CHS {

	/**
	 * \
	 */
	right_slash("\\"),

	/**
	 * ／
	 */
	left_slash("/"),

	/**
	 * ]
	 */
	right_bracket("]"),

	/**
	 * [
	 */
	left_bracket("["),

	/**
	 * }
	 */
	right_brace("}"),

	/**
	 * {
	 */
	left_brace("{"),

	/**
	 * !
	 */
	exclamation("!"),

	/**
	 * "@"
	 */
	arobase("@"),

	/**
	 * #
	 */
	no_("#"),

	/**
	 * ￥
	 * 
	 */
	RMB("￥"),

	/**
	 * $
	 */
	dollar("$"),

	/**
	 * %
	 */
	percent("%"),

	/**
	 * ^
	 */
	top_arrows("^"),

	/**
	 * \&
	 */
	and("&"),

	/**
	 * *
	 */
	asterisk("*"),

	/**
	 * (
	 */
	left_small_bracket("("),

	/**
	 * )
	 */
	right_small_bracket(")"),

	/**
	 * |
	 */
	vertical_line("|"),

	/**
	 * =
	 */
	equal("="),

	/**
	 * +
	 */
	plus("+"),

	/**
	 * -
	 */
	subtract("-"),

	/**
	 * _
	 */
	underline("_"),

	/**
	 * `
	 */
	connector("`"),

	/**
	 * ?
	 */
	question("?"),

	/**
	 * .
	 */
	dit("."),

	/**
	 * ,
	 */
	english_comma(","),
	
	/**
	 * ;
	 */
	semicolon(";"),

	/**
	 * ，
	 */
	chinese_comma("，"),

	/**
	 * :
	 */
	colon(":"),

	/**
	 * '
	 */
	single_quotes("'"),

	/**
	 * "
	 */
	quotation_marks("\""),

	/**
	 * A
	 * 
	 */
	A("A"),

	/**
	 * B
	 * 
	 */
	B("B"),

	/**
	 * C
	 * 
	 */
	C("C"),

	/**
	 * D
	 * 
	 */
	D("D"),

	/**
	 * E
	 * 
	 */
	E("E"),

	/**
	 * F
	 * 
	 */
	F("F"),

	/**
	 * G
	 * 
	 */
	G("G"),

	/**
	 * H
	 * 
	 */
	H("H"),

	/**
	 * I
	 * 
	 */
	I("I"),

	/**
	 * J
	 * 
	 */
	J("J"),

	/**
	 * K
	 * 
	 */
	K("K"),

	/**
	 * L
	 * 
	 */
	L("L"),

	/**
	 * M
	 * 
	 */
	M("M"),

	/**
	 * N
	 * 
	 */
	N("N"),

	/**
	 * O
	 * 
	 */
	O("O"),

	/**
	 * P
	 * 
	 */
	P("P"),

	/**
	 * Q
	 * 
	 */
	Q("Q"),

	/**
	 * S
	 * 
	 */
	S("S"),

	/**
	 * T
	 * 
	 */
	T("T"),

	/**
	 * U
	 * 
	 */
	U("U"),

	/**
	 * V
	 * 
	 */
	V("V"),

	/**
	 * W
	 * 
	 */
	W("W"),

	/**
	 * X
	 * 
	 */
	X("X"),

	/**
	 * Y
	 * 
	 */
	Y("Y"),
	/**
	 * Z
	 * 
	 */
	Z("Z");

	private String value;

	private CHS(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public synchronized static String listCh(CHS... values) {
		StringBuilder ch = new StringBuilder();
		if (values != null && values.length > BigInteger.ZERO.intValue()) {
			for (CHS value : values) {
				ch.append(value.getValue());
			}
		}
		return ch.toString();
	}

}
