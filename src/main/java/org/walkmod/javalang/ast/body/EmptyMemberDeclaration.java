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

import org.walkmod.javalang.comparators.EmptyMemberDeclarationComparator;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;
import org.walkmod.merger.MergeEngine;
import org.walkmod.merger.Mergeable;

/**
 * @author Julio Vilmar Gesser
 */
public final class EmptyMemberDeclaration extends BodyDeclaration implements Mergeable<EmptyMemberDeclaration> {

   public EmptyMemberDeclaration() {
   }

   public EmptyMemberDeclaration(JavadocComment javaDoc) {
      super(null, javaDoc);
   }

   public EmptyMemberDeclaration(int beginLine, int beginColumn, int endLine, int endColumn, JavadocComment javaDoc) {
      super(beginLine, beginColumn, endLine, endColumn, null, javaDoc);
   }

   @Override
   public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
      return v.visit(this, arg);
   }

   @Override
   public <A> void accept(VoidVisitor<A> v, A arg) {
      v.visit(this, arg);
   }

   @Override
   public Comparator<?> getIdentityComparator() {

      return new EmptyMemberDeclarationComparator();
   }

   @Override
   public void merge(EmptyMemberDeclaration t1, MergeEngine configuration) {
      super.merge(t1, configuration);
   }

   @Override
   public EmptyMemberDeclaration clone() throws CloneNotSupportedException {
      return new EmptyMemberDeclaration();
   }
}
