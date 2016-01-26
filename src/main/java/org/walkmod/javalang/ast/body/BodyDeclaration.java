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

import java.util.LinkedList;
import java.util.List;

import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.expr.AnnotationExpr;
import org.walkmod.merger.MergeEngine;

/**
 * @author Julio Vilmar Gesser
 */
public abstract class BodyDeclaration extends Node {

   private JavadocComment javaDoc;

   private List<AnnotationExpr> annotations;

   public BodyDeclaration() {
   }

   public BodyDeclaration(List<AnnotationExpr> annotations, JavadocComment javaDoc) {
      setJavaDoc(javaDoc);
      setAnnotations(annotations);
   }

   public BodyDeclaration(int beginLine, int beginColumn, int endLine, int endColumn, List<AnnotationExpr> annotations,
         JavadocComment javaDoc) {
      super(beginLine, beginColumn, endLine, endColumn);
      setJavaDoc(javaDoc);
      setAnnotations(annotations);
   }

   public final JavadocComment getJavaDoc() {
      return javaDoc;
   }

   public final List<AnnotationExpr> getAnnotations() {
      return annotations;
   }

   public final void setJavaDoc(JavadocComment javaDoc) {
      this.javaDoc = javaDoc;
      setAsParentNodeOf(javaDoc);
   }

   public final void setAnnotations(List<AnnotationExpr> annotations) {
      this.annotations = annotations;
      setAsParentNodeOf(annotations);
   }

   public void merge(BodyDeclaration remoteBodyDeclaration, MergeEngine configuration) {
      List<AnnotationExpr> resultAnnotations = new LinkedList<AnnotationExpr>();
      configuration.apply(getAnnotations(), remoteBodyDeclaration.getAnnotations(), resultAnnotations,
            AnnotationExpr.class);
      setAnnotations(resultAnnotations);

      setJavaDoc((JavadocComment) (configuration.apply(getJavaDoc(), remoteBodyDeclaration.getJavaDoc(),
            JavadocComment.class)));
   }

   @Override
   public boolean replaceChildNode(Node oldChild, Node newChild) {
      return replaceChildNodeInList(oldChild, newChild, annotations);
   }
}
