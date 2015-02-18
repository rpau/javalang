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
package org.walkmod.javalang.ast;

import java.io.File;
import org.junit.Assert;
import org.junit.Test;
import org.walkmod.javalang.ASTManager;
import org.walkmod.javalang.ast.CompilationUnit;

public class ASTParserTest {

	@Test
	public void testParsing() throws Exception {
		File f = new File("src/test/resources/source1.8.txt");
		CompilationUnit cu = ASTManager.parse(f);
		Assert.assertNotNull(cu);
		System.out.println(cu.toString());
	}

	@Test
	public void testLicense() throws Exception {
		File f = new File("src/test/resources/licensed-sources.txt");
		CompilationUnit cu = ASTManager.parse(f);
		Assert.assertNotNull(cu);
		cu.toString();
		// System.out.println(cu.toString());
	}

	@Test
	public void testLinesBetweenStmts() throws Exception {
		File f = new File("src/test/resources/multipleLinesBetweenStmt.txt");
		CompilationUnit cu = ASTManager.parse(f);
		Assert.assertNotNull(cu);
		System.out.println(cu.toString());
	}

	@Test
	public void testLinesBetweenImports() throws Exception {
		File f = new File("src/test/resources/multipleLinesBetweenImports.txt");
		CompilationUnit cu = ASTManager.parse(f);
		Assert.assertNotNull(cu);
		System.out.println(cu.toString());
	}
	
	@Test
	public void testLinesBetweenParameters() throws Exception {
		File f = new File("src/test/resources/multipleLinesBetweenParameters.txt");
		CompilationUnit cu = ASTManager.parse(f);
		Assert.assertNotNull(cu);
		System.out.println(cu.toString());
	}

	@Test
    public void testLines() throws Exception {
        File f = new File("src/test/resources/test.txt");
        CompilationUnit cu = ASTManager.parse(f);
        Assert.assertNotNull(cu);
        System.out.println(cu.toString());
    }
}
