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
import org.walkmod.javalang.ast.expr.VariableDeclarationExpr;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class ForeachStmt extends Statement {

   private VariableDeclarationExpr var;

   private Expression iterable;

   private Statement body;

   public ForeachStmt() {
   }

   public ForeachStmt(VariableDeclarationExpr var, Expression iterable, Statement body) {
      setVariable(var);
      setIterable(iterable);
      setBody(body);
   }

   public ForeachStmt(int beginLine, int beginColumn, int endLine, int endColumn, VariableDeclarationExpr var,
         Expression iterable, Statement body) {
      super(beginLine, beginColumn, endLine, endColumn);
      setVariable(var);
      setIterable(iterable);
      setBody(body);
   }

   @Override
   public List<Node> getChildren() {
      List<Node> children = super.getChildren();
      if (var != null) {
         children.add(var);
      }
      if (iterable != null) {
         children.add(iterable);
      }
      if (body != null) {
         children.add(body);
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

   public Statement getBody() {
      return body;
   }

   public Expression getIterable() {
      return iterable;
   }

   public VariableDeclarationExpr getVariable() {
      return var;
   }

   public void setBody(Statement body) {
      if (this.body != null) {
         updateReferences(this.body);
      }
      this.body = body;
      setAsParentNodeOf(body);
   }

   public void setIterable(Expression iterable) {
      if (this.iterable != null) {
         updateReferences(this.iterable);
      }
      this.iterable = iterable;
      setAsParentNodeOf(iterable);
   }

   public void setVariable(VariableDeclarationExpr var) {
      this.var = var;
      setAsParentNodeOf(var);
   }

   @Override
   public boolean replaceChildNode(Node oldChild, Node newChild) {
      boolean updated = false;
      if (oldChild == iterable) {
         setIterable((Expression) newChild);
         updated = true;
      }
      if (!updated) {
         if (oldChild == var) {
            setVariable((VariableDeclarationExpr) newChild);
            updated = true;
         }
         if (!updated) {
            if (oldChild == body) {
               setBody((Statement) newChild);
               updated = true;
            }
         }
      }
      return updated;
   }

   @Override
   public ForeachStmt clone() throws CloneNotSupportedException {
      return new ForeachStmt(clone(var), clone(iterable), clone(body));
   }
}
