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
package org.walkmod.javalang.ast.stmt;

import java.util.List;

import org.walkmod.javalang.ast.expr.VariableDeclarationExpr;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class TryStmt extends Statement {

	private List<VariableDeclarationExpr> resources;

	private BlockStmt tryBlock;

	private List<CatchClause> catchs;

	private BlockStmt finallyBlock;

	public TryStmt() {
	}

	public TryStmt(final BlockStmt tryBlock, final List<CatchClause> catchs,
			final BlockStmt finallyBlock) {
		setTryBlock(tryBlock);
		setCatchs(catchs);
		setFinallyBlock(finallyBlock);
	}

	public TryStmt(final int beginLine, final int beginColumn,
			final int endLine, final int endColumn,
			List<VariableDeclarationExpr> resources, final BlockStmt tryBlock,
			final List<CatchClause> catchs, final BlockStmt finallyBlock) {
		super(beginLine, beginColumn, endLine, endColumn);
		this.resources = resources;
		setTryBlock(tryBlock);
		setCatchs(catchs);
		setFinallyBlock(finallyBlock);
	}

	@Override
	public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
		return v.visit(this, arg);
	}

	@Override
	public <A> void accept(final VoidVisitor<A> v, final A arg) {
		v.visit(this, arg);
	}

	public List<CatchClause> getCatchs() {
		return catchs;
	}

	public BlockStmt getFinallyBlock() {
		return finallyBlock;
	}

	public BlockStmt getTryBlock() {
		return tryBlock;
	}

	public List<VariableDeclarationExpr> getResources() {
		return resources;
	}

	public void setCatchs(final List<CatchClause> catchs) {
		this.catchs = catchs;

	}

	public void setFinallyBlock(final BlockStmt finallyBlock) {
		this.finallyBlock = finallyBlock;

	}

	public void setTryBlock(final BlockStmt tryBlock) {
		this.tryBlock = tryBlock;

	}

	public void setResources(List<VariableDeclarationExpr> resources) {
		this.resources = resources;
	}
}
