package org.walkmod.javalang.actions;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.walkmod.javalang.util.FileUtils;

public class ActionsApplier {

	private String text;

	private List<Action> actions;

	private StringBuffer modifiedText;

	private Character indentationChar = null;

	public void setText(String text) {
		this.text = text;
	}

	public void setText(File file) {
		try {
			this.text = FileUtils.fileToString(file.getAbsolutePath());
		} catch (Exception e) {
			throw new RuntimeException("Error reading the file "
					+ file.getAbsolutePath());
		}
	}

	public void setActionList(List<Action> actions) {
		this.actions = actions;
	}

	public String getModifiedText() {
		return modifiedText.toString();
	}

	public Character getIndentationChar() {
		return indentationChar;
	}

	public void inferIndentationChar(char[] contents) {
		if (indentationChar == null) {
			indentationChar = getIndentationChar(contents, '\n');
			if (indentationChar == null) {
				indentationChar = getIndentationChar(contents, '{');
				if (indentationChar == null) {
					indentationChar = '\0';
				}
			}
		}
	}

	private Character getIndentationChar(char[] contents, char separator) {
		int spaces = 0;
		int tabs = 0;
		boolean containsSeparator = false;
		for (int i = 0; i < contents.length; i++) {
			if (contents[i] == separator) {
				containsSeparator = true;
				if (i + 1 < contents.length) {
					if (contents[i + 1] == ' ') {
						spaces++;
					} else if (contents[i + 1] == '\t') {
						tabs++;
					}
				}
			}
		}
		if (tabs > spaces) {
			return '\t';
		} else {
			if (!containsSeparator) {
				return null;
			} else if (spaces == 0 && spaces == tabs) {
				return '\0';
			}
			return ' ';
		}
	}

	public void execute() {
		modifiedText = new StringBuffer();
		if (actions != null && text != null) {

			Iterator<Action> it = actions.iterator();

			char[] contents = text.toCharArray();

			int index = 0;

			int line = 0;
			int actionColumn = 0;

			while (it.hasNext()) {
				Action next = it.next();
				int actionLine = next.getBeginLine() - 1;

				// the cursor is moved to the line
				for (int i = line; i < actionLine; i++) {

					boolean endLine = false;
					while (index < contents.length && !endLine) {
						endLine = contents[index] == '\n';
						modifiedText.append(contents[index]);
						index++;
						if (endLine) {
							line++;
							actionColumn = 0;
						}

					}
				}

				// the cursor is moved to the column
				if (index < contents.length) {
					int incr = next.getBeginColumn() - 1 - actionColumn;
					if (incr > 0) {
						for (int i = index; i < index + incr; i++) {
							modifiedText.append(contents[i]);
							actionColumn++;

						}
						index = index + incr;
					}

					if (next.getType().equals(ActionType.REMOVE)) {
						RemoveAction remove = (RemoveAction) next;

						for (; (actionLine < (remove.getEndLine() - 1) || (actionLine == (remove
								.getEndLine() - 1) && actionColumn < remove
								.getEndColumn())); index++) {

							if (contents[index] == '\r') {
								//modifiedText.append('\r');
								actionColumn++;
							} else if (contents[index] != '\n') {
								// modifiedText.append(' ');
								actionColumn++;
							} else {
								actionLine++;
								line++;
								actionColumn = 0;
								//modifiedText.append('\n');
							}
						}
					} else if (next.getType().equals(ActionType.APPEND)) {
						inferIndentationChar(contents);

						AppendAction append = (AppendAction) next;
						append.setIndentationChar(indentationChar);

						String code = append.getText();
						modifiedText.append(code);

					} else if (next.getType().equals(ActionType.REPLACE)) {
						inferIndentationChar(contents);

						ReplaceAction replace = (ReplaceAction) next;
						replace.setIndentationChar(indentationChar);

						String code = replace.getNewText();
						modifiedText.append(code);

						int futureLine = replace.getOldEndLine() - 1;
						// we need to update the index cursor according the old
						// value
						for (; actionLine < futureLine; index++) {
							if (contents[index] == '\r') {
								actionColumn++;
							} else if (contents[index] != '\n') {
								actionColumn++;
							} else {
								actionLine++;
								actionColumn = 0;
							}
						}
						line = futureLine;
						index += (replace.getOldEndColumn() - actionColumn);
						actionColumn = replace.getOldEndColumn();
					}
				}
			}

			for (int i = index; i < contents.length; i++) {
				modifiedText.append(contents[i]);
			}
		}
	}
}
