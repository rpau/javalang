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

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.walkmod.lang.ast.body.BodyDeclaration;

public class BodyDeclaratorComparator implements Comparator<BodyDeclaration> {

	private String[] order;

	@SuppressWarnings("rawtypes")
	private Map<String, Comparator> comparator = new HashMap<String, Comparator>();

	public BodyDeclaratorComparator() {
		order = new String[]{"FieldDeclaration", "EnumConstantDeclaration",
				"InitializerDeclaration", "ConstructorDeclaration",
				"AnnotationMemberDeclaration", "MethodDeclaration",
				"TypeDeclaration", "EmptyMemberDeclaration"};
		comparator.put("FieldDeclaration", new FieldDeclarationComparator());
		comparator.put("MethodDeclaration", new MethodDeclarationComparator());
		comparator.put("EnumConstantDeclaration", new EnumConstantComparator());
		comparator.put("AnnotationMemberDeclaration",
				new AnnotationMemberDeclarationComparator());
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compare(BodyDeclaration o1, BodyDeclaration o2) {
		Integer o1_position = Arrays.binarySearch(order, o1.getClass()
				.getSimpleName());
		Integer o2_position = Arrays.binarySearch(order, o2.getClass()
				.getSimpleName());
		if (o1_position < 0) {
			return 0;
		} else if (o1_position == o2_position) {
			if (comparator.containsKey(order[o1_position])) {
				return comparator.get(order[o1_position]).compare(o1, o2);
			} else {
				return 0;
			}
		} else {
			return o1_position.compareTo(o2_position);
		}
	}
}
