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
package org.walkmod.javalang.visitors;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.walkmod.javalang.ast.BlockComment;
import org.walkmod.javalang.ast.Comment;
import org.walkmod.javalang.ast.CompilationUnit;
import org.walkmod.javalang.ast.ImportDeclaration;
import org.walkmod.javalang.ast.LineComment;
import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.PackageDeclaration;
import org.walkmod.javalang.ast.TypeParameter;
import org.walkmod.javalang.ast.body.AnnotationDeclaration;
import org.walkmod.javalang.ast.body.AnnotationMemberDeclaration;
import org.walkmod.javalang.ast.body.BodyDeclaration;
import org.walkmod.javalang.ast.body.ClassOrInterfaceDeclaration;
import org.walkmod.javalang.ast.body.ConstructorDeclaration;
import org.walkmod.javalang.ast.body.EmptyMemberDeclaration;
import org.walkmod.javalang.ast.body.EmptyTypeDeclaration;
import org.walkmod.javalang.ast.body.EnumConstantDeclaration;
import org.walkmod.javalang.ast.body.EnumDeclaration;
import org.walkmod.javalang.ast.body.FieldDeclaration;
import org.walkmod.javalang.ast.body.InitializerDeclaration;
import org.walkmod.javalang.ast.body.JavadocComment;
import org.walkmod.javalang.ast.body.MethodDeclaration;
import org.walkmod.javalang.ast.body.MultiTypeParameter;
import org.walkmod.javalang.ast.body.Parameter;
import org.walkmod.javalang.ast.body.TypeDeclaration;
import org.walkmod.javalang.ast.body.VariableDeclarator;
import org.walkmod.javalang.ast.body.VariableDeclaratorId;
import org.walkmod.javalang.ast.expr.AnnotationExpr;
import org.walkmod.javalang.ast.expr.ArrayAccessExpr;
import org.walkmod.javalang.ast.expr.ArrayCreationExpr;
import org.walkmod.javalang.ast.expr.ArrayInitializerExpr;
import org.walkmod.javalang.ast.expr.AssignExpr;
import org.walkmod.javalang.ast.expr.BinaryExpr;
import org.walkmod.javalang.ast.expr.BooleanLiteralExpr;
import org.walkmod.javalang.ast.expr.CastExpr;
import org.walkmod.javalang.ast.expr.CharLiteralExpr;
import org.walkmod.javalang.ast.expr.ClassExpr;
import org.walkmod.javalang.ast.expr.ConditionalExpr;
import org.walkmod.javalang.ast.expr.DoubleLiteralExpr;
import org.walkmod.javalang.ast.expr.EnclosedExpr;
import org.walkmod.javalang.ast.expr.Expression;
import org.walkmod.javalang.ast.expr.FieldAccessExpr;
import org.walkmod.javalang.ast.expr.InstanceOfExpr;
import org.walkmod.javalang.ast.expr.IntegerLiteralExpr;
import org.walkmod.javalang.ast.expr.IntegerLiteralMinValueExpr;
import org.walkmod.javalang.ast.expr.LambdaExpr;
import org.walkmod.javalang.ast.expr.LongLiteralExpr;
import org.walkmod.javalang.ast.expr.LongLiteralMinValueExpr;
import org.walkmod.javalang.ast.expr.MarkerAnnotationExpr;
import org.walkmod.javalang.ast.expr.MemberValuePair;
import org.walkmod.javalang.ast.expr.MethodCallExpr;
import org.walkmod.javalang.ast.expr.MethodReferenceExpr;
import org.walkmod.javalang.ast.expr.NameExpr;
import org.walkmod.javalang.ast.expr.NormalAnnotationExpr;
import org.walkmod.javalang.ast.expr.NullLiteralExpr;
import org.walkmod.javalang.ast.expr.ObjectCreationExpr;
import org.walkmod.javalang.ast.expr.QualifiedNameExpr;
import org.walkmod.javalang.ast.expr.SingleMemberAnnotationExpr;
import org.walkmod.javalang.ast.expr.StringLiteralExpr;
import org.walkmod.javalang.ast.expr.SuperExpr;
import org.walkmod.javalang.ast.expr.ThisExpr;
import org.walkmod.javalang.ast.expr.TypeExpr;
import org.walkmod.javalang.ast.expr.UnaryExpr;
import org.walkmod.javalang.ast.expr.VariableDeclarationExpr;
import org.walkmod.javalang.ast.stmt.AssertStmt;
import org.walkmod.javalang.ast.stmt.BlockStmt;
import org.walkmod.javalang.ast.stmt.BreakStmt;
import org.walkmod.javalang.ast.stmt.CatchClause;
import org.walkmod.javalang.ast.stmt.ContinueStmt;
import org.walkmod.javalang.ast.stmt.DoStmt;
import org.walkmod.javalang.ast.stmt.EmptyStmt;
import org.walkmod.javalang.ast.stmt.ExplicitConstructorInvocationStmt;
import org.walkmod.javalang.ast.stmt.ExpressionStmt;
import org.walkmod.javalang.ast.stmt.ForStmt;
import org.walkmod.javalang.ast.stmt.ForeachStmt;
import org.walkmod.javalang.ast.stmt.IfStmt;
import org.walkmod.javalang.ast.stmt.LabeledStmt;
import org.walkmod.javalang.ast.stmt.ReturnStmt;
import org.walkmod.javalang.ast.stmt.Statement;
import org.walkmod.javalang.ast.stmt.SwitchEntryStmt;
import org.walkmod.javalang.ast.stmt.SwitchStmt;
import org.walkmod.javalang.ast.stmt.SynchronizedStmt;
import org.walkmod.javalang.ast.stmt.ThrowStmt;
import org.walkmod.javalang.ast.stmt.TryStmt;
import org.walkmod.javalang.ast.stmt.TypeDeclarationStmt;
import org.walkmod.javalang.ast.stmt.WhileStmt;
import org.walkmod.javalang.ast.type.ClassOrInterfaceType;
import org.walkmod.javalang.ast.type.PrimitiveType;
import org.walkmod.javalang.ast.type.ReferenceType;
import org.walkmod.javalang.ast.type.Type;
import org.walkmod.javalang.ast.type.VoidType;
import org.walkmod.javalang.ast.type.WildcardType;

public class CloneVisitor implements GenericVisitor<Node, Object> {

	@Override
	public Node visit(CompilationUnit _n, Object _arg) {
		PackageDeclaration package_ = cloneNodes(_n.getPackage(), _arg);
		List<ImportDeclaration> imports = visit(_n.getImports(), _arg);
		List<TypeDeclaration> types = visit(_n.getTypes(), _arg);
		List<Comment> comments = visit(_n.getComments(), _arg);
		return new CompilationUnit(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), package_, imports, types,
				comments);
	}

	@Override
	public Node visit(PackageDeclaration _n, Object _arg) {
		List<AnnotationExpr> annotations = visit(_n.getAnnotations(), _arg);
		NameExpr name = cloneNodes(_n.getName(), _arg);
		PackageDeclaration r = new PackageDeclaration(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
				annotations, name);
		return r;
	}

	@Override
	public Node visit(ImportDeclaration _n, Object _arg) {
		NameExpr name = cloneNodes(_n.getName(), _arg);
		ImportDeclaration r = new ImportDeclaration(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(), name,
				_n.isStatic(), _n.isAsterisk());
		return r;
	}

	@Override
	public Node visit(TypeParameter _n, Object _arg) {
		List<ClassOrInterfaceType> typeBound = visit(_n.getTypeBound(), _arg);
		List<AnnotationExpr> annotations = visit(_n.getAnnotations(), _arg);

		TypeParameter r = new TypeParameter(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
				_n.getName(), typeBound, annotations);
		return r;
	}

	@Override
	public Node visit(LineComment _n, Object _arg) {
		return new LineComment(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), _n.getContent());
	}

	@Override
	public Node visit(BlockComment _n, Object _arg) {
		return new BlockComment(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), _n.getContent());
	}

	@Override
	public Node visit(ClassOrInterfaceDeclaration _n, Object _arg) {
		JavadocComment javaDoc = cloneNodes(_n.getJavaDoc(), _arg);
		List<AnnotationExpr> annotations = visit(_n.getAnnotations(), _arg);
		List<TypeParameter> typeParameters = visit(_n.getTypeParameters(), _arg);
		List<ClassOrInterfaceType> extendsList = visit(_n.getExtends(), _arg);
		List<ClassOrInterfaceType> implementsList = visit(_n.getImplements(),
				_arg);
		List<BodyDeclaration> members = visit(_n.getMembers(), _arg);
		ClassOrInterfaceDeclaration r = new ClassOrInterfaceDeclaration(
				_n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(),
				_n.getEndColumn(), javaDoc, _n.getModifiers(), annotations,
				_n.isInterface(), _n.getName(), typeParameters, extendsList,
				implementsList, members);
		return r;
	}

	@Override
	public Node visit(EnumDeclaration _n, Object _arg) {
		JavadocComment javaDoc = cloneNodes(_n.getJavaDoc(), _arg);
		List<AnnotationExpr> annotations = visit(_n.getAnnotations(), _arg);
		List<ClassOrInterfaceType> implementsList = visit(_n.getImplements(),
				_arg);
		List<EnumConstantDeclaration> entries = visit(_n.getEntries(), _arg);
		List<BodyDeclaration> members = visit(_n.getMembers(), _arg);
		EnumDeclaration r = new EnumDeclaration(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
				javaDoc, _n.getModifiers(), annotations, _n.getName(),
				implementsList, entries, members);
		return r;
	}

	@Override
	public Node visit(EmptyTypeDeclaration _n, Object _arg) {
		JavadocComment javaDoc = cloneNodes(_n.getJavaDoc(), _arg);
		EmptyTypeDeclaration r = new EmptyTypeDeclaration(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
				javaDoc);
		return r;
	}

	@Override
	public Node visit(EnumConstantDeclaration _n, Object _arg) {
		JavadocComment javaDoc = cloneNodes(_n.getJavaDoc(), _arg);
		List<AnnotationExpr> annotations = visit(_n.getAnnotations(), _arg);
		List<Expression> args = visit(_n.getArgs(), _arg);
		List<BodyDeclaration> classBody = visit(_n.getClassBody(), _arg);
		EnumConstantDeclaration r = new EnumConstantDeclaration(
				_n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(),
				_n.getEndColumn(), javaDoc, annotations, _n.getName(), args,
				classBody);
		return r;
	}

	@Override
	public Node visit(AnnotationDeclaration _n, Object _arg) {
		JavadocComment javaDoc = cloneNodes(_n.getJavaDoc(), _arg);
		List<AnnotationExpr> annotations = visit(_n.getAnnotations(), _arg);
		List<BodyDeclaration> members = visit(_n.getMembers(), _arg);
		AnnotationDeclaration r = new AnnotationDeclaration(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
				javaDoc, _n.getModifiers(), annotations, _n.getName(), members);
		return r;
	}

	@Override
	public Node visit(AnnotationMemberDeclaration _n, Object _arg) {
		JavadocComment javaDoc = cloneNodes(_n.getJavaDoc(), _arg);
		List<AnnotationExpr> annotations = visit(_n.getAnnotations(), _arg);
		Type type_ = cloneNodes(_n.getType(), _arg);
		Expression defaultValue = cloneNodes(_n.getDefaultValue(), _arg);
		AnnotationMemberDeclaration r = new AnnotationMemberDeclaration(
				_n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(),
				_n.getEndColumn(), javaDoc, _n.getModifiers(), annotations,
				type_, _n.getName(), defaultValue);
		return r;
	}

	@Override
	public Node visit(FieldDeclaration _n, Object _arg) {
		JavadocComment javaDoc = cloneNodes(_n.getJavaDoc(), _arg);
		List<AnnotationExpr> annotations = visit(_n.getAnnotations(), _arg);
		Type type_ = cloneNodes(_n.getType(), _arg);
		List<VariableDeclarator> variables = visit(_n.getVariables(), _arg);
		FieldDeclaration r = new FieldDeclaration(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
				javaDoc, _n.getModifiers(), annotations, type_, variables);
		return r;
	}

	@Override
	public Node visit(VariableDeclarator _n, Object _arg) {
		VariableDeclaratorId id = cloneNodes(_n.getId(), _arg);
		Expression init = cloneNodes(_n.getInit(), _arg);
		VariableDeclarator r = new VariableDeclarator(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(), id,
				init);
		return r;
	}

	@Override
	public Node visit(VariableDeclaratorId _n, Object _arg) {
		VariableDeclaratorId r = new VariableDeclaratorId(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
				_n.getName(), _n.getArrayCount());
		return r;
	}

	@Override
	public Node visit(ConstructorDeclaration _n, Object _arg) {
		JavadocComment javaDoc = cloneNodes(_n.getJavaDoc(), _arg);
		List<AnnotationExpr> annotations = visit(_n.getAnnotations(), _arg);
		List<TypeParameter> typeParameters = visit(_n.getTypeParameters(), _arg);
		List<Parameter> parameters = visit(_n.getParameters(), _arg);
		List<ClassOrInterfaceType> throws_ = visit(_n.getThrows(), _arg);
		BlockStmt block = cloneNodes(_n.getBlock(), _arg);
		ConstructorDeclaration r = new ConstructorDeclaration(
				_n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(),
				_n.getEndColumn(), javaDoc, _n.getModifiers(), annotations,
				typeParameters, _n.getName(), parameters, throws_, block);
		return r;
	}

	@Override
	public Node visit(MethodDeclaration _n, Object _arg) {
		JavadocComment javaDoc = cloneNodes(_n.getJavaDoc(), _arg);
		List<AnnotationExpr> annotations = visit(_n.getAnnotations(), _arg);
		List<TypeParameter> typeParameters = visit(_n.getTypeParameters(), _arg);
		Type type_ = cloneNodes(_n.getType(), _arg);
		List<Parameter> parameters = visit(_n.getParameters(), _arg);
		List<ClassOrInterfaceType> throws_ = visit(_n.getThrows(), _arg);
		BlockStmt block = cloneNodes(_n.getBody(), _arg);
		MethodDeclaration r = new MethodDeclaration(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
				javaDoc, _n.getModifiers(), annotations, typeParameters, type_,
				_n.getName(), parameters, _n.getArrayCount(), throws_, block,
				_n.isDefault());
		return r;
	}

	@Override
	public Node visit(Parameter _n, Object _arg) {
		List<AnnotationExpr> annotations = visit(_n.getAnnotations(), _arg);
		Type type_ = cloneNodes(_n.getType(), _arg);
		VariableDeclaratorId id = cloneNodes(_n.getId(), _arg);
		Parameter r = new Parameter(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), _n.getModifiers(),
				annotations, type_, _n.isVarArgs(), id);
		return r;
	}

	@Override
	public Node visit(MultiTypeParameter _n, Object _arg) {
		List<AnnotationExpr> annotations = visit(_n.getAnnotations(), _arg);
		List<Type> types = visit(_n.getTypes(), _arg);
		VariableDeclaratorId id = cloneNodes(_n.getId(), _arg);
		MultiTypeParameter r = new MultiTypeParameter(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
				_n.getModifiers(), annotations, types, id);
		return r;
	}

	@Override
	public Node visit(EmptyMemberDeclaration _n, Object _arg) {
		JavadocComment javaDoc = cloneNodes(_n.getJavaDoc(), _arg);
		EmptyMemberDeclaration r = new EmptyMemberDeclaration(
				_n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(),
				_n.getEndColumn(), javaDoc);
		return r;
	}

	@Override
	public Node visit(InitializerDeclaration _n, Object _arg) {
		JavadocComment javaDoc = cloneNodes(_n.getJavaDoc(), _arg);
		BlockStmt block = cloneNodes(_n.getBlock(), _arg);
		InitializerDeclaration r = new InitializerDeclaration(
				_n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(),
				_n.getEndColumn(), javaDoc, _n.isStatic(), block);
		return r;
	}

	@Override
	public Node visit(JavadocComment _n, Object _arg) {
		JavadocComment r = new JavadocComment(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
				_n.getContent());
		return r;
	}

	@Override
	public Node visit(ClassOrInterfaceType _n, Object _arg) {
		ClassOrInterfaceType scope = cloneNodes(_n.getScope(), _arg);
		List<Type> typeArgs = visit(_n.getTypeArgs(), _arg);
		List<AnnotationExpr> ann = visit(_n.getAnnotations(), _arg);

		ClassOrInterfaceType r = new ClassOrInterfaceType(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(), scope,
				_n.getName(), typeArgs, ann);
		return r;
	}

	@Override
	public Node visit(PrimitiveType _n, Object _arg) {
		List<AnnotationExpr> ann = visit(_n.getAnnotations(), _arg);
		PrimitiveType r = new PrimitiveType(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
				_n.getType(), ann);
		return r;
	}

	@Override
	public Node visit(ReferenceType _n, Object _arg) {
		List<AnnotationExpr> ann = visit(_n.getAnnotations(), _arg);
		Type type_ = cloneNodes(_n.getType(), _arg);
		List<List<AnnotationExpr>> arraysAnnotations = _n
				.getArraysAnnotations();
		List<List<AnnotationExpr>> _arraysAnnotations = null;
		if (arraysAnnotations != null) {
			_arraysAnnotations = new LinkedList<List<AnnotationExpr>>();
			for (List<AnnotationExpr> aux : arraysAnnotations) {
				_arraysAnnotations.add(visit(aux, _arg));
			}
		}

		ReferenceType r = new ReferenceType(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(), type_,
				_n.getArrayCount(), ann, _arraysAnnotations);
		return r;
	}

	@Override
	public Node visit(VoidType _n, Object _arg) {
		List<AnnotationExpr> ann = visit(_n.getAnnotations(), _arg);
		VoidType r = new VoidType(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), ann);
		return r;
	}

	@Override
	public Node visit(WildcardType _n, Object _arg) {
		ReferenceType ext = cloneNodes(_n.getExtends(), _arg);
		ReferenceType sup = cloneNodes(_n.getSuper(), _arg);
		List<AnnotationExpr> ann = visit(_n.getAnnotations(), _arg);
		WildcardType r = new WildcardType(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(), ext,
				sup, ann);
		return r;
	}

	@Override
	public Node visit(ArrayAccessExpr _n, Object _arg) {
		Expression name = cloneNodes(_n.getName(), _arg);
		Expression index = cloneNodes(_n.getIndex(), _arg);
		ArrayAccessExpr r = new ArrayAccessExpr(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(), name,
				index);
		return r;
	}

	@Override
	public Node visit(ArrayCreationExpr _n, Object _arg) {
		Type type_ = cloneNodes(_n.getType(), _arg);
		List<Expression> dimensions = visit(_n.getDimensions(), _arg);
		ArrayCreationExpr r = new ArrayCreationExpr(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(), type_,
				dimensions, _n.getArrayCount());
		if (_n.getInitializer() != null) {
			// ArrayCreationExpr has two mutually
			// exclusive constructors
			r.setInitializer(cloneNodes(_n.getInitializer(), _arg));
		}
		List<List<AnnotationExpr>> arraysAnnotations = _n
				.getArraysAnnotations();
		List<List<AnnotationExpr>> _arraysAnnotations = null;
		if (arraysAnnotations != null) {
			_arraysAnnotations = new LinkedList<List<AnnotationExpr>>();
			for (List<AnnotationExpr> aux : arraysAnnotations) {
				_arraysAnnotations.add(visit(aux, _arg));
			}
		}
		r.setArraysAnnotations(_arraysAnnotations);
		return r;
	}

	@Override
	public Node visit(ArrayInitializerExpr _n, Object _arg) {
		List<Expression> values = visit(_n.getValues(), _arg);
		ArrayInitializerExpr r = new ArrayInitializerExpr(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(), values);
		return r;
	}

	@Override
	public Node visit(AssignExpr _n, Object _arg) {
		Expression target = cloneNodes(_n.getTarget(), _arg);
		Expression value = cloneNodes(_n.getValue(), _arg);
		AssignExpr r = new AssignExpr(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), target, value,
				_n.getOperator());
		return r;
	}

	@Override
	public Node visit(BinaryExpr _n, Object _arg) {
		Expression left = cloneNodes(_n.getLeft(), _arg);
		Expression right = cloneNodes(_n.getRight(), _arg);
		BinaryExpr r = new BinaryExpr(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), left, right,
				_n.getOperator());
		return r;
	}

	@Override
	public Node visit(CastExpr _n, Object _arg) {
		Type type_ = cloneNodes(_n.getType(), _arg);
		Expression expr = cloneNodes(_n.getExpr(), _arg);
		CastExpr r = new CastExpr(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), type_, expr);
		return r;
	}

	@Override
	public Node visit(ClassExpr _n, Object _arg) {
		Type type_ = cloneNodes(_n.getType(), _arg);
		ClassExpr r = new ClassExpr(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), type_);
		return r;
	}

	@Override
	public Node visit(ConditionalExpr _n, Object _arg) {
		Expression condition = cloneNodes(_n.getCondition(), _arg);
		Expression thenExpr = cloneNodes(_n.getThenExpr(), _arg);
		Expression elseExpr = cloneNodes(_n.getElseExpr(), _arg);
		ConditionalExpr r = new ConditionalExpr(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
				condition, thenExpr, elseExpr);
		return r;
	}

	@Override
	public Node visit(EnclosedExpr _n, Object _arg) {
		Expression inner = cloneNodes(_n.getInner(), _arg);
		EnclosedExpr r = new EnclosedExpr(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(), inner);
		return r;
	}

	@Override
	public Node visit(FieldAccessExpr _n, Object _arg) {
		Expression scope = cloneNodes(_n.getScope(), _arg);
		List<Type> typeArgs = visit(_n.getTypeArgs(), _arg);
		FieldAccessExpr r = new FieldAccessExpr(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(), scope,
				typeArgs, _n.getField());
		return r;
	}

	@Override
	public Node visit(InstanceOfExpr _n, Object _arg) {
		Expression expr = cloneNodes(_n.getExpr(), _arg);
		Type type_ = cloneNodes(_n.getType(), _arg);
		InstanceOfExpr r = new InstanceOfExpr(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(), expr,
				type_);
		return r;
	}

	@Override
	public Node visit(StringLiteralExpr _n, Object _arg) {
		StringLiteralExpr r = new StringLiteralExpr(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
				_n.getValue());
		return r;
	}

	@Override
	public Node visit(IntegerLiteralExpr _n, Object _arg) {
		IntegerLiteralExpr r = new IntegerLiteralExpr(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
				_n.getValue());
		return r;
	}

	@Override
	public Node visit(LongLiteralExpr _n, Object _arg) {
		LongLiteralExpr r = new LongLiteralExpr(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
				_n.getValue());
		return r;
	}

	@Override
	public Node visit(IntegerLiteralMinValueExpr _n, Object _arg) {
		IntegerLiteralMinValueExpr r = new IntegerLiteralMinValueExpr(
				_n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(),
				_n.getEndColumn());
		return r;
	}

	@Override
	public Node visit(LongLiteralMinValueExpr _n, Object _arg) {
		LongLiteralMinValueExpr r = new LongLiteralMinValueExpr(
				_n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(),
				_n.getEndColumn());
		return r;
	}

	@Override
	public Node visit(CharLiteralExpr _n, Object _arg) {
		CharLiteralExpr r = new CharLiteralExpr(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
				_n.getValue());
		return r;
	}

	@Override
	public Node visit(DoubleLiteralExpr _n, Object _arg) {
		DoubleLiteralExpr r = new DoubleLiteralExpr(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
				_n.getValue());
		return r;
	}

	@Override
	public Node visit(BooleanLiteralExpr _n, Object _arg) {
		BooleanLiteralExpr r = new BooleanLiteralExpr(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
				_n.getValue());
		return r;
	}

	@Override
	public Node visit(NullLiteralExpr _n, Object _arg) {
		NullLiteralExpr r = new NullLiteralExpr(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn());
		return r;
	}

	@Override
	public Node visit(MethodCallExpr _n, Object _arg) {
		Expression scope = cloneNodes(_n.getScope(), _arg);
		List<Type> typeArgs = visit(_n.getTypeArgs(), _arg);
		List<Expression> args = visit(_n.getArgs(), _arg);
		MethodCallExpr r = new MethodCallExpr(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(), scope,
				typeArgs, _n.getName(), args);
		return r;
	}

	@Override
	public Node visit(NameExpr _n, Object _arg) {
		NameExpr r = new NameExpr(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), _n.getName());
		return r;
	}

	@Override
	public Node visit(ObjectCreationExpr _n, Object _arg) {
		Expression scope = cloneNodes(_n.getScope(), _arg);
		ClassOrInterfaceType type_ = cloneNodes(_n.getType(), _arg);
		List<Type> typeArgs = visit(_n.getTypeArgs(), _arg);
		List<Expression> args = visit(_n.getArgs(), _arg);
		List<BodyDeclaration> anonymousBody = visit(_n.getAnonymousClassBody(),
				_arg);
		ObjectCreationExpr r = new ObjectCreationExpr(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(), scope,
				type_, typeArgs, args, anonymousBody);
		return r;
	}

	@Override
	public Node visit(QualifiedNameExpr _n, Object _arg) {
		NameExpr scope = cloneNodes(_n.getQualifier(), _arg);
		QualifiedNameExpr r = new QualifiedNameExpr(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(), scope,
				_n.getName());
		return r;
	}

	@Override
	public Node visit(ThisExpr _n, Object _arg) {
		Expression classExpr = cloneNodes(_n.getClassExpr(), _arg);
		ThisExpr r = new ThisExpr(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), classExpr);
		return r;
	}

	@Override
	public Node visit(SuperExpr _n, Object _arg) {
		Expression classExpr = cloneNodes(_n.getClassExpr(), _arg);
		SuperExpr r = new SuperExpr(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), classExpr);
		return r;
	}

	@Override
	public Node visit(UnaryExpr _n, Object _arg) {
		Expression expr = cloneNodes(_n.getExpr(), _arg);
		UnaryExpr r = new UnaryExpr(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), expr, _n.getOperator());
		return r;
	}

	@Override
	public Node visit(VariableDeclarationExpr _n, Object _arg) {
		List<AnnotationExpr> annotations = visit(_n.getAnnotations(), _arg);
		Type type_ = cloneNodes(_n.getType(), _arg);
		List<VariableDeclarator> vars = visit(_n.getVars(), _arg);
		VariableDeclarationExpr r = new VariableDeclarationExpr(
				_n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(),
				_n.getEndColumn(), _n.getModifiers(), annotations, type_, vars);
		return r;
	}

	@Override
	public Node visit(MarkerAnnotationExpr _n, Object _arg) {
		NameExpr name = cloneNodes(_n.getName(), _arg);
		MarkerAnnotationExpr r = new MarkerAnnotationExpr(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(), name);
		return r;
	}

	@Override
	public Node visit(SingleMemberAnnotationExpr _n, Object _arg) {
		NameExpr name = cloneNodes(_n.getName(), _arg);
		Expression memberValue = cloneNodes(_n.getMemberValue(), _arg);
		SingleMemberAnnotationExpr r = new SingleMemberAnnotationExpr(
				_n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(),
				_n.getEndColumn(), name, memberValue);
		return r;
	}

	@Override
	public Node visit(NormalAnnotationExpr _n, Object _arg) {
		NameExpr name = cloneNodes(_n.getName(), _arg);
		List<MemberValuePair> pairs = visit(_n.getPairs(), _arg);
		NormalAnnotationExpr r = new NormalAnnotationExpr(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(), name,
				pairs);
		return r;
	}

	@Override
	public Node visit(MemberValuePair _n, Object _arg) {
		Expression value = cloneNodes(_n.getValue(), _arg);
		MemberValuePair r = new MemberValuePair(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
				_n.getName(), value);
		return r;
	}

	@Override
	public Node visit(ExplicitConstructorInvocationStmt _n, Object _arg) {
		List<Type> typeArgs = visit(_n.getTypeArgs(), _arg);
		Expression expr = cloneNodes(_n.getExpr(), _arg);
		List<Expression> args = visit(_n.getArgs(), _arg);
		ExplicitConstructorInvocationStmt r = new ExplicitConstructorInvocationStmt(
				_n.getBeginLine(), _n.getBeginColumn(), _n.getEndLine(),
				_n.getEndColumn(), typeArgs, _n.isThis(), expr, args);
		return r;
	}

	@Override
	public Node visit(TypeDeclarationStmt _n, Object _arg) {
		TypeDeclaration typeDecl = cloneNodes(_n.getTypeDeclaration(), _arg);
		TypeDeclarationStmt r = new TypeDeclarationStmt(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
				typeDecl);
		return r;
	}

	@Override
	public Node visit(AssertStmt _n, Object _arg) {
		Expression check = cloneNodes(_n.getCheck(), _arg);
		Expression message = cloneNodes(_n.getMessage(), _arg);
		AssertStmt r = new AssertStmt(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), check, message);
		return r;
	}

	@Override
	public Node visit(BlockStmt _n, Object _arg) {
		List<Statement> stmts = visit(_n.getStmts(), _arg);
		BlockStmt r = new BlockStmt(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), stmts);
		return r;
	}

	@Override
	public Node visit(LabeledStmt _n, Object _arg) {
		Statement stmt = cloneNodes(_n.getStmt(), _arg);
		LabeledStmt r = new LabeledStmt(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), _n.getLabel(), stmt);
		return r;
	}

	@Override
	public Node visit(EmptyStmt _n, Object _arg) {
		EmptyStmt r = new EmptyStmt(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn());
		return r;
	}

	@Override
	public Node visit(ExpressionStmt _n, Object _arg) {
		Expression expr = cloneNodes(_n.getExpression(), _arg);
		ExpressionStmt r = new ExpressionStmt(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(), expr);
		return r;
	}

	@Override
	public Node visit(SwitchStmt _n, Object _arg) {
		Expression selector = cloneNodes(_n.getSelector(), _arg);
		List<SwitchEntryStmt> entries = visit(_n.getEntries(), _arg);
		SwitchStmt r = new SwitchStmt(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), selector, entries);
		return r;
	}

	@Override
	public Node visit(SwitchEntryStmt _n, Object _arg) {
		Expression label = cloneNodes(_n.getLabel(), _arg);
		List<Statement> stmts = visit(_n.getStmts(), _arg);
		SwitchEntryStmt r = new SwitchEntryStmt(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(), label,
				stmts);
		return r;
	}

	@Override
	public Node visit(BreakStmt _n, Object _arg) {
		BreakStmt r = new BreakStmt(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), _n.getId());
		return r;
	}

	@Override
	public Node visit(ReturnStmt _n, Object _arg) {
		Expression expr = cloneNodes(_n.getExpr(), _arg);
		ReturnStmt r = new ReturnStmt(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), expr);
		return r;
	}

	@Override
	public Node visit(IfStmt _n, Object _arg) {
		Expression condition = cloneNodes(_n.getCondition(), _arg);
		Statement thenStmt = cloneNodes(_n.getThenStmt(), _arg);
		Statement elseStmt = cloneNodes(_n.getElseStmt(), _arg);
		IfStmt r = new IfStmt(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), condition, thenStmt,
				elseStmt);
		return r;
	}

	@Override
	public Node visit(WhileStmt _n, Object _arg) {
		Expression condition = cloneNodes(_n.getCondition(), _arg);
		Statement body = cloneNodes(_n.getBody(), _arg);
		WhileStmt r = new WhileStmt(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), condition, body);
		return r;
	}

	@Override
	public Node visit(ContinueStmt _n, Object _arg) {
		ContinueStmt r = new ContinueStmt(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(),
				_n.getId());
		return r;
	}

	@Override
	public Node visit(DoStmt _n, Object _arg) {
		Statement body = cloneNodes(_n.getBody(), _arg);
		Expression condition = cloneNodes(_n.getCondition(), _arg);
		DoStmt r = new DoStmt(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), body, condition);
		return r;
	}

	@Override
	public Node visit(ForeachStmt _n, Object _arg) {
		VariableDeclarationExpr var = cloneNodes(_n.getVariable(), _arg);
		Expression iterable = cloneNodes(_n.getIterable(), _arg);
		Statement body = cloneNodes(_n.getBody(), _arg);
		ForeachStmt r = new ForeachStmt(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), var, iterable, body);
		return r;
	}

	@Override
	public Node visit(ForStmt _n, Object _arg) {
		List<Expression> init = visit(_n.getInit(), _arg);
		Expression compare = cloneNodes(_n.getCompare(), _arg);
		List<Expression> update = visit(_n.getUpdate(), _arg);
		Statement body = cloneNodes(_n.getBody(), _arg);
		ForStmt r = new ForStmt(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), init, compare, update, body);
		return r;
	}

	@Override
	public Node visit(ThrowStmt _n, Object _arg) {
		Expression expr = cloneNodes(_n.getExpr(), _arg);
		ThrowStmt r = new ThrowStmt(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), expr);
		return r;
	}

	@Override
	public Node visit(SynchronizedStmt _n, Object _arg) {
		Expression expr = cloneNodes(_n.getExpr(), _arg);
		BlockStmt block = cloneNodes(_n.getBlock(), _arg);
		SynchronizedStmt r = new SynchronizedStmt(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(), expr,
				block);
		return r;
	}

	@Override
	public Node visit(TryStmt _n, Object _arg) {
		List<VariableDeclarationExpr> resources = visit(_n.getResources(), _arg);
		BlockStmt tryBlock = cloneNodes(_n.getTryBlock(), _arg);
		List<CatchClause> catchs = visit(_n.getCatchs(), _arg);
		BlockStmt finallyBlock = cloneNodes(_n.getFinallyBlock(), _arg);
		TryStmt r = new TryStmt(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), resources, tryBlock,
				catchs, finallyBlock);
		return r;
	}

	@Override
	public Node visit(CatchClause _n, Object _arg) {
		MultiTypeParameter except = cloneNodes(_n.getExcept(), _arg);
		BlockStmt catchBlock = cloneNodes(_n.getCatchBlock(), _arg);
		CatchClause r = new CatchClause(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), except.getModifiers(),
				except.getAnnotations(), except.getTypes(), except.getId(),
				catchBlock);
		return r;
	}

	public <T extends Node> List<T> visit(List<T> _nodes, Object _arg) {
		if (_nodes == null)
			return null;
		List<T> r = new ArrayList<T>(_nodes.size());
		for (T n : _nodes) {
			T rN = cloneNodes(n, _arg);
			if (rN != null)
				r.add(rN);
		}
		return r;
	}

	protected <T extends Node> T cloneNodes(T _node, Object _arg) {
		if (_node == null)
			return null;
		Node r = _node.accept(this, _arg);
		if (r == null)
			return null;
		return (T) r;
	}

	@Override
	public Node visit(LambdaExpr _n, Object _arg) {

		List<Parameter> lambdaParameters = visit(_n.getParameters(), _arg);

		Statement body = cloneNodes(_n.getBody(), _arg);

		LambdaExpr r = new LambdaExpr(_n.getBeginLine(), _n.getBeginColumn(),
				_n.getEndLine(), _n.getEndColumn(), lambdaParameters, body,
				_n.isParametersEnclosed());

		return r;
	}

	@Override
	public Node visit(MethodReferenceExpr _n, Object arg) {

		List<TypeParameter> typeParams = visit(_n.getTypeParameters(), arg);
		Expression scope = cloneNodes(_n.getScope(), arg);

		MethodReferenceExpr r = new MethodReferenceExpr(_n.getBeginLine(),
				_n.getBeginColumn(), _n.getEndLine(), _n.getEndColumn(), scope,
				typeParams, _n.getIdentifier());
		return r;
	}

	@Override
	public Node visit(TypeExpr n, Object arg) {

		Type t = cloneNodes(n.getType(), arg);

		TypeExpr r = new TypeExpr(n.getBeginLine(), n.getBeginColumn(),
				n.getEndLine(), n.getEndColumn(), t);

		return r;
	}
}
