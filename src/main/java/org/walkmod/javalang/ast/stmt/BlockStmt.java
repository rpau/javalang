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

import java.util.LinkedList;
import java.util.List;

import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class BlockStmt extends Statement {

   private List<Statement> stmts;

   public BlockStmt() {
   }

   public BlockStmt(List<Statement> stmts) {
      setStmts(stmts);
   }

   public BlockStmt(int beginLine, int beginColumn, int endLine, int endColumn, List<Statement> stmts) {
      super(beginLine, beginColumn, endLine, endColumn);
      setStmts(stmts);
   }

   @Override
   public List<Node> getChildren() {
      List<Node> children = new LinkedList<Node>();
      if (stmts != null) {
         children.addAll(stmts);
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

   public List<Statement> getStmts() {
      return stmts;
   }

   public void setStmts(List<Statement> stmts) {
      this.stmts = stmts;
      setAsParentNodeOf(stmts);
   }

   @Override
   public boolean replaceChildNode(Node oldChild, Node newChild) {
      return replaceChildNodeInList(oldChild, newChild, stmts);
   }

   @Override
   public BlockStmt clone() throws CloneNotSupportedException {
      return new BlockStmt(clone(stmts));
   }
}
