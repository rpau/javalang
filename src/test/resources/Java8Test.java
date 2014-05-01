import java.util.Collection;
import java.util.LinkedList;


public class Java8Test{
	
	
	public static void main(String[] args) throws Exception{
		
		Collection<String> c = new LinkedList<String>();
		
		c.add("albert");
		c.add("raquel");
		
		c.forEach(s -> {
			   if (s.startsWith("a"))
				     System.out.println(s);
				});
		
	}
	
}