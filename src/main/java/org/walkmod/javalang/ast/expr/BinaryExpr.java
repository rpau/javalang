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

import java.util.LinkedList;
import java.util.List;

import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class BinaryExpr extends Expression {

   public static enum Operator {

      or, and, binOr, binAnd, xor, equals, notEquals, less, greater, lessEquals, greaterEquals, lShift, rSignedShift, rUnsignedShift, plus, minus, times, divide, remainder
   }

   private Expression left;

   private Expression right;

   private Operator op;

   public BinaryExpr() {
   }

   public BinaryExpr(Expression left, Expression right, Operator op) {
      setLeft(left);
      setRight(right);
      setOperator(op);
   }

   public BinaryExpr(int beginLine, int beginColumn, int endLine, int endColumn, Expression left, Expression right,
         Operator op) {
      super(beginLine, beginColumn, endLine, endColumn);
      setLeft(left);
      setRight(right);
      setOperator(op);
   }

   @Override
   public List<Node> getChildren() {
      List<Node> children = new LinkedList<Node>();
      if (left != null) {
         children.add(left);
      }
      if (right != null) {
         children.add(right);
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

   public Expression getLeft() {
      return left;
   }

   public Operator getOperator() {
      return op;
   }

   public Expression getRight() {
      return right;
   }

   public void setLeft(Expression left) {
      if (this.left != null) {
         updateReferences(this.left);
      }
      this.left = left;
      setAsParentNodeOf(left);
   }

   public void setOperator(Operator op) {
      this.op = op;
   }

   public void setRight(Expression right) {
      if (this.right != null) {
         updateReferences(this.right);
      }
      this.right = right;
      setAsParentNodeOf(right);
   }

   @Override
   public boolean replaceChildNode(Node oldChild, Node newChild) {
      boolean updated = false;
      if (left == oldChild) {
         setLeft((Expression) newChild);
         updated = true;
      }
      if (right == oldChild) {
         setRight((Expression) newChild);
         updated = true;
      }
      return updated;
   }

   @Override
   public BinaryExpr clone() throws CloneNotSupportedException {
      return new BinaryExpr(clone(getLeft()), clone(getRight()), getOperator());
   }

}
