package org.walkmod.javalang.ast;

import org.junit.Test;
import org.walkmod.javalang.ASTManager;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class CompilationUnitTest {

  @Test
  public void shouldReturnCorrectFilenameForClass() throws Exception {
    final File packageInfoFile = new File(getClass().getClassLoader()
        .getResource("test.txt").getFile());
    final CompilationUnit cu = ASTManager.parse(packageInfoFile);

    assertEquals("IntMath", cu.getSimpleName());
    assertEquals("com/google/common/math/IntMath.java", cu.getFileName());
  }

  @Test
  public void shouldReturnCorrectFilenameForPackageInfoFile() throws Exception {
    final File packageInfoFile = new File(getClass().getClassLoader()
        .getResource("package-info.txt").getFile());
    final CompilationUnit cu = ASTManager.parse(packageInfoFile);

    assertEquals("package-info", cu.getSimpleName());
    assertEquals("com/example/hello/package-info.java", cu.getFileName());
  }
}
