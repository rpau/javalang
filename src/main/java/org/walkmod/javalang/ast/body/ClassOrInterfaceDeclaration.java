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
import java.util.Map;

import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.SymbolDefinition;
import org.walkmod.javalang.ast.TypeParameter;
import org.walkmod.javalang.ast.expr.AnnotationExpr;
import org.walkmod.javalang.ast.type.ClassOrInterfaceType;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;
import org.walkmod.merger.MergeEngine;

/**
 * @author Julio Vilmar Gesser
 */
public final class ClassOrInterfaceDeclaration extends TypeDeclaration {

   private boolean interface_;

   private List<TypeParameter> typeParameters;

   private List<ClassOrInterfaceType> extendsList;

   private List<ClassOrInterfaceType> implementsList;

   public ClassOrInterfaceDeclaration() {
   }

   public ClassOrInterfaceDeclaration(int modifiers, boolean isInterface, String name) {
      this.interface_ = isInterface;
      setModifiers(modifiers);
      setName(name);
   }

   public ClassOrInterfaceDeclaration(JavadocComment javaDoc, int modifiers, List<AnnotationExpr> annotations,
         boolean isInterface, String name, List<TypeParameter> typeParameters, List<ClassOrInterfaceType> extendsList,
         List<ClassOrInterfaceType> implementsList, List<BodyDeclaration> members) {
      this.interface_ = isInterface;
      setModifiers(modifiers);
      setName(name);
      setTypeParameters(typeParameters);
      setExtends(extendsList);
      setImplements(implementsList);
      setJavaDoc(javaDoc);
      setMembers(members);
   }

   public ClassOrInterfaceDeclaration(int beginLine, int beginColumn, int endLine, int endColumn,
         JavadocComment javaDoc, int modifiers, List<AnnotationExpr> annotations, boolean isInterface, String name,
         List<TypeParameter> typeParameters, List<ClassOrInterfaceType> extendsList,
         List<ClassOrInterfaceType> implementsList, List<BodyDeclaration> members) {
      super(beginLine, beginColumn, endLine, endColumn, annotations, javaDoc, modifiers, name, members);
      this.interface_ = isInterface;
      setTypeParameters(typeParameters);
      setExtends(extendsList);
      setImplements(implementsList);
   }

   @Override
   public boolean removeChild(Node child) {
      boolean result = false;
      if (child != null) {
         result = super.removeChild(child);
         if (!result) {
            if (child instanceof TypeParameter) {
               if (typeParameters != null) {
                  List<TypeParameter> tp = new LinkedList<TypeParameter>(typeParameters);
                  result = tp.remove(child);
                  typeParameters = tp;
               }
            } else if (child instanceof ClassOrInterfaceType) {
               if (extendsList != null) {
                  List<ClassOrInterfaceType> extendsListAux = new LinkedList<ClassOrInterfaceType>(extendsList);
                  result = extendsListAux.remove(child);
                  extendsList = extendsListAux;
               }
               if (!result && implementsList != null) {
                  List<ClassOrInterfaceType> implementsListAux = new LinkedList<ClassOrInterfaceType>(implementsList);
                  result = implementsListAux.remove(child);
                  implementsList = implementsListAux;
               }
            }
         }
      }
      if (result) {
         updateReferences(child);
      }
      return result;
   }

   @Override
   public List<Node> getChildren() {
      List<Node> children = super.getChildren();
      if (typeParameters != null) {
         children.addAll(typeParameters);
      }
      if (extendsList != null) {
         children.addAll(extendsList);
      }
      if (implementsList != null) {
         children.addAll(implementsList);
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

   public List<ClassOrInterfaceType> getExtends() {
      return extendsList;
   }

   public List<ClassOrInterfaceType> getImplements() {
      return implementsList;
   }

   public List<TypeParameter> getTypeParameters() {
      return typeParameters;
   }

   public boolean isInterface() {
      return interface_;
   }

   public void setExtends(List<ClassOrInterfaceType> extendsList) {
      this.extendsList = extendsList;
      setAsParentNodeOf(extendsList);
   }

   public void setImplements(List<ClassOrInterfaceType> implementsList) {
      this.implementsList = implementsList;
      setAsParentNodeOf(implementsList);
   }

   public void setInterface(boolean interface_) {
      this.interface_ = interface_;
   }

   public void setTypeParameters(List<TypeParameter> typeParameters) {
      this.typeParameters = typeParameters;
      setAsParentNodeOf(typeParameters);
   }

   @Override
   public void merge(TypeDeclaration remoteTypeDeclaration, MergeEngine configuration) {
      super.merge(remoteTypeDeclaration, configuration);
      List<TypeParameter> typeParametersList = new LinkedList<TypeParameter>();
      configuration.apply(getTypeParameters(),
            ((ClassOrInterfaceDeclaration) remoteTypeDeclaration).getTypeParameters(), typeParametersList,
            TypeParameter.class);
      if (!typeParametersList.isEmpty()) {
         setTypeParameters(typeParametersList);
      } else {
         setTypeParameters(null);
      }
      List<ClassOrInterfaceType> implementsList = new LinkedList<ClassOrInterfaceType>();
      configuration.apply(getImplements(), ((ClassOrInterfaceDeclaration) remoteTypeDeclaration).getImplements(),
            implementsList, ClassOrInterfaceType.class);
      if (!implementsList.isEmpty()) {
         setImplements(implementsList);
      } else {
         setImplements(null);
      }
      List<ClassOrInterfaceType> extendsList = new LinkedList<ClassOrInterfaceType>();
      configuration.apply(getExtends(), ((ClassOrInterfaceDeclaration) remoteTypeDeclaration).getExtends(), extendsList,
            ClassOrInterfaceType.class);
      if (!extendsList.isEmpty()) {
         setExtends(extendsList);
      } else {
         setExtends(null);
      }
   }

   @Override
   public boolean replaceChildNode(Node oldChild, Node newChild) {
      boolean update = super.replaceChildNode(oldChild, newChild);
      if (!update) {
         update = replaceChildNodeInList(oldChild, newChild, extendsList);
         if (!update) {
            update = replaceChildNodeInList(oldChild, newChild, implementsList);
            if (!update) {
               update = replaceChildNodeInList(oldChild, newChild, typeParameters);
            }
         }
      }
      return update;
   }

   @Override
   public ClassOrInterfaceDeclaration clone() throws CloneNotSupportedException {
      return new ClassOrInterfaceDeclaration(clone(getJavaDoc()), getModifiers(), clone(getAnnotations()), interface_,
            getName(), clone(getTypeParameters()), clone(getExtends()), clone(getImplements()), clone(getMembers()));
   }
   
   @Override
   public Map<String, SymbolDefinition> getTypeDefinitions() {
      Map<String, SymbolDefinition> result = super.getTypeDefinitions();
      if(typeParameters != null){
         for(TypeParameter tp: typeParameters){
            result.put(tp.getSymbolName(), tp);
         }
      }
      return result;
   }

}
