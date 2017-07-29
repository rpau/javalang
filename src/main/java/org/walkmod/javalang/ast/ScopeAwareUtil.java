package org.walkmod.javalang.ast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Common implementations of {@link ScopeAware} methods for delegation.
 */
public class ScopeAwareUtil {
    private ScopeAwareUtil() {}

    public static Map<String, SymbolDefinition> getVariableDefinitions(Node n) {
        Node parent = n.getParentNode();
        while (parent != null && parent instanceof ScopeAware) {
            parent = parent.getParentNode();
        }
        if (parent != null && (parent instanceof ScopeAware)) {
            return ((ScopeAware) parent).getVariableDefinitions();
        }
        return new HashMap<String, SymbolDefinition>();
    }

    public static Map<String, SymbolDefinition> getVariableDefinitions2(Node n) {
        Node parent = n.getParentNode();
        while (parent != null && parent instanceof ScopeAware) {
            parent = parent.getParentNode();
        }
        if (parent != null) {
            return ((ScopeAware) parent).getVariableDefinitions();
        }
        return new HashMap<String, SymbolDefinition>();
    }

    public static Map<String, SymbolDefinition> getVariableDefinitions3(Node n) {
        Node parent = n.getParentNode();
        Map<String, SymbolDefinition> result = null;
        while (parent != null && parent instanceof ScopeAware) {
            parent = parent.getParentNode();
        }
        if (parent != null && (parent instanceof ScopeAware)) {
            result = ((ScopeAware) parent).getVariableDefinitions();
        }
        if (result == null) {
            result = new HashMap<String, SymbolDefinition>();
        }
        return result;
    }

    public static ScopeAware findParentScope(Node n) {
        Node parent = n.getParentNode();
        while (parent != null && !(parent instanceof ScopeAware)) {
            parent = parent.getParentNode();
        }
        return parent != null ? (ScopeAware) parent : null;
    }

    public static ScopeAware findParentScope2(Node n) {
        Node parent = n.getParentNode();
        while (parent != null && parent instanceof ScopeAware) {
            parent = parent.getParentNode();
        }
        return parent != null ? (ScopeAware) parent : null;
    }

    public static Map<String, List<SymbolDefinition>> getMethodDefinitions(Node n) {
        Node parent = n.getParentNode();
        while (parent != null && parent instanceof ScopeAware) {
            parent = parent.getParentNode();
        }
        if (parent != null && (parent instanceof ScopeAware)) {
            return ((ScopeAware) parent).getMethodDefinitions();
        }
        return new HashMap<String, List<SymbolDefinition>>();
    }

    public static Map<String, List<SymbolDefinition>> getMethodDefinitions2(Node n) {
        Node parent = n.getParentNode();
        while (parent != null && parent instanceof ScopeAware) {
            parent = parent.getParentNode();
        }
        if (parent != null) {
            return ((ScopeAware) parent).getMethodDefinitions();
        }
        return new HashMap<String, List<SymbolDefinition>>();
    }

    public static Map<String, SymbolDefinition> getTypeDefinitions(Node n) {
        Node parent = n.getParentNode();
        while (parent != null && parent instanceof ScopeAware) {
            parent = parent.getParentNode();
        }
        if (parent != null && (parent instanceof ScopeAware)) {
            return ((ScopeAware) parent).getTypeDefinitions();
        }
        return new HashMap<String, SymbolDefinition>();
    }

    public static Map<String, SymbolDefinition> getTypeDefinitions2(Node n) {
        Node parent = n.getParentNode();
        while (parent != null && parent instanceof ScopeAware) {
            parent = parent.getParentNode();
        }
        if (parent != null) {
            return ((ScopeAware) parent).getTypeDefinitions();
        }
        return new HashMap<String, SymbolDefinition>();
    }
}
