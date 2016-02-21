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

import java.util.List;

import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.expr.Expression;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class WhileStmt extends Statement {

   private Expression condition;

   private Statement body;

   public WhileStmt() {
   }

   public WhileStmt(Expression condition, Statement body) {
      setCondition(condition);
      setBody(body);
   }

   public WhileStmt(int beginLine, int beginColumn, int endLine, int endColumn, Expression condition, Statement body) {
      super(beginLine, beginColumn, endLine, endColumn);
      setCondition(condition);
      setBody(body);
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
            if (body == child) {
               body = null;
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
      List<Node> children = super.getChildren();
      children.add(condition);
      children.add(body);
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

   public Statement getBody() {
      return body;
   }

   public Expression getCondition() {
      return condition;
   }

   public void setBody(Statement body) {
      if (this.body != null) {
         updateReferences(this.body);
      }
      this.body = body;
      setAsParentNodeOf(body);
   }

   public void setCondition(Expression condition) {
      if (this.condition != null) {
         updateReferences(this.condition);
      }
      this.condition = condition;
      setAsParentNodeOf(condition);
   }

   @Override
   public boolean replaceChildNode(Node oldChild, Node newChild) {
      boolean updated = false;
      if (oldChild == condition) {
         setCondition((Expression) newChild);
         updated = true;
      }

      if (!updated) {
         if (oldChild == body) {
            setBody((Statement) newChild);
            updated = true;
         }
      }
      return updated;
   }

   @Override
   public WhileStmt clone() throws CloneNotSupportedException {

      return new WhileStmt(clone(condition), clone(body));
   }
}
