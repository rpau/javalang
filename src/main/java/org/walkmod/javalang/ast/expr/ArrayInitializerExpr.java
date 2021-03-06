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
public final class ArrayInitializerExpr extends Expression {

    private List<Expression> values;

    public ArrayInitializerExpr() {}

    public ArrayInitializerExpr(List<Expression> values) {
        this.values = values;
    }

    public ArrayInitializerExpr(int beginLine, int beginColumn, int endLine, int endColumn, List<Expression> values) {
        super(beginLine, beginColumn, endLine, endColumn);
        setValues(values);
    }

    @Override
    public boolean removeChild(Node child) {
        boolean result = false;
        if (child != null) {
            if (values != null) {
                if (child instanceof Expression) {
                    List<Expression> valuesAux = new LinkedList<Expression>();
                    result = valuesAux.remove(child);
                    values = valuesAux;
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
        List<Node> children = new LinkedList<Node>();
        if (values != null) {
            children.addAll(values);
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

    public List<Expression> getValues() {
        return values;
    }

    public void setValues(List<Expression> values) {
        this.values = values;
        setAsParentNodeOf(values);
    }

    @Override
    public boolean replaceChildNode(Node oldChild, Node newChild) {
        boolean updated = false;
        if (values != null) {
            List<Expression> auxValues = new LinkedList<Expression>(values);
            updated = replaceChildNodeInList(oldChild, newChild, auxValues);
            values = auxValues;
        }
        return updated;

    }

    @Override
    public ArrayInitializerExpr clone() throws CloneNotSupportedException {

        return new ArrayInitializerExpr(clone(values));
    }

}
