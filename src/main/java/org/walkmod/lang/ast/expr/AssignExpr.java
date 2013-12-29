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
package org.walkmod.lang.ast.expr;

import org.walkmod.visitors.GenericVisitor;
import org.walkmod.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class AssignExpr extends Expression {

	public static enum Operator {

		assign, plus, minus, star, slash, and, or, xor, rem, lShift, rSignedShift, rUnsignedShift
	}

	private Expression target;

	private Expression value;

	private Operator op;

	public AssignExpr() {
	}

	public AssignExpr(Expression target, Expression value, Operator op) {
		this.target = target;
		this.value = value;
		this.op = op;
	}

	public AssignExpr(int beginLine, int beginColumn, int endLine,
			int endColumn, Expression target, Expression value, Operator op) {
		super(beginLine, beginColumn, endLine, endColumn);
		this.target = target;
		this.value = value;
		this.op = op;
	}

	@Override
	public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	@Override
	public <A> void accept(VoidVisitor<A> v, A arg) {
		v.visit(this, arg);
	}

	public Operator getOperator() {
		return op;
	}

	public Expression getTarget() {
		return target;
	}

	public Expression getValue() {
		return value;
	}

	public void setOperator(Operator op) {
		this.op = op;
	}

	public void setTarget(Expression target) {
		this.target = target;
	}

	public void setValue(Expression value) {
		this.value = value;
	}
}
