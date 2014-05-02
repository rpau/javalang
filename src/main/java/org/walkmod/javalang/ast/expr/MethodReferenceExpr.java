package org.walkmod.javalang.ast.expr;

import java.util.List;

import org.walkmod.javalang.ast.TypeParameter;
import org.walkmod.javalang.visitors.GenericVisitor;
import org.walkmod.javalang.visitors.VoidVisitor;

public class MethodReferenceExpr extends Expression {

	private Expression scope;

	private List<TypeParameter> typeParameters;

	private String identifier;

	public MethodReferenceExpr() {
	}

	public MethodReferenceExpr(int beginLine, int beginColumn, int endLine,
			int endColumn, Expression scope,
			List<TypeParameter> typeParameters, String identifier) {

		super(beginLine, beginColumn, endLine, endColumn);
		this.scope = scope;
		this.typeParameters = typeParameters;
		this.identifier = identifier;
	}

	@Override
	public <R, A> R accept(GenericVisitor<R, A> v, A arg) {

		return v.visit(this, arg);
	}

	@Override
	public <A> void accept(VoidVisitor<A> v, A arg) {
		v.visit(this, arg);
	}

	public Expression getScope() {
		return scope;
	}

	public void setScope(Expression scope) {
		this.scope = scope;
	}

	public List<TypeParameter> getTypeParameters() {
		return typeParameters;
	}

	public void setTypeParameters(List<TypeParameter> typeParameters) {
		this.typeParameters = typeParameters;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

}
