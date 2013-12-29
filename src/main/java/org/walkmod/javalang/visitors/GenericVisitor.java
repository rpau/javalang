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

import org.walkmod.javalang.ast.BlockComment;
import org.walkmod.javalang.ast.CompilationUnit;
import org.walkmod.javalang.ast.ImportDeclaration;
import org.walkmod.javalang.ast.LineComment;
import org.walkmod.javalang.ast.PackageDeclaration;
import org.walkmod.javalang.ast.TypeParameter;
import org.walkmod.javalang.ast.body.AnnotationDeclaration;
import org.walkmod.javalang.ast.body.AnnotationMemberDeclaration;
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
import org.walkmod.javalang.ast.body.VariableDeclarator;
import org.walkmod.javalang.ast.body.VariableDeclaratorId;
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
import org.walkmod.javalang.ast.expr.FieldAccessExpr;
import org.walkmod.javalang.ast.expr.InstanceOfExpr;
import org.walkmod.javalang.ast.expr.IntegerLiteralExpr;
import org.walkmod.javalang.ast.expr.IntegerLiteralMinValueExpr;
import org.walkmod.javalang.ast.expr.LongLiteralExpr;
import org.walkmod.javalang.ast.expr.LongLiteralMinValueExpr;
import org.walkmod.javalang.ast.expr.MarkerAnnotationExpr;
import org.walkmod.javalang.ast.expr.MemberValuePair;
import org.walkmod.javalang.ast.expr.MethodCallExpr;
import org.walkmod.javalang.ast.expr.NameExpr;
import org.walkmod.javalang.ast.expr.NormalAnnotationExpr;
import org.walkmod.javalang.ast.expr.NullLiteralExpr;
import org.walkmod.javalang.ast.expr.ObjectCreationExpr;
import org.walkmod.javalang.ast.expr.QualifiedNameExpr;
import org.walkmod.javalang.ast.expr.SingleMemberAnnotationExpr;
import org.walkmod.javalang.ast.expr.StringLiteralExpr;
import org.walkmod.javalang.ast.expr.SuperExpr;
import org.walkmod.javalang.ast.expr.ThisExpr;
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
import org.walkmod.javalang.ast.type.VoidType;
import org.walkmod.javalang.ast.type.WildcardType;

/**
 * @author Julio Vilmar Gesser
 */
public interface GenericVisitor<R, A> {

	public R visit(CompilationUnit n, A arg);

	public R visit(PackageDeclaration n, A arg);

	public R visit(ImportDeclaration n, A arg);

	public R visit(TypeParameter n, A arg);

	public R visit(LineComment n, A arg);

	public R visit(BlockComment n, A arg);

	public R visit(ClassOrInterfaceDeclaration n, A arg);

	public R visit(EnumDeclaration n, A arg);

	public R visit(EmptyTypeDeclaration n, A arg);

	public R visit(EnumConstantDeclaration n, A arg);

	public R visit(AnnotationDeclaration n, A arg);

	public R visit(AnnotationMemberDeclaration n, A arg);

	public R visit(FieldDeclaration n, A arg);

	public R visit(VariableDeclarator n, A arg);

	public R visit(VariableDeclaratorId n, A arg);

	public R visit(ConstructorDeclaration n, A arg);

	public R visit(MethodDeclaration n, A arg);

	public R visit(Parameter n, A arg);

	public R visit(EmptyMemberDeclaration n, A arg);

	public R visit(InitializerDeclaration n, A arg);

	public R visit(JavadocComment n, A arg);

	public R visit(ClassOrInterfaceType n, A arg);

	public R visit(PrimitiveType n, A arg);

	public R visit(ReferenceType n, A arg);

	public R visit(VoidType n, A arg);

	public R visit(WildcardType n, A arg);

	public R visit(ArrayAccessExpr n, A arg);

	public R visit(ArrayCreationExpr n, A arg);

	public R visit(ArrayInitializerExpr n, A arg);

	public R visit(AssignExpr n, A arg);

	public R visit(BinaryExpr n, A arg);

	public R visit(CastExpr n, A arg);

	public R visit(ClassExpr n, A arg);

	public R visit(ConditionalExpr n, A arg);

	public R visit(EnclosedExpr n, A arg);

	public R visit(FieldAccessExpr n, A arg);

	public R visit(InstanceOfExpr n, A arg);

	public R visit(StringLiteralExpr n, A arg);

	public R visit(IntegerLiteralExpr n, A arg);

	public R visit(LongLiteralExpr n, A arg);

	public R visit(IntegerLiteralMinValueExpr n, A arg);

	public R visit(LongLiteralMinValueExpr n, A arg);

	public R visit(CharLiteralExpr n, A arg);

	public R visit(DoubleLiteralExpr n, A arg);

	public R visit(BooleanLiteralExpr n, A arg);

	public R visit(NullLiteralExpr n, A arg);

	public R visit(MethodCallExpr n, A arg);

	public R visit(NameExpr n, A arg);

	public R visit(ObjectCreationExpr n, A arg);

	public R visit(QualifiedNameExpr n, A arg);

	public R visit(ThisExpr n, A arg);

	public R visit(SuperExpr n, A arg);

	public R visit(UnaryExpr n, A arg);

	public R visit(VariableDeclarationExpr n, A arg);

	public R visit(MarkerAnnotationExpr n, A arg);

	public R visit(SingleMemberAnnotationExpr n, A arg);

	public R visit(NormalAnnotationExpr n, A arg);

	public R visit(MemberValuePair n, A arg);

	public R visit(ExplicitConstructorInvocationStmt n, A arg);

	public R visit(TypeDeclarationStmt n, A arg);

	public R visit(AssertStmt n, A arg);

	public R visit(BlockStmt n, A arg);

	public R visit(LabeledStmt n, A arg);

	public R visit(EmptyStmt n, A arg);

	public R visit(ExpressionStmt n, A arg);

	public R visit(SwitchStmt n, A arg);

	public R visit(SwitchEntryStmt n, A arg);

	public R visit(BreakStmt n, A arg);

	public R visit(ReturnStmt n, A arg);

	public R visit(IfStmt n, A arg);

	public R visit(WhileStmt n, A arg);

	public R visit(ContinueStmt n, A arg);

	public R visit(DoStmt n, A arg);

	public R visit(ForeachStmt n, A arg);

	public R visit(ForStmt n, A arg);

	public R visit(ThrowStmt n, A arg);

	public R visit(SynchronizedStmt n, A arg);

	public R visit(TryStmt n, A arg);

	public R visit(CatchClause n, A arg);
	
	public R visit(MultiTypeParameter n, A arg);
}
