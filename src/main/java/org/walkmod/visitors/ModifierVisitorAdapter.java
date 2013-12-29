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

import java.util.List;
import org.walkmod.lang.ast.BlockComment;
import org.walkmod.lang.ast.CompilationUnit;
import org.walkmod.lang.ast.ImportDeclaration;
import org.walkmod.lang.ast.LineComment;
import org.walkmod.lang.ast.Node;
import org.walkmod.lang.ast.PackageDeclaration;
import org.walkmod.lang.ast.TypeParameter;
import org.walkmod.lang.ast.body.AnnotationDeclaration;
import org.walkmod.lang.ast.body.AnnotationMemberDeclaration;
import org.walkmod.lang.ast.body.BaseParameter;
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
 * This visitor adapter can be used to save time when some specific nodes needs
 * to be changed. To do that just extend this class and override the methods
 * from the nodes who needs to be changed, returning the changed node.
 * 
 * @author Julio Vilmar Gesser
 */
public abstract class ModifierVisitorAdapter<A> implements
		GenericVisitor<Node, A> {

	private void removeNulls(List<?> list) {
		for (int i = list.size() - 1; i >= 0; i--) {
			if (list.get(i) == null) {
				list.remove(i);
			}
		}
	}

	public Node visit(AnnotationDeclaration n, A arg) {
		if (n.getJavaDoc() != null) {
			n.setJavaDoc((JavadocComment) n.getJavaDoc().accept(this, arg));
		}
		List<AnnotationExpr> annotations = n.getAnnotations();
		if (annotations != null) {
			for (int i = 0; i < annotations.size(); i++) {
				annotations.set(i,
						(AnnotationExpr) annotations.get(i).accept(this, arg));
			}
			removeNulls(annotations);
		}
		List<BodyDeclaration> members = n.getMembers();
		if (members != null) {
			for (int i = 0; i < members.size(); i++) {
				members.set(i,
						(BodyDeclaration) members.get(i).accept(this, arg));
			}
			removeNulls(members);
		}
		return n;
	}

	public Node visit(AnnotationMemberDeclaration n, A arg) {
		if (n.getJavaDoc() != null) {
			n.setJavaDoc((JavadocComment) n.getJavaDoc().accept(this, arg));
		}
		List<AnnotationExpr> annotations = n.getAnnotations();
		if (annotations != null) {
			for (int i = 0; i < annotations.size(); i++) {
				annotations.set(i,
						(AnnotationExpr) annotations.get(i).accept(this, arg));
			}
			removeNulls(annotations);
		}
		n.setType((Type) n.getType().accept(this, arg));
		if (n.getDefaultValue() != null) {
			n.setDefaultValue((Expression) n.getDefaultValue()
					.accept(this, arg));
		}
		return n;
	}

	public Node visit(ArrayAccessExpr n, A arg) {
		n.setName((Expression) n.getName().accept(this, arg));
		n.setIndex((Expression) n.getIndex().accept(this, arg));
		return n;
	}

	public Node visit(ArrayCreationExpr n, A arg) {
		n.setType((Type) n.getType().accept(this, arg));
		if (n.getDimensions() != null) {
			List<Expression> dimensions = n.getDimensions();
			if (dimensions != null) {
				for (int i = 0; i < dimensions.size(); i++) {
					dimensions.set(i,
							(Expression) dimensions.get(i).accept(this, arg));
				}
				removeNulls(dimensions);
			}
		} else {
			n.setInitializer((ArrayInitializerExpr) n.getInitializer().accept(
					this, arg));
		}
		return n;
	}

	public Node visit(ArrayInitializerExpr n, A arg) {
		if (n.getValues() != null) {
			List<Expression> values = n.getValues();
			if (values != null) {
				for (int i = 0; i < values.size(); i++) {
					values.set(i, (Expression) values.get(i).accept(this, arg));
				}
				removeNulls(values);
			}
		}
		return n;
	}

	public Node visit(AssertStmt n, A arg) {
		n.setCheck((Expression) n.getCheck().accept(this, arg));
		if (n.getMessage() != null) {
			n.setMessage((Expression) n.getMessage().accept(this, arg));
		}
		return n;
	}

	public Node visit(AssignExpr n, A arg) {
		n.setTarget((Expression) n.getTarget().accept(this, arg));
		n.setValue((Expression) n.getValue().accept(this, arg));
		return n;
	}

	public Node visit(BinaryExpr n, A arg) {
		n.setLeft((Expression) n.getLeft().accept(this, arg));
		n.setRight((Expression) n.getRight().accept(this, arg));
		return n;
	}

	public Node visit(BlockStmt n, A arg) {
		List<Statement> stmts = n.getStmts();
		if (stmts != null) {
			for (int i = 0; i < stmts.size(); i++) {
				stmts.set(i, (Statement) stmts.get(i).accept(this, arg));
			}
			removeNulls(stmts);
		}
		return n;
	}

	public Node visit(BooleanLiteralExpr n, A arg) {
		return n;
	}

	public Node visit(BreakStmt n, A arg) {
		return n;
	}

	public Node visit(CastExpr n, A arg) {
		n.setType((Type) n.getType().accept(this, arg));
		n.setExpr((Expression) n.getExpr().accept(this, arg));
		return n;
	}

	@Override
	public Node visit(final CatchClause n, final A arg) {
		n.setExcept((MultiTypeParameter) n.getExcept().accept(this, arg));
		n.setCatchBlock((BlockStmt) n.getCatchBlock().accept(this, arg));
		return n;

	}

	public Node visit(CharLiteralExpr n, A arg) {
		return n;
	}

	public Node visit(ClassExpr n, A arg) {
		n.setType((Type) n.getType().accept(this, arg));
		return n;
	}

	public Node visit(ClassOrInterfaceDeclaration n, A arg) {
		if (n.getJavaDoc() != null) {
			n.setJavaDoc((JavadocComment) n.getJavaDoc().accept(this, arg));
		}
		List<AnnotationExpr> annotations = n.getAnnotations();
		if (annotations != null) {
			for (int i = 0; i < annotations.size(); i++) {
				annotations.set(i,
						(AnnotationExpr) annotations.get(i).accept(this, arg));
			}
			removeNulls(annotations);
		}
		List<TypeParameter> typeParameters = n.getTypeParameters();
		if (typeParameters != null) {
			for (int i = 0; i < typeParameters.size(); i++) {
				typeParameters.set(i, (TypeParameter) typeParameters.get(i)
						.accept(this, arg));
			}
			removeNulls(typeParameters);
		}
		List<ClassOrInterfaceType> extendz = n.getExtends();
		if (extendz != null) {
			for (int i = 0; i < extendz.size(); i++) {
				extendz.set(i,
						(ClassOrInterfaceType) extendz.get(i).accept(this, arg));
			}
			removeNulls(extendz);
		}
		List<ClassOrInterfaceType> implementz = n.getImplements();
		if (implementz != null) {
			for (int i = 0; i < implementz.size(); i++) {
				implementz.set(i, (ClassOrInterfaceType) implementz.get(i)
						.accept(this, arg));
			}
			removeNulls(implementz);
		}
		List<BodyDeclaration> members = n.getMembers();
		if (members != null) {
			for (int i = 0; i < members.size(); i++) {
				members.set(i,
						(BodyDeclaration) members.get(i).accept(this, arg));
			}
			removeNulls(members);
		}
		return n;
	}

	public Node visit(ClassOrInterfaceType n, A arg) {
		if (n.getScope() != null) {
			n.setScope((ClassOrInterfaceType) n.getScope().accept(this, arg));
		}
		List<Type> typeArgs = n.getTypeArgs();
		if (typeArgs != null) {
			for (int i = 0; i < typeArgs.size(); i++) {
				typeArgs.set(i, (Type) typeArgs.get(i).accept(this, arg));
			}
			removeNulls(typeArgs);
		}
		return n;
	}

	public Node visit(CompilationUnit n, A arg) {
		if (n.getPackage() != null) {
			n.setPackage((PackageDeclaration) n.getPackage().accept(this, arg));
		}
		List<ImportDeclaration> imports = n.getImports();
		if (imports != null) {
			for (int i = 0; i < imports.size(); i++) {
				imports.set(i,
						(ImportDeclaration) imports.get(i).accept(this, arg));
			}
			removeNulls(imports);
		}
		List<TypeDeclaration> types = n.getTypes();
		if (types != null) {
			for (int i = 0; i < types.size(); i++) {
				types.set(i, (TypeDeclaration) types.get(i).accept(this, arg));
			}
			removeNulls(types);
		}
		return n;
	}

	public Node visit(ConditionalExpr n, A arg) {
		n.setCondition((Expression) n.getCondition().accept(this, arg));
		n.setThenExpr((Expression) n.getThenExpr().accept(this, arg));
		n.setElseExpr((Expression) n.getElseExpr().accept(this, arg));
		return n;
	}

	public Node visit(ConstructorDeclaration n, A arg) {
		if (n.getJavaDoc() != null) {
			n.setJavaDoc((JavadocComment) n.getJavaDoc().accept(this, arg));
		}
		List<AnnotationExpr> annotations = n.getAnnotations();
		if (annotations != null) {
			for (int i = 0; i < annotations.size(); i++) {
				annotations.set(i,
						(AnnotationExpr) annotations.get(i).accept(this, arg));
			}
			removeNulls(annotations);
		}
		List<TypeParameter> typeParameters = n.getTypeParameters();
		if (typeParameters != null) {
			for (int i = 0; i < typeParameters.size(); i++) {
				typeParameters.set(i, (TypeParameter) typeParameters.get(i)
						.accept(this, arg));
			}
			removeNulls(typeParameters);
		}
		List<Parameter> parameters = n.getParameters();
		if (parameters != null) {
			for (int i = 0; i < parameters.size(); i++) {
				parameters.set(i,
						(Parameter) parameters.get(i).accept(this, arg));
			}
			removeNulls(parameters);
		}
		List<NameExpr> throwz = n.getThrows();
		if (throwz != null) {
			for (int i = 0; i < throwz.size(); i++) {
				throwz.set(i, (NameExpr) throwz.get(i).accept(this, arg));
			}
			removeNulls(throwz);
		}
		n.setBlock((BlockStmt) n.getBlock().accept(this, arg));
		return n;
	}

	public Node visit(ContinueStmt n, A arg) {
		return n;
	}

	public Node visit(DoStmt n, A arg) {
		n.setBody((Statement) n.getBody().accept(this, arg));
		n.setCondition((Expression) n.getCondition().accept(this, arg));
		return n;
	}

	public Node visit(DoubleLiteralExpr n, A arg) {
		return n;
	}

	public Node visit(EmptyMemberDeclaration n, A arg) {
		if (n.getJavaDoc() != null) {
			n.setJavaDoc((JavadocComment) n.getJavaDoc().accept(this, arg));
		}
		return n;
	}

	public Node visit(EmptyStmt n, A arg) {
		return n;
	}

	public Node visit(EmptyTypeDeclaration n, A arg) {
		if (n.getJavaDoc() != null) {
			n.setJavaDoc((JavadocComment) n.getJavaDoc().accept(this, arg));
		}
		return n;
	}

	public Node visit(EnclosedExpr n, A arg) {
		n.setInner((Expression) n.getInner().accept(this, arg));
		return n;
	}

	public Node visit(EnumConstantDeclaration n, A arg) {
		if (n.getJavaDoc() != null) {
			n.setJavaDoc((JavadocComment) n.getJavaDoc().accept(this, arg));
		}
		List<AnnotationExpr> annotations = n.getAnnotations();
		if (annotations != null) {
			for (int i = 0; i < annotations.size(); i++) {
				annotations.set(i,
						(AnnotationExpr) annotations.get(i).accept(this, arg));
			}
			removeNulls(annotations);
		}
		List<Expression> args = n.getArgs();
		if (args != null) {
			for (int i = 0; i < args.size(); i++) {
				args.set(i, (Expression) args.get(i).accept(this, arg));
			}
			removeNulls(args);
		}
		List<BodyDeclaration> classBody = n.getClassBody();
		if (classBody != null) {
			for (int i = 0; i < classBody.size(); i++) {
				classBody.set(i,
						(BodyDeclaration) classBody.get(i).accept(this, arg));
			}
			removeNulls(classBody);
		}
		return n;
	}

	public Node visit(EnumDeclaration n, A arg) {
		if (n.getJavaDoc() != null) {
			n.setJavaDoc((JavadocComment) n.getJavaDoc().accept(this, arg));
		}
		List<AnnotationExpr> annotations = n.getAnnotations();
		if (annotations != null) {
			for (int i = 0; i < annotations.size(); i++) {
				annotations.set(i,
						(AnnotationExpr) annotations.get(i).accept(this, arg));
			}
			removeNulls(annotations);
		}
		List<ClassOrInterfaceType> implementz = n.getImplements();
		if (implementz != null) {
			for (int i = 0; i < implementz.size(); i++) {
				implementz.set(i, (ClassOrInterfaceType) implementz.get(i)
						.accept(this, arg));
			}
			removeNulls(implementz);
		}
		List<EnumConstantDeclaration> entries = n.getEntries();
		if (entries != null) {
			for (int i = 0; i < entries.size(); i++) {
				entries.set(
						i,
						(EnumConstantDeclaration) entries.get(i).accept(this,
								arg));
			}
			removeNulls(entries);
		}
		List<BodyDeclaration> members = n.getMembers();
		if (members != null) {
			for (int i = 0; i < members.size(); i++) {
				members.set(i,
						(BodyDeclaration) members.get(i).accept(this, arg));
			}
			removeNulls(members);
		}
		return n;
	}

	public Node visit(ExplicitConstructorInvocationStmt n, A arg) {
		if (!n.isThis()) {
			if (n.getExpr() != null) {
				n.setExpr((Expression) n.getExpr().accept(this, arg));
			}
		}
		List<Type> typeArgs = n.getTypeArgs();
		if (typeArgs != null) {
			for (int i = 0; i < typeArgs.size(); i++) {
				typeArgs.set(i, (Type) typeArgs.get(i).accept(this, arg));
			}
			removeNulls(typeArgs);
		}
		List<Expression> args = n.getArgs();
		if (args != null) {
			for (int i = 0; i < args.size(); i++) {
				args.set(i, (Expression) args.get(i).accept(this, arg));
			}
			removeNulls(args);
		}
		return n;
	}

	public Node visit(ExpressionStmt n, A arg) {
		n.setExpression((Expression) n.getExpression().accept(this, arg));
		return n;
	}

	public Node visit(FieldAccessExpr n, A arg) {
		n.setScope((Expression) n.getScope().accept(this, arg));
		return n;
	}

	public Node visit(FieldDeclaration n, A arg) {
		if (n.getJavaDoc() != null) {
			n.setJavaDoc((JavadocComment) n.getJavaDoc().accept(this, arg));
		}
		List<AnnotationExpr> annotations = n.getAnnotations();
		if (annotations != null) {
			for (int i = 0; i < annotations.size(); i++) {
				annotations.set(i,
						(AnnotationExpr) annotations.get(i).accept(this, arg));
			}
			removeNulls(annotations);
		}
		n.setType((Type) n.getType().accept(this, arg));
		List<VariableDeclarator> variables = n.getVariables();
		for (int i = 0; i < variables.size(); i++) {
			variables.set(i,
					(VariableDeclarator) variables.get(i).accept(this, arg));
		}
		removeNulls(variables);
		return n;
	}

	public Node visit(ForeachStmt n, A arg) {
		n.setVariable((VariableDeclarationExpr) n.getVariable().accept(this,
				arg));
		n.setIterable((Expression) n.getIterable().accept(this, arg));
		n.setBody((Statement) n.getBody().accept(this, arg));
		return n;
	}

	public Node visit(ForStmt n, A arg) {
		List<Expression> init = n.getInit();
		if (init != null) {
			for (int i = 0; i < init.size(); i++) {
				init.set(i, (Expression) init.get(i).accept(this, arg));
			}
			removeNulls(init);
		}
		if (n.getCompare() != null) {
			n.setCompare((Expression) n.getCompare().accept(this, arg));
		}
		List<Expression> update = n.getUpdate();
		if (update != null) {
			for (int i = 0; i < update.size(); i++) {
				update.set(i, (Expression) update.get(i).accept(this, arg));
			}
			removeNulls(update);
		}
		n.setBody((Statement) n.getBody().accept(this, arg));
		return n;
	}

	public Node visit(IfStmt n, A arg) {
		n.setCondition((Expression) n.getCondition().accept(this, arg));
		n.setThenStmt((Statement) n.getThenStmt().accept(this, arg));
		if (n.getElseStmt() != null) {
			n.setElseStmt((Statement) n.getElseStmt().accept(this, arg));
		}
		return n;
	}

	public Node visit(ImportDeclaration n, A arg) {
		n.setName((NameExpr) n.getName().accept(this, arg));
		return n;
	}

	public Node visit(InitializerDeclaration n, A arg) {
		if (n.getJavaDoc() != null) {
			n.setJavaDoc((JavadocComment) n.getJavaDoc().accept(this, arg));
		}
		n.setBlock((BlockStmt) n.getBlock().accept(this, arg));
		return n;
	}

	public Node visit(InstanceOfExpr n, A arg) {
		n.setExpr((Expression) n.getExpr().accept(this, arg));
		n.setType((Type) n.getType().accept(this, arg));
		return n;
	}

	public Node visit(IntegerLiteralExpr n, A arg) {
		return n;
	}

	public Node visit(IntegerLiteralMinValueExpr n, A arg) {
		return n;
	}

	public Node visit(JavadocComment n, A arg) {
		return n;
	}

	public Node visit(LabeledStmt n, A arg) {
		n.setStmt((Statement) n.getStmt().accept(this, arg));
		return n;
	}

	public Node visit(LongLiteralExpr n, A arg) {
		return n;
	}

	public Node visit(LongLiteralMinValueExpr n, A arg) {
		return n;
	}

	public Node visit(MarkerAnnotationExpr n, A arg) {
		n.setName((NameExpr) n.getName().accept(this, arg));
		return n;
	}

	public Node visit(MemberValuePair n, A arg) {
		n.setValue((Expression) n.getValue().accept(this, arg));
		return n;
	}

	public Node visit(MethodCallExpr n, A arg) {
		if (n.getScope() != null) {
			n.setScope((Expression) n.getScope().accept(this, arg));
		}
		List<Type> typeArgs = n.getTypeArgs();
		if (typeArgs != null) {
			for (int i = 0; i < typeArgs.size(); i++) {
				typeArgs.set(i, (Type) typeArgs.get(i).accept(this, arg));
			}
			removeNulls(typeArgs);
		}
		List<Expression> args = n.getArgs();
		if (args != null) {
			for (int i = 0; i < args.size(); i++) {
				args.set(i, (Expression) args.get(i).accept(this, arg));
			}
			removeNulls(args);
		}
		return n;
	}

	public Node visit(MethodDeclaration n, A arg) {
		if (n.getJavaDoc() != null) {
			n.setJavaDoc((JavadocComment) n.getJavaDoc().accept(this, arg));
		}
		List<AnnotationExpr> annotations = n.getAnnotations();
		if (annotations != null) {
			for (int i = 0; i < annotations.size(); i++) {
				annotations.set(i,
						(AnnotationExpr) annotations.get(i).accept(this, arg));
			}
			removeNulls(annotations);
		}
		List<TypeParameter> typeParameters = n.getTypeParameters();
		if (typeParameters != null) {
			for (int i = 0; i < typeParameters.size(); i++) {
				typeParameters.set(i, (TypeParameter) typeParameters.get(i)
						.accept(this, arg));
			}
			removeNulls(typeParameters);
		}
		n.setType((Type) n.getType().accept(this, arg));
		List<Parameter> parameters = n.getParameters();
		if (parameters != null) {
			for (int i = 0; i < parameters.size(); i++) {
				parameters.set(i,
						(Parameter) parameters.get(i).accept(this, arg));
			}
			removeNulls(parameters);
		}
		List<NameExpr> throwz = n.getThrows();
		if (throwz != null) {
			for (int i = 0; i < throwz.size(); i++) {
				throwz.set(i, (NameExpr) throwz.get(i).accept(this, arg));
			}
			removeNulls(throwz);
		}
		if (n.getBody() != null) {
			n.setBody((BlockStmt) n.getBody().accept(this, arg));
		}
		return n;
	}

	public Node visit(NameExpr n, A arg) {
		return n;
	}

	public Node visit(NormalAnnotationExpr n, A arg) {
		n.setName((NameExpr) n.getName().accept(this, arg));
		List<MemberValuePair> pairs = n.getPairs();
		if (pairs != null) {
			for (int i = 0; i < pairs.size(); i++) {
				pairs.set(i, (MemberValuePair) pairs.get(i).accept(this, arg));
			}
			removeNulls(pairs);
		}
		return n;
	}

	public Node visit(NullLiteralExpr n, A arg) {
		return n;
	}

	public Node visit(ObjectCreationExpr n, A arg) {
		if (n.getScope() != null) {
			n.setScope((Expression) n.getScope().accept(this, arg));
		}
		List<Type> typeArgs = n.getTypeArgs();
		if (typeArgs != null) {
			for (int i = 0; i < typeArgs.size(); i++) {
				typeArgs.set(i, (Type) typeArgs.get(i).accept(this, arg));
			}
			removeNulls(typeArgs);
		}
		n.setType((ClassOrInterfaceType) n.getType().accept(this, arg));
		List<Expression> args = n.getArgs();
		if (args != null) {
			for (int i = 0; i < args.size(); i++) {
				args.set(i, (Expression) args.get(i).accept(this, arg));
			}
			removeNulls(args);
		}
		List<BodyDeclaration> anonymousClassBody = n.getAnonymousClassBody();
		if (anonymousClassBody != null) {
			for (int i = 0; i < anonymousClassBody.size(); i++) {
				anonymousClassBody.set(i, (BodyDeclaration) anonymousClassBody
						.get(i).accept(this, arg));
			}
			removeNulls(anonymousClassBody);
		}
		return n;
	}

	public Node visit(PackageDeclaration n, A arg) {
		List<AnnotationExpr> annotations = n.getAnnotations();
		if (annotations != null) {
			for (int i = 0; i < annotations.size(); i++) {
				annotations.set(i,
						(AnnotationExpr) annotations.get(i).accept(this, arg));
			}
			removeNulls(annotations);
		}
		n.setName((NameExpr) n.getName().accept(this, arg));
		return n;
	}

	public Node visit(Parameter n, A arg) {
		List<AnnotationExpr> annotations = n.getAnnotations();
		if (annotations != null) {
			for (int i = 0; i < annotations.size(); i++) {
				annotations.set(i,
						(AnnotationExpr) annotations.get(i).accept(this, arg));
			}
			removeNulls(annotations);
		}
		n.setType((Type) n.getType().accept(this, arg));
		n.setId((VariableDeclaratorId) n.getId().accept(this, arg));
		return n;
	}
	
	protected Node visit(final BaseParameter n, final A arg) {
		final List<AnnotationExpr> annotations = n.getAnnotations();
		if (annotations != null) {
			for (int i = 0; i < annotations.size(); i++) {
				annotations.set(i, (AnnotationExpr) annotations.get(i).accept(this, arg));
			}
			removeNulls(annotations);
		}
		
		n.setId((VariableDeclaratorId) n.getId().accept(this, arg));
		return n;
	}

	public Node visit(PrimitiveType n, A arg) {
		return n;
	}

	public Node visit(QualifiedNameExpr n, A arg) {
		n.setQualifier((NameExpr) n.getQualifier().accept(this, arg));
		return n;
	}

	public Node visit(ReferenceType n, A arg) {
		n.setType((Type) n.getType().accept(this, arg));
		return n;
	}

	public Node visit(ReturnStmt n, A arg) {
		if (n.getExpr() != null) {
			n.setExpr((Expression) n.getExpr().accept(this, arg));
		}
		return n;
	}

	public Node visit(SingleMemberAnnotationExpr n, A arg) {
		n.setName((NameExpr) n.getName().accept(this, arg));
		n.setMemberValue((Expression) n.getMemberValue().accept(this, arg));
		return n;
	}

	public Node visit(StringLiteralExpr n, A arg) {
		return n;
	}

	public Node visit(SuperExpr n, A arg) {
		if (n.getClassExpr() != null) {
			n.setClassExpr((Expression) n.getClassExpr().accept(this, arg));
		}
		return n;
	}

	public Node visit(SwitchEntryStmt n, A arg) {
		if (n.getLabel() != null) {
			n.setLabel((Expression) n.getLabel().accept(this, arg));
		}
		List<Statement> stmts = n.getStmts();
		if (stmts != null) {
			for (int i = 0; i < stmts.size(); i++) {
				stmts.set(i, (Statement) stmts.get(i).accept(this, arg));
			}
			removeNulls(stmts);
		}
		return n;
	}

	public Node visit(SwitchStmt n, A arg) {
		n.setSelector((Expression) n.getSelector().accept(this, arg));
		List<SwitchEntryStmt> entries = n.getEntries();
		if (entries != null) {
			for (int i = 0; i < entries.size(); i++) {
				entries.set(i,
						(SwitchEntryStmt) entries.get(i).accept(this, arg));
			}
			removeNulls(entries);
		}
		return n;
	}

	public Node visit(SynchronizedStmt n, A arg) {
		n.setExpr((Expression) n.getExpr().accept(this, arg));
		n.setBlock((BlockStmt) n.getBlock().accept(this, arg));
		return n;
	}

	public Node visit(ThisExpr n, A arg) {
		if (n.getClassExpr() != null) {
			n.setClassExpr((Expression) n.getClassExpr().accept(this, arg));
		}
		return n;
	}

	public Node visit(ThrowStmt n, A arg) {
		n.setExpr((Expression) n.getExpr().accept(this, arg));
		return n;
	}

	public Node visit(TryStmt n, A arg) {
		n.setTryBlock((BlockStmt) n.getTryBlock().accept(this, arg));
		List<CatchClause> catchs = n.getCatchs();
		if (catchs != null) {
			for (int i = 0; i < catchs.size(); i++) {
				catchs.set(i, (CatchClause) catchs.get(i).accept(this, arg));
			}
			removeNulls(catchs);
		}
		if (n.getFinallyBlock() != null) {
			n.setFinallyBlock((BlockStmt) n.getFinallyBlock().accept(this, arg));
		}
		return n;
	}

	public Node visit(TypeDeclarationStmt n, A arg) {
		n.setTypeDeclaration((TypeDeclaration) n.getTypeDeclaration().accept(
				this, arg));
		return n;
	}

	public Node visit(TypeParameter n, A arg) {
		List<ClassOrInterfaceType> typeBound = n.getTypeBound();
		if (typeBound != null) {
			for (int i = 0; i < typeBound.size(); i++) {
				typeBound.set(i, (ClassOrInterfaceType) typeBound.get(i)
						.accept(this, arg));
			}
			removeNulls(typeBound);
		}
		return n;
	}

	public Node visit(UnaryExpr n, A arg) {
		n.setExpr((Expression) n.getExpr().accept(this, arg));
		return n;
	}

	public Node visit(VariableDeclarationExpr n, A arg) {
		List<AnnotationExpr> annotations = n.getAnnotations();
		if (annotations != null) {
			for (int i = 0; i < annotations.size(); i++) {
				annotations.set(i,
						(AnnotationExpr) annotations.get(i).accept(this, arg));
			}
			removeNulls(annotations);
		}
		n.setType((Type) n.getType().accept(this, arg));
		List<VariableDeclarator> vars = n.getVars();
		for (int i = 0; i < vars.size(); i++) {
			vars.set(i, (VariableDeclarator) vars.get(i).accept(this, arg));
		}
		removeNulls(vars);
		return n;
	}

	public Node visit(VariableDeclarator n, A arg) {
		n.setId((VariableDeclaratorId) n.getId().accept(this, arg));
		if (n.getInit() != null) {
			n.setInit((Expression) n.getInit().accept(this, arg));
		}
		return n;
	}

	public Node visit(VariableDeclaratorId n, A arg) {
		return n;
	}

	public Node visit(VoidType n, A arg) {
		return n;
	}

	public Node visit(WhileStmt n, A arg) {
		n.setCondition((Expression) n.getCondition().accept(this, arg));
		n.setBody((Statement) n.getBody().accept(this, arg));
		return n;
	}

	public Node visit(WildcardType n, A arg) {
		if (n.getExtends() != null) {
			n.setExtends((ReferenceType) n.getExtends().accept(this, arg));
		}
		if (n.getSuper() != null) {
			n.setSuper((ReferenceType) n.getSuper().accept(this, arg));
		}
		return n;
	}

	public Node visit(BlockComment n, A arg) {
		return n;
	}

	public Node visit(LineComment n, A arg) {
		return n;
	}
}
