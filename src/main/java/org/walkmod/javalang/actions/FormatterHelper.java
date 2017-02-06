package org.walkmod.javalang.actions;

public class FormatterHelper {

   public static String indent(String text, String indentation, char indentationChar, int indentationLevel,
         int indentationSize, boolean requiresExtraIndentationOnFirstLine) {
      
      String result = text;

      StringBuilder sb = new StringBuilder();
      String[] lines = text.split("\n");
      
      String theoricalIndentationText = lines[0].substring(0, indentationSize * indentationLevel);
      
      if(requiresExtraIndentationOnFirstLine){
         for(int i = 0; i < indentationSize; i++){
            sb.append(indentationChar);
         }
      }
      sb.append(lines[0].substring(indentationSize * indentationLevel));
     

      for (int i = 1; i < lines.length; i++) {
         String line = lines[i];

         if (indentation.length() > 0) {
            //we replace the supposed indentation chars at indentation level for the existing ones.
            line = line.replaceFirst(theoricalIndentationText, indentation);
         }

         sb.append('\n').append(line);
      }

      if (text.endsWith("\n")) {
         sb.append('\n');
      }
      result = sb.toString();

      return result;
   }
}
