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
public final class ConditionalExpr extends Expression {

   private Expression condition;

   private Expression thenExpr;

   private Expression elseExpr;

   public ConditionalExpr() {
   }

   public ConditionalExpr(Expression condition, Expression thenExpr, Expression elseExpr) {
      setCondition(condition);
      setThenExpr(thenExpr);
      setElseExpr(elseExpr);
   }

   public ConditionalExpr(int beginLine, int beginColumn, int endLine, int endColumn, Expression condition,
         Expression thenExpr, Expression elseExpr) {
      super(beginLine, beginColumn, endLine, endColumn);
      setCondition(condition);
      setThenExpr(thenExpr);
      setElseExpr(elseExpr);
   }

   @Override
   public boolean removeChild(Node child) {
      boolean result = false;
      if (child != null) {
         if (condition == child) {
            condition = null;
            result = true;
         }
         if (!result) {
            if (thenExpr == child) {
               thenExpr = null;
               result = true;
            }
         }
         if (!result) {
            if (elseExpr == child) {
               elseExpr = null;
               result = true;
            }
         }
      }
      if(result){
         updateReferences(child);
      }
      return result;
   }

   @Override
   public List<Node> getChildren() {
      List<Node> children = new LinkedList<Node>();
      if (condition != null) {
         children.add(condition);
      }
      if (thenExpr != null) {
         children.add(thenExpr);
      }
      if (elseExpr != null) {
         children.add(elseExpr);
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

   public Expression getCondition() {
      return condition;
   }

   public Expression getElseExpr() {
      return elseExpr;
   }

   public Expression getThenExpr() {
      return thenExpr;
   }

   public void setCondition(Expression condition) {
      if (this.condition != null) {
         updateReferences(this.condition);
      }
      this.condition = condition;
      setAsParentNodeOf(condition);
   }

   public void setElseExpr(Expression elseExpr) {
      if (this.elseExpr != null) {
         updateReferences(this.elseExpr);
      }
      this.elseExpr = elseExpr;
      setAsParentNodeOf(elseExpr);
   }

   public void setThenExpr(Expression thenExpr) {
      if (this.thenExpr != null) {
         updateReferences(this.thenExpr);
      }
      this.thenExpr = thenExpr;
      setAsParentNodeOf(thenExpr);
   }

   @Override
   public boolean replaceChildNode(Node oldChild, Node newChild) {
      boolean updated = false;
      if (condition == oldChild) {
         setCondition((Expression) newChild);
         updated = true;
      }
      if (!updated) {
         if (thenExpr == oldChild) {
            setThenExpr((Expression) newChild);
            updated = true;
         }
         if (!updated) {
            if (elseExpr == oldChild) {
               setElseExpr((Expression) newChild);
               updated = true;
            }
         }
      }
      return updated;
   }

   @Override
   public ConditionalExpr clone() throws CloneNotSupportedException {

      return new ConditionalExpr(clone(getCondition()), clone(getThenExpr()), clone(getElseExpr()));
   }

}
