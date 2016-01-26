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
public final class ForStmt extends Statement {

   private List<Expression> init;

   private Expression compare;

   private List<Expression> update;

   private Statement body;

   public ForStmt() {
   }

   public ForStmt(List<Expression> init, Expression compare, List<Expression> update, Statement body) {
      setCompare(compare);
      setInit(init);
      setUpdate(update);
      setBody(body);
   }

   public ForStmt(int beginLine, int beginColumn, int endLine, int endColumn, List<Expression> init, Expression compare,
         List<Expression> update, Statement body) {
      super(beginLine, beginColumn, endLine, endColumn);
      setCompare(compare);
      setInit(init);
      setUpdate(update);
      setBody(body);
   }

   @Override
   public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
      return v.visit(this, arg);
   }

   @Override
   public <A> void accept(VoidVisitor<A> v, A arg) {
      v.visit(this, arg);
   }

   public Statement getBody() {
      return body;
   }

   public Expression getCompare() {
      return compare;
   }

   public List<Expression> getInit() {
      return init;
   }

   public List<Expression> getUpdate() {
      return update;
   }

   public void setBody(Statement body) {
      this.body = body;
      setAsParentNodeOf(body);
   }

   public void setCompare(Expression compare) {
      this.compare = compare;
      setAsParentNodeOf(compare);
   }

   public void setInit(List<Expression> init) {
      this.init = init;
      setAsParentNodeOf(init);
   }

   public void setUpdate(List<Expression> update) {
      this.update = update;
      setAsParentNodeOf(update);
   }

   @Override
   public boolean replaceChildNode(Node oldChild, Node newChild) {
      boolean updated = false;
      if (oldChild == compare) {
         compare = (Expression) newChild;
         updated = true;
      }
      if (!updated) {
         if (oldChild == body) {
            body = (Statement) newChild;
            updated = true;
         }
         if (!updated) {
            updated = replaceChildNodeInList(oldChild, newChild, init);

            if (!updated) {
               updated = replaceChildNodeInList(oldChild, newChild, update);
            }
         }
      }
      return updated;
   }
}
