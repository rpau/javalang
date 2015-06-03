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

import org.walkmod.javalang.ast.SymbolDefinition;
import org.walkmod.javalang.ast.SymbolReference;
import org.walkmod.javalang.ast.body.TypeDeclaration;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class TypeDeclarationStmt extends Statement implements
		SymbolDefinition {

	private TypeDeclaration typeDecl;

	public TypeDeclarationStmt() {
	}

	public TypeDeclarationStmt(TypeDeclaration typeDecl) {
		setTypeDeclaration(typeDecl);
	}

	public TypeDeclarationStmt(int beginLine, int beginColumn, int endLine,
			int endColumn, TypeDeclaration typeDecl) {
		super(beginLine, beginColumn, endLine, endColumn);
		setTypeDeclaration(typeDecl);
	}

	@Override
	public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	@Override
	public <A> void accept(VoidVisitor<A> v, A arg) {
		v.visit(this, arg);
	}

	public TypeDeclaration getTypeDeclaration() {
		return typeDecl;
	}

	public void setTypeDeclaration(TypeDeclaration typeDecl) {
		this.typeDecl = typeDecl;
		setAsParentNodeOf(typeDecl);
	}

	@Override
	public List<SymbolReference> getUsages() {
		return typeDecl.getUsages();
	}

	@Override
	public void setUsages(List<SymbolReference> usages) {
		typeDecl.setUsages(usages);
	}

	@Override
	public boolean addUsage(SymbolReference usage) {
		return typeDecl.addUsage(usage);
	}

	@Override
	public List<SymbolReference> getBodyReferences() {
		return typeDecl.getBodyReferences();
	}

	@Override
	public void setBodyReferences(List<SymbolReference> bodyReferences) {
		typeDecl.setBodyReferences(bodyReferences);
	}

	@Override
	public boolean addBodyReference(SymbolReference bodyReference) {
		return typeDecl.addBodyReference(bodyReference);
	}

	@Override
	public int getScopeLevel() {
		return typeDecl.getScopeLevel();
	}

	@Override
	public void setScopeLevel(int scopeLevel) {
		typeDecl.setScopeLevel(scopeLevel);
	}
}
