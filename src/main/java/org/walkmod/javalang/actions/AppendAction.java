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
package org.walkmod.javalang.actions;

import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.expr.Expression;

public class AppendAction extends Action {

   private Node node;

   private int endLine = -1;

   private int endColumn = -1;

   private int indentationLevel = 0;

   private int indentationSize = 0;

   private char indentationChar = ' ';

   private String text = null;

   public AppendAction(int beginLine, int beginPosition, Node node, int level, int indentationSize) {
      super(beginLine, beginPosition, ActionType.APPEND);

      this.indentationLevel = level;
      this.indentationSize = indentationSize;
      this.node = node;

      generateText();

      String[] lines = text.split("\n");
      endLine = getBeginLine() + lines.length - 1;
      if (endLine == beginLine) {
         endColumn = getBeginColumn() + lines[lines.length - 1].length();
      } else {
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

      if (!(node instanceof Expression) && getBeginLine() > 1) {
         if (!text.endsWith("\n")) {
            if (text.endsWith(" ")) {
               text = text.substring(0, text.length() - 1);
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
