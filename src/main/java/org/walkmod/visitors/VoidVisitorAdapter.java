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
import org.walkmod.lang.ast.body.BodyDeclaration;
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
import org.walkmod.lang.ast.body.TypeDeclaration;
import org.walkmod.lang.ast.body.VariableDeclarator;
import org.walkmod.lang.ast.body.VariableDeclaratorId;
import org.walkmod.lang.ast.expr.AnnotationExpr;
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
import org.walkmod.lang.ast.expr.Expression;
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
import org.walkmod.lang.ast.stmt.Statement;
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
import org.walkmod.lang.ast.type.Type;
import org.walkmod.lang.ast.type.VoidType;
import org.walkmod.lang.ast.type.WildcardType;

/**
 * @author Julio Vilmar Gesser
 */
public abstract class VoidVisitorAdapter<A> implements VoidVisitor<A> {

	public void visit(AnnotationDeclaration n, A arg) {
		if (n.getJavaDoc() != null) {
			n.getJavaDoc().accept(this, arg);
		}
		if (n.getAnnotations() != null) {
			for (AnnotationExpr a : n.getAnnotations()) {
				a.accept(this, arg);
			}
		}
		if (n.getMembers() != null) {
			for (BodyDeclaration member : n.getMembers()) {
				member.accept(this, arg);
			}
		}
	}

	public void visit(AnnotationMemberDeclaration n, A arg) {
		if (n.getJavaDoc() != null) {
			n.getJavaDoc().accept(this, arg);
		}
		if (n.getAnnotations() != null) {
			for (AnnotationExpr a : n.getAnnotations()) {
				a.accept(this, arg);
			}
		}
		n.getType().accept(this, arg);
		if (n.getDefaultValue() != null) {
			n.getDefaultValue().accept(this, arg);
		}
	}

	public void visit(ArrayAccessExpr n, A arg) {
		n.getName().accept(this, arg);
		n.getIndex().accept(this, arg);
	}

	public void visit(ArrayCreationExpr n, A arg) {
		n.getType().accept(this, arg);
		if (n.getDimensions() != null) {
			for (Expression dim : n.getDimensions()) {
				dim.accept(this, arg);
			}
		} else {
			n.getInitializer().accept(this, arg);
		}
	}

	public void visit(ArrayInitializerExpr n, A arg) {
		if (n.getValues() != null) {
			for (Expression expr : n.getValues()) {
				expr.accept(this, arg);
			}
		}
	}

	public void visit(AssertStmt n, A arg) {
		n.getCheck().accept(this, arg);
		if (n.getMessage() != null) {
			n.getMessage().accept(this, arg);
		}
	}

	public void visit(AssignExpr n, A arg) {
		n.getTarget().accept(this, arg);
		n.getValue().accept(this, arg);
	}

	public void visit(BinaryExpr n, A arg) {
		n.getLeft().accept(this, arg);
		n.getRight().accept(this, arg);
	}

	public void visit(BlockComment n, A arg) {
	}

	public void visit(BlockStmt n, A arg) {
		if (n.getStmts() != null) {
			for (Statement s : n.getStmts()) {
				s.accept(this, arg);
			}
		}
	}

	public void visit(BooleanLiteralExpr n, A arg) {
	}

	public void visit(BreakStmt n, A arg) {
	}

	public void visit(CastExpr n, A arg) {
		n.getType().accept(this, arg);
		n.getExpr().accept(this, arg);
	}

	public void visit(CatchClause n, A arg) {
		n.getExcept().accept(this, arg);
		n.getCatchBlock().accept(this, arg);
	}

	public void visit(CharLiteralExpr n, A arg) {
	}

	public void visit(ClassExpr n, A arg) {
		n.getType().accept(this, arg);
	}

	public void visit(ClassOrInterfaceDeclaration n, A arg) {
		if (n.getJavaDoc() != null) {
			n.getJavaDoc().accept(this, arg);
		}
		if (n.getAnnotations() != null) {
			for (AnnotationExpr a : n.getAnnotations()) {
				a.accept(this, arg);
			}
		}
		if (n.getTypeParameters() != null) {
			for (TypeParameter t : n.getTypeParameters()) {
				t.accept(this, arg);
			}
		}
		if (n.getExtends() != null) {
			for (ClassOrInterfaceType c : n.getExtends()) {
				c.accept(this, arg);
			}
		}
		if (n.getImplements() != null) {
			for (ClassOrInterfaceType c : n.getImplements()) {
				c.accept(this, arg);
			}
		}
		if (n.getMembers() != null) {
			for (BodyDeclaration member : n.getMembers()) {
				member.accept(this, arg);
			}
		}
	}

	public void visit(ClassOrInterfaceType n, A arg) {
		if (n.getScope() != null) {
			n.getScope().accept(this, arg);
		}
		if (n.getTypeArgs() != null) {
			for (Type t : n.getTypeArgs()) {
				t.accept(this, arg);
			}
		}
	}

	public void visit(CompilationUnit n, A arg) {
		if (n.getPackage() != null) {
			n.getPackage().accept(this, arg);
		}
		if (n.getImports() != null) {
			for (ImportDeclaration i : n.getImports()) {
				i.accept(this, arg);
			}
		}
		if (n.getTypes() != null) {
			for (TypeDeclaration typeDeclaration : n.getTypes()) {
				typeDeclaration.accept(this, arg);
			}
		}
	}

	public void visit(ConditionalExpr n, A arg) {
		n.getCondition().accept(this, arg);
		n.getThenExpr().accept(this, arg);
		n.getElseExpr().accept(this, arg);
	}

	public void visit(ConstructorDeclaration n, A arg) {
		if (n.getJavaDoc() != null) {
			n.getJavaDoc().accept(this, arg);
		}
		if (n.getAnnotations() != null) {
			for (AnnotationExpr a : n.getAnnotations()) {
				a.accept(this, arg);
			}
		}
		if (n.getTypeParameters() != null) {
			for (TypeParameter t : n.getTypeParameters()) {
				t.accept(this, arg);
			}
		}
		if (n.getParameters() != null) {
			for (Parameter p : n.getParameters()) {
				p.accept(this, arg);
			}
		}
		if (n.getThrows() != null) {
			for (NameExpr name : n.getThrows()) {
				name.accept(this, arg);
			}
		}
		n.getBlock().accept(this, arg);
	}

	public void visit(ContinueStmt n, A arg) {
	}

	public void visit(DoStmt n, A arg) {
		n.getBody().accept(this, arg);
		n.getCondition().accept(this, arg);
	}

	public void visit(DoubleLiteralExpr n, A arg) {
	}

	public void visit(EmptyMemberDeclaration n, A arg) {
		if (n.getJavaDoc() != null) {
			n.getJavaDoc().accept(this, arg);
		}
	}

	public void visit(EmptyStmt n, A arg) {
	}

	public void visit(EmptyTypeDeclaration n, A arg) {
		if (n.getJavaDoc() != null) {
			n.getJavaDoc().accept(this, arg);
		}
	}

	public void visit(EnclosedExpr n, A arg) {
		n.getInner().accept(this, arg);
	}

	public void visit(EnumConstantDeclaration n, A arg) {
		if (n.getJavaDoc() != null) {
			n.getJavaDoc().accept(this, arg);
		}
		if (n.getAnnotations() != null) {
			for (AnnotationExpr a : n.getAnnotations()) {
				a.accept(this, arg);
			}
		}
		if (n.getArgs() != null) {
			for (Expression e : n.getArgs()) {
				e.accept(this, arg);
			}
		}
		if (n.getClassBody() != null) {
			for (BodyDeclaration member : n.getClassBody()) {
				member.accept(this, arg);
			}
		}
	}

	public void visit(EnumDeclaration n, A arg) {
		if (n.getJavaDoc() != null) {
			n.getJavaDoc().accept(this, arg);
		}
		if (n.getAnnotations() != null) {
			for (AnnotationExpr a : n.getAnnotations()) {
				a.accept(this, arg);
			}
		}
		if (n.getImplements() != null) {
			for (ClassOrInterfaceType c : n.getImplements()) {
				c.accept(this, arg);
			}
		}
		if (n.getEntries() != null) {
			for (EnumConstantDeclaration e : n.getEntries()) {
				e.accept(this, arg);
			}
		}
		if (n.getMembers() != null) {
			for (BodyDeclaration member : n.getMembers()) {
				member.accept(this, arg);
			}
		}
	}

	public void visit(ExplicitConstructorInvocationStmt n, A arg) {
		if (!n.isThis()) {
			if (n.getExpr() != null) {
				n.getExpr().accept(this, arg);
			}
		}
		if (n.getTypeArgs() != null) {
			for (Type t : n.getTypeArgs()) {
				t.accept(this, arg);
			}
		}
		if (n.getArgs() != null) {
			for (Expression e : n.getArgs()) {
				e.accept(this, arg);
			}
		}
	}

	public void visit(ExpressionStmt n, A arg) {
		n.getExpression().accept(this, arg);
	}

	public void visit(FieldAccessExpr n, A arg) {
		n.getScope().accept(this, arg);
	}

	public void visit(FieldDeclaration n, A arg) {
		if (n.getJavaDoc() != null) {
			n.getJavaDoc().accept(this, arg);
		}
		if (n.getAnnotations() != null) {
			for (AnnotationExpr a : n.getAnnotations()) {
				a.accept(this, arg);
			}
		}
		n.getType().accept(this, arg);
		for (VariableDeclarator var : n.getVariables()) {
			var.accept(this, arg);
		}
	}

	public void visit(ForeachStmt n, A arg) {
		n.getVariable().accept(this, arg);
		n.getIterable().accept(this, arg);
		n.getBody().accept(this, arg);
	}

	public void visit(ForStmt n, A arg) {
		if (n.getInit() != null) {
			for (Expression e : n.getInit()) {
				e.accept(this, arg);
			}
		}
		if (n.getCompare() != null) {
			n.getCompare().accept(this, arg);
		}
		if (n.getUpdate() != null) {
			for (Expression e : n.getUpdate()) {
				e.accept(this, arg);
			}
		}
		n.getBody().accept(this, arg);
	}

	public void visit(IfStmt n, A arg) {
		n.getCondition().accept(this, arg);
		n.getThenStmt().accept(this, arg);
		if (n.getElseStmt() != null) {
			n.getElseStmt().accept(this, arg);
		}
	}

	public void visit(ImportDeclaration n, A arg) {
		n.getName().accept(this, arg);
	}

	public void visit(InitializerDeclaration n, A arg) {
		if (n.getJavaDoc() != null) {
			n.getJavaDoc().accept(this, arg);
		}
		n.getBlock().accept(this, arg);
	}

	public void visit(InstanceOfExpr n, A arg) {
		n.getExpr().accept(this, arg);
		n.getType().accept(this, arg);
	}

	public void visit(IntegerLiteralExpr n, A arg) {
	}

	public void visit(IntegerLiteralMinValueExpr n, A arg) {
	}

	public void visit(JavadocComment n, A arg) {
	}

	public void visit(LabeledStmt n, A arg) {
		n.getStmt().accept(this, arg);
	}

	public void visit(LineComment n, A arg) {
	}

	public void visit(LongLiteralExpr n, A arg) {
	}

	public void visit(LongLiteralMinValueExpr n, A arg) {
	}

	public void visit(MarkerAnnotationExpr n, A arg) {
		n.getName().accept(this, arg);
	}

	public void visit(MemberValuePair n, A arg) {
		n.getValue().accept(this, arg);
	}

	public void visit(MethodCallExpr n, A arg) {
		if (n.getScope() != null) {
			n.getScope().accept(this, arg);
		}
		if (n.getTypeArgs() != null) {
			for (Type t : n.getTypeArgs()) {
				t.accept(this, arg);
			}
		}
		if (n.getArgs() != null) {
			for (Expression e : n.getArgs()) {
				e.accept(this, arg);
			}
		}
	}

	public void visit(MethodDeclaration n, A arg) {
		if (n.getJavaDoc() != null) {
			n.getJavaDoc().accept(this, arg);
		}
		if (n.getAnnotations() != null) {
			for (AnnotationExpr a : n.getAnnotations()) {
				a.accept(this, arg);
			}
		}
		if (n.getTypeParameters() != null) {
			for (TypeParameter t : n.getTypeParameters()) {
				t.accept(this, arg);
			}
		}
		n.getType().accept(this, arg);
		if (n.getParameters() != null) {
			for (Parameter p : n.getParameters()) {
				p.accept(this, arg);
			}
		}
		if (n.getThrows() != null) {
			for (NameExpr name : n.getThrows()) {
				name.accept(this, arg);
			}
		}
		if (n.getBody() != null) {
			n.getBody().accept(this, arg);
		}
	}

	public void visit(NameExpr n, A arg) {
	}

	public void visit(NormalAnnotationExpr n, A arg) {
		n.getName().accept(this, arg);
		if (n.getPairs() != null) {
			for (MemberValuePair m : n.getPairs()) {
				m.accept(this, arg);
			}
		}
	}

	public void visit(NullLiteralExpr n, A arg) {
	}

	public void visit(ObjectCreationExpr n, A arg) {
		if (n.getScope() != null) {
			n.getScope().accept(this, arg);
		}
		if (n.getTypeArgs() != null) {
			for (Type t : n.getTypeArgs()) {
				t.accept(this, arg);
			}
		}
		n.getType().accept(this, arg);
		if (n.getArgs() != null) {
			for (Expression e : n.getArgs()) {
				e.accept(this, arg);
			}
		}
		if (n.getAnonymousClassBody() != null) {
			for (BodyDeclaration member : n.getAnonymousClassBody()) {
				member.accept(this, arg);
			}
		}
	}

	public void visit(PackageDeclaration n, A arg) {
		if (n.getAnnotations() != null) {
			for (AnnotationExpr a : n.getAnnotations()) {
				a.accept(this, arg);
			}
		}
		n.getName().accept(this, arg);
	}

	public void visit(Parameter n, A arg) {
		if (n.getAnnotations() != null) {
			for (AnnotationExpr a : n.getAnnotations()) {
				a.accept(this, arg);
			}
		}
		n.getType().accept(this, arg);
		n.getId().accept(this, arg);
	}

	public void visit(PrimitiveType n, A arg) {
	}

	public void visit(QualifiedNameExpr n, A arg) {
		n.getQualifier().accept(this, arg);
	}

	public void visit(ReferenceType n, A arg) {
		n.getType().accept(this, arg);
	}

	public void visit(ReturnStmt n, A arg) {
		if (n.getExpr() != null) {
			n.getExpr().accept(this, arg);
		}
	}

	public void visit(SingleMemberAnnotationExpr n, A arg) {
		n.getName().accept(this, arg);
		n.getMemberValue().accept(this, arg);
	}

	public void visit(StringLiteralExpr n, A arg) {
	}

	public void visit(SuperExpr n, A arg) {
		if (n.getClassExpr() != null) {
			n.getClassExpr().accept(this, arg);
		}
	}

	public void visit(SwitchEntryStmt n, A arg) {
		if (n.getLabel() != null) {
			n.getLabel().accept(this, arg);
		}
		if (n.getStmts() != null) {
			for (Statement s : n.getStmts()) {
				s.accept(this, arg);
			}
		}
	}

	public void visit(SwitchStmt n, A arg) {
		n.getSelector().accept(this, arg);
		if (n.getEntries() != null) {
			for (SwitchEntryStmt e : n.getEntries()) {
				e.accept(this, arg);
			}
		}
	}

	public void visit(SynchronizedStmt n, A arg) {
		n.getExpr().accept(this, arg);
		n.getBlock().accept(this, arg);
	}

	public void visit(ThisExpr n, A arg) {
		if (n.getClassExpr() != null) {
			n.getClassExpr().accept(this, arg);
		}
	}

	public void visit(ThrowStmt n, A arg) {
		n.getExpr().accept(this, arg);
	}

	public void visit(TryStmt n, A arg) {
		n.getTryBlock().accept(this, arg);
		if (n.getCatchs() != null) {
			for (CatchClause c : n.getCatchs()) {
				c.accept(this, arg);
			}
		}
		if (n.getFinallyBlock() != null) {
			n.getFinallyBlock().accept(this, arg);
		}
	}

	public void visit(TypeDeclarationStmt n, A arg) {
		n.getTypeDeclaration().accept(this, arg);
	}

	public void visit(TypeParameter n, A arg) {
		if (n.getTypeBound() != null) {
			for (ClassOrInterfaceType c : n.getTypeBound()) {
				c.accept(this, arg);
			}
		}
	}

	public void visit(UnaryExpr n, A arg) {
		n.getExpr().accept(this, arg);
	}

	public void visit(VariableDeclarationExpr n, A arg) {
		if (n.getAnnotations() != null) {
			for (AnnotationExpr a : n.getAnnotations()) {
				a.accept(this, arg);
			}
		}
		n.getType().accept(this, arg);
		for (VariableDeclarator v : n.getVars()) {
			v.accept(this, arg);
		}
	}

	public void visit(VariableDeclarator n, A arg) {
		n.getId().accept(this, arg);
		if (n.getInit() != null) {
			n.getInit().accept(this, arg);
		}
	}

	public void visit(VariableDeclaratorId n, A arg) {
	}

	public void visit(VoidType n, A arg) {
	}

	public void visit(WhileStmt n, A arg) {
		n.getCondition().accept(this, arg);
		n.getBody().accept(this, arg);
	}

	public void visit(WildcardType n, A arg) {
		if (n.getExtends() != null) {
			n.getExtends().accept(this, arg);
		}
		if (n.getSuper() != null) {
			n.getSuper().accept(this, arg);
		}
	}

	@Override
	public void visit(final MultiTypeParameter n, final A arg) {

		if (n.getAnnotations() != null) {
			for (final AnnotationExpr a : n.getAnnotations()) {
				a.accept(this, arg);
			}
		}
		for (final Type type : n.getTypes()) {
			type.accept(this, arg);
		}
		n.getId().accept(this, arg);
	}
}
