package org.walkmod.javalang.ast;

import java.io.File;
import org.junit.Assert;
import org.junit.Test;
import org.walkmod.javalang.ASTManager;
import org.walkmod.javalang.visitors.VoidVisitorAdapter;

public class VoidVisitorAdapterTest {

  class MyVisitor extends VoidVisitorAdapter {
  }

  ;

  @Test
  public void testVisit() throws Exception {
    File f = new File("src/test/resources/source1.8.txt");
    CompilationUnit cu = ASTManager.parse(f);
    Assert.assertNotNull(cu);
    VoidVisitorAdapter vva = new MyVisitor();
    vva.visit(cu, null);
    Assert.assertTrue(true);
  }
}
