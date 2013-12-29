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
package org.walkmod.lang.ast.body;

import java.util.List;
import org.walkmod.lang.ast.Node;
import org.walkmod.lang.ast.expr.AnnotationExpr;

public abstract class BaseParameter extends Node {

	private static final long serialVersionUID = -920000439540828718L;

	private int modifiers;

	private List<AnnotationExpr> annotations;

	private VariableDeclaratorId id;

	public BaseParameter() {
	}

	public BaseParameter(VariableDeclaratorId id) {
		setId(id);
	}

	public BaseParameter(int modifiers, VariableDeclaratorId id) {
		setModifiers(modifiers);
		setId(id);
	}

	public BaseParameter(int modifiers, List<AnnotationExpr> annotations,
			VariableDeclaratorId id) {
		setModifiers(modifiers);
		setAnnotations(annotations);
		setId(id);
	}

	public BaseParameter(int beginLine, int beginColumn, int endLine,
			int endColumn, int modifiers, List<AnnotationExpr> annotations,
			VariableDeclaratorId id) {
		super(beginLine, beginColumn, endLine, endColumn);
		setModifiers(modifiers);
		setAnnotations(annotations);
		setId(id);
	}

	public List<AnnotationExpr> getAnnotations() {
		return annotations;
	}

	public VariableDeclaratorId getId() {
		return id;
	}

	/**
	 * Return the modifiers of this parameter declaration.
	 * 
	 * @see ModifierSet
	 * @return modifiers
	 */
	public int getModifiers() {
		return modifiers;
	}

	public void setAnnotations(List<AnnotationExpr> annotations) {
		this.annotations = annotations;
	}

	public void setId(VariableDeclaratorId id) {
		this.id = id;
	}

	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}
}
