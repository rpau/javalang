/* 
  Copyright (C) 2013 Raquel Pau and Albert Coroleu.
 
 Walkmod is free software: you can redistribute it and/or modify
 it under the terms of the GNU Lesser General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.
 
 Walkmod is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Lesser General Public License for more details.
 
 You should have received a copy of the GNU Lesser General Public License
 along with Walkmod.  If not, see <http://www.gnu.org/licenses/>.*/

package org.walkmod.javalang.ast.expr;

import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public class StringLiteralExpr extends LiteralExpr {

	protected String value;

	public StringLiteralExpr() {
	}

	public StringLiteralExpr(String value) {
		this.value = escape(value);
	}

	public StringLiteralExpr(int beginLine, int beginColumn, int endLine,
			int endColumn, String value) {
		super(beginLine, beginColumn, endLine, endColumn);
		this.value = escape(value);
	}

	@Override
	public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	@Override
	public <A> void accept(VoidVisitor<A> v, A arg) {
		v.visit(this, arg);
	}

	public final String getValue() {
		return value;
	}

	public final void setValue(String value) {
		this.value = escape(value);
	}

	private static String escape(String str) {
		if (str == null) {
			return null;
		}
		int sz;
		sz = str.length();
		StringBuffer out = new StringBuffer();
		for (int i = 0; i < sz; i++) {
			char ch = str.charAt(i);
			// handle unicode

			if (ch > 0xfff) {
				out.append("\\u" + hex(ch));
			} else if (ch > 0xff) {
				out.append("\\u0" + hex(ch));
			} else if (ch > 0x7f) {
				out.append("\\u00" + hex(ch));
			} else if (ch < 32) {
				switch (ch) {
				case '\b':
				case '\n':
				case '\t':
				case '\f':
				case '\r':
					out.append(ch);
					break;
				default:
					if (ch > 0xf) {
						out.append("\\u00" + hex(ch));
					} else {
						out.append("\\u000" + hex(ch));
					}
					break;
				}
			} else {
				out.append(ch);
			}
		}
		return out.toString();
	}

	private static String hex(char ch) {
		return Integer.toHexString(ch).toUpperCase();
	}
}
