package org.walkmod.javalang.ast;

import java.lang.reflect.Constructor;

/**
 * This is the SymbolData used for representing Constructors.
 * @author rpau
 *
 */
public interface ConstructorSymbolData extends SymbolData{
	/**
	 * The related constructor signature
	 * @return the constructor signature
	 */
	public Constructor<?> getConstructor();
}
