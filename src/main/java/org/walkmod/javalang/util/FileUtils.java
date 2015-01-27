/* 
  Copyright (C) 2013 Raquel Pau and Albert Coroleu.
 
 Walkmod is free software: you can redistribute it and/or modify
 it under the terms of the GNU Lesser General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.
 
 Walkmod is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Lesser General Public License for more details.
 
 You should have received a copy of the GNU Lesser General Public License
 along with Walkmod.  If not, see <http://www.gnu.org/licenses/>.*/

package org.walkmod.javalang.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.walkmod.javalang.JavaParser;
import org.walkmod.javalang.ParseException;
import org.walkmod.javalang.ast.CompilationUnit;
import org.walkmod.javalang.ast.PackageDeclaration;
import org.walkmod.javalang.ast.body.TypeDeclaration;

public class FileUtils {

    public static CompilationUnit getCompilationUnit(File outputDirectory, PackageDeclaration pd, TypeDeclaration td) throws ParseException, IOException {
        File sourceFile = getSourceFile(outputDirectory, pd, td);
        if (sourceFile.exists()) {
            return JavaParser.parse(sourceFile);
        } else {
            CompilationUnit result = new CompilationUnit();
            result.setPackage(pd);
            return result;
        }
    }

    public static File getSourceFile(File outputDirectory, PackageDeclaration pd, TypeDeclaration td) {
        String pdName = ".";
        if (pd != null) {
            pdName = pd.getName().toString().replace('.', '/');
        }
        return new File(outputDirectory, pdName + "//" + td.getName() + ".java");
    }

    public static File getSourceFile(File outputDirectory, PackageDeclaration pd, String clazzName) {
        String pdName = ".";
        if (pd != null) {
            pdName = pd.getName().toString().replace('.', '/');
        }
        return new File(outputDirectory, pdName + "//" + clazzName + ".java");
    }

    public static String normalizeName(String name) {
        return name.replaceAll("::", ".");
    }

    public static String resolveFile(String qualifiedName) {
        return qualifiedName.replaceAll("::", "/");
    }

    public static void createSourceFile(File parent, File source) throws Exception {
        File owner = source.getParentFile();
        if (!owner.exists()) {
            owner.mkdirs();
        }
        source.createNewFile();
    }

    public static List<String> fileToLines(String filename) throws Exception {
        List<String> lines = new LinkedList<String>();
        String line = "";
        BufferedReader in = new BufferedReader(new FileReader(filename));
        while ((line = in.readLine()) != null) {
            lines.add(line);
        }
        in.close();
        return lines;
    }
}
