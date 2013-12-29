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

import java.util.LinkedList;
import java.util.List;

import org.walkmod.javalang.ast.TypeParameter;
import org.walkmod.javalang.ast.expr.AnnotationExpr;
import org.walkmod.javalang.ast.type.ClassOrInterfaceType;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;
import org.walkmod.merger.MergeEngine;

/**
 * @author Julio Vilmar Gesser
 */
public final class ClassOrInterfaceDeclaration extends TypeDeclaration {

	private boolean interface_;

	private List<TypeParameter> typeParameters;

	private List<ClassOrInterfaceType> extendsList;

	private List<ClassOrInterfaceType> implementsList;

	public ClassOrInterfaceDeclaration() {
	}

	public ClassOrInterfaceDeclaration(int modifiers, boolean isInterface,
			String name) {
		super(modifiers, name);
		this.interface_ = isInterface;
	}

	public ClassOrInterfaceDeclaration(JavadocComment javaDoc, int modifiers,
			List<AnnotationExpr> annotations, boolean isInterface, String name,
			List<TypeParameter> typeParameters,
			List<ClassOrInterfaceType> extendsList,
			List<ClassOrInterfaceType> implementsList,
			List<BodyDeclaration> members) {
		super(annotations, javaDoc, modifiers, name, members);
		this.interface_ = isInterface;
		this.typeParameters = typeParameters;
		this.extendsList = extendsList;
		this.implementsList = implementsList;
	}

	public ClassOrInterfaceDeclaration(int beginLine, int beginColumn,
			int endLine, int endColumn, JavadocComment javaDoc, int modifiers,
			List<AnnotationExpr> annotations, boolean isInterface, String name,
			List<TypeParameter> typeParameters,
			List<ClassOrInterfaceType> extendsList,
			List<ClassOrInterfaceType> implementsList,
			List<BodyDeclaration> members) {
		super(beginLine, beginColumn, endLine, endColumn, annotations, javaDoc,
				modifiers, name, members);
		this.interface_ = isInterface;
		this.typeParameters = typeParameters;
		this.extendsList = extendsList;
		this.implementsList = implementsList;
	}

	@Override
	public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	@Override
	public <A> void accept(VoidVisitor<A> v, A arg) {
		v.visit(this, arg);
	}

	public List<ClassOrInterfaceType> getExtends() {
		return extendsList;
	}

	public List<ClassOrInterfaceType> getImplements() {
		return implementsList;
	}

	public List<TypeParameter> getTypeParameters() {
		return typeParameters;
	}

	public boolean isInterface() {
		return interface_;
	}

	public void setExtends(List<ClassOrInterfaceType> extendsList) {
		this.extendsList = extendsList;
	}

	public void setImplements(List<ClassOrInterfaceType> implementsList) {
		this.implementsList = implementsList;
	}

	public void setInterface(boolean interface_) {
		this.interface_ = interface_;
	}

	public void setTypeParameters(List<TypeParameter> typeParameters) {
		this.typeParameters = typeParameters;
	}

	@Override
	public void merge(TypeDeclaration remoteTypeDeclaration,
			MergeEngine configuration) {
		super.merge(remoteTypeDeclaration, configuration);
		List<TypeParameter> typeParametersList = new LinkedList<TypeParameter>();
		configuration.apply(getTypeParameters(),
				((ClassOrInterfaceDeclaration) remoteTypeDeclaration)
						.getTypeParameters(), typeParametersList,
				TypeParameter.class);
		if (!typeParametersList.isEmpty()) {
			setTypeParameters(typeParametersList);
		} else {
			setTypeParameters(null);
		}
		List<ClassOrInterfaceType> implementsList = new LinkedList<ClassOrInterfaceType>();
		configuration.apply(getImplements(),
				((ClassOrInterfaceDeclaration) remoteTypeDeclaration)
						.getImplements(), implementsList,
				ClassOrInterfaceType.class);
		if (!implementsList.isEmpty()) {
			setImplements(implementsList);
		} else {
			setImplements(null);
		}
		List<ClassOrInterfaceType> extendsList = new LinkedList<ClassOrInterfaceType>();
		configuration.apply(getExtends(),
				((ClassOrInterfaceDeclaration) remoteTypeDeclaration)
						.getExtends(), extendsList, ClassOrInterfaceType.class);
		if (!extendsList.isEmpty()) {
			setExtends(extendsList);
		} else {
			setExtends(null);
		}
	}
}
