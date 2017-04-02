/*
 * Copyright (C) 2013 Raquel Pau and Albert Coroleu.
 * 
 * Walkmod is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * Walkmod is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Walkmod. If
 * not, see <http://www.gnu.org/licenses/>.
 */
package org.walkmod.javalang.ast.body;

import java.util.LinkedList;
import java.util.List;

import org.walkmod.javalang.ast.Node;
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

    public EnumDeclaration() {}

    public EnumDeclaration(int modifiers, String name) {
        super(modifiers, name);
    }

    public EnumDeclaration(JavadocComment javaDoc, int modifiers, List<AnnotationExpr> annotations, String name,
            List<ClassOrInterfaceType> implementsList, List<EnumConstantDeclaration> entries,
            List<BodyDeclaration> members) {
        this(0, 0, 0, 0, javaDoc, modifiers, annotations, name, implementsList, entries, members);

    }

    public EnumDeclaration(int beginLine, int beginColumn, int endLine, int endColumn, JavadocComment javaDoc,
            int modifiers, List<AnnotationExpr> annotations, String name, List<ClassOrInterfaceType> implementsList,
            List<EnumConstantDeclaration> entries, List<BodyDeclaration> members) {
        super(beginLine, beginColumn, endLine, endColumn, annotations, javaDoc, modifiers, name, members);
        setImplements(implementsList);
        setEntries(entries);
    }

    @Override
    public boolean removeChild(Node child) {
        boolean result = false;
        if (child != null) {
            result = super.removeChild(child);
            if (!result) {
                if (child instanceof ClassOrInterfaceType) {
                    if (implementsList != null) {
                        List<ClassOrInterfaceType> auxImplementsList =
                                new LinkedList<ClassOrInterfaceType>(implementsList);
                        result = auxImplementsList.remove(child);
                        implementsList = auxImplementsList;
                    }
                } else if (child instanceof EnumConstantDeclaration) {
                    if (entries != null) {
                        List<EnumConstantDeclaration> entriesAux = new LinkedList<EnumConstantDeclaration>();
                        result = entriesAux.remove(child);
                        entries = entriesAux;
                    }
                }
            }
        }
        if (result) {
            updateReferences(child);
        }
        return result;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> children = super.getChildren();
        if (implementsList != null) {
            children.addAll(implementsList);
        }
        if (entries != null) {
            children.addAll(entries);
        }
        return children;
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        if (!check()) {
            return null;
        }
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        if (check()) {
            v.visit(this, arg);
        }
    }

    public List<EnumConstantDeclaration> getEntries() {
        return entries;
    }

    public List<ClassOrInterfaceType> getImplements() {
        return implementsList;
    }

    public void setEntries(List<EnumConstantDeclaration> entries) {
        this.entries = entries;
        setAsParentNodeOf(entries);
    }

    public void setImplements(List<ClassOrInterfaceType> implementsList) {
        this.implementsList = implementsList;
        setAsParentNodeOf(implementsList);
    }

    @Override
    public boolean replaceChildNode(Node oldChild, Node newChild) {
        boolean update = super.replaceChildNode(oldChild, newChild);

        if (!update && implementsList != null) {
            List<ClassOrInterfaceType> auxImplementsList = new LinkedList<ClassOrInterfaceType>(implementsList);
            update = replaceChildNodeInList(oldChild, newChild, auxImplementsList);
            if (update) {
                implementsList = auxImplementsList;
            }

        }
        if (!update && entries != null) {
            List<EnumConstantDeclaration> auxEntries = new LinkedList<EnumConstantDeclaration>(entries);
            update = replaceChildNodeInList(oldChild, newChild, auxEntries);
            if (update) {
                entries = auxEntries;
            }
        }
        return update;
    }

    @Override
    public EnumDeclaration clone() throws CloneNotSupportedException {
        return new EnumDeclaration(clone(getJavaDoc()), getModifiers(), clone(getAnnotations()), getName(),
                clone(getImplements()), clone(getEntries()), clone(getMembers()));
    }

}
