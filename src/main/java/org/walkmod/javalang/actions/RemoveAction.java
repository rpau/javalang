package org.walkmod.javalang.actions;

import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.body.BodyDeclaration;
import org.walkmod.javalang.ast.body.JavadocComment;

public class RemoveAction extends Action {

	private int endLine;

	private int endColumn;

	private String text;

	public RemoveAction(int beginLine, int beginPosition, int endLine,
			int endColumn, Node node) {
		super(beginLine, beginPosition, ActionType.REMOVE);
		this.endLine = endLine;
		this.endColumn = endColumn;
		if (node instanceof BodyDeclaration) {
			JavadocComment javadoc = (((BodyDeclaration) node).getJavaDoc());
			if (javadoc != null) {
				setBeginLine(javadoc.getBeginLine());
				setBeginColumn(javadoc.getBeginColumn());
			}
		}

		this.text = node.toString();
		
	}

	public String getText() {
		return text;
	}

	public int getEndLine() {
		return endLine;
	}

	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}

	public int getEndColumn() {
		return endColumn;
	}

	public void setEndColumn(int endColumn) {
		this.endColumn = endColumn;
	}

}
