import java.util.Collection;
import java.util.LinkedList;


public class Java8Test{
	
	
	public static void main(String[] args) throws Exception{
		
		Object o = s -> String::length;		
		
		() -> {};                   // No parameters; result is void

		
		() -> 42;                     // No parameters, expression body
		() -> null;                   // No parameters, expression body
		() -> { return 42; };         // No parameters, block body with return
		() -> { System.gc(); };       // No parameters, void block body

		() -> {
		  if (true) return 12;
		  else {
		    int result = 15;
		    for (int i = 1; i < 10; i++)
		      result *= i;
		    return result;
		  }
		}   ;                       // Complex block body with returns

		(int x) -> x+1 ;            // Single declared-type parameter
		
		(int x) -> { return x+1; }; // Single declared-type parameter
		(x) -> x+1;                 // Single inferred-type parameter
		x -> x+1;                   // Parens optional for single inferred-type case

		(String s) -> s.length();   // Single declared-type parameter
		(Thread t) -> { t.start(); }; // Single declared-type parameter
		s -> s.length();              // Single inferred-type parameter
		t -> { t.start(); };          // Single inferred-type parameter

		(int x, int y) -> x+y;      // Multiple declared-type parameters
		(x,y) -> x+y;               // Multiple inferred-type parameters
		(final int x) -> x+1;       // Modified declared-type parameter
		
		
	}
	
}