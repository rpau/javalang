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
package org.walkmod.javalang.ast.stmt;

import java.util.List;

import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.expr.Expression;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class ThrowStmt extends Statement {

    private Expression expr;

    public ThrowStmt() {}

    public ThrowStmt(Expression expr) {
        setExpr(expr);
    }

    public ThrowStmt(int beginLine, int beginColumn, int endLine, int endColumn, Expression expr) {
        super(beginLine, beginColumn, endLine, endColumn);
        setExpr(expr);
    }

    @Override
    public boolean removeChild(Node child) {
        boolean result = false;
        if (child != null) {
            if (expr == child) {
                expr = null;
                result = true;
            }
        }
        if (result) {
            updateReferences(child);
        }
        return result;
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

    @Override
    public List<Node> getChildren() {
        List<Node> children = super.getChildren();
        if (expr != null) {
            children.add(expr);
        }
        return children;
    }

    public Expression getExpr() {
        return expr;
    }

    public void setExpr(Expression expr) {
        if (this.expr != null) {
            updateReferences(this.expr);
        }
        this.expr = expr;
        setAsParentNodeOf(expr);
    }

    @Override
    public boolean replaceChildNode(Node oldChild, Node newChild) {
        boolean updated = false;
        if (oldChild == expr) {
            setExpr((Expression) newChild);
            updated = true;
        }

        return updated;
    }

    @Override
    public ThrowStmt clone() throws CloneNotSupportedException {
        return new ThrowStmt(clone(expr));
    }
}
