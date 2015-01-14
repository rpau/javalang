/*
 * Copyright (C) 2013 Raquel Pau and Albert Coroleu.
 * 
 * Walkmod is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * Walkmod is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Walkmod. If
 * not, see <http://www.gnu.org/licenses/>.
 */

package org.walkmod.javalang.merger;

import java.util.LinkedList;
import java.util.List;
import org.walkmod.javalang.ast.body.FieldDeclaration;
import org.walkmod.javalang.ast.body.VariableDeclarator;
import org.walkmod.merger.AppendMergePolicy;

public class FieldDeclarationPolicy extends AppendMergePolicy<FieldDeclaration> {

  @Override
  public void apply(FieldDeclaration remoteObject, List<FieldDeclaration> localList,
      List<FieldDeclaration> resultList) {
    List<FieldDeclaration> localFields = new LinkedList<FieldDeclaration>();
    if (localList != null) {
      for (FieldDeclaration localField : localList) {
        localFields.addAll(split(localField));
      }
    }
    if (remoteObject.getVariables() != null && remoteObject.getVariables().size() > 1) {
      List<FieldDeclaration> remoteFields = split(remoteObject);
      for (FieldDeclaration remoteField : remoteFields) {
        super.apply(remoteField, localFields, resultList);
      }
    } else {
      super.apply(remoteObject, localFields, resultList);
    }
  }

  private List<FieldDeclaration> split(FieldDeclaration fieldDeclaration) {
    List<FieldDeclaration> res = new LinkedList<FieldDeclaration>();
    if (fieldDeclaration.getVariables().size() > 1) {
      for (VariableDeclarator vd : fieldDeclaration.getVariables()) {
        FieldDeclaration fd = new FieldDeclaration();
        fd.setAnnotations(fieldDeclaration.getAnnotations());
        fd.setJavaDoc(fieldDeclaration.getJavaDoc());
        fd.setModifiers(fieldDeclaration.getModifiers());
        fd.setType(fieldDeclaration.getType());
        fd.setBeginLine(fieldDeclaration.getBeginLine());
        fd.setBeginColumn(fieldDeclaration.getBeginColumn());
        fd.setEndColumn(vd.getEndColumn());
        fd.setEndLine(vd.getEndLine());
        List<VariableDeclarator> vdList = new LinkedList<VariableDeclarator>();
        vdList.add(vd);
        fd.setVariables(vdList);
        res.add(fd);
      }
    } else {
      res.add(fieldDeclaration);
    }
    return res;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void apply(List<FieldDeclaration> localList, List<FieldDeclaration> remoteList,
      @SuppressWarnings("rawtypes") List resultList) {
    List<FieldDeclaration> localFields = new LinkedList<FieldDeclaration>();
    if (localList != null) {
      for (FieldDeclaration localField : localList) {
        localFields.addAll(split(localField));
      }
    }
    List<FieldDeclaration> remoteFields = new LinkedList<FieldDeclaration>();
    if (remoteList != null) {
      for (FieldDeclaration remoteField : remoteList) {
        remoteFields.addAll(split(remoteField));
      }
    }
    super.apply(localFields, remoteFields, resultList);
  }
}
