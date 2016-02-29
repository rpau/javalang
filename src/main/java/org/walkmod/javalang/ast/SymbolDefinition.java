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

import java.util.List;

/**
 * Represents a node that is a symbol or a set of symbols. A symbol is an
 * identifiable node that can be referenced from other points of the code.
 * 
 * @author rpau
 *
 */
public interface SymbolDefinition extends ScopeAware{

   /**
    * Returns the references that represent usages to that definition.
    * 
    * @return the references that represent usages to that definition.
    */
   public List<SymbolReference> getUsages();

   /**
    * Sets the references that represent usages to that definition.
    * 
    * @param usages
    *            references that represent usages to that definition.
    */
   public void setUsages(List<SymbolReference> usages);

   /**
    * Adds a usage reference to this definition and updates the definition in
    * that reference.
    * 
    * @param usage
    *            reference to this definition.
    * @return if the usage has been added.
    */
   public boolean addUsage(SymbolReference usage);

   /**
    * Returns the external references contained inside the body of this
    * definition. External references are references for definitions of an
    * external scope.
    * 
    * @return references for definitions of an external scope.
    */
   public List<SymbolReference> getBodyReferences();

   /**
    * Sets the external references contained inside the body of this
    * definition.
    * 
    * @param bodyReferences
    *            external references contained inside the body of this
    *            definition.
    */
   public void setBodyReferences(List<SymbolReference> bodyReferences);

   /**
    * Adds a symbol reference to this definition. If the reference is about an
    * external definition (higher scope level), it will be added. Otherwise, it
    * will be omitted.
    * 
    * @param bodyReference
    *            the reference to add.
    * @return true if the body reference has been added. False, otherwise.
    */
   public boolean addBodyReference(SymbolReference bodyReference);

   /**
    * Returns the scope level where this definition is specified. It starts
    * from 0.
    * 
    * @return the scope level where this definition is specified.
    */
   public int getScopeLevel();

   /**
    * Sets the scope level where this definition is specified.
    * 
    * @param scopeLevel
    *            the scope level where this definition is specified.
    */
   public void setScopeLevel(int scopeLevel);
   
   /**
    * Gets the symbol name
    * @return the symbol name
    */
   public String getSymbolName();
   
}
