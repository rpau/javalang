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
package org.walkmod.javalang.ast;

/**
 * Abstract class for all AST nodes that represent comments.
 * 
 * @see BlockComment
 * @see LineComment
 * @see org.walkmod.javalang.ast.body.JavadocComment
 * @author Julio Vilmar Gesser
 */
public abstract class Comment extends Node {

	private String content;

	public Comment() {
	}

	public Comment(String content) {
		this.content = content;
	}

	public Comment(int beginLine, int beginColumn, int endLine, int endColumn,
			String content) {
		super(beginLine, beginColumn, endLine, endColumn);
		this.content = content;
	}

	/**
	 * Return the text of the comment.
	 * 
	 * @return text of the comment
	 */
	public final String getContent() {
		return content;
	}

	/**
	 * Sets the text of the comment.
	 * 
	 * @param content
	 *            the text of the comment to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
}
