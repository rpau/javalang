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

import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.expr.AnnotationExpr;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class PrimitiveType extends Type {

   public enum Primitive {

      Boolean, Char, Byte, Short, Int, Long, Float, Double
   }

   private Primitive type;

   public PrimitiveType() {
   }

   public PrimitiveType(Primitive type) {
      this.type = type;
   }

   public PrimitiveType(int beginLine, int beginColumn, int endLine, int endColumn, Primitive type) {
      super(beginLine, beginColumn, endLine, endColumn);
      this.type = type;
   }

   public PrimitiveType(int beginLine, int beginColumn, int endLine, int endColumn, Primitive type,
         List<AnnotationExpr> annotations) {
      super(beginLine, beginColumn, endLine, endColumn, annotations);
      this.type = type;
   }

   @Override
   public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
      return v.visit(this, arg);
   }

   @Override
   public <A> void accept(VoidVisitor<A> v, A arg) {
      v.visit(this, arg);
   }

   public Primitive getType() {
      return type;
   }

   public void setType(Primitive type) {
      this.type = type;
   }

   @Override
   public boolean replaceChildNode(Node oldChild, Node newChild) {
      return super.replaceChildNode(oldChild, newChild);
   }
   
   @Override
   public PrimitiveType clone() throws CloneNotSupportedException {
      return new PrimitiveType(type);
   }
}
