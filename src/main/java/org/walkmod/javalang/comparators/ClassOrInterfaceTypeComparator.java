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
package org.walkmod.javalang.comparators;

import java.util.Comparator;

import org.walkmod.javalang.ast.type.ClassOrInterfaceType;

public class ClassOrInterfaceTypeComparator implements
		Comparator<ClassOrInterfaceType> {

	@Override
	public int compare(ClassOrInterfaceType o1, ClassOrInterfaceType o2) {
		if (o1 == null && o2 == null) {
			return 0;
		}
		if (o1 == null && o2 != null) {
			return 1;
		}
		if (o1 != null && o2 == null) {
			return -1;
		}
		// o1 != null && o2 != null
		int scope = compare(o1.getScope(), o2.getScope());
		if (scope == 0) {
			return o1.getName().compareTo(o2.getName());
		}
		return scope;
	}
}
