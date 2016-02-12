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

import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class AssignExpr extends Expression {

   public static enum Operator {

      assign, plus, minus, star, slash, and, or, xor, rem, lShift, rSignedShift, rUnsignedShift
   }

   private Expression target;

   private Expression value;

   private Operator op;

   public AssignExpr() {
   }

   public AssignExpr(Expression target, Expression value, Operator op) {
      setTarget(target);
      setValue(value);
      setOperator(op);
   }

   public AssignExpr(int beginLine, int beginColumn, int endLine, int endColumn, Expression target, Expression value,
         Operator op) {
      super(beginLine, beginColumn, endLine, endColumn);
      setTarget(target);
      setValue(value);
      setOperator(op);
   }

   @Override
   public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
      return v.visit(this, arg);
   }

   @Override
   public <A> void accept(VoidVisitor<A> v, A arg) {
      v.visit(this, arg);
   }

   public Operator getOperator() {
      return op;
   }

   public Expression getTarget() {
      return target;
   }

   public Expression getValue() {
      return value;
   }

   public void setOperator(Operator op) {
      this.op = op;
   }

   public void setTarget(Expression target) {
      if (this.target != null) {
         updateReferences(this.target);
      }
      this.target = target;
      setAsParentNodeOf(target);
   }

   public void setValue(Expression value) {
      if (this.value != null) {
         updateReferences(this.value);
      }
      this.value = value;
      setAsParentNodeOf(value);
   }

   @Override
   public boolean replaceChildNode(Node oldChild, Node newChild) {
      boolean updated = false;
      if (target == oldChild) {
         setTarget((Expression) newChild);
         updated = true;
      }
      if (!updated) {
         if (value == oldChild) {
            setValue((Expression) newChild);
            updated = true;
         }
      }

      return updated;
   }

   @Override
   public AssignExpr clone() throws CloneNotSupportedException {

      return new AssignExpr(clone(getTarget()), clone(getValue()), getOperator());
   }
}
