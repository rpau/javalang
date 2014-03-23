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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.walkmod.javalang.ast.CompilationUnit;
import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.body.AnnotationDeclaration;
import org.walkmod.javalang.ast.body.AnnotationMemberDeclaration;
import org.walkmod.javalang.ast.body.BodyDeclaration;
import org.walkmod.javalang.ast.body.ConstructorDeclaration;
import org.walkmod.javalang.ast.body.EnumConstantDeclaration;
import org.walkmod.javalang.ast.body.EnumDeclaration;
import org.walkmod.javalang.ast.body.FieldDeclaration;
import org.walkmod.javalang.ast.body.JavadocTag;
import org.walkmod.javalang.ast.body.MethodDeclaration;
import org.walkmod.javalang.ast.body.ModifierSet;
import org.walkmod.javalang.ast.body.Parameter;
import org.walkmod.javalang.ast.body.TypeDeclaration;
import org.walkmod.javalang.ast.body.VariableDeclarator;
import org.walkmod.javalang.ast.body.VariableDeclaratorId;
import org.walkmod.javalang.ast.expr.AnnotationExpr;
import org.walkmod.javalang.ast.expr.Expression;
import org.walkmod.javalang.ast.expr.MemberValuePair;
import org.walkmod.javalang.ast.expr.MethodCallExpr;
import org.walkmod.javalang.ast.expr.NameExpr;
import org.walkmod.javalang.ast.expr.QualifiedNameExpr;
import org.walkmod.javalang.ast.expr.StringLiteralExpr;
import org.walkmod.javalang.ast.expr.ThisExpr;
import org.walkmod.javalang.ast.expr.VariableDeclarationExpr;
import org.walkmod.javalang.ast.stmt.BlockStmt;
import org.walkmod.javalang.ast.stmt.ExpressionStmt;
import org.walkmod.javalang.ast.stmt.Statement;
import org.walkmod.javalang.ast.type.ClassOrInterfaceType;
import org.walkmod.javalang.ast.type.PrimitiveType;
import org.walkmod.javalang.ast.type.PrimitiveType.Primitive;
import org.walkmod.javalang.ast.type.ReferenceType;
import org.walkmod.javalang.ast.type.Type;
import org.walkmod.javalang.ast.type.VoidType;
import org.walkmod.javalang.comparators.AnnotationExprComparator;
import org.walkmod.javalang.comparators.AnnotationMemberDeclarationComparator;
import org.walkmod.javalang.comparators.EnumConstantComparator;
import org.walkmod.javalang.comparators.FieldDeclarationComparator;
import org.walkmod.javalang.comparators.MethodDeclarationComparator;
import org.walkmod.javalang.comparators.TypeDeclarationComparator;
import org.walkmod.javalang.tags.TagsParser;
import org.walkmod.merger.CollectionUtil;

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

	static {
		List<MemberValuePair> pairs = new LinkedList<MemberValuePair>();
		pairs.add(new MemberValuePair("value", new StringLiteralExpr(
				"org.walkmod")));
		pairs.add(new MemberValuePair("date", new StringLiteralExpr(DateFormat
				.getDateTimeInstance().format(new Date()))));
	}

	public static Expression parse(String expression) throws ParseException {
		StringReader sr = new StringReader(expression);
		ASTParser astParser = new ASTParser(sr);
		Expression e = null;
		try {
			e = astParser.Expression();
		} finally {
			sr.close();
		}
		return e;
	}

	public static Statement parseBlockStmt(String blockStmt)
			throws ParseException {
		StringReader sr = new StringReader(blockStmt);
		ASTParser astParser = new ASTParser(sr);
		Statement block = null;
		try {
			block = (Statement) astParser.BlockStatement();
		} finally {
			sr.close();
		}
		return block;
	}

	public static CompilationUnit parse(File file) throws ParseException,
			IOException {
		return parse(file, "UTF-8");
	}

	public static CompilationUnit parse(File file, String encoding)
			throws ParseException, IOException {
		Reader reader = new InputStreamReader(new FileInputStream(file),
				encoding);
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
	
	public static CompilationUnit parse(Reader reader)
			throws ParseException, IOException {	
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

	public static BlockStmt parseBlock(String blockStmt) throws ParseException {
		StringReader sr = new StringReader(blockStmt);
		ASTParser astParser = new ASTParser(sr);
		astParser.jj_input_stream.setTabSize(1);
		BlockStmt block = null;
		try {
			block = (BlockStmt) astParser.Block();
		} finally {
			sr.close();
		}
		return block;
	}

	public static NameExpr parseName(String expression) throws ParseException {
		StringReader sr = new StringReader(expression);
		ASTParser astParser = new ASTParser(sr);
		NameExpr e = null;
		try {
			e = astParser.Name();
		} finally {
			sr.close();
		}
		return e;
	}

	public static CompilationUnit parseCompilationUnit(String expression)
			throws ParseException {
		
		return parseCompilationUnit(expression, false);
	}
	
	public static CompilationUnit parseCompilationUnit(String expression,  boolean withoutLocation)throws ParseException {
		
		ASTParser astParser = null;
		StringReader sr = new StringReader(expression);
		
		if(!withoutLocation){
			 astParser = new ASTParser(sr);
		}
		else{
			JavaCharStream stream = new JavaCharStream(sr, 1, 1);
			CleanerTokenManager ctm = new CleanerTokenManager(stream);
			astParser = new ASTParser(ctm);
		}
		
		CompilationUnit cu = null;
		try{
			cu = astParser.CompilationUnit();
		}finally{
			sr.close();
		}
		return cu;
	}

	public static ClassOrInterfaceType parseClassOrInterfaceType(String name)
			throws ParseException {
		if (name != null) {
			name = name.replace("$", ".");
		}
		StringReader sr = new StringReader(name);
		ASTParser astParser = new ASTParser(sr);
		ClassOrInterfaceType e = null;
		try {
			e = astParser.ClassOrInterfaceType();
		} finally {
			sr.close();
		}
		return e;
	}

	public static Type parseType(String name) throws ParseException {
		if (name != null) {
			name = name.replace("$", ".");
		}
		StringReader sr = new StringReader(name);
		ASTParser astParser = new ASTParser(sr);
		Type e = null;
		try {
			e = astParser.Type();
		} finally {
			sr.close();
		}
		return e;
	}

	public static MethodDeclaration parseMethod(String method)
			throws ParseException {
		StringReader sr = new StringReader(method);
		ASTParser astParser = new ASTParser(sr);
		MethodDeclaration md = null;
		try {
			md = (MethodDeclaration) astParser
					.ClassOrInterfaceBodyDeclaration(method.endsWith(";"));
		} catch (Error e) {
			if (!"Missing return statement in function".equals(e.getMessage())) {
				throw e;
			}
		} finally {
			sr.close();
		}
		return md;
	}

	

	public static ConstructorDeclaration createConstructorWithFields(
			EnumDeclaration enumeration) {
		ConstructorDeclaration cd = new ConstructorDeclaration();
		List<Parameter> params = new LinkedList<Parameter>();
		Collection<FieldDeclaration> fields = getFields(enumeration);
		Iterator<FieldDeclaration> it = fields.iterator();
		while (it.hasNext()) {
			FieldDeclaration fd = it.next();
			Parameter p = new Parameter();
			p.setType(fd.getType());
			p.setId(new VariableDeclaratorId(fd.getVariables().get(0).getId()
					.getName()));
			params.add(p);
		}
		cd.setParameters(params);
		return cd;
	}

	public static List<FieldDeclaration> getFields(TypeDeclaration td) {
		Collection<BodyDeclaration> members = td.getMembers();
		List<FieldDeclaration> fields = new LinkedList<FieldDeclaration>();
		Iterator<BodyDeclaration> it = members.iterator();
		while (it.hasNext()) {
			BodyDeclaration current = it.next();
			if (current instanceof FieldDeclaration) {
				fields.add((FieldDeclaration) current);
			}
		}
		return fields;
	}

	public static List<MethodDeclaration> getMethods(TypeDeclaration td) {
		Collection<BodyDeclaration> members = td.getMembers();
		List<MethodDeclaration> methods = new LinkedList<MethodDeclaration>();
		Iterator<BodyDeclaration> it = members.iterator();
		while (it.hasNext()) {
			BodyDeclaration current = it.next();
			if (current instanceof MethodDeclaration) {
				methods.add((MethodDeclaration) current);
			}
		}
		return methods;
	}

	public static TypeDeclaration getTypeDeclaration(CompilationUnit cu,
			TypeDeclaration td) {
		List<TypeDeclaration> types = cu.getTypes();
		if (types != null) {
			Iterator<TypeDeclaration> it = types.iterator();
			while (it.hasNext()) {
				TypeDeclaration current = it.next();
				if (current.getName().equals(td.getName())) {
					return current;
				}
			}
		}
		return null;
	}

	public static Collection<MethodDeclaration> getNonExistentMethods(
			TypeDeclaration td, Collection<MethodDeclaration> methods) {
		Collection<MethodDeclaration> result = new LinkedList<MethodDeclaration>();
		Iterator<MethodDeclaration> it = methods.iterator();
		while (it.hasNext()) {
			MethodDeclaration md = it.next();
			MethodDeclaration md2 = getCurrentMethodDeclaration(td, md);
			if (md2 == null) {
				result.add(md);
			}
		}
		return result;
	}

	public static Collection<FieldDeclaration> getNonExistentFields(
			TypeDeclaration td, Collection<FieldDeclaration> fields)
			throws Exception {
		Collection<FieldDeclaration> result = new LinkedList<FieldDeclaration>();
		Iterator<FieldDeclaration> it = fields.iterator();
		while (it.hasNext()) {
			FieldDeclaration fd = it.next();
			FieldDeclaration fd2 = getCurrentFieldDeclaration(td, fd);
			if (fd2 == null) {				
				result.add(fd);
			}
		}
		return result;
	}

	public static Collection<EnumConstantDeclaration> getNonExistentEnumConstants(
			EnumDeclaration enumeration,
			Collection<EnumConstantDeclaration> enumConstants) throws Exception {
		Collection<EnumConstantDeclaration> result = new LinkedList<EnumConstantDeclaration>();
		Iterator<EnumConstantDeclaration> it = enumConstants.iterator();
		while (it.hasNext()) {
			EnumConstantDeclaration c = it.next();
			if (getCurrentEnumConstant(enumeration, c) == null) {
				result.add(c);
			}
		}
		return result;
	}

	public static Collection<AnnotationMemberDeclaration> getNonExistentAnnotationMembers(
			AnnotationDeclaration ad,
			Collection<AnnotationMemberDeclaration> annotationMembers) {
		Collection<AnnotationMemberDeclaration> result = new LinkedList<AnnotationMemberDeclaration>();
		Iterator<AnnotationMemberDeclaration> it = annotationMembers.iterator();
		while (it.hasNext()) {
			AnnotationMemberDeclaration current = it.next();
			if (getCurrentAnnotationMember(ad, current) == null) {
				result.add(current);
			}
		}
		return result;
	}
	
	public static Collection<TypeDeclaration> getNonExistentTypeDeclarations(CompilationUnit cu, Collection<TypeDeclaration> tds){
		Collection<TypeDeclaration> result = new LinkedList<TypeDeclaration>();
		Iterator<TypeDeclaration> it = tds.iterator();
		TypeDeclarationComparator tdc = new TypeDeclarationComparator();
		while(it.hasNext()){
			TypeDeclaration current = it.next();
			if(CollectionUtil.findObject(cu.getTypes(), current, tdc) == null){
				result.add(current);
			}
		}
		return result;
	}
	
	public static Collection<TypeDeclaration> getExistentTypeDeclarations(CompilationUnit cu, Collection<TypeDeclaration> tds){
		Collection<TypeDeclaration> result = new LinkedList<TypeDeclaration>();
		Iterator<TypeDeclaration> it = tds.iterator();
		TypeDeclarationComparator tdc = new TypeDeclarationComparator();
		while(it.hasNext()){
			TypeDeclaration current = it.next();
			if(CollectionUtil.findObject(cu.getTypes(), current, tdc) != null){
				result.add(current);
			}
		}
		return result;
	}
	
	public static AnnotationMemberDeclaration getCurrentAnnotationMember(
			AnnotationDeclaration ad, AnnotationMemberDeclaration amd) {
		List<BodyDeclaration> members = ad.getMembers();
		AnnotationMemberDeclarationComparator amdc = new AnnotationMemberDeclarationComparator();
		return (AnnotationMemberDeclaration) CollectionUtil.findObject(members,
				amd, amdc);
	}

	public static FieldDeclaration getCurrentFieldDeclaration(
			TypeDeclaration td, FieldDeclaration fd) {
		List<BodyDeclaration> members = td.getMembers();
		FieldDeclarationComparator fdc = new FieldDeclarationComparator();
		return (FieldDeclaration) CollectionUtil.findObject(members, fd, fdc);
	}

	public static MethodDeclaration getCurrentMethodDeclaration(
			TypeDeclaration td, MethodDeclaration md) {
		List<BodyDeclaration> members = td.getMembers();
		MethodDeclarationComparator mdc = new MethodDeclarationComparator();
		return (MethodDeclaration) CollectionUtil.findObject(members, md, mdc);
	}

	public static EnumConstantDeclaration getCurrentEnumConstant(
			EnumDeclaration enumeration, EnumConstantDeclaration constant) {
		Collection<EnumConstantDeclaration> entries = enumeration.getEntries();
		EnumConstantComparator cmp = new EnumConstantComparator();
		return (EnumConstantDeclaration) CollectionUtil.findObject(entries,
				constant, cmp);
	}

	

	public static void addAllMethodDeclarations(TypeDeclaration td,
			Collection<MethodDeclaration> methods) {
		Collection<MethodDeclaration> memberList = getNonExistentMethods(td,
				methods);
		for (BodyDeclaration member : memberList) {
			addMember(td, member);
		}
	}

	public static void addMethodDeclaration(TypeDeclaration td,
			MethodDeclaration method) {
		MethodDeclaration md = getCurrentMethodDeclaration(td, method);
		if (md == null) {
			addMember(td, method);
		}
	}

	public static boolean addFieldDeclaration(TypeDeclaration td,
			FieldDeclaration field) {
		FieldDeclaration fd = getCurrentFieldDeclaration(td, field);
		if (fd == null) {			
			addMember(td, field);
			return true;
		}
		return false;
	}

	public static void addMethodDeclaration(TypeDeclaration td, int modifiers,
			Type type, String name, Collection<Parameter> parameters,
			String blockStmts) throws ParseException {
		MethodDeclaration mtd = new MethodDeclaration(ModifierSet.PUBLIC,
				ASTManager.VOID_TYPE, name);
		if (parameters != null) {
			for (Parameter parameter : parameters) {
				addParameter(mtd, parameter);
			}
		}
		BlockStmt setterBlock = ASTManager.parseBlock(blockStmts);
		mtd.setBody(setterBlock);
		addMethodDeclaration(td, mtd);
	}

	public static void addMethodDeclaration(TypeDeclaration td, int modifiers,
			Type type, String name, Parameter parameter, String blockStmts)
			throws ParseException {
		MethodDeclaration mtd = new MethodDeclaration(ModifierSet.PUBLIC,
				type, name);
		if (parameter != null) {
			addParameter(mtd, parameter);
		}
		BlockStmt setterBlock = ASTManager.parseBlock(blockStmts);
		mtd.setBody(setterBlock);
		addMethodDeclaration(td, mtd);
	}



	public static Collection<EnumConstantDeclaration> getAnnotatedEnumConstants(
			AnnotationExpr annotation, EnumDeclaration enumeration) {
		Collection<EnumConstantDeclaration> result = new LinkedList<EnumConstantDeclaration>();
		Collection<EnumConstantDeclaration> entries = enumeration.getEntries();
		Iterator<EnumConstantDeclaration> it = entries.iterator();
		AnnotationExprComparator cmp = new AnnotationExprComparator();
		while (it.hasNext()) {
			EnumConstantDeclaration current = it.next();
			List<AnnotationExpr> anns = current.getAnnotations();
			AnnotationExpr currentAnnotationExpr = (AnnotationExpr) CollectionUtil
					.findObject(anns, annotation, cmp);
			if (currentAnnotationExpr != null) {
				result.add(current);
			}
		}
		return result;
	}

	public static Collection<AnnotationMemberDeclaration> getAnnotatedAnnotationMembers(
			AnnotationExpr annotation, AnnotationDeclaration ad) {
		Collection<AnnotationMemberDeclaration> result = new LinkedList<AnnotationMemberDeclaration>();
		Collection<BodyDeclaration> entries = ad.getMembers();
		Iterator<BodyDeclaration> it = entries.iterator();
		AnnotationExprComparator cmp = new AnnotationExprComparator();
		while (it.hasNext()) {
			BodyDeclaration current = it.next();
			if (current instanceof AnnotationMemberDeclaration) {
				List<AnnotationExpr> anns = current.getAnnotations();
				AnnotationExpr currentAnnotationExpr = (AnnotationExpr) CollectionUtil
						.findObject(anns, annotation, cmp);
				if (currentAnnotationExpr != null) {
					result.add((AnnotationMemberDeclaration) current);
				}
			}
		}
		return result;
	}


	public static Collection<MethodDeclaration> getAnnotatedMethods(
			AnnotationExpr annotation, TypeDeclaration td) {
		AnnotationExprComparator cmp = new AnnotationExprComparator();
		Collection<MethodDeclaration> result = new LinkedList<MethodDeclaration>();
		List<BodyDeclaration> members = td.getMembers();
		if (members != null) {
			Iterator<BodyDeclaration> it = members.iterator();
			while (it.hasNext()) {
				BodyDeclaration current = it.next();
				if (current instanceof MethodDeclaration) {
					List<AnnotationExpr> anns = current.getAnnotations();
					AnnotationExpr currentAnnotationExpr = (AnnotationExpr) CollectionUtil
							.findObject(anns, annotation, cmp);
					if (currentAnnotationExpr != null) {
						result.add((MethodDeclaration) current);
					}
				}
			}
		}
		return result;
	}

	public static Collection<FieldDeclaration> getAnnotatedFields(
			AnnotationExpr annotation, TypeDeclaration td) {
		AnnotationExprComparator cmp = new AnnotationExprComparator();
		Collection<FieldDeclaration> result = new LinkedList<FieldDeclaration>();
		List<BodyDeclaration> members = td.getMembers();
		if (members != null) {
			Iterator<BodyDeclaration> it = members.iterator();
			while (it.hasNext()) {
				BodyDeclaration current = it.next();
				if (current instanceof FieldDeclaration) {
					List<AnnotationExpr> anns = current.getAnnotations();
					AnnotationExpr currentAnnotationExpr = (AnnotationExpr) CollectionUtil
							.findObject(anns, annotation, cmp);
					if (currentAnnotationExpr != null) {
						result.add((FieldDeclaration) current);
					}
				}
			}
		}
		return result;
	}

	 /** Creates a new {@link NameExpr} from a qualified name.<br>
	 * The qualified name can contains "." (dot) characters.
	 * 
	 * @param qualifiedName
	 *            qualified name
	 * @return instanceof {@link NameExpr}
	 */
	public static NameExpr createNameExpr(String qualifiedName) {
		String[] split = qualifiedName.split("\\.");
		NameExpr ret = new NameExpr(split[0]);
		for (int i = 1; i < split.length; i++) {
			ret = new QualifiedNameExpr(ret, split[i]);
		}
		return ret;
	}

	/**
	 * Creates a new {@link Parameter}.
	 * 
	 * @param type
	 *            type of the parameter
	 * @param name
	 *            name of the parameter
	 * @return instance of {@link Parameter}
	 */
	public static Parameter createParameter(Type type, String name) {
		return new Parameter(type, new VariableDeclaratorId(name));
	}

	/**
	 * Creates a {@link FieldDeclaration}.
	 * 
	 * @param modifiers
	 *            modifiers
	 * @param type
	 *            type
	 * @param variable
	 *            variable declarator
	 * @return instance of {@link FieldDeclaration}
	 */
	public static FieldDeclaration createFieldDeclaration(int modifiers,
			Type type, VariableDeclarator variable) {
		List<VariableDeclarator> variables = new ArrayList<VariableDeclarator>();
		variables.add(variable);
		FieldDeclaration ret = new FieldDeclaration(modifiers, type, variables);
		return ret;
	}

	/**
	 * Creates a {@link FieldDeclaration}.
	 * 
	 * @param modifiers
	 *            modifiers
	 * @param type
	 *            type
	 * @param name
	 *            field name
	 * @return instance of {@link FieldDeclaration}
	 */
	public static FieldDeclaration createFieldDeclaration(int modifiers,
			Type type, String name) {
		VariableDeclaratorId id = new VariableDeclaratorId(name);
		VariableDeclarator variable = new VariableDeclarator(id);
		return createFieldDeclaration(modifiers, type, variable);
	}

	/**
	 * Creates a {@link VariableDeclarationExpr}.
	 * 
	 * @param type
	 *            type
	 * @param name
	 *            name
	 * @return instance of {@link VariableDeclarationExpr}
	 */
	public static VariableDeclarationExpr createVariableDeclarationExpr(
			Type type, String name) {
		List<VariableDeclarator> vars = new ArrayList<VariableDeclarator>();
		vars.add(new VariableDeclarator(new VariableDeclaratorId(name)));
		return new VariableDeclarationExpr(type, vars);
	}

	/**
	 * Adds the given parameter to the method. The list of parameters will be
	 * initialized if it is <code>null</code>.
	 * 
	 * @param method
	 *            method
	 * @param parameter
	 *            parameter
	 */
	public static void addParameter(MethodDeclaration method,
			Parameter parameter) {
		List<Parameter> parameters = method.getParameters();
		if (parameters == null) {
			parameters = new ArrayList<Parameter>();
			method.setParameters(parameters);
		}
		parameters.add(parameter);
	}

	/**
	 * Adds the given argument to the method call. The list of arguments will be
	 * initialized if it is <code>null</code>.
	 * 
	 * @param call
	 *            method call
	 * @param arg
	 *            argument value
	 */
	public static void addArgument(MethodCallExpr call, Expression arg) {
		List<Expression> args = call.getArgs();
		if (args == null) {
			args = new ArrayList<Expression>();
			call.setArgs(args);
		}
		args.add(arg);
	}

	/**
	 * Adds the given type declaration to the compilation unit. The list of
	 * types will be initialized if it is <code>null</code>.
	 * 
	 * @param cu
	 *            compilation unit
	 * @param type
	 *            type declaration
	 */
	public static void addTypeDeclaration(CompilationUnit cu,
			TypeDeclaration type) {
		List<TypeDeclaration> types = cu.getTypes();
		if (types == null) {
			types = new ArrayList<TypeDeclaration>();
			cu.setTypes(types);
		}
		types.add(type);
	}

	/**
	 * Creates a new {@link ReferenceType} for a class or interface.
	 * 
	 * @param name
	 *            name of the class or interface
	 * @param arrayCount
	 *            number os arrays or <code>0</code> if is not a array.
	 * @return instanceof {@link ReferenceType}
	 */
	public static ReferenceType createReferenceType(String name, int arrayCount) {
		return new ReferenceType(new ClassOrInterfaceType(name), arrayCount);
	}

	/**
	 * Creates a new {@link ReferenceType} for the given primitive type.
	 * 
	 * @param type
	 *            primitive type
	 * @param arrayCount
	 *            number os arrays or <code>0</code> if is not a array.
	 * @return instanceof {@link ReferenceType}
	 */
	public static ReferenceType createReferenceType(PrimitiveType type,
			int arrayCount) {
		return new ReferenceType(type, arrayCount);
	}

	/**
	 * Adds the given statement to the specified block. The list of statements
	 * will be initialized if it is <code>null</code>.
	 * 
	 * @param block
	 * @param stmt
	 */
	public static void addStmt(BlockStmt block, Statement stmt) {
		List<Statement> stmts = block.getStmts();
		if (stmts == null) {
			stmts = new ArrayList<Statement>();
			block.setStmts(stmts);
		}
		stmts.add(stmt);
	}

	/**
	 * Adds the given expression to the specified block. The list of statements
	 * will be initialized if it is <code>null</code>.
	 * 
	 * @param block
	 * @param stmt
	 */
	public static void addStmt(BlockStmt block, Expression expr) {
		addStmt(block, new ExpressionStmt(expr));
	}

	/**
	 * Adds the given declaration to the specified type. The list of members
	 * will be initialized if it is <code>null</code>.
	 * 
	 * @param type
	 *            type declaration
	 * @param decl
	 *            member declaration
	 */
	public static void addMember(TypeDeclaration type, BodyDeclaration decl) {
		List<BodyDeclaration> members = type.getMembers();
		if (members == null) {
			members = new ArrayList<BodyDeclaration>();
			type.setMembers(members);
		}
		members.add(decl);
	}


	public static boolean isNewNode(Node node) {
		return (0 == node.getEndColumn()) && (node.getBeginLine() == 0);
	}

	public static boolean areSorted(List<? extends Node> nodes) {
		if (nodes != null) {
			Iterator<? extends Node> it = nodes.iterator();
			boolean sorted = true;
			while (it.hasNext() && sorted) {
				Node node = it.next();
				if (it.hasNext()) {
					Node node2 = it.next();
					sorted = sorted
							&& (node.getBeginLine() < node2.getBeginLine() || ((node
									.getBeginLine() == node2.getBeginLine()) && (node
									.getBeginColumn() <= node2.getBeginColumn())));
				} else {
					return sorted;
				}
			}
		}
		return true;
	}

	public static boolean isPrevious(Node node, Node previous) {
		if (previous.getEndLine() < node.getBeginLine()) {
			return true;
		} else if ((previous.getEndLine() == node.getBeginLine())
				&& (previous.getEndColumn() <= node.getBeginColumn())) {
			return true;
		}
		return false;
	}

	public static boolean isEqualLocation(Node node1, Node node2) {
		if (!isNewNode(node1) && !isNewNode(node2)) {
			return node1.getBeginLine() == node2.getBeginLine()
					&& node1.getBeginColumn() == node2.getBeginColumn()
					&& node1.getEndLine() == node2.getEndLine()
					&& node1.getEndColumn() == node2.getEndColumn();
		}
		return false;
	}

	public static boolean contains(Node node1, Node node2) {
		if ((node1.getBeginLine() < node2.getBeginLine())
				|| ((node1.getBeginLine() == node2.getBeginLine()) && node1
						.getBeginColumn() <= node2.getBeginColumn())) {
			if (node1.getEndLine() > node2.getEndLine()) {
				return true;
			} else if ((node1.getEndLine() == node2.getEndLine())
					&& node1.getEndColumn() >= node2.getEndColumn()) {
				return true;
			}
		}
		return false;
	}

	public static Node getPreviousNode(Node currentNode,
			List<? extends Node> nodeList) {
		Node previous = null;
		if (nodeList != null) {
			for (Node node : nodeList) {
				if (previous == null) {
					if (isPrevious(currentNode, node)) {
						previous = node;
					}
				} else {
					if (isPrevious(previous, node)
							&& isPrevious(currentNode, node)) {
						previous = node;
					}
				}
			}
		}
		return previous;
	}

	public static List<JavadocTag> getTags(String content) throws Exception {
		List<JavadocTag> javadocTags = null;
		if (content != null) {
			InputStream is = new ByteArrayInputStream(content.getBytes());
			try {
				javadocTags = TagsParser.parse(is);
			} finally {
				is.close();
			}
		}
		return javadocTags;
	}
}
