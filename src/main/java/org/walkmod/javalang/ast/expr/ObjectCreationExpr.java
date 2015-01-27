/* 
  Copyright (C) 2013 Raquel Pau and Albert Coroleu.
 
 Walkmod is free software: you can redistribute it and/or modify
 it under the terms of the GNU Lesser General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.
 
 Walkmod is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Lesser General Public License for more details.
 
 You should have received a copy of the GNU Lesser General Public License
 along with Walkmod.  If not, see <http://www.gnu.org/licenses/>.*/

package org.walkmod.javalang.ast.expr;

import java.util.List;
import org.walkmod.javalang.ast.body.BodyDeclaration;
import org.walkmod.javalang.ast.type.ClassOrInterfaceType;
import org.walkmod.javalang.ast.type.Type;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class ObjectCreationExpr extends Expression {

    private Expression scope;

    private ClassOrInterfaceType type;

    private List<Type> typeArgs;

    private List<Expression> args;

    private List<BodyDeclaration> anonymousClassBody;

    public ObjectCreationExpr() {
    }

    public ObjectCreationExpr(Expression scope, ClassOrInterfaceType type, List<Expression> args) {
        this.scope = scope;
        this.type = type;
        this.args = args;
    }

    public ObjectCreationExpr(int beginLine, int beginColumn, int endLine, int endColumn, Expression scope, ClassOrInterfaceType type, List<Type> typeArgs, List<Expression> args, List<BodyDeclaration> anonymousBody) {
        super(beginLine, beginColumn, endLine, endColumn);
        this.scope = scope;
        this.type = type;
        this.typeArgs = typeArgs;
        this.args = args;
        this.anonymousClassBody = anonymousBody;
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
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
    }

    public void setArgs(List<Expression> args) {
        this.args = args;
    }

    public void setScope(Expression scope) {
        this.scope = scope;
    }

    public void setType(ClassOrInterfaceType type) {
        this.type = type;
    }

    public void setTypeArgs(List<Type> typeArgs) {
        this.typeArgs = typeArgs;
    }
}
