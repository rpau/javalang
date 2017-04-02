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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.walkmod.javalang.ast.expr.FieldAccessExpr;
import org.walkmod.javalang.ast.expr.NameExpr;
import org.walkmod.javalang.ast.expr.ThisExpr;
import org.walkmod.javalang.visitors.VoidVisitorAdapter;

public class Refactorization {

    /**
    * Generic method to rename a SymbolDefinition variable/parameter.
    * @param n variable to rename.
    * @param newName new name to set.
    * @return if the rename procedure has been applied successfully.
    */
    public boolean refactorVariable(SymbolDefinition n, final String newName) {
        Map<String, SymbolDefinition> scope = n.getVariableDefinitions();

        if (!scope.containsKey(newName)) {

            if (n.getUsages() != null) {
                List<SymbolReference> usages = new LinkedList<SymbolReference>(n.getUsages());

                VoidVisitorAdapter<?> visitor = new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(NameExpr nexpr, Object ctx) {
                        Map<String, SymbolDefinition> innerScope = nexpr.getVariableDefinitions();
                        if (innerScope.containsKey(newName)) {
                            nexpr.getParentNode().replaceChildNode(nexpr, new FieldAccessExpr(new ThisExpr(), newName));
                        } else {
                            nexpr.getParentNode().replaceChildNode(nexpr, new NameExpr(newName));
                        }
                    }

                    @Override
                    public void visit(FieldAccessExpr nexpr, Object ctx) {
                        nexpr.getParentNode().replaceChildNode(nexpr,
                                new FieldAccessExpr(nexpr.getScope(), nexpr.getTypeArgs(), newName));
                    }
                };

                for (SymbolReference usage : usages) {
                    Node aux = (Node) usage;

                    aux.accept(visitor, null);

                }
            }

            return true;
        }
        return false;
    }

}
