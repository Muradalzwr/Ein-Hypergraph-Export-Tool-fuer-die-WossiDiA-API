package com.example.demo;

public class ResponseDto {
    String graph;
    String Graphml;
    private String message;
    private String graphmlContent;

    public ResponseDto() {
    }

    public ResponseDto(String graph, String graphml) {
        this.graph = graph;
        Graphml = graphml;
    }

    public String getGraph() {
        return graph;
    }

    public void setGraph(String graph) {
        this.graph = graph;
    }

    public String getGraphml() {
        return Graphml;
    }

    public void setGraphml(String graphml) {
        Graphml = graphml;
    }
}