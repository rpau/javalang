/*
 * Copyright (C) 2013 Raquel Pau and Albert Coroleu.
 * 
 * Walkmod is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * Walkmod is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Walkmod. If
 * not, see <http://www.gnu.org/licenses/>.
 */
package org.walkmod.javalang.visitors;

import java.util.Iterator;
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
import org.walkmod.javalang.ast.body.ModifierSet;
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

/**
 * @author Julio Vilmar Gesser
 */
public final class DumpVisitor implements VoidVisitor<Object> {

	private List<Comment> comments = new LinkedList<Comment>();

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public void setIndentationLevel(int level) {
		printer.indent(level);
	}

	public void setIndentationSize(int size) {
		printer.setSize(size);
	}

	public void setIndentationChar(char indentationChar) {
		printer.setIndentationChar(indentationChar);
	}

	private static class SourcePrinter {

		private int level = 0;

		private String indentationString = "    ";

		private char indentationChar = ' ';

		private boolean indented = false;

		private final StringBuilder buf = new StringBuilder();

		public void indent() {
			level++;
		}

		public void indent(int level) {
			this.level = level;
		}

		public void setIndentationChar(char indentationChar) {
			this.indentationChar = indentationChar;
		}

		public void setSize(int size) {

			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < size; i++) {
				buffer.append(indentationChar);
			}
			indentationString = buffer.toString();
		}

		public void unindent() {
			level--;
		}

		private void makeIndent() {
			for (int i = 0; i < level; i++) {
				buf.append(indentationString);
			}
		}

		public void print(String arg) {
			if (!indented) {
				makeIndent();
				indented = true;
			}
			buf.append(arg);
		}

		public void printLn(String arg) {
			print(arg);
			printLn();
		}

		public void printLn() {
			buf.append("\n");
			indented = false;
		}

		public String getSource() {
			return buf.toString();
		}

		@Override
		public String toString() {
			return getSource();
		}
	}

	private final SourcePrinter printer = new SourcePrinter();

	public String getSource() {
		return printer.getSource();
	}

	private void printModifiers(int modifiers) {
		if (ModifierSet.isPrivate(modifiers)) {
			printer.print("private ");
		}
		if (ModifierSet.isProtected(modifiers)) {
			printer.print("protected ");
		}
		if (ModifierSet.isPublic(modifiers)) {
			printer.print("public ");
		}
		if (ModifierSet.isAbstract(modifiers)) {
			printer.print("abstract ");
		}
		if (ModifierSet.isStatic(modifiers)) {
			printer.print("static ");
		}
		if (ModifierSet.isFinal(modifiers)) {
			printer.print("final ");
		}
		if (ModifierSet.isNative(modifiers)) {
			printer.print("native ");
		}
		if (ModifierSet.isStrictfp(modifiers)) {
			printer.print("strictfp ");
		}
		if (ModifierSet.isSynchronized(modifiers)) {
			printer.print("synchronized ");
		}
		if (ModifierSet.isTransient(modifiers)) {
			printer.print("transient ");
		}
		if (ModifierSet.isVolatile(modifiers)) {
			printer.print("volatile ");
		}
	}

	private <T extends Node, K extends Node> void printChildrenNodes(K parent,
			List<T> members, String sepChar, Object arg) {
		Node previous = null;
		if (members != null) {
			Iterator<T> it = members.iterator();
			while (it.hasNext()) {
				T member = it.next();
				if (previous != null) {

					JavadocComment comment = null;
					if (member instanceof BodyDeclaration) {
						comment = ((BodyDeclaration) member).getJavaDoc();
					}
					List<Node> comments = printInnerComments(previous, member,
							arg);
					if (comments.isEmpty()) {
						if (!previous.isNewNode() && !member.isNewNode()) {
							int start = previous.getEndLine();
							if (previous instanceof SwitchEntryStmt) {
								List<Statement> stmts = ((SwitchEntryStmt) previous)
										.getStmts();
								if (stmts != null && !stmts.isEmpty()) {
									start++;
								}
							}
							int end = member.getBeginLine();

							if (comment != null && !comment.isNewNode()) {
								end = comment.getBeginLine();
							}

							for (int i = start; i < end; i++) {
								printer.printLn();
							}
						} else {
							printer.printLn();
						}
					} else {
						previous = comments.get(comments.size() - 1);
					}
				}
				member.accept(this, arg);
				if (sepChar != null) {
					if (it.hasNext()) {
						printer.print(sepChar);
					}

				}
				previous = member;
			}
			if (previous != null && previous.isNewNode()) {
				printSameLineComments(previous, arg);
			}
		}
		if (sepChar == null) {
			if (previous == null || !(previous instanceof SwitchEntryStmt)) {
				if (!parent.isNewNode()) {
					if(parent.getEndLine() != previous.getEndLine()){
						printer.printLn();
					}
				} else {
					printer.printLn();
				}
			}
		}
	}

	private void printParameterList(List<Parameter> list, Object arg) {
		Node previous = null;
		Iterator<Parameter> it = list.iterator();
		while (it.hasNext()) {
			Parameter member = it.next();
			if (previous != null) {
				List<Node> comments = printInnerComments(previous, member, arg);
				if (comments.isEmpty()) {
					if (!previous.isNewNode() && !member.isNewNode()) {
						int start = previous.getEndLine();
						int end = member.getBeginLine();
						for (int i = start; i < end; i++) {
							printer.printLn();
						}
					} else {
						printer.printLn();
					}
				} else {
					previous = comments.get(comments.size() - 1);
				}
			}
			member.accept(this, arg);
			if (it.hasNext()) {
				printer.print(", ");
			}
			previous = member;
		}
		if (previous != null && previous.isNewNode()) {
			printSameLineComments(previous, arg);
		}

	}

	private void printMemberAnnotations(List<AnnotationExpr> annotations,
			Object arg) {
		if (annotations != null) {
			Node previous = null;
			Iterator<AnnotationExpr> it = annotations.iterator();
			while (it.hasNext()) {
				AnnotationExpr current = it.next();
				if (previous != null) {
					if (!previous.isNewNode() && !current.isNewNode()) {
						addEntersBetween(previous, current);
					} else {
						printer.printLn();
					}
				}
				current.accept(this, arg);
				previous = current;
			}
			if (!annotations.isEmpty()) {
				printer.printLn();
			}
		}
	}

	private void printAnnotations(List<AnnotationExpr> annotations, Object arg) {
		if (annotations != null) {
			Node previous = null;
			Iterator<AnnotationExpr> it = annotations.iterator();
			while (it.hasNext()) {
				AnnotationExpr current = it.next();
				if (previous != null && !previous.isNewNode()
						&& !current.isNewNode()) {
					addEntersBetween(previous, current);
				}
				current.accept(this, arg);
				printer.print(" ");
				previous = current;
			}
		}
	}

	private void printTypeArgs(List<Type> args, Object arg) {
		if (args != null) {
			printer.print("<");
			for (Iterator<Type> i = args.iterator(); i.hasNext();) {
				Type t = i.next();
				t.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
			printer.print(">");
		}
	}

	private void printTypeParameters(List<TypeParameter> args, Object arg) {
		if (args != null) {
			printer.print("<");
			for (Iterator<TypeParameter> i = args.iterator(); i.hasNext();) {
				TypeParameter t = i.next();
				t.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
			printer.print(">");
		}
	}

	private void printArguments(List<Expression> args, Object arg) {
		printer.print("(");
		if (args != null) {
			for (Iterator<Expression> i = args.iterator(); i.hasNext();) {
				Expression e = i.next();
				e.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}
		printer.print(")");
	}

	private void printJavadoc(JavadocComment javadoc, Object arg) {
		if (javadoc != null) {
			javadoc.accept(this, arg);
		}
	}

	private List<Node> printPreviousComments(Node n, Object arg) {
		List<Node> printedComments = new LinkedList<Node>();
		if (comments != null) {
			Iterator<Comment> it = comments.iterator();
			Node previous = null;
			if (n instanceof BodyDeclaration) {
				previous = ((BodyDeclaration) n).getJavaDoc();
			}
			boolean isPrevious = true;
			while (it.hasNext() && isPrevious) {
				Comment c = it.next();
				isPrevious = !n.isNewNode() && !c.isNewNode()
						&& c.isPreviousThan(n);
				if (isPrevious) {

					if (previous != null) {
						addEntersBetween(previous, c);
					}

					c.accept(this, arg);
					printedComments.add(c);
					previous = c;

					it.remove();
				}
			}
			if (previous != null && !previous.isNewNode()) {
				addEntersBetween(previous, n);
			}
		}
		return printedComments;
	}

	private void addSpacesBetween(Node n1, Node n2) {

		if (n1.getEndLine() == n2.getBeginLine()) {
			int endColumn = n1.getEndColumn();
			int beginLine = n2.getBeginColumn();
			if (n1 instanceof Statement) {
				endColumn++;
			}
			for (int i = endColumn; i < beginLine; i++) {
				printer.print(" ");
			}
		}
	}

	private List<Node> printInnerComments(Node previousNode, Node nextNode,
			Object arg) {
		List<Node> printedComments = new LinkedList<Node>();
		if (comments != null) {
			Iterator<Comment> it = comments.iterator();
			Node previous = null;
			if (nextNode instanceof BodyDeclaration) {
				if (!nextNode.isNewNode()) {
					JavadocComment javadoc = ((BodyDeclaration) nextNode)
							.getJavaDoc();
					if (javadoc != null && !javadoc.isNewNode()) {
						nextNode = javadoc;
					}
				}
			}
			while (it.hasNext()) {
				Comment c = it.next();
				if (!nextNode.isNewNode() && !c.isNewNode()
						&& c.isPreviousThan(nextNode)) {

					if (previous != null) {
						addEntersBetween(previous, c);
						addSpacesBetween(previous, c);
					} else {
						addEntersBetween(previousNode, c);
						addSpacesBetween(previousNode, c);
					}
					c.accept(this, arg);
					printedComments.add(c);
					previous = c;

					it.remove();
				}
			}
			if (previous != null) {
				addEntersBetween(previous, nextNode);
			}
		}
		return printedComments;
	}

	private List<Node> printSameLineComments(Node n, Object arg) {
		List<Node> printedComments = new LinkedList<Node>();
		if (comments != null) {
			Iterator<Comment> it = comments.iterator();
			while (it.hasNext()) {
				Comment c = it.next();
				if (!n.isNewNode() && !c.isNewNode()) {
					if (n instanceof BlockStmt) {
						List<Statement> stmts = ((BlockStmt) n).getStmts();

						if (n.getBeginLine() == c.getBeginLine()) {
							if (stmts == null || stmts.isEmpty()) {
								c.accept(this, arg);
								printedComments.add(c);
								it.remove();
							} else {
								Statement stmt = stmts.iterator().next();
								if (!stmt.isNewNode()
										&& stmt.getBeginLine() > n
												.getBeginLine()) {
									for (int i = n.getBeginColumn() + 1; i < c
											.getBeginColumn(); i++) {
										printer.print(" ");
									}
									c.accept(this, arg);
									printedComments.add(c);

									it.remove();
								}
							}
						}
					} else if (n instanceof SwitchEntryStmt) {
						List<Statement> stmts = ((SwitchEntryStmt) n)
								.getStmts();

						if (n.getBeginLine() == c.getBeginLine()) {
							if (stmts == null || stmts.isEmpty()) {
								c.accept(this, arg);
								printedComments.add(c);
								it.remove();
							} else {
								Statement stmt = stmts.iterator().next();
								if (!stmt.isNewNode()
										&& stmt.getBeginLine() > n
												.getBeginLine()) {
									for (int i = n.getBeginColumn() + 1; i < c
											.getBeginColumn(); i++) {
										printer.print(" ");
									}
									c.accept(this, arg);
									printedComments.add(c);

									it.remove();
								}
							}
						}
					} else {
						if (n.getEndLine() == c.getBeginLine()) {
							addSpacesBetween(n, c);
							c.accept(this, arg);
							printedComments.add(c);

							it.remove();
						}
					}
				}

			}
		}
		return printedComments;

	}

	private List<Node> printMissingComments(Node n, Object arg) {
		List<Node> printedComments = new LinkedList<Node>();
		if (comments != null) {
			Iterator<Comment> it = comments.iterator();
			while (it.hasNext()) {
				Comment c = it.next();
				if (!n.isNewNode() && !c.isNewNode()) {

					c.accept(this, arg);
					printedComments.add(c);

					it.remove();
				}
			}
		}
		return printedComments;
	}

	private void addEntersBetween(Node n1, Node n2) {
		int end = n2.getBeginLine();
		if (n2 instanceof BodyDeclaration) {
			JavadocComment jc = ((BodyDeclaration) n2).getJavaDoc();
			if (jc != null && !jc.isNewNode()) {
				end = jc.getBeginLine();
			}
		}
		if (n1.getEndLine() < n2.getBeginLine()) {
			int start = n1.getEndLine();
			if (n1 instanceof LineComment || n1 instanceof JavadocComment
					|| n1 instanceof SwitchEntryStmt) {
				start = start + 1;
			}
			if (start > end) {
				end = n2.getBeginLine();
			}
			for (int i = start; i < end; i++) {
				printer.printLn();
			}

		}
	}

	private Node printContainingComments(Node n, int startingLine, Object arg) {
		Node lastNode = null;
		if (comments != null) {
			Iterator<Comment> it = comments.iterator();
			while (it.hasNext()) {
				Comment c = it.next();

				if (!n.isNewNode() && !c.isNewNode() && n.contains(c)) {

					if (lastNode != null) {
						addEntersBetween(lastNode, c);
					} else {
						if (startingLine != -1
								&& startingLine < c.getBeginLine()) {
							for (int i = startingLine; i < c.getBeginLine(); i++) {
								printer.printLn();
							}

						}
					}
					c.accept(this, arg);

					it.remove();
					lastNode = c;
				}
			}
		}

		return lastNode;
	}

	private List<Node> getContainingComments(Node n) {
		List<Node> result = new LinkedList<Node>();
		if (comments != null) {
			Iterator<Comment> it = comments.iterator();
			while (it.hasNext()) {
				Comment c = it.next();

				if (!n.isNewNode() && !c.isNewNode() && n.contains(c)) {
					result.add(c);
				}
			}
		}
		return result;
	}

	public void visit(CompilationUnit n, Object arg) {

		if (n.getComments() != null) {
			comments = new LinkedList<Comment>(n.getComments());
		}

		Iterator<Comment> it = comments.iterator();
		while (it.hasNext()) {
			Comment c = it.next();
			if (c instanceof JavadocComment) {
				it.remove();
			}
		}
		if (n.getPackage() != null) {
			n.getPackage().accept(this, arg);
		}
		List<Node> comments = null;
		if (n.getImports() != null) {
			ImportDeclaration previous = null;
			for (ImportDeclaration i : n.getImports()) {
				if (previous != null) {
					if (previous.isNewNode() || i.isNewNode()) {
						printer.printLn();
					} else {
						if (comments == null || comments.isEmpty()) {
							int beginLine = previous.getEndLine();
							int lastLine = i.getBeginLine();
							for (int j = beginLine; j < lastLine; j++) {
								printer.printLn();
							}
						}
					}
				}
				i.accept(this, arg);
				comments = printSameLineComments(i, arg);
				previous = i;
			}
			printer.printLn();
			printer.printLn();
		}
		if (n.getTypes() != null) {
			for (Iterator<TypeDeclaration> i = n.getTypes().iterator(); i
					.hasNext();) {
				TypeDeclaration next = i.next();
				next.accept(this, arg);
				comments = printSameLineComments(next, arg);
				if (comments == null || comments.isEmpty()) {
					printer.printLn();
					if (i.hasNext()) {
						printer.printLn();
					}
				}
			}
		}
		printMissingComments(n, arg);
	}

	public void visit(PackageDeclaration n, Object arg) {
		List<Node> comments = printPreviousComments(n, arg);

		List<AnnotationExpr> annotations = n.getAnnotations();
		/*
		 * if (comments != null && !comments.isEmpty()) { Node lastComment =
		 * comments.get(comments.size() - 1); Node firstNode = n.getName(); if
		 * (!lastComment.isNewNode()) { if (annotations != null &&
		 * !annotations.isEmpty()) { firstNode = annotations.get(0); if
		 * (!firstNode.isNewNode()) { addEntersBetween(lastComment, firstNode);
		 * } }
		 * 
		 * } }
		 */
		printAnnotations(annotations, arg);
		printer.print("package ");
		n.getName().accept(this, arg);
		printer.printLn(";");
		printSameLineComments(n, arg);
		printer.printLn();
	}

	public void visit(NameExpr n, Object arg) {
		printPreviousComments(n, arg);
		printer.print(n.getName());
	}

	public void visit(QualifiedNameExpr n, Object arg) {
		printPreviousComments(n, arg);
		n.getQualifier().accept(this, arg);
		printer.print(".");
		printer.print(n.getName());
	}

	public void visit(ImportDeclaration n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("import ");
		if (n.isStatic()) {
			printer.print("static ");
		}
		n.getName().accept(this, arg);
		if (n.isAsterisk()) {
			printer.print(".*");
		}
		printer.print(";");
	}

	public void visit(ClassOrInterfaceDeclaration n, Object arg) {
		Node comment = n.getJavaDoc();
		if (comment != null) {
			printPreviousComments(comment, arg);
		}

		printJavadoc((JavadocComment) comment, arg);

		printPreviousComments(n, arg);

		int beginLineMembers = n.getBeginLine();
		List<AnnotationExpr> annotations = n.getAnnotations();
		printMemberAnnotations(annotations, arg);
		if (annotations != null && !annotations.isEmpty()) {
			AnnotationExpr last = annotations.get(annotations.size() - 1);
			if (!last.isNewNode()) {
				beginLineMembers = last.getEndLine() + 1;
			}
		}

		List<ClassOrInterfaceType> extendsList = n.getExtends();
		List<ClassOrInterfaceType> implementsList = n.getImplements();

		List<TypeParameter> typeParameters = n.getTypeParameters();
		if (typeParameters != null && !typeParameters.isEmpty()) {
			TypeParameter first = typeParameters.get(0);
			printPreviousComments(first, arg);
		}
		if (extendsList != null && !extendsList.isEmpty()) {
			printPreviousComments(extendsList.get(0), arg);
		}
		if (implementsList != null && !implementsList.isEmpty()) {
			printPreviousComments(implementsList.get(0), arg);
		}

		printModifiers(n.getModifiers());
		if (n.isInterface()) {
			printer.print("interface ");
		} else {
			printer.print("class ");
		}
		printer.print(n.getName());
		printTypeParameters(typeParameters, arg);
		if (typeParameters != null && !typeParameters.isEmpty()) {
			TypeParameter last = typeParameters.get(typeParameters.size() - 1);
			if (!last.isNewNode()) {
				beginLineMembers = last.getEndLine();
			}
		}

		if (extendsList != null) {
			printer.print(" extends ");
			ClassOrInterfaceType c = null;
			for (Iterator<ClassOrInterfaceType> i = extendsList.iterator(); i
					.hasNext();) {
				c = i.next();
				c.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
			if (c != null && !c.isNewNode()) {
				beginLineMembers = c.getEndLine();
			}
		}
		if (implementsList != null) {
			printer.print(" implements ");
			ClassOrInterfaceType c = null;
			for (Iterator<ClassOrInterfaceType> i = implementsList.iterator(); i
					.hasNext();) {
				c = i.next();
				c.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
			if (c != null && !c.isNewNode()) {
				beginLineMembers = c.getEndLine();
			}
		}
		printer.print(" {");

		List<BodyDeclaration> members = n.getMembers();
		if (members != null && !members.isEmpty()) {
			// printer.printLn();
			printer.indent();
			printFirstBlankLines(n, members, beginLineMembers);
			printChildrenNodes(n, members, null, arg);
			printEntersAfterMembersAndBeforeComments(n, members);

		}
		printContainingCommentsAndEnters(n, members, arg, beginLineMembers);
		printer.unindent();
		printer.print("}");
	}

	private void printContainingCommentsAndEnters(Node n, List<?> members,
			Object arg, int beginLineMembers) {
		Node lastNode = null;
		if (members != null && !members.isEmpty()) {
			lastNode = printContainingComments(n, -1, arg);
		} else {
			lastNode = printContainingComments(n, beginLineMembers, arg);
		}
		if (lastNode == null && members != null && !members.isEmpty()) {
			lastNode = (Node) members.get(members.size() - 1);
		}
		if (!n.isNewNode()) {
			if (lastNode != null && !lastNode.isNewNode()) {
				int startLine = lastNode.getEndLine();
				if (lastNode instanceof BlockComment) {
					startLine--;
				}
				if (beginLineMembers > startLine) {
					startLine = beginLineMembers - 1;
				}

				int endLine = n.getEndLine();
				for (int i = startLine + 1; i < endLine; i++) {
					printer.printLn();
				}

			} else {
				int start = n.getBeginLine();
				if (beginLineMembers > start) {
					start = beginLineMembers /*- 1*/;
				}
				int end = n.getEndLine();
				for (int i = start; i < end; i++) {
					printer.printLn();
				}
			}
		}
	}

	public void visit(EmptyTypeDeclaration n, Object arg) {
		printPreviousComments(n, arg);
		JavadocComment comment = n.getJavaDoc();
		printJavadoc(comment, arg);
		if (comment != null && !n.isNewNode() && !comment.isNewNode()) {
			int start = comment.getEndLine();
			int end = n.getBeginLine();
			for (int i = start + 1; i < end; i++) {
				printer.printLn();
			}
		}
		printer.print(";");
	}

	public void visit(JavadocComment n, Object arg) {
		printer.print("/**");
		printer.print(n.getContent());
		printer.printLn("*/");
	}

	public void visit(ClassOrInterfaceType n, Object arg) {
		printPreviousComments(n, arg);

		if (n.getAnnotations() != null) {
			for (AnnotationExpr ae : n.getAnnotations()) {
				ae.accept(this, arg);
				printer.print(" ");
			}
		}

		if (n.getScope() != null) {
			n.getScope().accept(this, arg);
			printer.print(".");
		}
		printer.print(n.getName());
		printTypeArgs(n.getTypeArgs(), arg);
	}

	public void visit(TypeParameter n, Object arg) {
		printPreviousComments(n, arg);
		if (n.getAnnotations() != null) {
			for (AnnotationExpr ann : n.getAnnotations()) {
				ann.accept(this, arg);
				printer.print(" ");
			}
		}
		printer.print(n.getName());
		if (n.getTypeBound() != null) {
			printer.print(" extends ");
			for (Iterator<ClassOrInterfaceType> i = n.getTypeBound().iterator(); i
					.hasNext();) {
				ClassOrInterfaceType c = i.next();
				c.accept(this, arg);
				if (i.hasNext()) {
					printer.print(" & ");
				}
			}
		}
	}

	public void visit(PrimitiveType n, Object arg) {
		printPreviousComments(n, arg);
		if (n.getAnnotations() != null) {
			for (AnnotationExpr ae : n.getAnnotations()) {
				ae.accept(this, arg);
				printer.print(" ");
			}
		}
		switch (n.getType()) {
		case Boolean:
			printer.print("boolean");
			break;
		case Byte:
			printer.print("byte");
			break;
		case Char:
			printer.print("char");
			break;
		case Double:
			printer.print("double");
			break;
		case Float:
			printer.print("float");
			break;
		case Int:
			printer.print("int");
			break;
		case Long:
			printer.print("long");
			break;
		case Short:
			printer.print("short");
			break;
		}
	}

	public void visit(ReferenceType n, Object arg) {
		printPreviousComments(n, arg);
		if (n.getAnnotations() != null) {
			for (AnnotationExpr ae : n.getAnnotations()) {
				ae.accept(this, arg);
				printer.print(" ");
			}
		}
		n.getType().accept(this, arg);
		List<List<AnnotationExpr>> arraysAnnotations = n.getArraysAnnotations();
		for (int i = 0; i < n.getArrayCount(); i++) {
			if (arraysAnnotations != null && i < arraysAnnotations.size()) {
				List<AnnotationExpr> annotations = arraysAnnotations.get(i);
				if (annotations != null) {
					for (AnnotationExpr ae : annotations) {
						printer.print(" ");
						ae.accept(this, arg);

					}
				}
			}
			printer.print("[]");
		}
	}

	public void visit(WildcardType n, Object arg) {
		printPreviousComments(n, arg);
		if (n.getAnnotations() != null) {
			for (AnnotationExpr ae : n.getAnnotations()) {
				printer.print(" ");
				ae.accept(this, arg);
			}
		}
		printer.print("?");
		if (n.getExtends() != null) {
			printer.print(" extends ");
			n.getExtends().accept(this, arg);
		}
		if (n.getSuper() != null) {
			printer.print(" super ");
			n.getSuper().accept(this, arg);
		}

	}

	public void visit(FieldDeclaration n, Object arg) {
		printPreviousComments(n, arg);
		JavadocComment comment = n.getJavaDoc();
		printJavadoc(comment, arg);

		printPreviousComments(n, arg);
		printMemberAnnotations(n.getAnnotations(), arg);
		Type type = n.getType();
		printPreviousComments(type, arg);

		printModifiers(n.getModifiers());
		type.accept(this, arg);
		printer.print(" ");
		for (Iterator<VariableDeclarator> i = n.getVariables().iterator(); i
				.hasNext();) {
			VariableDeclarator var = i.next();
			var.accept(this, arg);
			if (i.hasNext()) {
				printer.print(", ");
			}
		}
		printer.print(";");
	}

	public void visit(VariableDeclarator n, Object arg) {
		printPreviousComments(n, arg);
		n.getId().accept(this, arg);
		if (n.getInit() != null) {
			printer.print(" = ");
			n.getInit().accept(this, arg);
		}
	}

	public void visit(VariableDeclaratorId n, Object arg) {
		printPreviousComments(n, arg);
		printer.print(n.getName());
		for (int i = 0; i < n.getArrayCount(); i++) {
			printer.print("[]");
		}
	}

	public void visit(ArrayInitializerExpr n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("{");
		if (n.getValues() != null) {
			printer.print(" ");
			for (Iterator<Expression> i = n.getValues().iterator(); i.hasNext();) {
				Expression expr = i.next();
				expr.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
			printer.print(" ");
		}
		printer.print("}");
	}

	public void visit(VoidType n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("void");
	}

	public void visit(ArrayAccessExpr n, Object arg) {
		printPreviousComments(n, arg);
		n.getName().accept(this, arg);
		printer.print("[");
		n.getIndex().accept(this, arg);
		printer.print("]");
	}

	public void visit(ArrayCreationExpr n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("new ");
		n.getType().accept(this, arg);
		List<List<AnnotationExpr>> arraysAnnotations = n.getArraysAnnotations();
		if (n.getDimensions() != null) {
			int j = 0;
			for (Expression dim : n.getDimensions()) {

				if (arraysAnnotations != null && j < arraysAnnotations.size()) {
					List<AnnotationExpr> annotations = arraysAnnotations.get(j);
					if (annotations != null) {
						for (AnnotationExpr ae : annotations) {
							printer.print(" ");
							ae.accept(this, arg);
						}
					}
				}
				printer.print("[");
				dim.accept(this, arg);
				printer.print("]");
				j++;
			}
			for (int i = 0; i < n.getArrayCount(); i++) {
				if (arraysAnnotations != null && i < arraysAnnotations.size()) {

					List<AnnotationExpr> annotations = arraysAnnotations.get(i);
					if (annotations != null) {
						for (AnnotationExpr ae : annotations) {
							printer.print(" ");
							ae.accept(this, arg);

						}
					}
				}
				printer.print("[]");
			}

		} else {
			for (int i = 0; i < n.getArrayCount(); i++) {
				if (arraysAnnotations != null && i < arraysAnnotations.size()) {
					List<AnnotationExpr> annotations = arraysAnnotations.get(i);
					if (annotations != null) {
						for (AnnotationExpr ae : annotations) {
							ae.accept(this, arg);
							printer.print(" ");
						}
					}
				}
				printer.print("[]");
			}
			printer.print(" ");
			n.getInitializer().accept(this, arg);
		}
	}

	public void visit(AssignExpr n, Object arg) {
		printPreviousComments(n, arg);
		n.getTarget().accept(this, arg);
		printer.print(" ");
		switch (n.getOperator()) {
		case assign:
			printer.print("=");
			break;
		case and:
			printer.print("&=");
			break;
		case or:
			printer.print("|=");
			break;
		case xor:
			printer.print("^=");
			break;
		case plus:
			printer.print("+=");
			break;
		case minus:
			printer.print("-=");
			break;
		case rem:
			printer.print("%=");
			break;
		case slash:
			printer.print("/=");
			break;
		case star:
			printer.print("*=");
			break;
		case lShift:
			printer.print("<<=");
			break;
		case rSignedShift:
			printer.print(">>=");
			break;
		case rUnsignedShift:
			printer.print(">>>=");
			break;
		}
		printer.print(" ");
		n.getValue().accept(this, arg);
	}

	public void visit(BinaryExpr n, Object arg) {
		// printPreviousComments(n, arg);
		n.getLeft().accept(this, arg);
		printer.print(" ");

		Node lastNode = n.getLeft();
		List<Node> comments = printInnerComments(n.getLeft(), n.getRight(), arg);
		if (comments != null && !comments.isEmpty()) {
			Node comment = comments.get(comments.size() - 1);
			if (!comment.isNewNode()) {
				lastNode = comment;
			}
		}
		if (!lastNode.isNewNode() && !n.getRight().isNewNode()) {
			addEntersBetween(lastNode, n.getRight());
		}
		switch (n.getOperator()) {
		case or:
			printer.print("||");
			break;
		case and:
			printer.print("&&");
			break;
		case binOr:
			printer.print("|");
			break;
		case binAnd:
			printer.print("&");
			break;
		case xor:
			printer.print("^");
			break;
		case equals:
			printer.print("==");
			break;
		case notEquals:
			printer.print("!=");
			break;
		case less:
			printer.print("<");
			break;
		case greater:
			printer.print(">");
			break;
		case lessEquals:
			printer.print("<=");
			break;
		case greaterEquals:
			printer.print(">=");
			break;
		case lShift:
			printer.print("<<");
			break;
		case rSignedShift:
			printer.print(">>");
			break;
		case rUnsignedShift:
			printer.print(">>>");
			break;
		case plus:
			printer.print("+");
			break;
		case minus:
			printer.print("-");
			break;
		case times:
			printer.print("*");
			break;
		case divide:
			printer.print("/");
			break;
		case remainder:
			printer.print("%");
			break;
		}
		printer.print(" ");
		n.getRight().accept(this, arg);
	}

	public void visit(CastExpr n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("(");
		n.getType().accept(this, arg);
		printer.print(") ");
		n.getExpr().accept(this, arg);
	}

	public void visit(ClassExpr n, Object arg) {
		printPreviousComments(n, arg);
		n.getType().accept(this, arg);
		printer.print(".class");
	}

	public void visit(ConditionalExpr n, Object arg) {
		printPreviousComments(n, arg);
		n.getCondition().accept(this, arg);

		Node lastNode = n.getCondition();
		List<Node> comments = printInnerComments(n.getCondition(),
				n.getThenExpr(), arg);
		if (comments != null && !comments.isEmpty()) {
			Node comment = comments.get(comments.size() - 1);
			if (!comment.isNewNode()) {
				lastNode = comment;
			}
		}
		if (!lastNode.isNewNode() && !n.getThenExpr().isNewNode()) {
			addEntersBetween(lastNode, n.getThenExpr());
		}
		printer.print(" ? ");
		n.getThenExpr().accept(this, arg);

		lastNode = n.getThenExpr();
		comments = printInnerComments(lastNode, n.getElseExpr(), arg);
		if (comments != null && !comments.isEmpty()) {
			Node comment = comments.get(comments.size() - 1);
			if (!comment.isNewNode()) {
				lastNode = comment;
			}
		}
		if (!lastNode.isNewNode() && !n.getElseExpr().isNewNode()) {
			addEntersBetween(lastNode, n.getElseExpr());
		}
		printer.print(" : ");
		n.getElseExpr().accept(this, arg);
	}

	public void visit(EnclosedExpr n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("(");
		if (n.getInner() != null) {
			n.getInner().accept(this, arg);
		}
		printer.print(")");
	}

	public void visit(FieldAccessExpr n, Object arg) {
		printPreviousComments(n, arg);
		n.getScope().accept(this, arg);
		printer.print(".");
		printer.print(n.getField());
	}

	public void visit(InstanceOfExpr n, Object arg) {
		printPreviousComments(n, arg);
		n.getExpr().accept(this, arg);
		printer.print(" instanceof ");
		n.getType().accept(this, arg);
	}

	public void visit(CharLiteralExpr n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("'");
		printer.print(n.getValue());
		printer.print("'");
	}

	public void visit(DoubleLiteralExpr n, Object arg) {
		printPreviousComments(n, arg);
		printer.print(n.getValue());
	}

	public void visit(IntegerLiteralExpr n, Object arg) {
		printPreviousComments(n, arg);
		printer.print(n.getValue());
	}

	public void visit(LongLiteralExpr n, Object arg) {
		printPreviousComments(n, arg);
		printer.print(n.getValue());
	}

	public void visit(IntegerLiteralMinValueExpr n, Object arg) {
		printPreviousComments(n, arg);
		printer.print(n.getValue());
	}

	public void visit(LongLiteralMinValueExpr n, Object arg) {
		printPreviousComments(n, arg);
		printer.print(n.getValue());
	}

	public void visit(StringLiteralExpr n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("\"");
		printer.print(n.getValue());
		printer.print("\"");
	}

	public void visit(BooleanLiteralExpr n, Object arg) {
		printPreviousComments(n, arg);
		printer.print(String.valueOf(n.getValue()));
	}

	public void visit(NullLiteralExpr n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("null");
	}

	public void visit(ThisExpr n, Object arg) {
		printPreviousComments(n, arg);
		if (n.getClassExpr() != null) {
			n.getClassExpr().accept(this, arg);
			printer.print(".");
		}
		printer.print("this");
	}

	public void visit(SuperExpr n, Object arg) {
		printPreviousComments(n, arg);
		if (n.getClassExpr() != null) {
			n.getClassExpr().accept(this, arg);
			printer.print(".");
		}
		printer.print("super");
	}

	public void visit(MethodCallExpr n, Object arg) {
		printPreviousComments(n, arg);
		if (n.getScope() != null) {
			n.getScope().accept(this, arg);
			printer.print(".");
		}
		printTypeArgs(n.getTypeArgs(), arg);
		printer.print(n.getName());
		printArguments(n.getArgs(), arg);
	}

	private void printEntersAfterMembersAndBeforeComments(Node n,
			List<?> members) {
		List<Node> comments = getContainingComments(n);
		if (!comments.isEmpty()) {
			Node c = comments.get(0);
			if (members != null && !members.isEmpty()) {
				Node last = (Node) members.get(members.size() - 1);
				if (!last.isNewNode()) {
					int start = last.getEndLine() + 1;
					int end = c.getBeginLine();
					for (int i = start; i < end; i++) {
						printer.printLn();
					}
				}
			}
		}
	}

	public void visit(ObjectCreationExpr n, Object arg) {
		printPreviousComments(n, arg);
		if (n.getScope() != null) {
			n.getScope().accept(this, arg);
			printer.print(".");
		}
		ClassOrInterfaceType type = n.getType();

		printPreviousComments(type, arg);
		int beginLine = n.getType().getEndLine();
		printer.print("new ");
		List<Type> typeArgs = n.getTypeArgs();
		if (typeArgs != null) {
			printTypeArgs(n.getTypeArgs(), arg);
			printer.print(" ");

		}
		n.getType().accept(this, arg);
		List<Expression> args = n.getArgs();
		printArguments(args, arg);
		if (args != null && !args.isEmpty()) {
			Expression lastType = args.get(args.size() - 1);
			int begin = lastType.getEndLine();
			if (begin > beginLine) {
				beginLine = begin;
			}
		}

		if (n.getAnonymousClassBody() != null) {
			printer.print(" {");
			printer.indent();
			List<BodyDeclaration> members = n.getAnonymousClassBody();
			if (members != null) {
				if (!members.isEmpty()) {
					printFirstBlankLines(n, members, beginLine);
					printChildrenNodes(n, members, null, arg);
					printEntersAfterMembersAndBeforeComments(n, members);
				}

			}
			printContainingCommentsAndEnters(n, members, arg, beginLine);
			printer.unindent();
			printer.print("}");
		}
	}

	public void visit(UnaryExpr n, Object arg) {
		printPreviousComments(n, arg);
		switch (n.getOperator()) {
		case positive:
			printer.print("+");
			break;
		case negative:
			printer.print("-");
			break;
		case inverse:
			printer.print("~");
			break;
		case not:
			printer.print("!");
			break;
		case preIncrement:
			printer.print("++");
			break;
		case preDecrement:
			printer.print("--");
			break;
		}
		n.getExpr().accept(this, arg);
		switch (n.getOperator()) {
		case posIncrement:
			printer.print("++");
			break;
		case posDecrement:
			printer.print("--");
			break;
		}
	}

	public void visit(ConstructorDeclaration n, Object arg) {

		Node comment = n.getJavaDoc();
		if (comment != null) {
			printPreviousComments(comment, arg);
		}

		printJavadoc((JavadocComment) comment, arg);

		printPreviousComments(n, arg);

		printMemberAnnotations(n.getAnnotations(), arg);
		printModifiers(n.getModifiers());
		printTypeParameters(n.getTypeParameters(), arg);
		if (n.getTypeParameters() != null) {
			printer.print(" ");
		}
		printer.print(n.getName());
		printer.print("(");
		if (n.getParameters() != null) {
			for (Iterator<Parameter> i = n.getParameters().iterator(); i
					.hasNext();) {
				Parameter p = i.next();
				p.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}
		printer.print(")");
		if (n.getThrows() != null) {
			printer.print(" throws ");
			for (Iterator<ClassOrInterfaceType> i = n.getThrows().iterator(); i
					.hasNext();) {
				ClassOrInterfaceType name = i.next();
				name.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}
		printer.print(" ");
		n.getBlock().accept(this, arg);
	}

	public void visit(MethodDeclaration n, Object arg) {

		Node comment = n.getJavaDoc();
		if (comment != null) {
			printPreviousComments(comment, arg);
		}

		printJavadoc((JavadocComment) comment, arg);

		printPreviousComments(n, arg);

		printMemberAnnotations(n.getAnnotations(), arg);
		List<TypeParameter> typeParameters = n.getTypeParameters();
		if (typeParameters != null && !typeParameters.isEmpty()) {
			TypeParameter first = typeParameters.get(0);
			printPreviousComments(first, arg);
		}
		printPreviousComments(n.getType(), arg);
		printModifiers(n.getModifiers());
		if (n.isDefault()) {
			printer.print("default ");
		}
		printTypeParameters(n.getTypeParameters(), arg);
		if (n.getTypeParameters() != null) {
			printer.print(" ");
		}
		n.getType().accept(this, arg);
		printer.print(" ");
		printer.print(n.getName());
		printer.print("(");
		if (n.getParameters() != null) {
			printParameterList(n.getParameters(), arg);

		}
		printer.print(")");
		for (int i = 0; i < n.getArrayCount(); i++) {
			printer.print("[]");
		}
		if (n.getThrows() != null) {
			printer.print(" throws ");
			for (Iterator<ClassOrInterfaceType> i = n.getThrows().iterator(); i
					.hasNext();) {
				ClassOrInterfaceType name = i.next();
				name.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}
		if (n.getBody() == null) {
			printer.print(";");
		} else {
			printer.print(" ");
			n.getBody().accept(this, arg);
		}
	}

	public void visit(Parameter n, Object arg) {
		printPreviousComments(n, arg);
		printAnnotations(n.getAnnotations(), arg);
		printModifiers(n.getModifiers());
		if (n.getType() != null) {
			n.getType().accept(this, arg);
		}
		if (n.isVarArgs()) {
			printer.print("...");
		}
		printer.print(" ");
		n.getId().accept(this, arg);
	}

	public void visit(ExplicitConstructorInvocationStmt n, Object arg) {
		printPreviousComments(n, arg);
		if (n.isThis()) {
			printTypeArgs(n.getTypeArgs(), arg);
			printer.print("this");
		} else {
			if (n.getExpr() != null) {
				n.getExpr().accept(this, arg);
				printer.print(".");
			}
			printTypeArgs(n.getTypeArgs(), arg);
			printer.print("super");
		}
		printArguments(n.getArgs(), arg);
		printer.print(";");
	}

	public void visit(VariableDeclarationExpr n, Object arg) {
		printPreviousComments(n, arg);
		printAnnotations(n.getAnnotations(), arg);
		printModifiers(n.getModifiers());
		n.getType().accept(this, arg);
		printer.print(" ");
		for (Iterator<VariableDeclarator> i = n.getVars().iterator(); i
				.hasNext();) {
			VariableDeclarator v = i.next();
			v.accept(this, arg);
			if (i.hasNext()) {
				printer.print(", ");
			}
		}
	}

	public void visit(TypeDeclarationStmt n, Object arg) {
		printPreviousComments(n, arg);
		n.getTypeDeclaration().accept(this, arg);
	}

	public void visit(AssertStmt n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("assert ");
		n.getCheck().accept(this, arg);
		if (n.getMessage() != null) {
			printer.print(" : ");
			n.getMessage().accept(this, arg);
		}
		printer.print(";");
	}

	private int getFirstWritenLine(int startLine, Node n2) {
		int line = n2.getBeginLine();
		if (n2 instanceof BodyDeclaration) {
			JavadocComment jc = ((BodyDeclaration) n2).getJavaDoc();
			if (jc != null && !jc.isNewNode()) {
				line = jc.getBeginLine();
			}
		}
		if (comments != null) {
			Iterator<Comment> it = comments.iterator();
			boolean found = false;
			while (it.hasNext() && !found) {
				Comment c = it.next();
				if (!c.isNewNode() && startLine <= c.getBeginLine()) {
					found = c.isPreviousThan(n2) && c.getBeginLine() < line;
					if (found) {
						line = c.getBeginLine();
					}
				}
			}
		}

		return line;
	}

	private void printFirstBlankLines(Node n, List<?> members, int lineNumber) {
		if (members != null && !members.isEmpty()) {
			Node member = (Node) members.get(0);
			if (!n.isNewNode() && !member.isNewNode()) {
				int beginLine = n.getBeginLine() /* + 1 */;
				if (lineNumber > beginLine) {
					beginLine = lineNumber;
				}
				int endLine = getFirstWritenLine(beginLine, member);
				for (int i = beginLine; i < endLine; i++) {
					printer.printLn();
				}
			}
		}
	}

	private void printStmtList(Node n, List<Statement> stmts, Object arg) {
		int index = 0;
		Node previousNode = null;
		List<Node> comments = null;
		if (stmts != null && !stmts.isEmpty()) {
			printFirstBlankLines(n, stmts, n.getBeginLine() + 1);

			for (Statement s : stmts) {

				if (previousNode == null) {
					printer.indent();
					List<Node> firstComments = printSameLineComments(n, arg);
					if (firstComments == null || firstComments.isEmpty()) {
						printer.printLn();
					}
					s.accept(this, arg);
					if (index + 1 < stmts.size()) {
						comments = printInnerComments(s, stmts.get(index + 1),
								arg);
					} else {
						comments = null;
					}
				} else {

					if (!previousNode.isNewNode() && !s.isNewNode()) {
						if (!(previousNode instanceof Comment)) {
							int firstLine = previousNode.getEndLine();
							int lastLine = getFirstWritenLine(firstLine, s);

							for (int i = firstLine + 1; i < lastLine; i++) {
								printer.printLn();
							}
						}
						printer.indent();
						s.accept(this, arg);
						comments = null;

						if (index + 1 < stmts.size()) {
							comments = printInnerComments(s,
									stmts.get(index + 1), arg);

						}
					} else {
						printer.indent();
						s.accept(this, arg);
						comments = null;
					}

				}
				if (comments == null || comments.isEmpty()) {
					if (index + 1 == stmts.size()) {
						List<Node> com = printSameLineComments(s, arg);

						if (com == null
								|| com.isEmpty()
								|| !(com.get(com.size() - 1) instanceof LineComment)) {
							printer.printLn();
						}
					} else {
						printer.printLn();
					}
					previousNode = s;
					printEntersAfterMembersAndBeforeComments(n, stmts);
				} else {
					previousNode = comments.get(comments.size() - 1);
				}
				printer.unindent();
				index++;
			}
		}
	}

	public void visit(BlockStmt n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("{");

		List<Statement> stmts = n.getStmts();
		printStmtList(n, stmts, arg);
		printer.indent();
		int startingLine = n.getBeginLine();

		printContainingCommentsAndEnters(n, stmts, arg, startingLine);
		printer.unindent();
		printer.print("}");
	}

	public void visit(LabeledStmt n, Object arg) {
		printPreviousComments(n, arg);
		printer.print(n.getLabel());
		printer.print(": ");
		n.getStmt().accept(this, arg);
	}

	public void visit(EmptyStmt n, Object arg) {
		printPreviousComments(n, arg);
		printer.print(";");
	}

	public void visit(ExpressionStmt n, Object arg) {
		printPreviousComments(n, arg);
		n.getExpression().accept(this, arg);
		printer.print(";");
	}

	public void visit(SwitchStmt n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("switch(");
		n.getSelector().accept(this, arg);
		printer.print(") {");
		if (n.isNewNode()) {
			printer.printLn();
		}
		if (n.getEntries() != null) {
			printer.indent();

			List<SwitchEntryStmt> entries = n.getEntries();
			if (entries != null && !entries.isEmpty()) {
				printFirstBlankLines(n, entries, n.getBeginLine());
				printChildrenNodes(n, entries, null, arg);
				printEntersAfterMembersAndBeforeComments(n, entries);
			}
			printContainingCommentsAndEnters(n, entries, arg, n.getBeginLine());
			printer.unindent();
		}

		printer.print("}");
	}

	public void visit(SwitchEntryStmt n, Object arg) {
		printPreviousComments(n, arg);
		if (n.getLabel() != null) {
			printer.print("case ");
			n.getLabel().accept(this, arg);
			printer.print(":");
		} else {
			printer.print("default:");
		}
		// printer.printLn();
		printer.indent();

		List<Statement> stmts = n.getStmts();

		printStmtList(n, stmts, arg);

		int startingLine = n.getBeginLine();
		if (stmts != null && !stmts.isEmpty()) {
			printEntersAfterMembersAndBeforeComments(n, stmts);
			printContainingCommentsAndEnters(n, stmts, arg, startingLine);
		} else {

			printContainingComments(n, startingLine, arg);
		}

		printer.unindent();
	}

	public void visit(BreakStmt n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("break");
		if (n.getId() != null) {
			printer.print(" ");
			printer.print(n.getId());
		}
		printer.print(";");
	}

	public void visit(ReturnStmt n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("return");
		if (n.getExpr() != null) {
			printer.print(" ");
			n.getExpr().accept(this, arg);
		}
		printer.print(";");
	}

	public void visit(EnumDeclaration n, Object arg) {
		Node comment = n.getJavaDoc();
		if (comment != null) {
			printPreviousComments(comment, arg);
		}

		printJavadoc((JavadocComment) comment, arg);

		printPreviousComments(n, arg);

		List<AnnotationExpr> annotations = n.getAnnotations();
		printMemberAnnotations(annotations, arg);
		int beginLineMembers = n.getBeginLine();

		if (annotations != null && !annotations.isEmpty()) {
			AnnotationExpr last = annotations.get(annotations.size() - 1);
			if (!last.isNewNode()) {
				beginLineMembers = last.getEndLine() + 1;
			}
		}

		printModifiers(n.getModifiers());
		printer.print("enum ");
		printer.print(n.getName());
		if (n.getImplements() != null) {
			printer.print(" implements ");
			for (Iterator<ClassOrInterfaceType> i = n.getImplements()
					.iterator(); i.hasNext();) {
				ClassOrInterfaceType c = i.next();
				c.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}
		printer.print(" {");
		if (n.isNewNode()) {
			printer.printLn();
		}
		printer.indent();
		List<EnumConstantDeclaration> entries = n.getEntries();
		if (entries != null) {

			if (n.isNewNode()) {
				printer.printLn();
			} else {
				printFirstBlankLines(n, entries, beginLineMembers);
			}

		}
		printChildrenNodes(n, entries, ", ", arg);
		List<BodyDeclaration> members = n.getMembers();
		if (members != null && !members.isEmpty()) {
			printer.printLn(";");

			if (entries != null && !entries.isEmpty() && !members.isEmpty()) {
				EnumConstantDeclaration lastEntry = entries
						.get(entries.size() - 1);
				if (!lastEntry.isNewNode()) {
					printFirstBlankLines(n, members, lastEntry.getEndLine() + 1);
				}
			}

			printChildrenNodes(n, members, null, arg);
			printEntersAfterMembersAndBeforeComments(n, members);
			printContainingCommentsAndEnters(n, members, arg, beginLineMembers);
		} else {

			printContainingCommentsAndEnters(n, entries, arg, n.getBeginLine());
		}
		printer.unindent();
		printer.print("}");
	}

	public void visit(EnumConstantDeclaration n, Object arg) {
		JavadocComment comment = n.getJavaDoc();
		if (comment != null) {
			printPreviousComments(comment, arg);
		}
		printJavadoc(comment, arg);
		if (comment != null && !n.isNewNode() && !comment.isNewNode()) {
			int start = comment.getEndLine();
			int end = n.getBeginLine();
			for (int i = start + 1; i < end; i++) {
				printer.printLn();
			}
		}
		int beginLineMembers = n.getBeginLine();
		List<AnnotationExpr> annotations = n.getAnnotations();
		printMemberAnnotations(annotations, arg);
		if (annotations != null && !annotations.isEmpty()) {
			AnnotationExpr last = annotations.get(annotations.size() - 1);
			if (!last.isNewNode()) {
				beginLineMembers = last.getEndLine() + 1;
			}
		}
		printer.print(n.getName());
		if (n.getArgs() != null) {
			printArguments(n.getArgs(), arg);
		}
		if (n.getClassBody() != null) {
			printer.print(" {");
			List<BodyDeclaration> classBody = n.getClassBody();
			if (n.isNewNode()) {
				printer.printLn();
			}
			printFirstBlankLines(n, classBody, beginLineMembers);
			printer.indent();

			printChildrenNodes(n, classBody, null, arg);
			printContainingComments(n, -1, arg);
			printer.unindent();
			printer.print("}");
		}
	}

	public void visit(EmptyMemberDeclaration n, Object arg) {
		printPreviousComments(n, arg);
		JavadocComment comment = n.getJavaDoc();
		printJavadoc(comment, arg);
		if (comment != null && !n.isNewNode() && !comment.isNewNode()) {
			int start = comment.getEndLine();
			int end = n.getBeginLine();
			for (int i = start + 1; i < end; i++) {
				printer.printLn();
			}
		}
		printer.print(";");
	}

	public void visit(InitializerDeclaration n, Object arg) {
		printPreviousComments(n, arg);
		JavadocComment comment = n.getJavaDoc();
		printJavadoc(comment, arg);
		if (comment != null && !n.isNewNode() && !comment.isNewNode()) {
			int start = comment.getEndLine();
			int end = n.getBeginLine();
			for (int i = start + 1; i < end; i++) {
				printer.printLn();
			}
		}
		if (n.isStatic()) {
			printer.print("static ");
		}
		n.getBlock().accept(this, arg);
	}

	public void visit(IfStmt n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("if (");
		n.getCondition().accept(this, arg);
		printer.print(") ");
		Statement thenStmt = n.getThenStmt();
		thenStmt.accept(this, arg);

		Statement elseStmt = n.getElseStmt();
		if (elseStmt != null) {
			if (!thenStmt.isNewNode() && !elseStmt.isNewNode()) {
				int start = thenStmt.getEndLine();
				int end = elseStmt.getBeginLine();

				for (int i = start; i < end; i++) {
					printer.printLn();
				}
				if (start == end) {
					printer.print(" else ");
				} else {
					printer.print("else ");
				}
			} else {
				printer.print(" else ");
			}
			elseStmt.accept(this, arg);
		}
	}

	public void visit(WhileStmt n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("while (");
		n.getCondition().accept(this, arg);
		printer.print(") ");
		n.getBody().accept(this, arg);
	}

	public void visit(ContinueStmt n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("continue");
		if (n.getId() != null) {
			printer.print(" ");
			printer.print(n.getId());
		}
		printer.print(";");
	}

	public void visit(DoStmt n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("do ");
		n.getBody().accept(this, arg);
		printer.print(" while (");
		n.getCondition().accept(this, arg);
		printer.print(");");
	}

	public void visit(ForeachStmt n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("for (");
		n.getVariable().accept(this, arg);
		printer.print(" : ");
		n.getIterable().accept(this, arg);
		printer.print(") ");
		n.getBody().accept(this, arg);
	}

	public void visit(ForStmt n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("for (");
		if (n.getInit() != null) {
			for (Iterator<Expression> i = n.getInit().iterator(); i.hasNext();) {
				Expression e = i.next();
				e.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}
		printer.print("; ");
		if (n.getCompare() != null) {
			n.getCompare().accept(this, arg);
		}
		printer.print("; ");
		if (n.getUpdate() != null) {
			for (Iterator<Expression> i = n.getUpdate().iterator(); i.hasNext();) {
				Expression e = i.next();
				e.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}
		printer.print(") ");
		n.getBody().accept(this, arg);
	}

	public void visit(ThrowStmt n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("throw ");
		n.getExpr().accept(this, arg);
		printer.print(";");
	}

	public void visit(SynchronizedStmt n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("synchronized (");
		n.getExpr().accept(this, arg);
		printer.print(") ");
		n.getBlock().accept(this, arg);
	}

	@Override
	public void visit(final TryStmt n, final Object arg) {
		printPreviousComments(n, arg);
		printer.print("try ");
		if (!n.getResources().isEmpty()) {
			printer.print("(");
			Iterator<VariableDeclarationExpr> resources = n.getResources()
					.iterator();
			boolean first = true;
			while (resources.hasNext()) {
				VariableDeclarationExpr next = resources.next();
				visit(next, arg);
				if (resources.hasNext()) {
					printer.print(";");
					printer.printLn();
					if (first) {
						printer.indent();
					}
				}
				first = false;
			}
			if (n.getResources().size() > 1) {
				printer.unindent();
			}
			printer.print(") ");
		}
		n.getTryBlock().accept(this, arg);
		if (n.getCatchs() != null) {
			for (final CatchClause c : n.getCatchs()) {
				c.accept(this, arg);
			}
		}
		if (n.getFinallyBlock() != null) {
			printer.print(" finally ");
			n.getFinallyBlock().accept(this, arg);
		}
	}

	public void visit(CatchClause n, Object arg) {
		printPreviousComments(n, arg);
		printer.print(" catch (");
		n.getExcept().accept(this, arg);
		printer.print(") ");
		n.getCatchBlock().accept(this, arg);
	}

	public void visit(AnnotationDeclaration n, Object arg) {

		Node comment = n.getJavaDoc();
		if (comment != null) {
			printPreviousComments(comment, arg);
		}

		printJavadoc((JavadocComment) comment, arg);

		List<Node> comments = printPreviousComments(n, arg);
		if (comments != null && !comments.isEmpty()) {
			comment = comments.get(comments.size() - 1);
		}

		if (comment != null && !n.isNewNode() && !comment.isNewNode()) {
			int start = comment.getEndLine();
			int end = n.getBeginLine();

			for (int i = start + 1; i < end; i++) {
				printer.printLn();
			}
		}

		List<AnnotationExpr> annotations = n.getAnnotations();
		int beginLineMembers = n.getBeginLine();
		printMemberAnnotations(annotations, arg);

		if (annotations != null && !annotations.isEmpty()) {
			AnnotationExpr last = annotations.get(annotations.size() - 1);
			if (!last.isNewNode()) {
				beginLineMembers = last.getEndLine() + 1;
			}
		}
		printModifiers(n.getModifiers());
		printer.print("@interface ");
		printer.print(n.getName());
		printer.print(" {");
		printer.indent();

		List<BodyDeclaration> members = n.getMembers();
		if (members != null) {

			printFirstBlankLines(n, members, beginLineMembers);
			printChildrenNodes(n, members, null, arg);
			printEntersAfterMembersAndBeforeComments(n, members);

		}

		printContainingCommentsAndEnters(n, members, arg, beginLineMembers);
		printer.unindent();
		printer.print("}");
	}

	public void visit(AnnotationMemberDeclaration n, Object arg) {

		JavadocComment comment = n.getJavaDoc();

		if (comment != null) {
			printPreviousComments(comment, arg);
		}

		printJavadoc(comment, arg);
		if (comment != null && !n.isNewNode() && !comment.isNewNode()) {
			int start = comment.getEndLine();
			int end = n.getBeginLine();
			for (int i = start + 1; i < end; i++) {
				printer.printLn();
			}
		}
		printMemberAnnotations(n.getAnnotations(), arg);
		printModifiers(n.getModifiers());
		n.getType().accept(this, arg);
		printer.print(" ");
		printer.print(n.getName());
		printer.print("()");
		if (n.getDefaultValue() != null) {
			printer.print(" default ");
			n.getDefaultValue().accept(this, arg);
		}
		printer.print(";");
	}

	public void visit(MarkerAnnotationExpr n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("@");
		n.getName().accept(this, arg);
	}

	public void visit(SingleMemberAnnotationExpr n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("@");
		n.getName().accept(this, arg);
		printer.print("(");
		n.getMemberValue().accept(this, arg);
		printer.print(")");
	}

	public void visit(NormalAnnotationExpr n, Object arg) {
		printPreviousComments(n, arg);
		printer.print("@");
		n.getName().accept(this, arg);
		printer.print("(");
		if (n.getPairs() != null) {
			for (Iterator<MemberValuePair> i = n.getPairs().iterator(); i
					.hasNext();) {
				MemberValuePair m = i.next();
				m.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}
		printer.print(")");
	}

	public void visit(MemberValuePair n, Object arg) {
		printPreviousComments(n, arg);
		printer.print(n.getName());
		printer.print(" = ");
		n.getValue().accept(this, arg);
	}

	public void visit(LineComment n, Object arg) {
		printer.print("//");
		String content = n.getContent();
		if (content.endsWith("\n")) {
			content = content.substring(0, content.length() - 1);
		}
		printer.printLn(content);

	}

	public void visit(BlockComment n, Object arg) {
		printer.print("/*");
		printer.print(n.getContent());
		printer.print("*/");
	}

	public void visit(MultiTypeParameter n, Object arg) {
		printPreviousComments(n, arg);
		printAnnotations(n.getAnnotations(), arg);
		printModifiers(n.getModifiers());

		Iterator<Type> types = n.getTypes().iterator();
		types.next().accept(this, arg);
		while (types.hasNext()) {
			printer.print(" | ");
			types.next().accept(this, arg);
		}

		printer.print(" ");
		n.getId().accept(this, arg);
	}

	@Override
	public void visit(LambdaExpr n, Object arg) {
		printPreviousComments(n, arg);

		List<Parameter> parameters = n.getParameters();
		boolean printPar = false;
		printPar = n.isParametersEnclosed();

		if (printPar) {
			printer.print("(");
		}
		if (parameters != null) {
			for (Iterator<Parameter> i = parameters.iterator(); i.hasNext();) {
				Parameter p = i.next();
				p.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}
		if (printPar) {
			printer.print(")");
		}

		printer.print("->");
		Statement body = n.getBody();
		String bodyStr = body.toString();
		if (body instanceof ExpressionStmt) {
			// removing ';'
			bodyStr = bodyStr.substring(0, bodyStr.length() - 1);
		}
		printer.print(bodyStr);

	}

	@Override
	public void visit(MethodReferenceExpr n, Object arg) {
		printPreviousComments(n, arg);
		Expression scope = n.getScope();
		String identifier = n.getIdentifier();
		if (scope != null) {
			n.getScope().accept(this, arg);
		}

		printer.print("::");
		if (n.getTypeParameters() != null) {
			printer.print("<");
			for (Iterator<TypeParameter> i = n.getTypeParameters().iterator(); i
					.hasNext();) {
				TypeParameter p = i.next();
				p.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
			printer.print(">");
		}
		if (identifier != null) {
			printer.print(identifier);
		}

	}

	@Override
	public void visit(TypeExpr n, Object arg) {
		printPreviousComments(n, arg);
		if (n.getType() != null) {
			n.getType().accept(this, arg);
		}
	}
}
