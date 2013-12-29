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

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.walkmod.lang.comparators.MethodDeclarationComparator;
import org.walkmod.lang.ast.TypeParameter;
import org.walkmod.lang.ast.expr.AnnotationExpr;
import org.walkmod.lang.ast.expr.NameExpr;
import org.walkmod.lang.ast.stmt.BlockStmt;
import org.walkmod.lang.ast.type.Type;
import org.walkmod.merger.MergeEngine;
import org.walkmod.merger.Mergeable;
import org.walkmod.visitors.GenericVisitor;
import org.walkmod.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class MethodDeclaration extends BodyDeclaration implements
		Mergeable<MethodDeclaration> {

	private int modifiers;

	private List<TypeParameter> typeParameters;

	private Type type;

	private String name;

	private List<Parameter> parameters;

	private int arrayCount;

	private List<NameExpr> throws_;

	private BlockStmt body;

	public MethodDeclaration() {
	}

	public MethodDeclaration(int modifiers, Type type, String name) {
		this.modifiers = modifiers;
		this.type = type;
		this.name = name;
	}

	public MethodDeclaration(int modifiers, Type type, String name,
			List<Parameter> parameters) {
		this.modifiers = modifiers;
		this.type = type;
		this.name = name;
		this.parameters = parameters;
	}

	public MethodDeclaration(JavadocComment javaDoc, int modifiers,
			List<AnnotationExpr> annotations,
			List<TypeParameter> typeParameters, Type type, String name,
			List<Parameter> parameters, int arrayCount, List<NameExpr> throws_,
			BlockStmt block) {
		super(annotations, javaDoc);
		this.modifiers = modifiers;
		this.typeParameters = typeParameters;
		this.type = type;
		this.name = name;
		this.parameters = parameters;
		this.arrayCount = arrayCount;
		this.throws_ = throws_;
		this.body = block;
	}

	public MethodDeclaration(int beginLine, int beginColumn, int endLine,
			int endColumn, JavadocComment javaDoc, int modifiers,
			List<AnnotationExpr> annotations,
			List<TypeParameter> typeParameters, Type type, String name,
			List<Parameter> parameters, int arrayCount, List<NameExpr> throws_,
			BlockStmt block) {
		super(beginLine, beginColumn, endLine, endColumn, annotations, javaDoc);
		this.modifiers = modifiers;
		this.typeParameters = typeParameters;
		this.type = type;
		this.name = name;
		this.parameters = parameters;
		this.arrayCount = arrayCount;
		this.throws_ = throws_;
		this.body = block;
	}

	@Override
	public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	@Override
	public <A> void accept(VoidVisitor<A> v, A arg) {
		v.visit(this, arg);
	}

	public int getArrayCount() {
		return arrayCount;
	}

	public BlockStmt getBody() {
		return body;
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

	public List<Parameter> getParameters() {
		return parameters;
	}

	public List<NameExpr> getThrows() {
		return throws_;
	}

	public Type getType() {
		return type;
	}

	public List<TypeParameter> getTypeParameters() {
		return typeParameters;
	}

	public void setArrayCount(int arrayCount) {
		this.arrayCount = arrayCount;
	}

	public void setBody(BlockStmt body) {
		this.body = body;
	}

	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public void setThrows(List<NameExpr> throws_) {
		this.throws_ = throws_;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setTypeParameters(List<TypeParameter> typeParameters) {
		this.typeParameters = typeParameters;
	}

	@Override
	public Comparator<?> getIdentityComparator() {

		return new MethodDeclarationComparator();
	}

	@Override
	public void merge(MethodDeclaration remote, MergeEngine configuration) {
		super.merge(remote, configuration);

		setBody((BlockStmt) configuration.apply(getBody(), remote.getBody(),
				BlockStmt.class));

		List<Parameter> resultParams = new LinkedList<Parameter>();
		configuration.apply(getParameters(), remote.getParameters(),
				resultParams, Parameter.class);
		if (!resultParams.isEmpty()) {
			setParameters(resultParams);
		}
		else{
			setParameters(null);
		}

		List<TypeParameter> resultTypeParams = new LinkedList<TypeParameter>();
		configuration.apply(getTypeParameters(), remote.getTypeParameters(),
				resultTypeParams, TypeParameter.class);
		if (!resultTypeParams.isEmpty()) {
			setTypeParameters(resultTypeParams);
		}
		else{
			setTypeParameters(null);
		}

		List<NameExpr> resultThrows = new LinkedList<NameExpr>();
		configuration.apply(getThrows(), remote.getThrows(), resultThrows,
				NameExpr.class);
		if (!resultThrows.isEmpty()) {
			setThrows(resultThrows);
		}
		else{
			setThrows(null);
		}

	}
}
