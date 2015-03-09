package org.walkmod.javalang.actions;

import org.walkmod.javalang.ast.Node;

public class AppendAction extends Action {

	private Node node;

	private int endLine = -1;

	private int endColumn = -1;

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

	public void generateText() {
		
		text = node.getPrettySource(indentationChar, indentationLevel, indentationSize);
	
		if (getBeginColumn() == 1 && getBeginLine() > 1) {
			if (!text.endsWith("\n")) {
				if(text.endsWith(" ")){
					text = text.substring(0, text.length()-1);
				}
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
		return indentationSize;
	}
	
}
