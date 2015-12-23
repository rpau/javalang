package org.walkmod.javalang.javadoclinks;

public class FieldLink implements Link {

	private String className;
	
	private String name;
	
	

	public FieldLink(String className, String name) {
		this.className = className;
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
