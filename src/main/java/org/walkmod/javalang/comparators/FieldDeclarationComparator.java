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
import java.util.List;

import org.walkmod.javalang.ast.body.FieldDeclaration;
import org.walkmod.javalang.ast.body.VariableDeclarator;

public class FieldDeclarationComparator implements Comparator<FieldDeclaration> {

	@Override
	public int compare(FieldDeclaration fd1, FieldDeclaration fd2) {
		if (fd1.getType().equals(fd2.getType())) {
			List<VariableDeclarator> vds = fd1.getVariables();
			List<VariableDeclarator> vds2 = fd2.getVariables();
			if (vds == null || vds.size() == 0 || vds2 == null
					|| vds2.size() == 0) {
				return -1;
			}
			if (vds.size() == vds2.size()) {
				if (vds.containsAll(vds2)) {
					return 0;
				}
			}
		}
		return -1;
	}
}
