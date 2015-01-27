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

import java.util.List;
import org.walkmod.javalang.ast.expr.AnnotationExpr;
import org.walkmod.javalang.ast.type.ClassOrInterfaceType;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class EnumDeclaration extends TypeDeclaration {

    private List<ClassOrInterfaceType> implementsList;

    private List<EnumConstantDeclaration> entries;

    public EnumDeclaration() {
    }

    public EnumDeclaration(int modifiers, String name) {
        super(modifiers, name);
    }

    public EnumDeclaration(JavadocComment javaDoc, int modifiers, List<AnnotationExpr> annotations, String name, List<ClassOrInterfaceType> implementsList, List<EnumConstantDeclaration> entries, List<BodyDeclaration> members) {
        super(annotations, javaDoc, modifiers, name, members);
        this.implementsList = implementsList;
        this.entries = entries;
    }

    public EnumDeclaration(int beginLine, int beginColumn, int endLine, int endColumn, JavadocComment javaDoc, int modifiers, List<AnnotationExpr> annotations, String name, List<ClassOrInterfaceType> implementsList, List<EnumConstantDeclaration> entries, List<BodyDeclaration> members) {
        super(beginLine, beginColumn, endLine, endColumn, annotations, javaDoc, modifiers, name, members);
        this.implementsList = implementsList;
        this.entries = entries;
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    public List<EnumConstantDeclaration> getEntries() {
        return entries;
    }

    public List<ClassOrInterfaceType> getImplements() {
        return implementsList;
    }

    public void setEntries(List<EnumConstantDeclaration> entries) {
        this.entries = entries;
    }

    public void setImplements(List<ClassOrInterfaceType> implementsList) {
        this.implementsList = implementsList;
    }
}
