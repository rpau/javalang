package org.walkmod.javalang.ast;

import junit.framework.Assert;

import org.junit.Test;
import org.walkmod.javalang.ASTManager;
import org.walkmod.javalang.ast.body.MethodDeclaration;
import org.walkmod.javalang.ast.body.Parameter;
import org.walkmod.javalang.ast.body.VariableDeclaratorId;
import org.walkmod.javalang.ast.type.PrimitiveType;
import org.walkmod.javalang.ast.type.PrimitiveType.Primitive;

public class DumpVisitorTest {


    @Test
    public void testAddParam() throws Exception {
        CompilationUnit cu = ASTManager.parse("public class A { public void foo(int x){}}");
        MethodDeclaration md = (MethodDeclaration) cu.getTypes().get(0).getMembers().get(0);
        md.getParameters().add(new Parameter(new PrimitiveType(Primitive.Int), new VariableDeclaratorId("y")));
        String s = md.toString();
        Assert.assertTrue(s.indexOf('\n') == -1);
    }

    @Test
    public void testIssue13() throws Exception {
        CompilationUnit cu = ASTManager.parse("public enum A{B{};}");
        System.out.println(cu.toString());
        Assert.assertTrue(true);
    }

}
