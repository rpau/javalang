package org.walkmod.javalang.ast;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.walkmod.javalang.ASTManager;
import org.walkmod.javalang.ast.body.BodyDeclaration;
import org.walkmod.javalang.ast.body.MethodDeclaration;
import org.walkmod.javalang.visitors.VoidVisitorAdapter;

public class ASTUpdatesTest {

    @Test
    public void test() throws Exception {
        CompilationUnit cu = ASTManager.parse("public class Foo{ public void bar(){}}");
        VoidVisitorAdapter<?> visitor = new VoidVisitorAdapter<Object>() {
            public void visit(MethodDeclaration md, Object ctx) {
                md.remove();
            }
        };
        cu.accept(visitor, null);

        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();
        Assert.assertTrue(members.isEmpty());
    }
}
