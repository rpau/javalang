package org.walkmod.javalang;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import org.walkmod.javalang.ast.body.JavadocTag;
import org.walkmod.javalang.tags.TagsParser;

public class JavadocManager {

  public static List<JavadocTag> parse(String content) throws Exception {
    List<JavadocTag> javadocTags = null;
    if (content != null) {
      InputStream is = new ByteArrayInputStream(content.getBytes());
      try {
        javadocTags = TagsParser.parse(is);
      } finally {
        is.close();
      }
    }
    return javadocTags;
  }
}
