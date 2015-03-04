package org.walkmod.javalang.actions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.walkmod.javalang.ast.Comment;
import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.body.BodyDeclaration;
import org.walkmod.javalang.ast.body.JavadocComment;
import org.walkmod.javalang.visitors.DumpVisitor;

public class ReplaceAction extends Action {

	private String newCode;

	private int endLine;

	private int endColumn;

	private String oldCode;

	private char indentationChar = ' ';

	private String oldCodeWithoutNonBlanks;

	private int indentationLevel;

	private int indentationSize;

	private Node newNode;
	
	private Node oldNode;
	
	private List<Comment> acceptedComments = new LinkedList<Comment>();

	public ReplaceAction(int beginLine, int beginPosition, Node oldNode,
			Node newNode, int indentation, int indentationSize,
			List<Comment> comments) {
		super(beginLine, beginPosition, ActionType.REPLACE);
		
		if(comments != null){
			Iterator<Comment> it = comments.iterator();
			boolean finish = false;
			while(it.hasNext() && ! finish){
				Comment next = it.next();
				if (oldNode.contains(next) && ! (next instanceof JavadocComment)){
					acceptedComments.add(next);
					it.remove();
				}
			}
		}
		
		DumpVisitor visitor = new DumpVisitor();
		visitor.setIndentationChar(indentationChar);
		visitor.setIndentationLevel(indentationLevel);
		visitor.setIndentationSize(indentationSize);
		visitor.setComments(new LinkedList<Comment>(acceptedComments));
		
		this.oldNode = oldNode;
		oldNode.accept(visitor, null);
		oldCode = visitor.getSource();
		
		oldCodeWithoutNonBlanks = oldCode.replaceAll("\\S", " ");

		if (oldNode instanceof BodyDeclaration) {
			JavadocComment jc = ((BodyDeclaration) oldNode).getJavaDoc();
			if (jc != null) {
				setBeginLine(jc.getBeginLine());
				setBeginColumn(jc.getBeginColumn());
			}
		}
		this.indentationLevel = indentation;
		this.indentationSize = indentationSize;
		this.newNode = newNode;

		updateCode();

		String[] lines = newCode.split("\n");
		endLine = getBeginLine() + lines.length - 1;
		endColumn = lines[lines.length - 1].length();

		if (oldNode.getEndLine() >= endLine) {
			if (oldNode.getEndLine() == endLine) {
				if (oldNode.getEndColumn() > endColumn) {
					endLine = oldNode.getEndLine();
					endColumn = oldNode.getEndColumn();
				}
			} else {
				endLine = oldNode.getEndLine();
				endColumn = oldNode.getEndColumn();
			}
		}
	}

	private void updateCode() {
		DumpVisitor visitor = new DumpVisitor();
		visitor.setIndentationChar(indentationChar);
		visitor.setIndentationLevel(indentationLevel);
		visitor.setIndentationSize(indentationSize);
		visitor.setComments(new LinkedList<Comment>(acceptedComments));
		
		newNode.accept(visitor, null);
		newCode = visitor.getSource();
		if (newCode.length() < oldCodeWithoutNonBlanks.length()) {
			newCode = newCode
					+ oldCodeWithoutNonBlanks.substring(newCode.length());
		}
		if (getBeginColumn() == 1 && getBeginLine() > 1) {
			if (!newCode.endsWith("\n")) {
				newCode += "\n";
			}
		}
	}

	@Override
	public int getEndLine() {
		return endLine;
	}
	
	public int getOldEndLine(){
		return oldNode.getBeginLine();
	}
	
	public int getOldEndColumn(){
		return oldNode.getEndColumn();
	}

	@Override
	public int getEndColumn() {
		return endColumn;
	}

	public void setIndentationChar(char indentationChar) {
		this.indentationChar = indentationChar;
		updateCode();
	}

	public String getNewText() {
		return newCode;
	}

	public String getOldText() {
		return oldCode;
	}

}
