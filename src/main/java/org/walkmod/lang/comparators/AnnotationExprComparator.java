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
package org.walkmod.lang.comparators;

import java.util.Comparator;
import org.walkmod.lang.ast.expr.AnnotationExpr;

public class AnnotationExprComparator implements Comparator<AnnotationExpr> {

	@Override
	public int compare(AnnotationExpr an1, AnnotationExpr an2) {
		if (an1.getName().toString().equals(an2.getName().toString())) {
			return 0;
		}
		return -1;
	}
}
