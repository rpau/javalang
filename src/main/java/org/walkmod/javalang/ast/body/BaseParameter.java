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
import java.util.Map;

import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.ScopeAwareUtil;
import org.walkmod.javalang.ast.SymbolData;
import org.walkmod.javalang.ast.SymbolDataAware;
import org.walkmod.javalang.ast.SymbolDefinition;
import org.walkmod.javalang.ast.SymbolReference;
import org.walkmod.javalang.ast.expr.AnnotationExpr;

public abstract class BaseParameter extends Node implements SymbolDataAware<SymbolData>, SymbolDefinition {

    private static final long serialVersionUID = -920000439540828718L;

    private int modifiers;

    private List<AnnotationExpr> annotations;

    private VariableDeclaratorId id;

    private SymbolData symbolData;

    private List<SymbolReference> usages;

    private int scopeLevel = 0;

    public BaseParameter() {}

    public BaseParameter(VariableDeclaratorId id) {
        setId(id);
    }

    public BaseParameter(int modifiers, VariableDeclaratorId id) {
        setModifiers(modifiers);
        setId(id);
    }

    public BaseParameter(int modifiers, List<AnnotationExpr> annotations, VariableDeclaratorId id) {
        setModifiers(modifiers);
        setAnnotations(annotations);
        setId(id);
    }

    public BaseParameter(int beginLine, int beginColumn, int endLine, int endColumn, int modifiers,
            List<AnnotationExpr> annotations, VariableDeclaratorId id) {
        super(beginLine, beginColumn, endLine, endColumn);
        setModifiers(modifiers);
        setAnnotations(annotations);
        setId(id);
    }

    @Override
    public boolean removeChild(Node child) {
        boolean result = false;
        if (child != null) {
            if (child instanceof AnnotationExpr) {
                if (annotations != null) {
                    List<AnnotationExpr> annotationAux = new LinkedList<AnnotationExpr>(annotations);
                    result = annotationAux.remove(child);
                    this.annotations = annotationAux;
                }
            }
            if (child == id && id != null) {
                id = null;
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
        if (annotations != null) {
            children.addAll(annotations);
        }
        if (id != null) {
            children.add(id);
        }

        return children;
    }

    public List<AnnotationExpr> getAnnotations() {
        return annotations;
    }

    public VariableDeclaratorId getId() {
        return id;
    }

    /**
    * Return the modifiers of this parameter declaration.
    * 
    * @see ModifierSet
    * @return modifiers
    */
    public int getModifiers() {
        return modifiers;
    }

    public void setAnnotations(List<AnnotationExpr> annotations) {
        this.annotations = annotations;
        setAsParentNodeOf(annotations);
    }

    public void setId(VariableDeclaratorId id) {
        if (this.id != null) {
            updateReferences(this.id);
        }
        this.id = id;
        setAsParentNodeOf(id);
    }

    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    @Override
    public SymbolData getSymbolData() {
        return symbolData;
    }

    @Override
    public void setSymbolData(SymbolData symbolData) {
        this.symbolData = symbolData;
    }

    public List<SymbolReference> getUsages() {
        return usages;
    }

    public List<SymbolReference> getBodyReferences() {
        return null;
    }

    public void setUsages(List<SymbolReference> usages) {
        this.usages = usages;
    }

    public void setBodyReferences(List<SymbolReference> bodyReferences) {}

    @Override
    public boolean addBodyReference(SymbolReference bodyReference) {
        return false;
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
        if (annotations != null) {
            List<AnnotationExpr> auxAnnotations = new LinkedList<AnnotationExpr>(annotations);
            update = replaceChildNodeInList(oldChild, newChild, auxAnnotations);
            if (update) {
                annotations = auxAnnotations;
            }
        }

        if (!update) {
            if (id == oldChild) {
                setId((VariableDeclaratorId) newChild);
                update = true;
            }
        }

        return update;
    }

    @Override
    public Map<String, SymbolDefinition> getVariableDefinitions() {
        return ScopeAwareUtil.getVariableDefinitions(this);
    }

    @Override
    public Map<String, List<SymbolDefinition>> getMethodDefinitions() {
        return ScopeAwareUtil.getMethodDefinitions(BaseParameter.this);
    }

    @Override
    public Map<String, SymbolDefinition> getTypeDefinitions() {
        return ScopeAwareUtil.getTypeDefinitions(this);
    }

    @Override
    public String getSymbolName() {
        return getId().getName();
    }
}
