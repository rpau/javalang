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

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.Refactorizable;
import org.walkmod.javalang.ast.Refactorization;
import org.walkmod.javalang.ast.ScopeAwareUtil;
import org.walkmod.javalang.ast.SymbolDefinition;
import org.walkmod.javalang.ast.SymbolReference;
import org.walkmod.javalang.ast.expr.Expression;
import org.walkmod.javalang.comparators.VariableDeclaratorComparator;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;
import org.walkmod.merger.MergeEngine;
import org.walkmod.merger.Mergeable;

/**
 * @author Julio Vilmar Gesser
 */
public final class VariableDeclarator extends Node
        implements Mergeable<VariableDeclarator>, SymbolDefinition, Refactorizable {

    private VariableDeclaratorId id;

    private Expression init;

    private List<SymbolReference> usages;

    private List<SymbolReference> bodyReferences;

    private int scopeLevel;

    public VariableDeclarator() {}

    public VariableDeclarator(VariableDeclaratorId id) {
        setId(id);
    }

    public VariableDeclarator(VariableDeclaratorId id, Expression init) {
        setId(id);
        setInit(init);
    }

    public VariableDeclarator(int beginLine, int beginColumn, int endLine, int endColumn, VariableDeclaratorId id,
            Expression init) {
        super(beginLine, beginColumn, endLine, endColumn);
        setId(id);
        setInit(init);
    }

    @Override
    public boolean removeChild(Node child) {
        boolean result = false;
        if (child != null) {
            if (id != null) {
                if (id == child) {
                    id = null;
                    result = true;
                }
            }
            if (!result) {
                if (init != null) {
                    init = null;
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
        if (id != null) {
            children.add(id);
        }
        if (init != null) {
            children.add(init);
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

    public VariableDeclaratorId getId() {
        return id;
    }

    public Expression getInit() {
        return init;
    }

    public void setId(VariableDeclaratorId id) {
        if (this.id != null) {
            updateReferences(this.id);
        }
        this.id = id;
        setAsParentNodeOf(id);
    }

    public void setInit(Expression init) {
        if (this.init != null) {
            updateReferences(this.init);
        }
        this.init = init;
        setAsParentNodeOf(init);
    }

    @Override
    public Comparator<?> getIdentityComparator() {
        // TODO pensar si es un singleton o un atributo estatico de la clase
        return new VariableDeclaratorComparator();
    }

    @Override
    public void merge(VariableDeclarator remote, MergeEngine configuration) {
        setInit((Expression) configuration.apply(getInit(), remote.getInit(), Expression.class));
        setId((VariableDeclaratorId) configuration.apply(getId(), remote.getId(), VariableDeclaratorId.class));

    }

    @Override
    public List<SymbolReference> getUsages() {
        return usages;
    }

    @Override
    public void setUsages(List<SymbolReference> usages) {
        this.usages = usages;
    }

    @Override
    public List<SymbolReference> getBodyReferences() {
        return bodyReferences;
    }

    @Override
    public void setBodyReferences(List<SymbolReference> bodyReferences) {
        this.bodyReferences = bodyReferences;
    }

    @Override
    public int getScopeLevel() {
        return scopeLevel;
    }

    @Override
    public void setScopeLevel(int scopeLevel) {
        this.scopeLevel = scopeLevel;
    }

    @Override
    public boolean addBodyReference(SymbolReference bodyReference) {
        if (bodyReference != null) {
            SymbolDefinition definition = bodyReference.getSymbolDefinition();
            if (definition != null) {
                int scope = definition.getScopeLevel();
                if (scope <= scopeLevel) {
                    if (bodyReferences == null) {
                        bodyReferences = new LinkedList<SymbolReference>();
                    }
                    return bodyReferences.add(bodyReference);
                }
            }
        }
        return false;
    }

    @Override
    public boolean addUsage(SymbolReference usage) {
        if (usage != null) {
            usage.setSymbolDefinition(this);
            if (usages == null) {
                usages = new LinkedList<SymbolReference>();
            }
            return usages.add(usage);
        }
        return false;

    }

    @Override
    public boolean replaceChildNode(Node oldChild, Node newChild) {
        boolean update = false;

        if (oldChild == id) {
            setId((VariableDeclaratorId) newChild);
            update = true;
        }
        if (!update) {
            if (init == oldChild) {
                setInit((Expression) newChild);
            }
        }
        return update;
    }

    @Override
    public VariableDeclarator clone() throws CloneNotSupportedException {
        return new VariableDeclarator(clone(getId()), clone(getInit()));
    }

    @Override
    public Map<String, SymbolDefinition> getVariableDefinitions() {
        return ScopeAwareUtil.getVariableDefinitions(this);
    }

    @Override
    public Map<String, List<SymbolDefinition>> getMethodDefinitions() {
        return ScopeAwareUtil.getMethodDefinitions(VariableDeclarator.this);
    }

    @Override
    public Map<String, SymbolDefinition> getTypeDefinitions() {
        return ScopeAwareUtil.getTypeDefinitions(VariableDeclarator.this);
    }

    @Override
    public String getSymbolName() {
        return getId().getName();
    }

    @Override
    public boolean rename(String newName) {
        Refactorization refactorization = new Refactorization();
        if (refactorization.refactorVariable(this, newName)) {
            replaceChildNode(getId(), new VariableDeclaratorId(newName));
            return true;
        }
        return false;
    }
}
