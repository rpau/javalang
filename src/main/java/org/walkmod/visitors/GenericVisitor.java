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
package org.walkmod.visitors;

import org.walkmod.lang.ast.BlockComment;
import org.walkmod.lang.ast.CompilationUnit;
import org.walkmod.lang.ast.ImportDeclaration;
import org.walkmod.lang.ast.LineComment;
import org.walkmod.lang.ast.PackageDeclaration;
import org.walkmod.lang.ast.TypeParameter;
import org.walkmod.lang.ast.body.AnnotationDeclaration;
import org.walkmod.lang.ast.body.AnnotationMemberDeclaration;
import org.walkmod.lang.ast.body.ClassOrInterfaceDeclaration;
import org.walkmod.lang.ast.body.ConstructorDeclaration;
import org.walkmod.lang.ast.body.EmptyMemberDeclaration;
import org.walkmod.lang.ast.body.EmptyTypeDeclaration;
import org.walkmod.lang.ast.body.EnumConstantDeclaration;
import org.walkmod.lang.ast.body.EnumDeclaration;
import org.walkmod.lang.ast.body.FieldDeclaration;
import org.walkmod.lang.ast.body.InitializerDeclaration;
import org.walkmod.lang.ast.body.JavadocComment;
import org.walkmod.lang.ast.body.MethodDeclaration;
import org.walkmod.lang.ast.body.MultiTypeParameter;
import org.walkmod.lang.ast.body.Parameter;
import org.walkmod.lang.ast.body.VariableDeclarator;
import org.walkmod.lang.ast.body.VariableDeclaratorId;
import org.walkmod.lang.ast.expr.ArrayAccessExpr;
import org.walkmod.lang.ast.expr.ArrayCreationExpr;
import org.walkmod.lang.ast.expr.ArrayInitializerExpr;
import org.walkmod.lang.ast.expr.AssignExpr;
import org.walkmod.lang.ast.expr.BinaryExpr;
import org.walkmod.lang.ast.expr.BooleanLiteralExpr;
import org.walkmod.lang.ast.expr.CastExpr;
import org.walkmod.lang.ast.expr.CharLiteralExpr;
import org.walkmod.lang.ast.expr.ClassExpr;
import org.walkmod.lang.ast.expr.ConditionalExpr;
import org.walkmod.lang.ast.expr.DoubleLiteralExpr;
import org.walkmod.lang.ast.expr.EnclosedExpr;
import org.walkmod.lang.ast.expr.FieldAccessExpr;
import org.walkmod.lang.ast.expr.InstanceOfExpr;
import org.walkmod.lang.ast.expr.IntegerLiteralExpr;
import org.walkmod.lang.ast.expr.IntegerLiteralMinValueExpr;
import org.walkmod.lang.ast.expr.LongLiteralExpr;
import org.walkmod.lang.ast.expr.LongLiteralMinValueExpr;
import org.walkmod.lang.ast.expr.MarkerAnnotationExpr;
import org.walkmod.lang.ast.expr.MemberValuePair;
import org.walkmod.lang.ast.expr.MethodCallExpr;
import org.walkmod.lang.ast.expr.NameExpr;
import org.walkmod.lang.ast.expr.NormalAnnotationExpr;
import org.walkmod.lang.ast.expr.NullLiteralExpr;
import org.walkmod.lang.ast.expr.ObjectCreationExpr;
import org.walkmod.lang.ast.expr.QualifiedNameExpr;
import org.walkmod.lang.ast.expr.SingleMemberAnnotationExpr;
import org.walkmod.lang.ast.expr.StringLiteralExpr;
import org.walkmod.lang.ast.expr.SuperExpr;
import org.walkmod.lang.ast.expr.ThisExpr;
import org.walkmod.lang.ast.expr.UnaryExpr;
import org.walkmod.lang.ast.expr.VariableDeclarationExpr;
import org.walkmod.lang.ast.stmt.AssertStmt;
import org.walkmod.lang.ast.stmt.BlockStmt;
import org.walkmod.lang.ast.stmt.BreakStmt;
import org.walkmod.lang.ast.stmt.CatchClause;
import org.walkmod.lang.ast.stmt.ContinueStmt;
import org.walkmod.lang.ast.stmt.DoStmt;
import org.walkmod.lang.ast.stmt.EmptyStmt;
import org.walkmod.lang.ast.stmt.ExplicitConstructorInvocationStmt;
import org.walkmod.lang.ast.stmt.ExpressionStmt;
import org.walkmod.lang.ast.stmt.ForStmt;
import org.walkmod.lang.ast.stmt.ForeachStmt;
import org.walkmod.lang.ast.stmt.IfStmt;
import org.walkmod.lang.ast.stmt.LabeledStmt;
import org.walkmod.lang.ast.stmt.ReturnStmt;
import org.walkmod.lang.ast.stmt.SwitchEntryStmt;
import org.walkmod.lang.ast.stmt.SwitchStmt;
import org.walkmod.lang.ast.stmt.SynchronizedStmt;
import org.walkmod.lang.ast.stmt.ThrowStmt;
import org.walkmod.lang.ast.stmt.TryStmt;
import org.walkmod.lang.ast.stmt.TypeDeclarationStmt;
import org.walkmod.lang.ast.stmt.WhileStmt;
import org.walkmod.lang.ast.type.ClassOrInterfaceType;
import org.walkmod.lang.ast.type.PrimitiveType;
import org.walkmod.lang.ast.type.ReferenceType;
import org.walkmod.lang.ast.type.VoidType;
import org.walkmod.lang.ast.type.WildcardType;

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
