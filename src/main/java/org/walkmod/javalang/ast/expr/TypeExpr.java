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
package org.walkmod.javalang.ast.expr;

import java.util.LinkedList;
import java.util.List;

import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.type.Type;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * This class is just instantiated as scopes for MethodReferenceExpr nodes to encapsulate Types.
 * 
 * @author Raquel Pau
 *
 */
public class TypeExpr extends Expression {

    private Type type;

    public TypeExpr() {}

    public TypeExpr(Type type) {
        setType(type);
    }

    public TypeExpr(int beginLine, int beginColumn, int endLine, int endColumn, Type type) {
        super(beginLine, beginColumn, endLine, endColumn);
        setType(type);
    }

    @Override
    public boolean removeChild(Node child) {
        boolean result = false;

        if (child != null) {
            if (child == type) {
                type = null;
                result = true;
            }
        }
        if (result) {
            updateReferences(child);
        }
        return result;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> children = new LinkedList<Node>();
        if (type != null) {
            children.add(type);
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        if (this.type != null) {
            updateReferences(this.type);
        }
        this.type = type;
        setAsParentNodeOf(type);
    }

    @Override
    public boolean replaceChildNode(Node oldChild, Node newChild) {
        boolean updated = false;
        if (oldChild == type) {
            setType((Type) newChild);
            updated = true;
        }
        return updated;
    }

    @Override
    public TypeExpr clone() throws CloneNotSupportedException {
        return new TypeExpr(clone(type));
    }

}
