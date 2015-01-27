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

public class CompositeVisitor<VisitorContext> extends VoidVisitorAdapter<VisitorContext> {

    private VoidVisitor<VisitorContext> preVisitor = null;

    private VoidVisitor<VisitorContext> postVisitor = null;

    public CompositeVisitor(VoidVisitor<VisitorContext> preVisitor, VoidVisitor<VisitorContext> postVisitor) {
        this.preVisitor = preVisitor;
        this.postVisitor = postVisitor;
    }

    public void visit(AnnotationDeclaration n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(AnnotationMemberDeclaration n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(ArrayAccessExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(ArrayCreationExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(ArrayInitializerExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(AssertStmt n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(AssignExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(BinaryExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(BlockComment n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(BlockStmt n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(BooleanLiteralExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(BreakStmt n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(CastExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(CatchClause n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(CharLiteralExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(ClassExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(ClassOrInterfaceDeclaration n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(ClassOrInterfaceType n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(CompilationUnit n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(ConditionalExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(ConstructorDeclaration n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(ContinueStmt n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(DoStmt n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(DoubleLiteralExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(EmptyMemberDeclaration n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(EmptyStmt n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(EmptyTypeDeclaration n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(EnclosedExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(EnumConstantDeclaration n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(EnumDeclaration n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(ExplicitConstructorInvocationStmt n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(ExpressionStmt n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(FieldAccessExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(FieldDeclaration n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(ForeachStmt n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(ForStmt n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(IfStmt n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(ImportDeclaration n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(InitializerDeclaration n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(InstanceOfExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(IntegerLiteralExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(IntegerLiteralMinValueExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(JavadocComment n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(LabeledStmt n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(LineComment n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(LongLiteralExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(LongLiteralMinValueExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(MarkerAnnotationExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(MemberValuePair n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(MethodCallExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(MethodDeclaration n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(NameExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(NormalAnnotationExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(NullLiteralExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(ObjectCreationExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(PackageDeclaration n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(Parameter n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(MultiTypeParameter n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(PrimitiveType n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(QualifiedNameExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(ReferenceType n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(ReturnStmt n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(SingleMemberAnnotationExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(StringLiteralExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(SuperExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(SwitchEntryStmt n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(SwitchStmt n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(SynchronizedStmt n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(ThisExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(ThrowStmt n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(TryStmt n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(TypeDeclarationStmt n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(TypeParameter n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(UnaryExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(VariableDeclarationExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(VariableDeclarator n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(VariableDeclaratorId n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(VoidType n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(WhileStmt n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public void visit(WildcardType n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    public VoidVisitor<VisitorContext> getPreVisitor() {
        return preVisitor;
    }

    public void setPreVisitor(VoidVisitor<VisitorContext> preVisitor) {
        this.preVisitor = preVisitor;
    }

    public VoidVisitor<VisitorContext> getPostVisitor() {
        return postVisitor;
    }

    public void setPostVisitor(VoidVisitor<VisitorContext> postVisitor) {
        this.postVisitor = postVisitor;
    }

    @Override
    public void visit(LambdaExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    @Override
    public void visit(MethodReferenceExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }

    @Override
    public void visit(TypeExpr n, VisitorContext arg) {
        if (preVisitor != null) {
            preVisitor.visit(n, arg);
        }
        super.visit(n, arg);
        if (postVisitor != null) {
            postVisitor.visit(n, arg);
        }
    }
}
