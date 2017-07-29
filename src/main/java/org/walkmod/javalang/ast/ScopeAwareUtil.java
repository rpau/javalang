package org.walkmod.javalang.ast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Common implementations of {@link ScopeAware} methods for delegation.
 */
public class ScopeAwareUtil {
    private ScopeAwareUtil() {}

    public static ScopeAware findParentScope(Node n) {
        Node parent = n.getParentNode();
        while (parent != null && !(parent instanceof ScopeAware)) {
            parent = parent.getParentNode();
        }
        return parent != null ? (ScopeAware) parent : null;
    }

    public static Map<String, SymbolDefinition> getVariableDefinitions(Node n) {
        final ScopeAware scope = findParentScope(n);
        return scope != null ? scope.getVariableDefinitions() : new HashMap<String, SymbolDefinition>();
    }

    public static Map<String, List<SymbolDefinition>> getMethodDefinitions(Node n) {
        final ScopeAware scope = findParentScope(n);
        return scope != null ? scope.getMethodDefinitions() : new HashMap<String, List<SymbolDefinition>>();
    }

    public static Map<String, SymbolDefinition> getTypeDefinitions(Node n) {
        final ScopeAware scope = findParentScope(n);
        return scope != null ? scope.getTypeDefinitions() : new HashMap<String, SymbolDefinition>();
    }
}
