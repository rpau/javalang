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
package org.walkmod.javalang.ast.expr;

import java.util.LinkedList;
import java.util.List;

import org.walkmod.javalang.ast.ConstructorSymbolData;
import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.SymbolDefinition;
import org.walkmod.javalang.ast.SymbolReference;
import org.walkmod.javalang.ast.body.BodyDeclaration;
import org.walkmod.javalang.ast.type.ClassOrInterfaceType;
import org.walkmod.javalang.ast.type.Type;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class ObjectCreationExpr extends Expression implements SymbolReference, SymbolDefinition {

   private Expression scope;

   private ClassOrInterfaceType type;

   private List<Type> typeArgs;

   private List<Expression> args;

   private List<BodyDeclaration> anonymousClassBody;

   private SymbolDefinition symbolDefinition;

   private List<SymbolReference> usages;

   private List<SymbolReference> bodyReferences;

   private int scopeLevel = 0;

   public ObjectCreationExpr() {
   }

   public ObjectCreationExpr(Expression scope, ClassOrInterfaceType type, List<Expression> args) {
      setScope(scope);
      setType(type);
      setArgs(args);
   }

   public ObjectCreationExpr(int beginLine, int beginColumn, int endLine, int endColumn, Expression scope,
         ClassOrInterfaceType type, List<Type> typeArgs, List<Expression> args, List<BodyDeclaration> anonymousBody) {
      super(beginLine, beginColumn, endLine, endColumn);
      setScope(scope);
      setType(type);
      setArgs(args);
      setTypeArgs(typeArgs);
      setAnonymousClassBody(anonymousBody);
   }

   @Override
   public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
      return v.visit(this, arg);
   }

   @Override
   public <A> void accept(VoidVisitor<A> v, A arg) {
      v.visit(this, arg);
   }

   public List<BodyDeclaration> getAnonymousClassBody() {
      return anonymousClassBody;
   }

   public List<Expression> getArgs() {
      return args;
   }

   public Expression getScope() {
      return scope;
   }

   public ClassOrInterfaceType getType() {
      return type;
   }

   public List<Type> getTypeArgs() {
      return typeArgs;
   }

   public void setAnonymousClassBody(List<BodyDeclaration> anonymousClassBody) {
      this.anonymousClassBody = anonymousClassBody;
      setAsParentNodeOf(anonymousClassBody);
   }

   public void setArgs(List<Expression> args) {
      this.args = args;
      setAsParentNodeOf(args);
   }

   public void setScope(Expression scope) {
      this.scope = scope;
      setAsParentNodeOf(scope);
   }

   public void setType(ClassOrInterfaceType type) {
      this.type = type;
      setAsParentNodeOf(type);
   }

   public void setTypeArgs(List<Type> typeArgs) {
      this.typeArgs = typeArgs;
      setAsParentNodeOf(typeArgs);
   }

   @Override
   public ConstructorSymbolData getSymbolData() {
      return (ConstructorSymbolData) super.getSymbolData();
   }

   @Override
   public SymbolDefinition getSymbolDefinition() {
      return symbolDefinition;
   }

   @Override
   public void setSymbolDefinition(SymbolDefinition symbolDefinition) {
      this.symbolDefinition = symbolDefinition;
   }

   public List<SymbolReference> getUsages() {
      return usages;
   }

   public List<SymbolReference> getBodyReferences() {
      return bodyReferences;
   }

   public void setUsages(List<SymbolReference> usages) {
      this.usages = usages;
   }

   public void setBodyReferences(List<SymbolReference> bodyReferences) {
      this.bodyReferences = bodyReferences;
   }

   @Override
   public boolean addBodyReference(SymbolReference bodyReference) {
      if (bodyReference != null) {
         SymbolDefinition definition = bodyReference.getSymbolDefinition();
         if (definition != null) {
            int scope = definition.getScopeLevel();
            if (scope <= scopeLevel) {
               if (bodyReferences == null) {
                  bodyReferences = new LinkedList<SymbolReference>();
               }
               return bodyReferences.add(bodyReference);
            }
         }
      }
      return false;
   }

   @Override
   public boolean addUsage(SymbolReference usage) {
      if (usage != null) {
         usage.setSymbolDefinition(this);
         if (usages == null) {
            usages = new LinkedList<SymbolReference>();
         }
         return usages.add(usage);
      }
      return false;

   }

   @Override
   public int getScopeLevel() {
      return scopeLevel;
   }

   @Override
   public void setScopeLevel(int scopeLevel) {
      this.scopeLevel = scopeLevel;
   }

   @Override
   public boolean replaceChildNode(Node oldChild, Node newChild) {
      boolean updated = false;

      if (oldChild == scope) {
         scope = (Expression) newChild;
         updated = true;
      }
      if (!updated) {

         if (oldChild == type) {
            type = (ClassOrInterfaceType) newChild;
            updated = true;
         }
         if (!updated) {
            updated = replaceChildNodeInList(oldChild, newChild, typeArgs);

            if (!updated) {
               updated = replaceChildNodeInList(oldChild, newChild, args);
            }
            if(!updated){
               updated = replaceChildNodeInList(oldChild, newChild, anonymousClassBody);
            }
         }
      }

      return updated;
   }
}
