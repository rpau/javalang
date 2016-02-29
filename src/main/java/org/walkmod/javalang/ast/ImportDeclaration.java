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
package org.walkmod.javalang.ast;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.walkmod.javalang.ast.body.TypeDeclaration;
import org.walkmod.javalang.ast.expr.NameExpr;
import org.walkmod.javalang.comparators.ImportDeclarationComparator;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;
import org.walkmod.merger.MergeEngine;
import org.walkmod.merger.Mergeable;

/**
 * <p>
 * This class represents a import declaration. Imports are optional for the {@link CompilationUnit}.
 * </p>
 * The ImportDeclaration is constructed following the syntax:<br>
 * <code>
 * <table>
 * <tr valign=baseline>
 *   <td align=right>ImportDeclaration</td>
 *   <td align=center>::=</td>
 *   <td align=left>
 *       "import" ( "static" )? {@link NameExpr} ( "." "*" )? ";"
 *   </td>
 * </tr>
 * </table> 
 * </code>
 * 
 * @author Julio Vilmar Gesser
 */
public final class ImportDeclaration extends Node implements Mergeable<ImportDeclaration>, SymbolDefinition {

   private NameExpr name;

   private boolean static_;

   private boolean asterisk;

   private List<SymbolReference> usages;

   public ImportDeclaration() {
   }

   public ImportDeclaration(NameExpr name, boolean isStatic, boolean isAsterisk) {
      setName(name);
      this.static_ = isStatic;
      this.asterisk = isAsterisk;
   }

   public ImportDeclaration(int beginLine, int beginColumn, int endLine, int endColumn, NameExpr name, boolean isStatic,
         boolean isAsterisk) {
      super(beginLine, beginColumn, endLine, endColumn);
      setName(name);
      this.static_ = isStatic;
      this.asterisk = isAsterisk;
   }

   @Override
   public boolean removeChild(Node child) {
      if (child == name && child != null) {
         name = null;
         updateReferences(child);
         return true;
      }

      return false;
   }

   @Override
   public List<Node> getChildren() {
      List<Node> aux = new LinkedList<Node>();
      if (name != null) {
         aux.add(name);
      }
      return aux;
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

   /**
    * Retrieves the name of the import.
    * 
    * @return the name of the import
    */
   public NameExpr getName() {
      return name;
   }

   /**
    * Return if the import ends with "*".
    * 
    * @return <code>true</code> if the import ends with "*", <code>false</code> otherwise
    */
   public boolean isAsterisk() {
      return asterisk;
   }

   /**
    * Return if the import is static.
    * 
    * @return <code>true</code> if the import is static, <code>false</code> otherwise
    */
   public boolean isStatic() {
      return static_;
   }

   /**
    * Sets if this import is asterisk.
    * 
    * @param asterisk
    *           <code>true</code> if this import is asterisk
    */
   public void setAsterisk(boolean asterisk) {
      this.asterisk = asterisk;
   }

   /**
    * Sets the name this import.
    * 
    * @param name
    *           the name to set
    */
   public void setName(NameExpr name) {
      if (this.name != null) {
         updateReferences(this.name);
      }
      this.name = name;
      setAsParentNodeOf(name);
   }

   /**
    * Sets if this import is static.
    * 
    * @param static_
    *           <code>true</code> if this import is static
    */
   public void setStatic(boolean static_) {
      this.static_ = static_;
   }

   @Override
   public Comparator<?> getIdentityComparator() {
      return new ImportDeclarationComparator();
   }

   @Override
   public void merge(ImportDeclaration t1, MergeEngine configuration) {
      if (t1.isStatic()) {
         setStatic(t1.isAsterisk());
      }
      if (t1.isAsterisk()) {
         setAsterisk(t1.isAsterisk());
      }
   }

   @Override
   public List<SymbolReference> getUsages() {
      return usages;
   }

   @Override
   public void setUsages(List<SymbolReference> usages) {
      this.usages = usages;
   }

   @Override
   public List<SymbolReference> getBodyReferences() {
      return null;
   }

   @Override
   public void setBodyReferences(List<SymbolReference> bodyReferences) {
   }

   @Override
   public int getScopeLevel() {
      return 0;
   }

   @Override
   public void setScopeLevel(int scopeLevel) {
   }

   @Override
   public boolean addBodyReference(SymbolReference bodyReference) {
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
   public boolean replaceChildNode(Node oldChild, Node newChild) {
      if (oldChild == name) {
         setName((NameExpr) newChild);
         return true;
      }
      return false;
   }

   @Override
   public ImportDeclaration clone() throws CloneNotSupportedException {
      return new ImportDeclaration(clone(name), static_, asterisk);
   }
   
   @Override
   public Map<String, SymbolDefinition> getVariableDefinitions(){
      return new HashMap<String, SymbolDefinition>();
   }
   
   @Override
   public Map<String, List<SymbolDefinition>> getMethodDefinitions(){
      return new HashMap<String, List<SymbolDefinition>>();
   }

   @Override
   public Map<String, SymbolDefinition> getTypeDefinitions() {
      Map<String, SymbolDefinition> result = new HashMap<String, SymbolDefinition>();
      List<Node> children = getParentNode().getChildren();
      for (Node child : children) {

         if (child instanceof ImportDeclaration) {
            ImportDeclaration id = (ImportDeclaration) child;
            if (id.isAsterisk()) {
               List<SymbolReference> usages = id.getUsages();
               if (usages != null) {
                  for (SymbolReference usage : usages) {
                     if (usage instanceof SymbolDataAware) {
                        SymbolDataAware<?> sda = (SymbolDataAware) usage;
                        result.put(sda.getSymbolData().getName(), id);
                     }
                  }
               }
            } else {
               result.put(getSymbolName(), id);
            }
         } else if (child instanceof TypeDeclaration) {
            TypeDeclaration td = (TypeDeclaration) child;
            result.put(td.getName(), td);
         }
      }
      return result;
   }

   @Override
   public String getSymbolName() {
      return name.getName();
   }

}
