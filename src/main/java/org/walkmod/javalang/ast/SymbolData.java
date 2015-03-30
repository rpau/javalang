/* 
  Copyright (C) 2015 Raquel Pau and Albert Coroleu.
 
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
 * This class represents the equivalent byte code data referenced by the node.
 * The byte code data could be a class, a field or a method.
 * 
 * @author rpau
 *
 */
public interface SymbolData {

	/**
	 * Full name of the type represented by the node. In the case 
	 * of representing a Generics type, like @code{<A extends Map>},
	 * it contains the name of the variable. 
	 * 
	 * @return Full name of the type
	 */
	public String getName();

	/**
	 * The class referenced by the node. In the case of simple type references, this is 
	 * the class corresponding to an specific enumeration, annotation or type.
	 *  
	 * In case of field references, method calls, field declaration or 
	 * method declaration, represents the class of the field or the returning 
	 * type of the method. 
	 * 
	 *  In case of a Generics variable, it corresponds to the defined bounds or the
	 *  @ref{java.lang.Object Object} class.
	 *  
	 * @return the referenced class
	 */
	public Class<?> getClazz();

	/**
	 * Java Generics allows expressions like <? extends A & B & C>. These boundary 
	 * classes corresponds to A, B and C. When there are not multiple classes 
	 * it returns the same than @link{#getClass() getClass}. 
	 * @return the bound classes
	 */
	public List<Class<?>> getBoundClasses();

	/**
	 * Returns in case of arrays, it returns the dimensions.
	 * @return the dimensions of the array type.
	 */
	public int getArrayCount();

	/**
	 * Returns if it is a template variable used in a Java Generics expression.
	 * @return if it is a generics variable.
	 */
	public boolean isTemplateVariable();

	/**
	 * Returns the parametrized types of a generics expression. For example:
	 * if the referenced type is List<String>, it corresponds to String.
	 * @return
	 */
	public <T extends SymbolData> List<T> getParameterizedTypes();
	
	
	/**
	 * Merges to symbol data types calculating the common type hierarchy. This 
	 * interface has been useful to calculate the result type of a lambda expression.
	 * 
	 * @param other another symbol data.
	 * @return a symbol data resulting of merging two symbol datas.
	 */
	public SymbolData merge(SymbolData other);
}
