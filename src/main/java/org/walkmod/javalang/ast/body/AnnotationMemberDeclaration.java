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
package org.walkmod.javalang.ast.body;

import java.util.Comparator;
import java.util.List;

import org.walkmod.javalang.ast.expr.AnnotationExpr;
import org.walkmod.javalang.ast.expr.Expression;
import org.walkmod.javalang.ast.type.Type;
import org.walkmod.javalang.comparators.AnnotationMemberDeclarationComparator;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;
import org.walkmod.merger.MergeEngine;
import org.walkmod.merger.Mergeable;

/**
 * @author Julio Vilmar Gesser
 */
public final class AnnotationMemberDeclaration extends BodyDeclaration implements Mergeable<AnnotationMemberDeclaration>{

	private int modifiers;

	private Type type;

	private String name;

	private Expression defaultValue;

	public AnnotationMemberDeclaration() {
	}

	public AnnotationMemberDeclaration(int modifiers, Type type, String name,
			Expression defaultValue) {
		this.modifiers = modifiers;
		this.type = type;
		this.name = name;
		this.defaultValue = defaultValue;
	}

	public AnnotationMemberDeclaration(JavadocComment javaDoc, int modifiers,
			List<AnnotationExpr> annotations, Type type, String name,
			Expression defaultValue) {
		super(annotations, javaDoc);
		this.modifiers = modifiers;
		this.type = type;
		this.name = name;
		this.defaultValue = defaultValue;
	}

	public AnnotationMemberDeclaration(int beginLine, int beginColumn,
			int endLine, int endColumn, JavadocComment javaDoc, int modifiers,
			List<AnnotationExpr> annotations, Type type, String name,
			Expression defaultValue) {
		super(beginLine, beginColumn, endLine, endColumn, annotations, javaDoc);
		this.modifiers = modifiers;
		this.type = type;
		this.name = name;
		this.defaultValue = defaultValue;
	}

	@Override
	public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	@Override
	public <A> void accept(VoidVisitor<A> v, A arg) {
		v.visit(this, arg);
	}

	public Expression getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Return the modifiers of this member declaration.
	 * 
	 * @see ModifierSet
	 * @return modifiers
	 */
	public int getModifiers() {
		return modifiers;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	public void setDefaultValue(Expression defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public Comparator<?> getIdentityComparator() {
		return new AnnotationMemberDeclarationComparator();
	}

	@Override
	public void merge(AnnotationMemberDeclaration remote, MergeEngine configuration) {
		super.merge(remote, configuration);
		setType((Type)configuration.apply(getType(), remote.getType(), Type.class));
		setDefaultValue((Expression)configuration.apply(getDefaultValue(), remote.getDefaultValue(), Expression.class));
		
	}
}
