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
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class MemberValuePair extends Node {

    private String name;

    private Expression value;

    public MemberValuePair() {}

    public MemberValuePair(String name, Expression value) {
        this.name = name;
        setValue(value);
    }

    public MemberValuePair(int beginLine, int beginColumn, int endLine, int endColumn, String name, Expression value) {
        super(beginLine, beginColumn, endLine, endColumn);
        this.name = name;
        setValue(value);
    }

    @Override
    public boolean removeChild(Node child) {
        boolean result = false;

        if (child != null) {
            if (value == child) {
                value = null;
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
        if (value != null) {
            children.add(value);
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

    public String getName() {
        return name;
    }

    public Expression getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Expression value) {
        if (this.value != null) {
            updateReferences(this.value);
        }
        this.value = value;
        setAsParentNodeOf(value);
    }

    @Override
    public boolean replaceChildNode(Node oldChild, Node newChild) {
        boolean updated = false;

        if (oldChild == value) {
            setValue((Expression) newChild);
            updated = true;
        }

        return updated;
    }

    @Override
    public MemberValuePair clone() throws CloneNotSupportedException {
        return new MemberValuePair(new String(name), clone(value));
    }
}
