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
package org.walkmod.lang.ast.stmt;

import java.util.List;

import org.walkmod.lang.ast.Node;
import org.walkmod.lang.ast.body.MultiTypeParameter;
import org.walkmod.lang.ast.body.VariableDeclaratorId;
import org.walkmod.lang.ast.expr.AnnotationExpr;
import org.walkmod.lang.ast.type.Type;
import org.walkmod.visitors.GenericVisitor;
import org.walkmod.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class CatchClause extends Node {

	private MultiTypeParameter except;

	private BlockStmt catchBlock;

	public CatchClause() {
	}

	public CatchClause(final MultiTypeParameter except,
			final BlockStmt catchBlock) {
		setExcept(except);
		setCatchBlock(catchBlock);
	}

	public CatchClause(int exceptModifier,
			List<AnnotationExpr> exceptAnnotations, List<Type> exceptTypes,
			VariableDeclaratorId exceptId, BlockStmt catchBlock) {
		this(new MultiTypeParameter(exceptModifier, exceptAnnotations,
				exceptTypes, exceptId), catchBlock);
	}

	public CatchClause(final int beginLine, final int beginColumn,
			final int endLine, final int endColumn, final int exceptModifier,
			final List<AnnotationExpr> exceptAnnotations,
			final List<Type> exceptTypes, final VariableDeclaratorId exceptId,
			final BlockStmt catchBlock) {
		super(beginLine, beginColumn, endLine, endColumn);
		setExcept(new MultiTypeParameter(beginLine, beginColumn, endLine,
				endColumn, exceptModifier, exceptAnnotations, exceptTypes,
				exceptId));
		setCatchBlock(catchBlock);
	}

	@Override
	public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
		return v.visit(this, arg);
	}

	@Override
	public <A> void accept(final VoidVisitor<A> v, final A arg) {
		v.visit(this, arg);
	}

	public BlockStmt getCatchBlock() {
		return catchBlock;
	}

	public MultiTypeParameter getExcept() {
		return except;
	}

	public void setCatchBlock(final BlockStmt catchBlock) {
		this.catchBlock = catchBlock;

	}

	public void setExcept(final MultiTypeParameter except) {
		this.except = except;

	}
}
