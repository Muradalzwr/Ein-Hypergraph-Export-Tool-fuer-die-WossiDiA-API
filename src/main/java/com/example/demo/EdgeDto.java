package com.example.demo;

public class EdgeDto {
    private String id;
    private String source;
    private String target;
 private  String type;

    public EdgeDto(String id, String source, String target, String type) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EdgeDto(String id, String source, String target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public EdgeDto() {
    }


    public EdgeDto(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + source + " -> " + target;
    }
}

