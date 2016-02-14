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
public final class ArrayAccessExpr extends Expression {

   private Expression name;

   private Expression index;

   public ArrayAccessExpr() {
   }

   public ArrayAccessExpr(Expression name, Expression index) {
      setName(name);
      setIndex(index);
   }

   public ArrayAccessExpr(int beginLine, int beginColumn, int endLine, int endColumn, Expression name,
         Expression index) {
      super(beginLine, beginColumn, endLine, endColumn);
      setName(name);
      setIndex(index);
   }

   @Override
   public List<Node> getChildren() {
      List<Node> children = new LinkedList<Node>();
      if (name != null) {
         children.add(name);
      }
      if (index != null) {
         children.add(index);
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

   public Expression getIndex() {
      return index;
   }

   public Expression getName() {
      return name;
   }

   public void setIndex(Expression index) {
      if (this.index != null) {
         updateReferences(this.index);
      }
      this.index = index;
      setAsParentNodeOf(index);
   }

   public void setName(Expression name) {
      if (this.name != null) {
         updateReferences(this.name);
      }
      this.name = name;
      setAsParentNodeOf(name);
   }

   @Override
   public boolean replaceChildNode(Node oldChild, Node newChild) {
      boolean updated = false;
      if (index == oldChild) {
         setIndex((Expression) newChild);
         updated = true;
      }
      if (!updated) {
         if (name == oldChild) {
            setName((Expression) newChild);
         }
      }
      return updated;
   }

   @Override
   public ArrayAccessExpr clone() throws CloneNotSupportedException {
      return new ArrayAccessExpr(clone(name), clone(index));
   }
}
