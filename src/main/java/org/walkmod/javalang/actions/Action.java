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

public abstract class Action {
   private int beginLine;
   private int beginColumn;
   private ActionType type;

   public Action(int beginLine, int beginPosition, ActionType type) {
      this.beginLine = beginLine;
      this.beginColumn = beginPosition;
      this.type = type;
   }

   public int getBeginLine() {
      return beginLine;
   }

   public void setBeginLine(int beginLine) {
      this.beginLine = beginLine;
   }

   public int getBeginColumn() {
      return beginColumn;
   }

   public void setBeginColumn(int beginPosition) {
      this.beginColumn = beginPosition;
   }

   public ActionType getType() {
      return type;
   }

   public void setType(ActionType type) {
      this.type = type;
   }

   public abstract int getEndLine();

   public abstract int getEndColumn();

   public boolean isPreviousThan(int beginLine, int beginColumn) {

      if (beginLine > getEndLine()) {
         return true;
      }
      if (beginLine == getEndLine() && beginColumn >= getEndColumn()) {
         return true;
      }
      return false;
   }

   public boolean contains(Action action) {

      if ((getBeginLine() < action.getBeginLine())
            || ((getBeginLine() == action.getBeginLine()) && getBeginColumn() <= action.getBeginColumn())) {
         if (getEndLine() > action.getEndLine()) {
            return true;
         } else if ((getEndLine() == action.getEndLine()) && getEndColumn() >= action.getEndColumn()) {
            return true;
         }
      }
      return false;
   }

   public boolean isEmpty() {
      return getBeginLine() == getEndLine() && getBeginColumn() == getEndColumn();
   }
}
