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

import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.SymbolDefinition;
import org.walkmod.javalang.ast.SymbolReference;
import org.walkmod.javalang.ast.expr.AnnotationExpr;
import org.walkmod.javalang.ast.expr.Expression;
import org.walkmod.javalang.ast.type.Type;
import org.walkmod.javalang.comparators.AnnotationMemberDeclarationComparator;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;
import org.walkmod.merger.MergeEngine;
import org.walkmod.merger.Mergeable;

/**
 * @author Julio Vilmar Gesser
 */
public final class AnnotationMemberDeclaration extends BodyDeclaration
        implements Mergeable<AnnotationMemberDeclaration>, SymbolDefinition {

    private int modifiers;

    private Type type;

    private String name;

    private Expression defaultValue;

    private List<SymbolReference> usages;

    private List<SymbolReference> bodyReferences;

    private int scopeLevel = 0;

    public AnnotationMemberDeclaration() {}

    public AnnotationMemberDeclaration(int modifiers, Type type, String name, Expression defaultValue) {
        this.modifiers = modifiers;
        setType(type);
        this.name = name;
        setDefaultValue(defaultValue);
    }

    public AnnotationMemberDeclaration(JavadocComment javaDoc, int modifiers, List<AnnotationExpr> annotations,
            Type type, String name, Expression defaultValue) {
        setJavaDoc(javaDoc);
        this.modifiers = modifiers;
        setType(type);
        this.name = name;
        setDefaultValue(defaultValue);

    }

    public AnnotationMemberDeclaration(int beginLine, int beginColumn, int endLine, int endColumn,
            JavadocComment javaDoc, int modifiers, List<AnnotationExpr> annotations, Type type, String name,
            Expression defaultValue) {
        super(beginLine, beginColumn, endLine, endColumn, annotations, javaDoc);
        this.modifiers = modifiers;
        setType(type);
        this.name = name;
        setDefaultValue(defaultValue);
    }

    @Override
    public boolean removeChild(Node child) {
        boolean result = false;
        if (child != null) {
            result = super.removeChild(child);
            if (!result) {
                if (type == child && type != null) {
                    type = null;
                    result = true;
                } else if (defaultValue == child && defaultValue != null) {
                    defaultValue = null;
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

        if (type != null) {
            children.add(type);
        }

        if (defaultValue != null) {
            children.add(defaultValue);
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

    public Expression getDefaultValue() {
        return defaultValue;
    }

    /**
    * Return the modifiers of this member declaration.
    * 
    * @see ModifierSet
    * @return modifiers
    */
    public int getModifiers() {
        return modifiers;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public void setDefaultValue(Expression defaultValue) {
        this.defaultValue = defaultValue;
        setAsParentNodeOf(defaultValue);
    }

    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(Type type) {
        this.type = type;
        setAsParentNodeOf(type);
    }

    @Override
    public Comparator<?> getIdentityComparator() {
        return new AnnotationMemberDeclarationComparator();
    }

    @Override
    public void merge(AnnotationMemberDeclaration remote, MergeEngine configuration) {
        super.merge(remote, configuration);
        setType((Type) configuration.apply(getType(), remote.getType(), Type.class));
        setDefaultValue(
                (Expression) configuration.apply(getDefaultValue(), remote.getDefaultValue(), Expression.class));

    }

    @Override
    public AnnotationDeclaration getParentNode() {
        return (AnnotationDeclaration) super.getParentNode();
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
    public List<SymbolReference> getBodyReferences() {
        return bodyReferences;
    }

    @Override
    public void setBodyReferences(List<SymbolReference> bodyReferences) {
        this.bodyReferences = bodyReferences;
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
    public int getScopeLevel() {
        return scopeLevel;
    }

    @Override
    public void setScopeLevel(int scopeLevel) {
        this.scopeLevel = scopeLevel;
    }

    @Override
    public AnnotationMemberDeclaration clone() throws CloneNotSupportedException {
        return new AnnotationMemberDeclaration(getModifiers(), clone(getType()), getName(), clone(getDefaultValue()));
    }

    @Override
    public String getSymbolName() {
        return name;
    }


}
