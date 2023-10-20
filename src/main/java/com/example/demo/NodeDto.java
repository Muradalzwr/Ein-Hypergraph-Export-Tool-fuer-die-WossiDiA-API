package com.example.demo;

public class NodeDto {
     String id;
     String label ;

     String signature;
     String type ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NodeDto() {
    }

    public NodeDto(String id, String label, String signature, String type) {
        this.id = id;
        this.label = label;
        this.signature = signature;
        this.type = type;
    }
}
