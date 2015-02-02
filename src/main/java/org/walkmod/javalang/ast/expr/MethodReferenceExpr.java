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

import java.util.List;

import org.walkmod.javalang.ast.TypeParameter;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * Method reference expressions introduced in Java 8 specifically designed to
 * simplify lambda Expressions. These are some examples:
 * 
 * System.out::println;
 * 
 * (test ? stream.map(String::trim) : stream)::toArray;
 * 
 * @author Raquel Pau
 *
 */
public class MethodReferenceExpr extends Expression {

	private Expression scope;

	private List<TypeParameter> typeParameters;

	private String identifier;

	public MethodReferenceExpr() {
	}

	public MethodReferenceExpr(int beginLine, int beginColumn, int endLine,
			int endColumn, Expression scope,
			List<TypeParameter> typeParameters, String identifier) {

		super(beginLine, beginColumn, endLine, endColumn);
		this.scope = scope;
		this.typeParameters = typeParameters;
		this.identifier = identifier;
	}

	@Override
	public <R, A> R accept(GenericVisitor<R, A> v, A arg) {

		return v.visit(this, arg);
	}

	@Override
	public <A> void accept(VoidVisitor<A> v, A arg) {
		v.visit(this, arg);
	}

	public Expression getScope() {
		return scope;
	}

	public void setScope(Expression scope) {
		this.scope = scope;
	}

	public List<TypeParameter> getTypeParameters() {
		return typeParameters;
	}

	public void setTypeParameters(List<TypeParameter> typeParameters) {
		this.typeParameters = typeParameters;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

}
