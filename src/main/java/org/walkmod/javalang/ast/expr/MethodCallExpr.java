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

import org.walkmod.javalang.ast.MethodSymbolData;
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
public final class MethodCallExpr extends Expression implements SymbolReference {

    private Expression scope;

    private List<Type> typeArgs;

    private String name;

    private List<Expression> args;

    private SymbolDefinition symbolDefinition;

    public MethodCallExpr() {}

    public MethodCallExpr(Expression scope, String name) {
        setScope(scope);
        this.name = name;
    }

    public MethodCallExpr(Expression scope, String name, List<Expression> args) {
        setScope(scope);
        this.name = name;
        setArgs(args);
    }

    public MethodCallExpr(Expression scope, List<Type> typeArgs, String name, List<Expression> args) {
        setScope(scope);
        setTypeArgs(typeArgs);
        this.name = name;
        setArgs(args);
    }

    public MethodCallExpr(int beginLine, int beginColumn, int endLine, int endColumn, Expression scope,
            List<Type> typeArgs, String name, List<Expression> args) {
        super(beginLine, beginColumn, endLine, endColumn);
        setScope(scope);
        setTypeArgs(typeArgs);
        this.name = name;
        setArgs(args);
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
            if (!result) {
                if (args != null) {
                    if (child instanceof Expression) {
                        List<Expression> argsAux = new LinkedList<Expression>(args);
                        result = argsAux.remove(child);
                        args = argsAux;
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
        if (args != null) {
            children.addAll(args);
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

    public List<Expression> getArgs() {
        return args;
    }

    public String getName() {
        return name;
    }

    public Expression getScope() {
        return scope;
    }

    public List<Type> getTypeArgs() {
        return typeArgs;
    }

    public void setArgs(List<Expression> args) {
        this.args = args;
        setAsParentNodeOf(args);
    }

    public void setName(String name) {
        this.name = name;
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
    public MethodSymbolData getSymbolData() {
        return (MethodSymbolData) super.getSymbolData();
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
        if (!updated) {
            if (typeArgs != null) {
                List<Type> typeArgsAux = new LinkedList<Type>(typeArgs);
                updated = replaceChildNodeInList(oldChild, newChild, typeArgsAux);
                typeArgs = typeArgsAux;
            }
            if (!updated) {
                if (args != null) {
                    List<Expression> argsAux = new LinkedList<Expression>(args);
                    updated = replaceChildNodeInList(oldChild, newChild, argsAux);
                    if (updated) {
                        args = argsAux;
                    }
                }
            }
        }

        return updated;
    }

    @Override
    public MethodCallExpr clone() throws CloneNotSupportedException {
        return new MethodCallExpr(clone(scope), clone(typeArgs), name, clone(args));
    }

    @Override
    public Map<String, SymbolDefinition> getVariableDefinitions() {
        return ScopeAwareUtil.getVariableDefinitions(this);
    }

    @Override
    public Map<String, List<SymbolDefinition>> getMethodDefinitions() {
        return ScopeAwareUtil.getMethodDefinitions(MethodCallExpr.this);
    }

    @Override
    public Map<String, SymbolDefinition> getTypeDefinitions() {
        return ScopeAwareUtil.getTypeDefinitions(MethodCallExpr.this);
    }
}
