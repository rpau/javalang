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
import org.walkmod.lang.ast.expr.AnnotationExpr;
import org.walkmod.lang.ast.expr.Expression;
import org.walkmod.merger.MergeEngine;
import org.walkmod.merger.Mergeable;
import org.walkmod.visitors.GenericVisitor;
import org.walkmod.visitors.VoidVisitor;
import org.walkmod.lang.comparators.EnumConstantDeclarationComparator;

/**
 * @author Julio Vilmar Gesser
 */
public final class EnumConstantDeclaration extends BodyDeclaration implements Mergeable<EnumConstantDeclaration>{

	private String name;

	private List<Expression> args;

	private List<BodyDeclaration> classBody;

	public EnumConstantDeclaration() {
	}

	public EnumConstantDeclaration(String name) {
		this.name = name;
	}

	public EnumConstantDeclaration(JavadocComment javaDoc,
			List<AnnotationExpr> annotations, String name,
			List<Expression> args, List<BodyDeclaration> classBody) {
		super(annotations, javaDoc);
		this.name = name;
		this.args = args;
		this.classBody = classBody;
	}

	public EnumConstantDeclaration(int beginLine, int beginColumn, int endLine,
			int endColumn, JavadocComment javaDoc,
			List<AnnotationExpr> annotations, String name,
			List<Expression> args, List<BodyDeclaration> classBody) {
		super(beginLine, beginColumn, endLine, endColumn, annotations, javaDoc);
		this.name = name;
		this.args = args;
		this.classBody = classBody;
	}

	@Override
	public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	@Override
	public <A> void accept(VoidVisitor<A> v, A arg) {
		v.visit(this, arg);
	}

	public List<Expression> getArgs() {
		return args;
	}

	public List<BodyDeclaration> getClassBody() {
		return classBody;
	}

	public String getName() {
		return name;
	}

	public void setArgs(List<Expression> args) {
		this.args = args;
	}

	public void setClassBody(List<BodyDeclaration> classBody) {
		this.classBody = classBody;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Comparator<?> getIdentityComparator() {
		
		return new EnumConstantDeclarationComparator();
	}

	@Override
	public void merge(EnumConstantDeclaration remote, MergeEngine configuration) {
		super.merge(remote, configuration);
		
		List<BodyDeclaration> resultClassBody = new LinkedList<BodyDeclaration>();
		configuration.apply(getClassBody(), remote.getClassBody(), resultClassBody, BodyDeclaration.class);
		
		if(!resultClassBody.isEmpty()){
			setClassBody(resultClassBody);
		}
		else{
			setClassBody(null);
		}
		
		List<Expression> resultArgs = new LinkedList<Expression>();
		configuration.apply(getArgs(), remote.getArgs(), resultArgs, Expression.class);
		if(!resultArgs.isEmpty()){
			setArgs(resultArgs);
		}
		else{
			setArgs(null);
		}
		
	}
}
