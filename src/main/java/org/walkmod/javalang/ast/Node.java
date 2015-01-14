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

package org.walkmod.javalang.ast;

import java.io.Serializable;
import org.walkmod.javalang.visitors.DumpVisitor;
import org.walkmod.javalang.visitors.EqualsVisitor;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * Abstract class for all nodes of the AST.
 * 
 * @author Julio Vilmar Gesser
 */
public abstract class Node implements Serializable {

  private int beginLine;

  private int beginColumn;

  private int endLine;

  private int endColumn;

  /**
   * This attribute can store additional information from semantic analysis.
   */
  private Object data;

  public Node() {}

  public Node(int beginLine, int beginColumn, int endLine, int endColumn) {
    this.beginLine = beginLine;
    this.beginColumn = beginColumn;
    this.endLine = endLine;
    this.endColumn = endColumn;
  }

  /**
   * Accept method for visitor support.
   * 
   * @param <R>
   *            the type the return value of the visitor
   * @param <A>
   *            the type the argument passed for the visitor
   * @param v
   *            the visitor implementation
   * @param arg
   *            any value relevant for the visitor
   * @return the result of the visit
   */
  public abstract <R, A> R accept(GenericVisitor<R, A> v, A arg);

  /**
   * Accept method for visitor support.
   * 
   * @param <A>
   *            the type the argument passed for the visitor
   * @param v
   *            the visitor implementation
   * @param arg
   *            any value relevant for the visitor
   */
  public abstract <A> void accept(VoidVisitor<A> v, A arg);

  /**
   * Return the begin column of this node.
   * 
   * @return the begin column of this node
   */
  public final int getBeginColumn() {
    return beginColumn;
  }

  /**
   * Return the begin line of this node.
   * 
   * @return the begin line of this node
   */
  public final int getBeginLine() {
    return beginLine;
  }

  /**
   * Use this to retrieve additional information associated to this node.
   */
  public final Object getData() {
    return data;
  }

  /**
   * Return the end column of this node.
   * 
   * @return the end column of this node
   */
  public final int getEndColumn() {
    return endColumn;
  }

  /**
   * Return the end line of this node.
   * 
   * @return the end line of this node
   */
  public final int getEndLine() {
    return endLine;
  }

  /**
   * Sets the begin column of this node.
   * 
   * @param beginColumn
   *            the begin column of this node
   */
  public final void setBeginColumn(int beginColumn) {
    this.beginColumn = beginColumn;
  }

  /**
   * Sets the begin line of this node.
   * 
   * @param beginLine
   *            the begin line of this node
   */
  public final void setBeginLine(int beginLine) {
    this.beginLine = beginLine;
  }

  /**
   * Use this to store additional information to this node.
   */
  public final void setData(Object data) {
    this.data = data;
  }

  /**
   * Sets the end column of this node.
   * 
   * @param endColumn
   *            the end column of this node
   */
  public final void setEndColumn(int endColumn) {
    this.endColumn = endColumn;
  }

  /**
   * Sets the end line of this node.
   * 
   * @param endLine
   *            the end line of this node
   */
  public final void setEndLine(int endLine) {
    this.endLine = endLine;
  }

  /**
   * Return the String representation of this node.
   * 
   * @return the String representation of this node
   */
  @Override
  public final String toString() {
    DumpVisitor visitor = new DumpVisitor();
    accept(visitor, null);
    return visitor.getSource();
  }

  @Override
  public final int hashCode() {
    return toString().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsVisitor.equals(this, (Node) obj);
  }

  /**
   * Return if the node has not been retrieved from any input because its end
   * column and begin line is 0.
   * 
   * @return if it is a new node.
   */
  public boolean isNewNode() {
    return (0 == getEndColumn()) && (getBeginLine() == 0);
  }

  /**
   * Return if this node contains another node according their line numbers
   * and columns
   * 
   * @param node2
   *            the probably contained node
   * @return if this node contains the argument as a child node by its
   *         position.
   */
  public boolean contains(Node node2) {
    if ((getBeginLine() < node2.getBeginLine())
        || ((getBeginLine() == node2.getBeginLine()) && getBeginColumn() <= node2.getBeginColumn())) {
      if (getEndLine() > node2.getEndLine()) {
        return true;
      } else if ((getEndLine() == node2.getEndLine()) && getEndColumn() >= node2.getEndColumn()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Return if this node has the same columns and lines than another one.
   * 
   * @param node2
   *            the node to compare
   * @return if this node has the same columns and lines than another one.
   */
  public boolean isInEqualLocation(Node node2) {
    if (!isNewNode() && !node2.isNewNode()) {
      return getBeginLine() == node2.getBeginLine() && getBeginColumn() == node2.getBeginColumn()
          && getEndLine() == node2.getEndLine() && getEndColumn() == node2.getEndColumn();
    }
    return false;
  }

  /**
   * Return if this node is previous than another one according their line and
   * column numbers
   * 
   * @param node to compare
   * @return if this node is previous than another one according their line and
   */
  public boolean isPreviousThan(Node node) {
    if (getEndLine() < node.getBeginLine()) {
      return true;
    } else if ((getEndLine() == node.getBeginLine()) && (getEndColumn() <= node.getBeginColumn())) {
      return true;
    }
    return false;
  }
}
