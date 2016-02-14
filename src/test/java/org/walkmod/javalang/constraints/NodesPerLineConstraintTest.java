package org.walkmod.javalang.constraints;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.walkmod.javalang.ASTManager;
import org.walkmod.javalang.ast.CompilationUnit;
import org.walkmod.javalang.ast.body.ClassOrInterfaceDeclaration;
import org.walkmod.javalang.ast.body.FieldDeclaration;
import org.walkmod.javalang.ast.expr.MethodCallExpr;
import org.walkmod.javalang.visitors.VoidVisitorAdapter;
import org.walkmod.modelchecker.Constraint;

public class NodesPerLineConstraintTest {

   @Test
   public void testNonConstrained() throws Exception {
      CompilationUnit cu = ASTManager.parse("public class Foo{}", false);
      List<Constraint> constraints = new LinkedList<Constraint>();
      NodesPerLineConstraint cons = new NodesPerLineConstraint();
      cons.addLine(1, 1);
      constraints.add(cons);
      cu.setConstraints(constraints);

      VoidVisitorAdapter<Map<String, Object>> visitor = new VoidVisitorAdapter<Map<String, Object>>() {
         @Override
         public void visit(ClassOrInterfaceDeclaration n, Map<String, Object> ctx) {
            ctx.put("visited", true);
         }
      };
      Map<String, Object> ctx = new HashMap<String, Object>();
      cu.accept(visitor, ctx);

      Assert.assertTrue(ctx.containsKey("visited"));

   }

   @Test
   public void testConstrained() throws Exception {
      CompilationUnit cu = ASTManager.parse("\n\npublic class Foo{}", false);
      List<Constraint> constraints = new LinkedList<Constraint>();
      NodesPerLineConstraint cons = new NodesPerLineConstraint();
      cons.addLine(1, 1);
      constraints.add(cons);
      cu.setConstraints(constraints);

      VoidVisitorAdapter<Map<String, Object>> visitor = new VoidVisitorAdapter<Map<String, Object>>() {
         @Override
         public void visit(ClassOrInterfaceDeclaration n, Map<String, Object> ctx) {
            ctx.put("visited", true);
         }
      };
      Map<String, Object> ctx = new HashMap<String, Object>();
      cu.accept(visitor, ctx);

      Assert.assertFalse(ctx.containsKey("visited"));

   }
   
   @Test
   public void testNotConstrainedInnerNodes() throws Exception{
      CompilationUnit cu = ASTManager.parse("public class Foo{\n public void bar() {\n System.out.println(\"hello\"); \n } \n}", false);
      List<Constraint> constraints = new LinkedList<Constraint>();
      NodesPerLineConstraint cons = new NodesPerLineConstraint();
      cons.addLine(3, 3);
      constraints.add(cons);
      cu.setConstraints(constraints);

      VoidVisitorAdapter<Map<String, Object>> visitor = new VoidVisitorAdapter<Map<String, Object>>() {
         @Override
         public void visit(MethodCallExpr n, Map<String, Object> ctx) {
            ctx.put("visited", true);
         }
      };
      Map<String, Object> ctx = new HashMap<String, Object>();
      cu.accept(visitor, ctx);

      Assert.assertTrue(ctx.containsKey("visited"));
   }
   
   @Test
   public void testConstrainedInnerNodes() throws Exception{
      CompilationUnit cu = ASTManager.parse("public class Foo{\n public void bar() {\n System.out.println(\"hello\"); \n public String name; \n} \n}", false);
      List<Constraint> constraints = new LinkedList<Constraint>();
      NodesPerLineConstraint cons = new NodesPerLineConstraint();
      cons.addLine(3, 3);
      constraints.add(cons);
      cu.setConstraints(constraints);

      VoidVisitorAdapter<Map<String, Object>> visitor = new VoidVisitorAdapter<Map<String, Object>>() {
         @Override
         public void visit(FieldDeclaration n, Map<String, Object> ctx) {
            ctx.put("visited", true);
         }
      };
      Map<String, Object> ctx = new HashMap<String, Object>();
      cu.accept(visitor, ctx);

      Assert.assertFalse(ctx.containsKey("visited"));
   }
}
