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
package org.walkmod.javalang.constraints;

import java.util.HashSet;

import org.walkmod.javalang.ast.Node;
import org.walkmod.modelchecker.Constraint;

public class NodesPerLineConstraint implements Constraint<Node> {

    private HashSet<Integer> segments = new HashSet<Integer>();

    public NodesPerLineConstraint() {}

    public void addLine(int beginLine, int endLine) {

        for (int i = beginLine; i <= endLine; i++) {
            segments.add(i);
        }
    }

    public void addLine(int lineNumber) {
        segments.add(lineNumber);
    }

    @Override
    public boolean isConstrained(Node o) {
        if (o.isNewNode()) {
            return false;
        }
        int beginLine = o.getBeginLine();
        int endLine = o.getEndLine();

        if (segments.contains(beginLine) && segments.contains(endLine)) {
            return false;
        }
        boolean includesAChildrenNode = false;
        for (int i = beginLine; i <= endLine && !includesAChildrenNode; i++) {
            includesAChildrenNode = segments.contains(i);
        }
        return !includesAChildrenNode;
    }

}
