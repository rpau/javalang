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
import org.walkmod.javalang.ast.body.VariableDeclarator;
import org.walkmod.javalang.ast.type.Type;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class VariableDeclarationExpr extends Expression {

    private int modifiers;

    private List<AnnotationExpr> annotations;

    private Type type;

    private List<VariableDeclarator> vars;

    public VariableDeclarationExpr() {
    }

    public VariableDeclarationExpr(Type type, List<VariableDeclarator> vars) {
        this.type = type;
        this.vars = vars;
    }

    public VariableDeclarationExpr(int modifiers, Type type, List<VariableDeclarator> vars) {
        this.modifiers = modifiers;
        this.type = type;
        this.vars = vars;
    }

    public VariableDeclarationExpr(int beginLine, int beginColumn, int endLine, int endColumn, int modifiers, List<AnnotationExpr> annotations, Type type, List<VariableDeclarator> vars) {
        super(beginLine, beginColumn, endLine, endColumn);
        this.modifiers = modifiers;
        this.annotations = annotations;
        this.type = type;
        this.vars = vars;
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    public List<AnnotationExpr> getAnnotations() {
        return annotations;
    }







    /**
	 * Return the modifiers of this variable declaration.
	 * 
	 * @see ModifierSet
	 * @return modifiers
	 */
    public int getModifiers() {
        return modifiers;
    }

    public Type getType() {
        return type;
    }

    public List<VariableDeclarator> getVars() {
        return vars;
    }

    public void setAnnotations(List<AnnotationExpr> annotations) {
        this.annotations = annotations;
    }

    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setVars(List<VariableDeclarator> vars) {
        this.vars = vars;
    }
}
