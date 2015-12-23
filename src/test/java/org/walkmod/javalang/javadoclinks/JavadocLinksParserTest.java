package org.walkmod.javalang.javadoclinks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

public class JavadocLinksParserTest {

	@Test
	public void testParser() throws ParseException{
		MethodLink ml = JavadocLinkParser.parse("Foo#bar()");
		Assert.assertEquals("Foo", ml.getClassName());
		Assert.assertEquals("bar", ml.getName());
	}
	
	@Test
	public void testFullName() throws ParseException{
		MethodLink ml = JavadocLinkParser.parse("org.walkmod.Foo#bar()");
		Assert.assertEquals("org.walkmod.Foo", ml.getClassName());
		Assert.assertEquals("bar", ml.getName());
	}
	
	@Test
	public void testInnerClass() throws ParseException{
		MethodLink ml = JavadocLinkParser.parse("org.walkmod.Foo$Aux#bar()");
		Assert.assertEquals("org.walkmod.Foo$Aux", ml.getClassName());
		Assert.assertEquals("bar", ml.getName());
	}
	
	@Test
	public void testAnonymousClass() throws ParseException{
		MethodLink ml = JavadocLinkParser.parse("org.walkmod.Foo$Aux$1$2#bar()");
		Assert.assertEquals("org.walkmod.Foo$Aux$1$2", ml.getClassName());
		Assert.assertEquals("bar", ml.getName());
	}
	
	@Test
	public void testOptionalName() throws ParseException{
		MethodLink ml = JavadocLinkParser.parse("#bar()");
		Assert.assertEquals("", ml.getClassName());
		Assert.assertEquals("bar", ml.getName());
	}
	
	@Test
	public void testArguments() throws ParseException{
		MethodLink ml = JavadocLinkParser.parse("#bar(int)");
		Assert.assertEquals("", ml.getClassName());
		Assert.assertEquals("bar", ml.getName());
		Assert.assertEquals(1, ml.getArguments().size());
	}
	
	@Test
	public void testMultipleArguments() throws ParseException{
		MethodLink ml = JavadocLinkParser.parse("#bar(int, int)");
		Assert.assertEquals("", ml.getClassName());
		Assert.assertEquals("bar", ml.getName());
		Assert.assertEquals(2, ml.getArguments().size());
	}
}
