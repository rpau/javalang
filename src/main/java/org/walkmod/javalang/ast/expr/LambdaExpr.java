package org.walkmod.javalang.ast.expr;

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

import java.util.List;

import org.walkmod.javalang.ast.body.Parameter;
import org.walkmod.javalang.ast.stmt.Statement;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * Lambda expressions.
 * 
 * @author Raquel Pau
 *
 */
public class LambdaExpr extends Expression {

	private List<Parameter> parameters;

	private boolean parametersEnclosed;

	private Statement body;

	public LambdaExpr() {
	}

	public LambdaExpr(int beginLine, int beginColumn, int endLine,
			int endColumn, List<Parameter> parameters, Statement body,
			boolean parametersEnclosed) {

		super(beginLine, beginColumn, endLine, endColumn);
		this.parameters = parameters;
		this.body = body;

		if (this.parameters != null && this.parameters.size() == 1
				&& this.parameters.get(0).getType() == null) {
			this.parametersEnclosed = parametersEnclosed;
		} else {
			this.parametersEnclosed = true;
		}
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public Statement getBody() {
		return body;
	}

	public void setBody(Statement body) {
		this.body = body;
	}

	@Override
	public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	@Override
	public <A> void accept(VoidVisitor<A> v, A arg) {
		v.visit(this, arg);
	}

	public boolean isParametersEnclosed() {
		return parametersEnclosed;
	}

	public void setParametersEnclosed(boolean parametersEnclosed) {
		this.parametersEnclosed = parametersEnclosed;
	}

}
