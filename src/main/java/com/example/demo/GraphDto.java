package com.example.demo;

import java.util.List;

public class GraphDto {
    public List<NodeDto> nodes;
    public List<EdgeDto> edges;

    public List<NodeDto> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodeDto> nodes) {
        this.nodes = nodes;
    }

    public List<EdgeDto> getEdges() {
        return edges;
    }

    public void setEdges(List<EdgeDto> edges) {
        this.edges = edges;
    }

    public GraphDto() {
    }

    public GraphDto(List<NodeDto> nodes, List<EdgeDto> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }
}
