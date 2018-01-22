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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.ScopeAware;
import org.walkmod.javalang.ast.ScopeAwareUtil;
import org.walkmod.javalang.ast.SymbolDefinition;
import org.walkmod.javalang.ast.body.VariableDeclarator;
import org.walkmod.javalang.ast.expr.Expression;
import org.walkmod.javalang.ast.expr.VariableDeclarationExpr;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class BlockStmt extends Statement implements ScopeAware {

    private List<Statement> stmts;

    public BlockStmt() {}

    public BlockStmt(List<Statement> stmts) {
        setStmts(stmts);
    }

    public BlockStmt(int beginLine, int beginColumn, int endLine, int endColumn, List<Statement> stmts) {
        super(beginLine, beginColumn, endLine, endColumn);
        setStmts(stmts);
    }

    @Override
    public boolean removeChild(Node child) {
        boolean result = false;
        if (child != null) {
            if (stmts != null) {
                if (child instanceof Statement) {
                    List<Statement> stmtsAux = new LinkedList<Statement>(stmts);
                    result = stmtsAux.remove(child);
                    stmts = stmtsAux;
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
        if (stmts != null) {
            children.addAll(stmts);
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

    public List<Statement> getStmts() {
        return stmts;
    }

    public void setStmts(List<Statement> stmts) {
        this.stmts = stmts;
        setAsParentNodeOf(stmts);
    }

    @Override
    public boolean replaceChildNode(Node oldChild, Node newChild) {
        if (stmts != null) {
            List<Statement> auxStmts = new LinkedList<Statement>(stmts);
            if (replaceChildNodeInList(oldChild, newChild, auxStmts)) {
                stmts = auxStmts;
                return true;
            }
        }
        return false;
    }

    @Override
    public BlockStmt clone() throws CloneNotSupportedException {
        return new BlockStmt(clone(stmts));
    }

    @Override
    public Map<String, SymbolDefinition> getVariableDefinitions() {
        Map<String, SymbolDefinition> result = ScopeAwareUtil.getVariableDefinitions(this);
        if (stmts != null) {
            for (Statement stmt : stmts) {
                if (stmt instanceof ExpressionStmt) {
                    ExpressionStmt exprStmt = (ExpressionStmt) stmt;
                    Expression expr = exprStmt.getExpression();
                    if (expr instanceof VariableDeclarationExpr) {
                        VariableDeclarationExpr vde = (VariableDeclarationExpr) expr;
                        List<VariableDeclarator> vars = vde.getVars();
                        if (vars != null) {
                            for (VariableDeclarator vd : vars) {
                                result.put(vd.getSymbolName(), vd);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Map<String, List<SymbolDefinition>> getMethodDefinitions() {
        return ScopeAwareUtil.getMethodDefinitions(BlockStmt.this);
    }

    @Override
    public Map<String, SymbolDefinition> getTypeDefinitions() {
        return ScopeAwareUtil.getTypeDefinitions(BlockStmt.this);
    }
}
