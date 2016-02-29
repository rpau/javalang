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
import java.util.Map;

public interface ScopeAware {

   /**
    * Gets the available variables
    * @return the available variables
    */
   public Map<String, SymbolDefinition> getVariableDefinitions();
   
   
   /**
    * Gets the available variables
    * @return the available variables
    */
   public Map<String, SymbolDefinition> getTypeDefinitions();
   
   
   
   /**
    * Gets the available methods
    * @return the available variables
    */
   public Map<String, List<SymbolDefinition>> getMethodDefinitions();
}
