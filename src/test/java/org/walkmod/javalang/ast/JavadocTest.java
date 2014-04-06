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

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.walkmod.javalang.JavadocManager;
import org.walkmod.javalang.ast.body.JavadocTag;

public class JavadocTest {

	@Test
	public void testSingleTag() throws Exception {
		String javadoc = "@author walkmod";
		List<JavadocTag> tags = JavadocManager.parse(javadoc);
		Assert.assertTrue(!tags.isEmpty());
	}

	@Test
	public void testMultipleTag() throws Exception {
		String javadoc = "@author <a href=\"mailto:benito@mail.com\">walkmod</a> \n @version 1.5 ";
		List<JavadocTag> tags = JavadocManager.parse(javadoc);
		Assert.assertTrue(tags.size() == 2);
		JavadocTag jt = tags.get(0);
		Assert.assertEquals("@author", jt.getName());
		Assert.assertNotNull(jt.getValues());
		Assert.assertEquals(1, jt.getValues().size());
		Assert.assertEquals("<a href=\"mailto:benito@mail.com\">walkmod</a>",
				jt.getValues().get(0));
		jt = tags.get(1);
		Assert.assertEquals("@version", jt.getName());
		Assert.assertNotNull(jt.getValues());
		Assert.assertEquals(1, jt.getValues().size());
		Assert.assertEquals("1.5", jt.getValues().get(0));
	}

	@Test
	public void testSpecificBlockTags() throws Exception {
		String javadoc = "@param  fooParam  my param description";
		List<JavadocTag> tags = JavadocManager.parse(javadoc);
		Assert.assertTrue(tags.size() == 1);
		JavadocTag jt = tags.get(0);
		Assert.assertEquals("@param", jt.getName());
		Assert.assertNotNull(jt.getValues());
		Assert.assertEquals(2, jt.getValues().size());
		Assert.assertEquals("fooParam", jt.getValues().get(0));
		Assert.assertEquals("my param description", jt.getValues().get(1));
		javadoc = "@param  fooParam";
		tags = JavadocManager.parse(javadoc);
		Assert.assertTrue(tags.size() == 1);
		jt = tags.get(0);
		Assert.assertEquals("@param", jt.getName());
		Assert.assertNotNull(jt.getValues());
		Assert.assertEquals(1, jt.getValues().size());
		Assert.assertEquals("fooParam", jt.getValues().get(0));
	}

	@Test
	public void testInlineTags() throws Exception {
		String javadoc = "Use the {@link #getComponentAt(int, int) getComponentAt} method.";
		List<JavadocTag> tags = JavadocManager.parse(javadoc);
		Assert.assertTrue(tags.size() == 1);
		JavadocTag jt = tags.get(0);
		Assert.assertEquals("@link", jt.getName());
		Assert.assertNotNull(jt.getValues());
		Assert.assertEquals(2, jt.getValues().size());
		Assert.assertEquals("#getComponentAt(int, int)", jt.getValues().get(0));
		Assert.assertEquals("getComponentAt", jt.getValues().get(1));
	}
}
