package org.walkmod.javalang.ast.type;

import java.util.List;

import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;


public class IntersectionType extends Type {
	
	private List<ReferenceType> bounds;
	
	public IntersectionType(){
		
	}
	public IntersectionType(List<ReferenceType> bounds){
		setBounds(bounds);
	}
	
	public IntersectionType(int beginLine, int beginColumn, int endLine,
			int endColumn, 
			List<ReferenceType> bounds) {
		super(beginLine, beginColumn, endLine, endColumn, null);
		setBounds(bounds);
	}

	@Override
	public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	@Override
	public <A> void accept(VoidVisitor<A> v, A arg) {
		v.visit(this, arg);
	}

	public List<ReferenceType> getBounds() {
		return bounds;
	}

	public void setBounds(List<ReferenceType> bounds) {
		this.bounds = bounds;
		setAsParentNodeOf(bounds);
	}
	
	

}
