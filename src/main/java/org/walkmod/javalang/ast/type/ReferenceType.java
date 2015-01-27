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

package org.walkmod.javalang.ast.type;

import java.util.List;
import org.walkmod.javalang.ast.expr.AnnotationExpr;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class ReferenceType extends Type {

    private Type type;

    private int arrayCount;

    private List<List<AnnotationExpr>> arraysAnnotations;

    public ReferenceType() {
    }

    public ReferenceType(Type type) {
        this.type = type;
    }

    public ReferenceType(Type type, int arrayCount) {
        this.type = type;
        this.arrayCount = arrayCount;
    }

    public ReferenceType(Type type, int arrayCount, List<AnnotationExpr> annotations) {
        super(annotations);
        this.type = type;
        this.arrayCount = arrayCount;
    }

    public ReferenceType(int beginLine, int beginColumn, int endLine, int endColumn, Type type, int arrayCount) {
        super(beginLine, beginColumn, endLine, endColumn);
        this.type = type;
        this.arrayCount = arrayCount;
    }

    public ReferenceType(int beginLine, int beginColumn, int endLine, int endColumn, Type type, int arrayCount, List<AnnotationExpr> annotations, List<List<AnnotationExpr>> arraysAnnotations) {
        super(beginLine, beginColumn, endLine, endColumn, annotations);
        this.type = type;
        this.arrayCount = arrayCount;
        this.arraysAnnotations = arraysAnnotations;
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    public int getArrayCount() {
        return arrayCount;
    }

    public Type getType() {
        return type;
    }

    public void setArrayCount(int arrayCount) {
        this.arrayCount = arrayCount;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<List<AnnotationExpr>> getArraysAnnotations() {
        return arraysAnnotations;
    }

    public void setArraysAnnotations(List<List<AnnotationExpr>> arraysAnnotations) {
        this.arraysAnnotations = arraysAnnotations;
    }
}
