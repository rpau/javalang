package org.walkmod.javalang.actions;

import org.walkmod.javalang.ast.Comment;
import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.visitors.DumpVisitor;

public class AppendAction extends Action {

	private Node node;

	private int endLine = -1;

	private int endColumn = -1;

	private int indentations = 0;

	private int indentationLevel = 0;

	private int indentationSize = 0;

	private char indentationChar = ' ';

	private String text = null;

	public AppendAction(int beginLine, int beginPosition, Node node, int level,
			int indentationSize) {
		super(beginLine, beginPosition, ActionType.APPEND);

		this.indentationLevel = level;
		this.indentationSize = indentationSize;
		this.node = node;

		generateText();

		String[] lines = text.split("\n");
		endLine = getBeginLine() + lines.length - 1;
		if(endLine == beginLine){
			endColumn = getBeginColumn() + lines[lines.length - 1].length();
		}
		else{
			endColumn = lines[lines.length - 1].length();
		}
	}

	public String getText() {
		if (text == null) {
			generateText();
		}
		return text;
	}

	private void generateText() {
		DumpVisitor visitor = new DumpVisitor();
		visitor.setIndentationChar(indentationChar);
		visitor.setIndentationLevel(indentationLevel);
		visitor.setIndentationSize(indentationSize);

		node.accept(visitor, null);
		text = visitor.getSource();
		if(node instanceof Comment){
			if (!text.endsWith("\n")) {
				text += "\n";
			}
		}
		if (getBeginColumn() == 1 && getBeginLine() > 1) {
			if (!text.endsWith("\n")) {
				text += "\n";
			}
		}
	}

	public void setIndentationChar(char indentationChar) {
		this.indentationChar = indentationChar;
		generateText();
	}

	public int getEndLine() {
		return endLine;
	}

	public int getEndColumn() {
		return endColumn;
	}

	public int getIndentations() {
		return indentations;
	}
}
