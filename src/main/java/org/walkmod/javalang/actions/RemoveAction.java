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
package org.walkmod.javalang.actions;

import org.walkmod.javalang.ast.LineComment;
import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.body.TypeDeclaration;

public class RemoveAction extends Action {

    private int endLine;

    private int endColumn;

    private String text;

    public RemoveAction(int beginLine, int beginPosition, int endLine, int endColumn, Node node) {
        super(beginLine, beginPosition, ActionType.REMOVE);
        this.endLine = endLine;
        this.endColumn = endColumn;


        this.text = node.toString();
        if (text.endsWith("\n") && node instanceof LineComment) {
            text = text.substring(0, text.length() - 1);
            this.endColumn--;
        } else if (text.endsWith("}") && node instanceof TypeDeclaration) {
            this.endColumn++;
        }
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
