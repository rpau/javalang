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
package org.walkmod.javalang.ast.stmt;

import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.expr.Expression;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class IfStmt extends Statement {

   private Expression condition;

   private Statement thenStmt;

   private Statement elseStmt;

   public IfStmt() {
   }

   public IfStmt(Expression condition, Statement thenStmt, Statement elseStmt) {
      setCondition(condition);
      setThenStmt(thenStmt);
      setElseStmt(elseStmt);
   }

   public IfStmt(int beginLine, int beginColumn, int endLine, int endColumn, Expression condition, Statement thenStmt,
         Statement elseStmt) {
      super(beginLine, beginColumn, endLine, endColumn);
      setCondition(condition);
      setThenStmt(thenStmt);
      setElseStmt(elseStmt);
   }

   @Override
   public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
      return v.visit(this, arg);
   }

   @Override
   public <A> void accept(VoidVisitor<A> v, A arg) {
      v.visit(this, arg);
   }

   public Expression getCondition() {
      return condition;
   }

   public Statement getElseStmt() {
      return elseStmt;
   }

   public Statement getThenStmt() {
      return thenStmt;
   }

   public void setCondition(Expression condition) {
      if (this.condition != null) {
         updateReferences(this.condition);
      }
      this.condition = condition;
      setAsParentNodeOf(condition);
   }

   public void setElseStmt(Statement elseStmt) {
      if (this.elseStmt != null) {
         updateReferences(this.elseStmt);
      }
      this.elseStmt = elseStmt;
      setAsParentNodeOf(elseStmt);
   }

   public void setThenStmt(Statement thenStmt) {
      if (this.thenStmt != null) {
         updateReferences(this.thenStmt);
      }
      this.thenStmt = thenStmt;
      setAsParentNodeOf(thenStmt);
   }

   @Override
   public boolean replaceChildNode(Node oldChild, Node newChild) {
      boolean updated = false;
      if (oldChild == condition) {
         setCondition((Expression) newChild);
         updated = true;
      }
      if (!updated) {
         if (oldChild == thenStmt) {
            setThenStmt((Statement) newChild);
            updated = true;
         }
         if (!updated) {
            if (oldChild == elseStmt) {
               setElseStmt((Statement) newChild);
               updated = true;
            }
         }
      }
      return updated;
   }

   @Override
   public IfStmt clone() throws CloneNotSupportedException {
      return new IfStmt(clone(condition), clone(thenStmt), clone(elseStmt));
   }
}
