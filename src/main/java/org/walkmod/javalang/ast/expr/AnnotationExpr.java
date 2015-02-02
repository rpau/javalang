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

import java.util.Comparator;

import org.walkmod.javalang.comparators.AnnotationExprComparator;
import org.walkmod.merger.Mergeable;

/**
 * @author Julio Vilmar Gesser
 */
public abstract class AnnotationExpr extends Expression implements
		Mergeable<AnnotationExpr> {

	protected NameExpr name;

	public AnnotationExpr() {
	}

	public AnnotationExpr(int beginLine, int beginColumn, int endLine,
			int endColumn) {
		super(beginLine, beginColumn, endLine, endColumn);
	}

	public NameExpr getName() {
		return name;
	}

	public void setName(NameExpr name) {
		this.name = name;
	}

	@Override
	public Comparator<?> getIdentityComparator() {
		return new AnnotationExprComparator();
	}
}
