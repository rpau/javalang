src/config/eclipse/formatter:

eclipse-java-google-style.xml - original googly java style
java.xml - slightly tuned
    
    <!--
     based on https://raw.githubusercontent.com/google/styleguide/gh-pages/eclipse-java-google-style.xml

     changes compared to original google java code style:
     - tab size is 4 chars instead of 2 chars
     - maximum line length 120 chars instead of 100
     - use same wrapping for "?" and ":" of ?:-Operator. Either none or both.

   alignments:
    48: wrap all if necessary, default indent
    50: when necessary on column
    82: wrap all but first if necessary, column indent
    18: wrap where necessary, indent on column
    16: wrap where necessary, default indent

-->

Format with:

    mvn formatter:format
