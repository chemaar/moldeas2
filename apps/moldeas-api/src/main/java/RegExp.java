import java.io.Console;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegExp {
	 public static void main(String[] args){


		 String uri = "http://purl.org/weso/cpv/2008/14521140";
		 String datasetBase = "http://purl.org/weso/";
	      // comprueba que contenga @
	      Pattern p  = Pattern.compile("cpv/2008/.*");
	      System.out.println(uri.startsWith(datasetBase));
	      String input = uri.substring(datasetBase.length());
	      System.out.println(input);
	      System.out.println(p.matcher(input).find());
          System.out.println(p.matcher(input).matches());
         	
	      // comprueba que no contenga caracteres prohibidos	
	    
	    }
}
