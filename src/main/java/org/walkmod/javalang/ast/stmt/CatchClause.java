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
import org.walkmod.javalang.ast.body.MultiTypeParameter;
import org.walkmod.javalang.ast.body.VariableDeclaratorId;
import org.walkmod.javalang.ast.expr.AnnotationExpr;
import org.walkmod.javalang.ast.type.Type;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class CatchClause extends Node {

   private MultiTypeParameter except;

   private BlockStmt catchBlock;

   public CatchClause() {
   }

   public CatchClause(final MultiTypeParameter except, final BlockStmt catchBlock) {
      setExcept(except);
      setCatchBlock(catchBlock);
   }

   public CatchClause(int exceptModifier, List<AnnotationExpr> exceptAnnotations, List<Type> exceptTypes,
         VariableDeclaratorId exceptId, BlockStmt catchBlock) {
      this(new MultiTypeParameter(exceptModifier, exceptAnnotations, exceptTypes, exceptId), catchBlock);
   }

   public CatchClause(final int beginLine, final int beginColumn, final int endLine, final int endColumn,
         final int exceptModifier, final List<AnnotationExpr> exceptAnnotations, final List<Type> exceptTypes,
         final VariableDeclaratorId exceptId, final BlockStmt catchBlock) {
      super(beginLine, beginColumn, endLine, endColumn);
      setExcept(new MultiTypeParameter(beginLine, beginColumn, endLine, endColumn, exceptModifier, exceptAnnotations,
            exceptTypes, exceptId));
      setCatchBlock(catchBlock);
   }

   @Override
   public boolean removeChild(Node child) {
      boolean result = false;
      if (child != null) {
         if (except == child) {
            except = null;
            result = true;
         }

         if (!result) {
            if (catchBlock == child) {
               catchBlock = null;
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
      if (except != null) {
         children.add(except);
      }
      if (catchBlock != null) {
         children.add(catchBlock);
      }
      return children;
   }

   @Override
   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      if (!check()) {
         return null;
      }
      return v.visit(this, arg);
   }

   @Override
   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      if (check()) {
         v.visit(this, arg);
      }
   }

   public BlockStmt getCatchBlock() {
      return catchBlock;
   }

   public MultiTypeParameter getExcept() {
      return except;
   }

   public void setCatchBlock(final BlockStmt catchBlock) {
      if (this.catchBlock != null) {
         updateReferences(this.catchBlock);
      }
      this.catchBlock = catchBlock;
      setAsParentNodeOf(catchBlock);
   }

   public void setExcept(final MultiTypeParameter except) {
      if (this.except != null) {
         updateReferences(this.except);
      }
      this.except = except;
      setAsParentNodeOf(except);
   }

   @Override
   public boolean replaceChildNode(Node oldChild, Node newChild) {
      boolean updated = false;
      if (oldChild == except) {
         setExcept((MultiTypeParameter) newChild);
         updated = true;
      }
      if (!updated) {
         if (oldChild == catchBlock) {
            setCatchBlock((BlockStmt) newChild);
            updated = true;
         }
      }
      return updated;
   }

   @Override
   public CatchClause clone() throws CloneNotSupportedException {
      return new CatchClause(clone(except), clone(catchBlock));
   }
}
