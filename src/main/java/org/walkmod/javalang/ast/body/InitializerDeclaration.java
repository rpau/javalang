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
package org.walkmod.javalang.ast.body;

import java.util.Comparator;
import java.util.List;

import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.stmt.BlockStmt;
import org.walkmod.javalang.comparators.InitializerDeclarationComparator;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;
import org.walkmod.merger.MergeEngine;
import org.walkmod.merger.Mergeable;

/**
 * @author Julio Vilmar Gesser
 */
public final class InitializerDeclaration extends BodyDeclaration implements Mergeable<InitializerDeclaration> {

   private boolean isStatic;

   private BlockStmt block;

   public InitializerDeclaration() {
   }

   public InitializerDeclaration(boolean isStatic, BlockStmt block) {
      this.isStatic = isStatic;
      setBlock(block);
   }

   public InitializerDeclaration(JavadocComment javaDoc, boolean isStatic, BlockStmt block) {
      super(null, javaDoc);
      this.isStatic = isStatic;
      setBlock(block);
   }

   public InitializerDeclaration(int beginLine, int beginColumn, int endLine, int endColumn, JavadocComment javaDoc,
         boolean isStatic, BlockStmt block) {
      super(beginLine, beginColumn, endLine, endColumn, null, javaDoc);
      this.isStatic = isStatic;
      setBlock(block);
   }

   @Override
   public boolean removeChild(Node child) {
      boolean result = false;
      if (child != null) {
         result = super.removeChild(child);
         if (!result) {
            if (child == block && block != null) {
               block = null;
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
      if (block != null) {
         children.add(block);
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

   public BlockStmt getBlock() {
      return block;
   }

   public boolean isStatic() {
      return isStatic;
   }

   public void setBlock(BlockStmt block) {
      if (this.block != null) {
         updateReferences(this.block);
      }
      this.block = block;
      setAsParentNodeOf(block);
   }

   public void setStatic(boolean isStatic) {
      this.isStatic = isStatic;
   }

   @Override
   public Comparator<?> getIdentityComparator() {
      return new InitializerDeclarationComparator();
   }

   @Override
   public void merge(InitializerDeclaration remote, MergeEngine configuration) {
      super.merge(remote, configuration);
      setBlock((BlockStmt) configuration.apply(getBlock(), remote.getBlock(), BlockStmt.class));

   }

   @Override
   public boolean replaceChildNode(Node oldChild, Node newChild) {
      boolean update = super.replaceChildNode(oldChild, newChild);
      if (!update) {
         if (oldChild == block) {
            setBlock((BlockStmt) newChild);
            update = true;
         }
      }
      return update;
   }

   @Override
   public InitializerDeclaration clone() throws CloneNotSupportedException {
      return new InitializerDeclaration(clone(getJavaDoc()), isStatic, clone(getBlock()));
   }

}
