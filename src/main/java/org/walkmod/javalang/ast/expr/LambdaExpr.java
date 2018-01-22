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

import java.util.List;
import java.util.Map;

import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.ScopeAwareUtil;
import org.walkmod.javalang.ast.SymbolDefinition;
import org.walkmod.javalang.ast.SymbolReference;
import org.walkmod.javalang.ast.body.Parameter;
import org.walkmod.javalang.ast.stmt.Statement;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * Lambda expressions.
 * 
 * @author Raquel Pau
 *
 */
public class LambdaExpr extends Expression implements SymbolReference {

    private List<Parameter> parameters;

    private boolean parametersEnclosed;

    private Statement body;

    private SymbolDefinition symbolDefinition;

    public LambdaExpr() {}

    public LambdaExpr(int beginLine, int beginColumn, int endLine, int endColumn, List<Parameter> parameters,
            Statement body, boolean parametersEnclosed) {

        super(beginLine, beginColumn, endLine, endColumn);
        setParameters(parameters);
        setBody(body);

        if (this.parameters != null && this.parameters.size() == 1 && this.parameters.get(0).getType() == null) {
            this.parametersEnclosed = parametersEnclosed;
        } else {
            this.parametersEnclosed = true;
        }
    }

    public LambdaExpr(List<Parameter> parameters, Statement body, boolean parametersEnclosed) {

        setParameters(parameters);
        setBody(body);

        if (this.parameters != null && this.parameters.size() == 1 && this.parameters.get(0).getType() == null) {
            this.parametersEnclosed = parametersEnclosed;
        } else {
            this.parametersEnclosed = true;
        }
    }

    @Override
    public boolean removeChild(Node child) {
        boolean result = false;
        if (child != null) {
            if (parameters != null) {
                if (child instanceof Parameter) {
                    List<Parameter> parametersAux = new LinkedList<Parameter>(parameters);
                    result = parametersAux.remove(child);
                    parameters = parametersAux;
                }
            }
            if (!result) {
                if (body == child) {
                    body = null;
                    result = true;
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

        if (parameters != null) {
            children.addAll(parameters);
        }
        if (body != null) {
            children.add(body);
        }
        return children;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
        setAsParentNodeOf(parameters);
    }

    public Statement getBody() {
        return body;
    }

    public void setBody(Statement body) {
        if (this.body != null) {
            updateReferences(this.body);
        }
        this.body = body;
        setAsParentNodeOf(body);
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

    public boolean isParametersEnclosed() {
        return parametersEnclosed;
    }

    public void setParametersEnclosed(boolean parametersEnclosed) {
        this.parametersEnclosed = parametersEnclosed;
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

        if (oldChild == body) {
            setBody((Statement) newChild);
            updated = true;
        }
        if (!updated && parameters != null) {

            List<Parameter> auxParams = new LinkedList<Parameter>(parameters);

            updated = replaceChildNodeInList(oldChild, newChild, auxParams);

            if (updated) {
                auxParams = parameters;
            }
        }

        return updated;
    }

    @Override
    public LambdaExpr clone() throws CloneNotSupportedException {
        return new LambdaExpr(clone(parameters), clone(body), parametersEnclosed);
    }

    @Override
    public Map<String, SymbolDefinition> getVariableDefinitions() {
        return ScopeAwareUtil.getVariableDefinitions(this);
    }

    @Override
    public Map<String, List<SymbolDefinition>> getMethodDefinitions() {
        return ScopeAwareUtil.getMethodDefinitions(LambdaExpr.this);
    }

    @Override
    public Map<String, SymbolDefinition> getTypeDefinitions() {
        return ScopeAwareUtil.getTypeDefinitions(LambdaExpr.this);
    }

}
