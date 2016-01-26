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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.walkmod.javalang.ast.FieldSymbolData;
import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.SymbolDefinition;
import org.walkmod.javalang.ast.SymbolReference;
import org.walkmod.javalang.ast.expr.AnnotationExpr;
import org.walkmod.javalang.ast.type.Type;
import org.walkmod.javalang.comparators.FieldDeclarationComparator;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;
import org.walkmod.merger.MergeEngine;
import org.walkmod.merger.Mergeable;

/**
 * @author Julio Vilmar Gesser
 */
public final class FieldDeclaration extends BodyDeclaration implements
		Mergeable<FieldDeclaration>, SymbolDefinition {

	private int modifiers;

	private Type type;

	private List<VariableDeclarator> variables;

	private List<FieldSymbolData> symbolData;
	
	private int scopeLevel = 0;

	public FieldDeclaration() {
	}

	public FieldDeclaration(int modifiers, Type type,
			VariableDeclarator variable) {
		this.modifiers = modifiers;
		setType(type);
		this.variables = new ArrayList<VariableDeclarator>();
		setAsParentNodeOf(variable);
		this.variables.add(variable);
	}

	public FieldDeclaration(int modifiers, Type type,
			List<VariableDeclarator> variables) {
		this.modifiers = modifiers;
		setType(type);
		setVariables(variables);
	}

	public FieldDeclaration(JavadocComment javaDoc, int modifiers,
			List<AnnotationExpr> annotations, Type type,
			List<VariableDeclarator> variables) {
		super(annotations, javaDoc);
		this.modifiers = modifiers;
		setType(type);
		setVariables(variables);
	}

	public FieldDeclaration(int beginLine, int beginColumn, int endLine,
			int endColumn, JavadocComment javaDoc, int modifiers,
			List<AnnotationExpr> annotations, Type type,
			List<VariableDeclarator> variables) {
		super(beginLine, beginColumn, endLine, endColumn, annotations, javaDoc);
		this.modifiers = modifiers;
		setType(type);
		setVariables(variables);
	}

	@Override
	public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	@Override
	public <A> void accept(VoidVisitor<A> v, A arg) {
		v.visit(this, arg);
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

	public Type getType() {
		return type;
	}

	public List<VariableDeclarator> getVariables() {
		return variables;
	}

	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}

	public void setType(Type type) {
		this.type = type;
		setAsParentNodeOf(type);
	}

	public void setVariables(List<VariableDeclarator> variables) {
		this.variables = variables;
		setAsParentNodeOf(variables);
	}

	@Override
	public Comparator<?> getIdentityComparator() {
		return new FieldDeclarationComparator();
	}

	@Override
	public void merge(FieldDeclaration remote, MergeEngine configuration) {
		super.merge(remote, configuration);
		setType((Type) configuration.apply(getType(), remote.getType(),
				Type.class));
		List<VariableDeclarator> resultList = new LinkedList<VariableDeclarator>();
		configuration.apply(getVariables(), remote.getVariables(), resultList,
				VariableDeclarator.class);
		setVariables(resultList);
	}

	public List<FieldSymbolData> getFieldsSymbolData() {
		return symbolData;
	}

	public void setFieldsSymbolData(List<FieldSymbolData> symbolData) {
		this.symbolData = symbolData;
	}

	@Override
	public List<SymbolReference> getUsages() {
		List<SymbolReference> result = null;
		if (variables != null) {
			result = new LinkedList<SymbolReference>();
			for (VariableDeclarator vd : variables) {
				List<SymbolReference> usages = vd.getUsages();
				if (usages != null) {
					result.addAll(usages);
				}
			}
			if (result.isEmpty()) {
				result = null;
			}
		}

		return result;
	}

	@Override
	public void setUsages(List<SymbolReference> usages) {
	}

	@Override
	public List<SymbolReference> getBodyReferences() {
		List<SymbolReference> result = null;
		if (variables != null) {
			result = new LinkedList<SymbolReference>();
			for (VariableDeclarator vd : variables) {
				List<SymbolReference> bodyReferences = vd.getBodyReferences();
				if (bodyReferences != null) {
					result.addAll(bodyReferences);
				}
			}
			if (result.isEmpty()) {
				result = null;
			}
		}
		return result;
	}

	@Override
	public void setBodyReferences(List<SymbolReference> bodyReferences) {
	}

	@Override
	public int getScopeLevel() {
		return scopeLevel;
	}

	@Override
	public void setScopeLevel(int scopeLevel) {
		this.scopeLevel = scopeLevel;
	}

	@Override
	public boolean addBodyReference(SymbolReference bodyReference) {
		return false;
	}

	@Override
	public boolean addUsage(SymbolReference usage) {
		return false;
	}
	
	@Override
   public boolean replaceChildNode(Node oldChild, Node newChild) {
      boolean update = super.replaceChildNode(oldChild, newChild);
   
      if(!update){
         if(oldChild == type){
            type = (Type) newChild;
            update = true;
         }
         else{
            update = replaceChildNodeInList(oldChild, newChild, variables);
         }
      }
      return update;
	}
	
}
