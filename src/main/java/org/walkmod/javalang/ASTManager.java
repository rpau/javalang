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

package org.walkmod.javalang;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.walkmod.javalang.ast.CompilationUnit;
import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.body.BodyDeclaration;
import org.walkmod.javalang.ast.body.InitializerDeclaration;
import org.walkmod.javalang.ast.expr.Expression;
import org.walkmod.javalang.ast.expr.NameExpr;
import org.walkmod.javalang.ast.expr.ThisExpr;
import org.walkmod.javalang.ast.stmt.BlockStmt;
import org.walkmod.javalang.ast.stmt.Statement;
import org.walkmod.javalang.ast.type.PrimitiveType;
import org.walkmod.javalang.ast.type.PrimitiveType.Primitive;
import org.walkmod.javalang.ast.type.Type;
import org.walkmod.javalang.ast.type.VoidType;


/**
 * <p>
 * Facade class to parse Java source files.
 * </p>
 * 
 * @author Raquel Pau
 * @author Albert Coroleu
 */
public class ASTManager {

	public static final PrimitiveType BYTE_TYPE = new PrimitiveType(
			Primitive.Byte);

	public static final PrimitiveType SHORT_TYPE = new PrimitiveType(
			Primitive.Short);

	public static final PrimitiveType INT_TYPE = new PrimitiveType(
			Primitive.Int);

	public static final PrimitiveType LONG_TYPE = new PrimitiveType(
			Primitive.Long);

	public static final PrimitiveType FLOAT_TYPE = new PrimitiveType(
			Primitive.Float);

	public static final PrimitiveType DOUBLE_TYPE = new PrimitiveType(
			Primitive.Double);

	public static final PrimitiveType BOOLEAN_TYPE = new PrimitiveType(
			Primitive.Boolean);

	public static final PrimitiveType CHAR_TYPE = new PrimitiveType(
			Primitive.Char);

	public static final VoidType VOID_TYPE = new VoidType();

	public static final ThisExpr THIS = new ThisExpr();

	/**
	 * Parses a Java source file and store the AST into a
	 * {@link org.walkmod.javalang.ast.CompilationUnit} object. The default
	 * encoding is UTF-8.
	 * 
	 * @param file
	 *            source code to parse
	 * @return the abstract syntax tree (AST)
	 * @throws ParseException
	 *             when the code contains an invalid syntax
	 * @throws IOException
	 *             file can not be read.
	 */
	public static CompilationUnit parse(File file) throws ParseException,
			IOException {
		return parse(file, "UTF-8");
	}

	/**
	 * Parses a Java source file with a given encoding and store the AST into a
	 * {@link org.walkmod.javalang.ast.CompilationUnit} object.
	 * 
	 * @param file
	 *            source code to parse
	 * @param encoding
	 * @return the abstract syntax tree (AST)
	 * @throws ParseException
	 *             when the code contains an invalid syntax
	 * @throws IOException
	 *             file can not be read.
	 */
	public static CompilationUnit parse(File file, String encoding)
			throws ParseException, IOException {
		Reader reader = new InputStreamReader(new FileInputStream(file),
				encoding);
		return parse(reader);
	}

	/**
	 * Parses Java code and store the AST into a
	 * {@link org.walkmod.javalang.ast.CompilationUnit} object.
	 * 
	 * @param reader
	 *            to read the source code to parse
	 * @return the abstract syntax tree (AST)
	 * @throws ParseException
	 *             when the code contains an invalid syntax
	 * @throws IOException
	 *             the code can not be read.
	 */
	public static CompilationUnit parse(Reader reader) throws ParseException,
			IOException {
		ASTParser astParser = new ASTParser(reader);
		astParser.jj_input_stream.setTabSize(1);
		CompilationUnit cu = null;
		try {
			cu = (CompilationUnit) astParser.CompilationUnit();
		} finally {
			reader.close();
		}
		return cu;
	}

	/**
	 * Parses Java code and store the AST into a
	 * {@link org.walkmod.javalang.ast.CompilationUnit} object. All nodes has
	 * their start and end location (line and column)
	 * 
	 * @param code
	 *            source code to parse.
	 * @return the abstract syntax tree (AST).
	 * @throws ParseException
	 *             when the code contains an invalid syntax.
	 */
	public static CompilationUnit parse(String code) throws ParseException {

		return parse(code, false);
	}

	/**
	 * Parses Java code and store the AST into a
	 * {@link org.walkmod.javalang.ast.CompilationUnit} object with or without
	 * the nodes start & end locations (line numbers and columns).
	 * 
	 * @param code
	 *            source code to parse.
	 * @param withoutLocation
	 *            true does not fulfill the location information inside the AST.
	 *            Otherwise, it is defined.
	 * @return the abstract syntax tree (AST).
	 * @throws ParseException
	 *             when the code contains an invalid syntax.
	 */
	public static CompilationUnit parse(String code, boolean withoutLocation)
			throws ParseException {

		ASTParser astParser = null;
		StringReader sr = new StringReader(code);

		if (!withoutLocation) {
			astParser = new ASTParser(sr);
		} else {
			JavaCharStream stream = new JavaCharStream(sr, 1, 1);
			CleanerTokenManager ctm = new CleanerTokenManager(stream);
			astParser = new ASTParser(ctm);
		}

		CompilationUnit cu = null;
		try {
			cu = astParser.CompilationUnit();
		} finally {
			sr.close();
		}
		return cu;
	}

	/**
	 * Parses any fragment of code and store the result into the subclass of
	 * {@link org.walkmod.javalang.ast.Node} defined. For example, if you need
	 * to parse a single method, the class must be
	 * {@link org.walkmod.javalang.ast.body.MethodDeclaration}. The result
	 * does NOT contain the location of the AST nodes.
	 * 
	 * @param clazz
	 *            the subclass of {@link org.walkmod.javalang.ast.Node}. The
	 *            result will be instance of that class.
	 * @param text
	 *            the fragment of code to parse.
	 * @return the partial abstract syntax tree (AST) produced.
	 * @throws ParseException
	 *             when the code contains an invalid syntax.
	 */
	public static Node parse(Class<?> clazz, String text) throws ParseException {
		return parse(clazz, text, true);
	}

	/**
	 * Parses any fragment of code and store the result into the subclass of
	 * {@link org.walkmod.javalang.ast.Node} defined. For example, if you need
	 * to parse a single method, the class must be
	 * {@link org.walkmod.javalang.ast.body.MethodDeclaration}. The result can
	 * contain or not the location of all AST nodes.
	 * 
	 * @param clazz
	 *            the subclass of {@link org.walkmod.javalang.ast.Node}. The
	 *            result will be instance of that class.
	 * @param text
	 *            the fragment of code to parse.
	 * @param withoutLocation
	 *            true does not fulfill the location information inside the AST.
	 *            Otherwise, it is defined.
	 * @return the partial abstract syntax tree (AST) produced.
	 * @throws ParseException
	 */
	public static Node parse(Class<?> clazz, String text,
			boolean withoutLocation) throws ParseException {

		if (text == null || clazz == null) {
			return null;
		}

		ASTParser astParser = null;
		StringReader sr = new StringReader(text);

		if (!withoutLocation) {
			astParser = new ASTParser(sr);
			astParser.jj_input_stream.setTabSize(1);
		} else {
			JavaCharStream stream = new JavaCharStream(sr, 1, 1);
			CleanerTokenManager ctm = new CleanerTokenManager(stream);
			astParser = new ASTParser(ctm);
		}

		Node result = null;
		if (clazz.equals(Type.class)) {
			text = text.replace("$", ".");
			result = astParser.Type();
		} else if (clazz.equals(NameExpr.class)) {
			result = astParser.Name();
		} else if (clazz.equals(BlockStmt.class)) {
			result = astParser.Block();
		} else if (BodyDeclaration.class.isAssignableFrom(clazz)) {
			if (InitializerDeclaration.class.isAssignableFrom(clazz)) {
				result = astParser.ClassOrInterfaceBodyDeclaration(false);
			} else {
				result = astParser.ClassOrInterfaceBodyDeclaration(true);
			}
		}
		else if (Expression.class.isAssignableFrom(clazz)){
			result = astParser.Expression(); 
		}
		else if (Statement.class.isAssignableFrom(clazz)){
			result = astParser.BlockStatement();
		}
		else {
			Method method = null;
			try {
				method = astParser.getClass().getMethod(clazz.getSimpleName());
			} catch (Exception e) {
				throw new ParseException("The " + clazz.getSimpleName()
						+ " cannot be parseable");
			}
			try {

				try {
					result = (Node) method.invoke(astParser);

				} catch (IllegalAccessException e) {
					throw new ParseException("The " + clazz.getSimpleName()
							+ " cannot be parseable");
				} catch (IllegalArgumentException e) {
					throw new ParseException("The " + clazz.getSimpleName()
							+ " cannot be parseable");
				} catch (InvocationTargetException e) {
					throw (ParseException) (e.getTargetException());
				}
			} finally {
				sr.close();
			}
		}
		return result;
	}
}
