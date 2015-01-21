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

package org.walkmod.javalang.ast.body;

import java.util.Comparator;
import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.expr.Expression;
import org.walkmod.javalang.comparators.VariableDeclaratorComparator;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;
import org.walkmod.merger.MergeEngine;
import org.walkmod.merger.Mergeable;

/**
 * @author Julio Vilmar Gesser
 */
public final class VariableDeclarator extends Node implements Mergeable<VariableDeclarator> {

  private VariableDeclaratorId id;

  private Expression init;

  public VariableDeclarator() {}

  public VariableDeclarator(VariableDeclaratorId id) {
    this.id = id;
  }

  public VariableDeclarator(VariableDeclaratorId id, Expression init) {
    this.id = id;
    this.init = init;
  }

  public VariableDeclarator(int beginLine, int beginColumn, int endLine, int endColumn,
      VariableDeclaratorId id, Expression init) {
    super(beginLine, beginColumn, endLine, endColumn);
    this.id = id;
    this.init = init;
  }

  @Override
  public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
    return v.visit(this, arg);
  }

  @Override
  public <A> void accept(VoidVisitor<A> v, A arg) {
    v.visit(this, arg);
  }

  public VariableDeclaratorId getId() {
    return id;
  }

  public Expression getInit() {
    return init;
  }

  public void setId(VariableDeclaratorId id) {
    this.id = id;
  }

  public void setInit(Expression init) {
    this.init = init;
  }

  @Override
  public Comparator<?> getIdentityComparator() {
    //TODO pensar si es un singleton o un atributo estatico de la clase
    return new VariableDeclaratorComparator();
  }

  @Override
  public void merge(VariableDeclarator remote, MergeEngine configuration) {
    setInit((Expression) configuration.apply(getInit(), remote.getInit(), Expression.class));
    setId((VariableDeclaratorId) configuration.apply(getId(), remote.getId(),
        VariableDeclaratorId.class));
  }
}
