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
import org.walkmod.javalang.ast.type.Type;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class CastExpr extends Expression {

   private Type type;

   private Expression expr;

   public CastExpr() {
   }

   public CastExpr(Type type, Expression expr) {
      setType(type);
      setExpr(expr);
   }

   public CastExpr(int beginLine, int beginColumn, int endLine, int endColumn, Type type, Expression expr) {
      super(beginLine, beginColumn, endLine, endColumn);
      setType(type);
      setExpr(expr);
   }

   @Override
   public List<Node> getChildren() {
      List<Node> children = new LinkedList<Node>();
      if (type != null) {
         children.add(type);
      }
      if (expr != null) {
         children.add(expr);
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

   public Expression getExpr() {
      return expr;
   }

   public Type getType() {
      return type;
   }

   public void setExpr(Expression expr) {
      if (this.expr != null) {
         updateReferences(this.expr);
      }
      this.expr = expr;
      setAsParentNodeOf(expr);
   }

   public void setType(Type type) {
      if (this.type != null) {
         updateReferences(this.type);
      }
      this.type = type;
      setAsParentNodeOf(type);
   }

   @Override
   public boolean replaceChildNode(Node oldChild, Node newChild) {
      boolean updated = false;
      if (type == oldChild) {
         setType((Type) newChild);
         updated = true;
      }
      if (!updated) {
         if (expr == oldChild) {
            setExpr((Expression) newChild);
            updated = true;
         }
      }
      return updated;
   }

   @Override
   public CastExpr clone() throws CloneNotSupportedException {

      return new CastExpr(clone(getType()), clone(getExpr()));
   }

}
