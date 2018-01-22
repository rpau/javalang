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

import org.walkmod.javalang.ast.ConstructorSymbolData;
import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.ScopeAwareUtil;
import org.walkmod.javalang.ast.SymbolDefinition;
import org.walkmod.javalang.ast.SymbolReference;
import org.walkmod.javalang.ast.body.BodyDeclaration;
import org.walkmod.javalang.ast.type.ClassOrInterfaceType;
import org.walkmod.javalang.ast.type.Type;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class ObjectCreationExpr extends Expression implements SymbolReference, SymbolDefinition {

    private Expression scope;

    private ClassOrInterfaceType type;

    private List<Type> typeArgs;

    private List<Expression> args;

    private List<BodyDeclaration> anonymousClassBody;

    private SymbolDefinition symbolDefinition;

    private List<SymbolReference> usages;

    private List<SymbolReference> bodyReferences;

    private int scopeLevel = 0;

    public ObjectCreationExpr() {}

    public ObjectCreationExpr(Expression scope, ClassOrInterfaceType type, List<Expression> args) {
        setScope(scope);
        setType(type);
        setArgs(args);
    }

    public ObjectCreationExpr(Expression scope, ClassOrInterfaceType type, List<Expression> args,
            List<BodyDeclaration> anonymousBody) {
        setScope(scope);
        setType(type);
        setArgs(args);
        setAnonymousClassBody(anonymousBody);
    }

    public ObjectCreationExpr(int beginLine, int beginColumn, int endLine, int endColumn, Expression scope,
            ClassOrInterfaceType type, List<Type> typeArgs, List<Expression> args,
            List<BodyDeclaration> anonymousBody) {
        super(beginLine, beginColumn, endLine, endColumn);
        setScope(scope);
        setType(type);
        setArgs(args);
        setTypeArgs(typeArgs);
        setAnonymousClassBody(anonymousBody);
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

                if (type == child) {
                    type = null;
                    result = true;
                }
            }
            if (!result) {
                if (child instanceof Type) {
                    if (typeArgs != null) {
                        List<Type> typeArgsAux = new LinkedList<Type>(typeArgs);
                        result = typeArgsAux.remove(child);
                        typeArgs = typeArgsAux;
                    }
                }
            }
            if (!result) {
                if (child instanceof Expression) {
                    if (args != null) {
                        List<Expression> argsAux = new LinkedList<Expression>(args);
                        result = argsAux.remove(child);
                        args = argsAux;
                    }
                }

            }
            if (!result) {
                if (child instanceof BodyDeclaration) {
                    if (anonymousClassBody != null) {
                        List<BodyDeclaration> anonymousClassBodyAux =
                                new LinkedList<BodyDeclaration>(anonymousClassBody);
                        result = anonymousClassBodyAux.remove(child);
                        anonymousClassBody = anonymousClassBodyAux;
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
        if (type != null) {
            children.add(type);
        }
        if (args != null) {
            children.addAll(args);
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

    public List<BodyDeclaration> getAnonymousClassBody() {
        return anonymousClassBody;
    }

    public List<Expression> getArgs() {
        return args;
    }

    public Expression getScope() {
        return scope;
    }

    public ClassOrInterfaceType getType() {
        return type;
    }

    public List<Type> getTypeArgs() {
        return typeArgs;
    }

    public void setAnonymousClassBody(List<BodyDeclaration> anonymousClassBody) {
        this.anonymousClassBody = anonymousClassBody;
        setAsParentNodeOf(anonymousClassBody);
    }

    public void setArgs(List<Expression> args) {
        this.args = args;
        setAsParentNodeOf(args);
    }

    public void setScope(Expression scope) {
        if (this.scope != null) {
            updateReferences(this.scope);
        }
        this.scope = scope;
        setAsParentNodeOf(scope);
    }

    public void setType(ClassOrInterfaceType type) {
        if (this.type != null) {
            updateReferences(this.type);
        }
        this.type = type;
        setAsParentNodeOf(type);
    }

    public void setTypeArgs(List<Type> typeArgs) {
        this.typeArgs = typeArgs;
        setAsParentNodeOf(typeArgs);
    }

    @Override
    public ConstructorSymbolData getSymbolData() {
        return (ConstructorSymbolData) super.getSymbolData();
    }

    @Override
    public SymbolDefinition getSymbolDefinition() {
        return symbolDefinition;
    }

    @Override
    public void setSymbolDefinition(SymbolDefinition symbolDefinition) {
        this.symbolDefinition = symbolDefinition;
    }

    public List<SymbolReference> getUsages() {
        return usages;
    }

    public List<SymbolReference> getBodyReferences() {
        return bodyReferences;
    }

    public void setUsages(List<SymbolReference> usages) {
        this.usages = usages;
    }

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
    public int getScopeLevel() {
        return scopeLevel;
    }

    @Override
    public void setScopeLevel(int scopeLevel) {
        this.scopeLevel = scopeLevel;
    }

    @Override
    public boolean replaceChildNode(Node oldChild, Node newChild) {
        boolean updated = false;

        if (oldChild == scope) {
            setScope((Expression) newChild);
            updated = true;
        }
        if (!updated) {

            if (oldChild == type) {
                setType((ClassOrInterfaceType) newChild);
                updated = true;
            }

        }
        if (!updated && typeArgs != null) {
            List<Type> auxTypeArgs = new LinkedList<Type>(typeArgs);
            updated = replaceChildNodeInList(oldChild, newChild, auxTypeArgs);

            if (updated) {
                typeArgs = auxTypeArgs;
            }
        }
        if (!updated && args != null) {
            List<Expression> auxArgs = new LinkedList<Expression>(args);
            updated = replaceChildNodeInList(oldChild, newChild, auxArgs);
            if (updated) {
                args = auxArgs;
            }
        }
        if (!updated && anonymousClassBody != null) {

            List<BodyDeclaration> auxAnonymousClassBody = new LinkedList<BodyDeclaration>(anonymousClassBody);

            updated = replaceChildNodeInList(oldChild, newChild, auxAnonymousClassBody);

            anonymousClassBody = auxAnonymousClassBody;
        }
        return updated;
    }

    @Override
    public ObjectCreationExpr clone() throws CloneNotSupportedException {

        return new ObjectCreationExpr(clone(scope), clone(type), clone(args), clone(anonymousClassBody));
    }

    @Override
    public Map<String, SymbolDefinition> getVariableDefinitions() {
        return ScopeAwareUtil.getVariableDefinitions(this);
    }

    @Override
    public Map<String, List<SymbolDefinition>> getMethodDefinitions() {
        return ScopeAwareUtil.getMethodDefinitions(ObjectCreationExpr.this);
    }

    @Override
    public Map<String, SymbolDefinition> getTypeDefinitions() {
        return ScopeAwareUtil.getTypeDefinitions(ObjectCreationExpr.this);
    }

    @Override
    public String getSymbolName() {
        return null;
    }

}
