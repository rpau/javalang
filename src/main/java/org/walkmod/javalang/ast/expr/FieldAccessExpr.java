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
import java.util.Map;

import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.ScopeAwareUtil;
import org.walkmod.javalang.ast.SymbolDefinition;
import org.walkmod.javalang.ast.SymbolReference;
import org.walkmod.javalang.ast.type.Type;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class FieldAccessExpr extends Expression implements SymbolReference {

    private Expression scope;

    private List<Type> typeArgs;

    private String field;

    private SymbolDefinition symbolDefinition;

    public FieldAccessExpr() {}

    public FieldAccessExpr(Expression scope, String field) {
        setScope(scope);
        this.field = field;
    }

    public FieldAccessExpr(Expression scope, List<Type> typeArgs, String field) {
        setScope(scope);
        setTypeArgs(typeArgs);
        this.field = field;
    }

    public FieldAccessExpr(int beginLine, int beginColumn, int endLine, int endColumn, Expression scope,
            List<Type> typeArgs, String field) {
        super(beginLine, beginColumn, endLine, endColumn);
        setScope(scope);
        setTypeArgs(typeArgs);
        this.field = field;
    }

    @Override
    public boolean removeChild(Node child) {
        boolean result = false;
        if (child != null) {
            if (scope == child) {
                scope = null;
                result = true;
            }

            if (!result) {
                if (typeArgs != null) {
                    if (child instanceof Type) {
                        List<Type> typeArgsAux = new LinkedList<Type>(typeArgs);
                        result = typeArgsAux.remove(child);
                        typeArgs = typeArgsAux;
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
        List<Node> children = new LinkedList<Node>();
        if (scope != null) {
            children.add(scope);
        }
        if (typeArgs != null) {
            children.addAll(typeArgs);
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

    public String getField() {
        return field;
    }

    public Expression getScope() {
        return scope;
    }

    public List<Type> getTypeArgs() {
        return typeArgs;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setScope(Expression scope) {
        if (this.scope != null) {
            updateReferences(this.scope);
        }
        this.scope = scope;
        setAsParentNodeOf(scope);
    }

    public void setTypeArgs(List<Type> typeArgs) {
        this.typeArgs = typeArgs;
        setAsParentNodeOf(typeArgs);
    }

    @Override
    public SymbolDefinition getSymbolDefinition() {
        return symbolDefinition;
    }

    @Override
    public void setSymbolDefinition(SymbolDefinition symbolDefinition) {
        this.symbolDefinition = symbolDefinition;
    }

    @Override
    public boolean replaceChildNode(Node oldChild, Node newChild) {
        boolean updated = false;

        if (oldChild == scope) {
            setScope((Expression) newChild);
            updated = true;
        }
        if (!updated && typeArgs != null) {
            List<Type> auxTypeArgs = new LinkedList<Type>(typeArgs);
            updated = replaceChildNodeInList(oldChild, newChild, auxTypeArgs);
            if (updated) {
                typeArgs = auxTypeArgs;
            }
        }

        return updated;
    }

    @Override
    public FieldAccessExpr clone() throws CloneNotSupportedException {
        return new FieldAccessExpr(clone(getScope()), clone(getTypeArgs()), getField());
    }

    @Override
    public Map<String, SymbolDefinition> getVariableDefinitions() {
        return ScopeAwareUtil.getVariableDefinitions(this);
    }

    @Override
    public Map<String, List<SymbolDefinition>> getMethodDefinitions() {
        return ScopeAwareUtil.getMethodDefinitions(FieldAccessExpr.this);
    }

    @Override
    public Map<String, SymbolDefinition> getTypeDefinitions() {
        return ScopeAwareUtil.getTypeDefinitions(FieldAccessExpr.this);
    }
}
