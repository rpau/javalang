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

package org.walkmod.javalang.ast.body;

import java.util.List;
import org.walkmod.javalang.ast.Node;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

public class JavadocTag extends Node {

    private String name;

    private List<String> values = null;

    private boolean isInline = false;

    public JavadocTag() {
    }

    public JavadocTag(String name, List<String> values, boolean isInline) {
        setName(name);
        setValues(values);
        setInline(isInline);
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        // TODO Auto-generated method stub
return null;
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
    }

    // TODO Auto-generated method stub
public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public boolean isInline() {
        return isInline;
    }

    public void setInline(boolean isInline) {
        this.isInline = isInline;
    }
}
