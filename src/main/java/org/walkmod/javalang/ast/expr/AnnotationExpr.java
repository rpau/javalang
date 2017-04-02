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

package org.walkmod.javalang.ast.expr;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.ast.SymbolDefinition;
import org.walkmod.javalang.ast.SymbolReference;
import org.walkmod.javalang.comparators.AnnotationExprComparator;
import org.walkmod.merger.Mergeable;

/**
 * @author Julio Vilmar Gesser
 */
public abstract class AnnotationExpr extends Expression implements Mergeable<AnnotationExpr>, SymbolReference {

    protected NameExpr name;

    private SymbolDefinition symbolDefinition;

    public AnnotationExpr() {}

    public AnnotationExpr(int beginLine, int beginColumn, int endLine, int endColumn) {
        super(beginLine, beginColumn, endLine, endColumn);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> children = new LinkedList<Node>();
        if (name != null) {
            children.add(name);
        }
        return children;
    }

    @Override
    public boolean removeChild(Node child) {
        boolean result = false;
        if (child != null) {
            if (name == child) {
                name = null;
                result = true;
            }
        }
        if (result) {
            updateReferences(child);
        }
        return result;
    }

    public NameExpr getName() {
        return name;
    }

    public void setName(NameExpr name) {
        if (this.name != null) {
            updateReferences(this.name);
        }
        this.name = name;
        setAsParentNodeOf(name);
    }

    @Override
    public Comparator<?> getIdentityComparator() {
        return new AnnotationExprComparator();
    }

    @Override
    public String getPrettySource(char indentationChar, int indentationLevel, int indentationSize) {
        String text = super.getPrettySource(indentationChar, 0, 0);
        text += " ";

        return text;
    }

    @Override
    public SymbolDefinition getSymbolDefinition() {
        return symbolDefinition;
    }

    @Override
    public void setSymbolDefinition(SymbolDefinition symbolDefinition) {
        this.symbolDefinition = symbolDefinition;
    }

    @Override
    public boolean replaceChildNode(Node oldChild, Node newChild) {
        if (name == oldChild) {
            setName((NameExpr) newChild);
            return true;
        }
        return false;
    }
}
