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

package org.walkmod.javalang.ast.stmt;

import java.util.List;
import org.walkmod.javalang.ast.expr.Expression;
import org.walkmod.javalang.ast.type.Type;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class ExplicitConstructorInvocationStmt extends Statement {

  private List<Type> typeArgs;

  private boolean isThis;

  private Expression expr;

  private List<Expression> args;

  public ExplicitConstructorInvocationStmt() {}

  public ExplicitConstructorInvocationStmt(boolean isThis, Expression expr, List<Expression> args) {
    this.isThis = isThis;
    this.expr = expr;
    this.args = args;
  }

  public ExplicitConstructorInvocationStmt(int beginLine, int beginColumn, int endLine,
      int endColumn, List<Type> typeArgs, boolean isThis, Expression expr, List<Expression> args) {
    super(beginLine, beginColumn, endLine, endColumn);
    this.typeArgs = typeArgs;
    this.isThis = isThis;
    this.expr = expr;
    this.args = args;
  }

  @Override
  public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
    return v.visit(this, arg);
  }

  @Override
  public <A> void accept(VoidVisitor<A> v, A arg) {
    v.visit(this, arg);
  }

  public List<Expression> getArgs() {
    return args;
  }

  public Expression getExpr() {
    return expr;
  }

  public List<Type> getTypeArgs() {
    return typeArgs;
  }

  public boolean isThis() {
    return isThis;
  }

  public void setArgs(List<Expression> args) {
    this.args = args;
  }

  public void setExpr(Expression expr) {
    this.expr = expr;
  }

  public void setThis(boolean isThis) {
    this.isThis = isThis;
  }

  public void setTypeArgs(List<Type> typeArgs) {
    this.typeArgs = typeArgs;
  }
}
