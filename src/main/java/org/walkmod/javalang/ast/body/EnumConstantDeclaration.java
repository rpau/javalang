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
import org.walkmod.javalang.ast.SymbolData;
import org.walkmod.javalang.ast.SymbolDataAware;
import org.walkmod.javalang.ast.SymbolDefinition;
import org.walkmod.javalang.ast.SymbolReference;
import org.walkmod.javalang.ast.expr.AnnotationExpr;
import org.walkmod.javalang.ast.expr.Expression;
import org.walkmod.javalang.comparators.EnumConstantDeclarationComparator;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;
import org.walkmod.merger.MergeEngine;
import org.walkmod.merger.Mergeable;

/**
 * @author Julio Vilmar Gesser
 */
public final class EnumConstantDeclaration extends BodyDeclaration
        implements Mergeable<EnumConstantDeclaration>, SymbolDefinition, SymbolDataAware<SymbolData> {

    private String name;

    private List<Expression> args;

    private List<BodyDeclaration> classBody;

    private List<SymbolReference> usages;

    private List<SymbolReference> bodyReferences;

    private int scopeLevel = 0;

    private SymbolData symbolData = null;

    public EnumConstantDeclaration() {}

    public EnumConstantDeclaration(String name) {
        this.name = name;
    }

    public EnumConstantDeclaration(JavadocComment javaDoc, List<AnnotationExpr> annotations, String name,
            List<Expression> args, List<BodyDeclaration> classBody) {
        super(annotations, javaDoc);
        this.name = name;
        setArgs(args);
        setClassBody(classBody);
    }

    public EnumConstantDeclaration(int beginLine, int beginColumn, int endLine, int endColumn, JavadocComment javaDoc,
            List<AnnotationExpr> annotations, String name, List<Expression> args, List<BodyDeclaration> classBody) {
        super(beginLine, beginColumn, endLine, endColumn, annotations, javaDoc);
        this.name = name;
        setArgs(args);
        setClassBody(classBody);
    }

    @Override
    public boolean removeChild(Node child) {
        boolean result = false;
        if (child != null) {
            result = super.removeChild(child);
            if (!result) {
                if (child instanceof Expression) {
                    if (args != null) {
                        List<Expression> argsAux = new LinkedList<Expression>(args);
                        result = argsAux.remove(child);
                        args = argsAux;
                    }
                } else if (child instanceof BodyDeclaration) {
                    if (classBody != null) {
                        List<BodyDeclaration> classBodyAux = new LinkedList<BodyDeclaration>(classBody);
                        result = classBodyAux.remove(child);
                        classBody = classBodyAux;
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
        List<Node> children = super.getChildren();
        if (args != null) {
            children.addAll(args);
        }
        if (classBody != null) {
            children.addAll(classBody);
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

    public List<BodyDeclaration> getClassBody() {
        return classBody;
    }

    public String getName() {
        return name;
    }

    public void setArgs(List<Expression> args) {
        this.args = args;
        setAsParentNodeOf(args);
    }

    public void setClassBody(List<BodyDeclaration> classBody) {
        this.classBody = classBody;
        setAsParentNodeOf(classBody);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Comparator<?> getIdentityComparator() {

        return new EnumConstantDeclarationComparator();
    }

    @Override
    public void merge(EnumConstantDeclaration remote, MergeEngine configuration) {
        super.merge(remote, configuration);

        List<BodyDeclaration> resultClassBody = new LinkedList<BodyDeclaration>();
        configuration.apply(getClassBody(), remote.getClassBody(), resultClassBody, BodyDeclaration.class);

        if (!resultClassBody.isEmpty()) {
            setClassBody(resultClassBody);
        } else {
            setClassBody(null);
        }

        List<Expression> resultArgs = new LinkedList<Expression>();
        configuration.apply(getArgs(), remote.getArgs(), resultArgs, Expression.class);
        if (!resultArgs.isEmpty()) {
            setArgs(resultArgs);
        } else {
            setArgs(null);
        }

    }

    @Override
    public EnumDeclaration getParentNode() {
        return (EnumDeclaration) super.getParentNode();
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
    public SymbolData getSymbolData() {
        return symbolData;
    }

    @Override
    public void setSymbolData(SymbolData symbolData) {
        this.symbolData = symbolData;
    }

    @Override
    public boolean replaceChildNode(Node oldChild, Node newChild) {
        boolean update = super.replaceChildNode(oldChild, newChild);
        if (!update) {
            if (args != null) {
                List<Expression> auxArgs = new LinkedList<Expression>(args);
                update = replaceChildNodeInList(oldChild, newChild, auxArgs);
                if (update) {
                    args = auxArgs;
                }
            }
        }
        if (!update && classBody != null) {
            List<BodyDeclaration> auxClassBody = new LinkedList<BodyDeclaration>(classBody);
            update = replaceChildNodeInList(oldChild, newChild, auxClassBody);
            if (update) {
                classBody = auxClassBody;
            }
        }
        return update;
    }

    @Override
    public EnumConstantDeclaration clone() throws CloneNotSupportedException {
        return new EnumConstantDeclaration(clone(getJavaDoc()), clone(getAnnotations()), name, clone(getArgs()),
                clone(getClassBody()));
    }

    @Override
    public String getSymbolName() {
        return name;
    }
}
