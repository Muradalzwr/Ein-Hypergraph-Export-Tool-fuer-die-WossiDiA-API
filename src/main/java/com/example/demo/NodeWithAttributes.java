package com.example.demo;

import java.util.HashMap;
import java.util.Map;

public class NodeWithAttributes {
    private String id;
    private Map<String, Object> attributes;


    public NodeWithAttributes(String id, Map<String, Object> attributes) {
        this.id = id;
        this.attributes = attributes;
    }

    public NodeWithAttributes(String id) {
        this.id = id;
        this.attributes = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void addAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
