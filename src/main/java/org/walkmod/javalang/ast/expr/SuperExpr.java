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
public final class SuperExpr extends Expression {

   private Expression classExpr;

   public SuperExpr() {
   }

   public SuperExpr(Expression classExpr) {
      setClassExpr(classExpr);
   }

   public SuperExpr(int beginLine, int beginColumn, int endLine, int endColumn, Expression classExpr) {
      super(beginLine, beginColumn, endLine, endColumn);
      setClassExpr(classExpr);
   }

   @Override
   public List<Node> getChildren() {
      List<Node> children = super.getChildren();
      if (classExpr != null) {
         children.add(classExpr);
      }
      return children;
   }

   @Override
   public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
      return v.visit(this, arg);
   }

   @Override
   public <A> void accept(VoidVisitor<A> v, A arg) {
      v.visit(this, arg);
   }

   public Expression getClassExpr() {
      return classExpr;
   }

   public void setClassExpr(Expression classExpr) {
      if (this.classExpr != null) {
         updateReferences(this.classExpr);
      }
      this.classExpr = classExpr;
      setAsParentNodeOf(classExpr);
   }

   @Override
   public boolean replaceChildNode(Node oldChild, Node newChild) {
      boolean updated = false;
      if (oldChild == classExpr) {
         setClassExpr((Expression) newChild);
         updated = true;
      }
      return updated;
   }

   @Override
   public SuperExpr clone() throws CloneNotSupportedException {
      return new SuperExpr(clone(classExpr));
   }

}
