/*package com.example.demo;


import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
public class exportcontroller {
    private final Restservice restservice;

    public exportcontroller(Restservice restservice) {
        this.restservice = restservice;
    }

    @GetMapping("/export-graph")
    public GraphDto  exportGraph() {
        String ss = restservice.getdatawoosida();
        System.out.println(ss);
        Graph<String, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);

        for (int i = 0; i < 10; i++) {
            graph.addVertex("Node" + i);
        }

        graph.addEdge("Node0", "Node1");
        graph.addEdge("Node1", "Node2");
        graph.addEdge("Node2", "Node3");
        graph.addEdge("Node3", "Node4");
        graph.addEdge("Node4", "Node5");
        graph.addEdge("Node5", "Node6");
        graph.addEdge("Node6", "Node7");
        graph.addEdge("Node7", "Node8");
        graph.addEdge("Node8", "Node9");
        graph.addEdge("Node9", "Node0");
        return convertGraphToDto(graph);

    }
    private GraphDto convertGraphToDto(Graph<String, DefaultEdge> graph) {
        GraphDto graphDto = new GraphDto();
        List<NodeDto> nodes = new ArrayList<>();
        List<EdgeDto> edges = new ArrayList<>();

        for (String vertex : graph.vertexSet()) {
            nodes.add(new NodeDto(vertex));
        }

        for (DefaultEdge edge : graph.edgeSet()) {
            String source = graph.getEdgeSource(edge);
            String target = graph.getEdgeTarget(edge);
            edges.add(new EdgeDto( "e1" ,source, target));
        }

        graphDto.setNodes(nodes);
        graphDto.setEdges(edges);

        return graphDto;
    }
}*/
package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.graphml.GraphMLExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

@RestController
@CrossOrigin("*")
public class ExportController {
    @Autowired
    RestTemplate restTemplate;

    @PostMapping("/export-graph")
    public ResponseDto exportGraph(@RequestBody String url) {
        ResponseDto res =  new ResponseDto();
        String decodedPath = URLDecoder.decode(url, StandardCharsets.UTF_8);
        String fullUrl = "https://api.wossidia.de" + decodedPath;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity <String> entity = new HttpEntity<String>(headers);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            ResponseEntity<String> response = restTemplate.exchange(fullUrl, HttpMethod.GET, entity, String.class);
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode nodesNode = rootNode.path("result").path("nodes");
            JsonNode edgesNode = rootNode.path("result").path("edges");



            JsonNode graphData = objectMapper.createObjectNode()
                    .putPOJO("nodes", nodesNode)
                    .putPOJO("edges", edgesNode);




/// convert to graphml file
            ObjectMapper objectMapperr = new ObjectMapper();
            JsonNode jsonNode = objectMapperr.readTree(graphData.toString());

            // Create a graph
            Graph<NodeDto, EdgeDto> graph = new DefaultDirectedGraph<>(EdgeDto.class);

            JsonNode nodes = jsonNode.get("nodes");

            ArrayList<NodeDto>  fullnode = new ArrayList<NodeDto>();
            for (Iterator<Map.Entry<String, JsonNode>> it = nodes.fields(); it.hasNext(); ) {

                Map.Entry<String, JsonNode> entry = it.next();
                NodeDto  nodee = new NodeDto();
                String nodeId = entry.getKey();
                String signature = entry.getValue().get("signature").asText();
                String type = entry.getValue().get("type").toString();


                nodee.setId(nodeId);
                nodee.setLabel(nodeId);
                nodee.setSignature(signature);
                nodee.setType(type);

                fullnode.add(nodee);
                graph.addVertex(nodee);


            }



            // Extract and add edges to the graph
            JsonNode edges = jsonNode.get("edges");
            for (Iterator<Map.Entry<String, JsonNode>> it = edges.fields(); it.hasNext(); ) {

                Map.Entry<String, JsonNode> entry = it.next();
                String edgeid = entry.getKey();
               String type = entry.getValue().get("type").toString();
                String sourceNodeId = entry.getValue().get("links").get("0").get("node").asText();
                String targetNodeId = entry.getValue().get("links").get("1").get("node").asText();
                NodeDto  srcnode = new NodeDto();
                NodeDto  targetnode = new NodeDto();
                for(int i=0; i <fullnode.size(); i++)
                {
                    NodeDto ee = fullnode.get(i);
                    if(ee.getId().equals(sourceNodeId))
                    {
                        srcnode = ee;
                    }
                    if(ee.getId().equals(targetNodeId))
                    {
                        targetnode =ee;
                    }

                }


                // Add the edge to the graph
          EdgeDto ee    =       graph.addEdge(srcnode, targetnode);
 ee.setId(edgeid);
 ee.setTarget(targetNodeId);
 ee.setSource(sourceNodeId);
 ee.setType(type);

            }
// Complete the GraphML content
            //  graphmlContent.append("</graphml>");

            GraphMLExporter<NodeDto, EdgeDto> exporter = new GraphMLExporter<>();
 exporter.setVertexIdProvider(new Function<NodeDto, String>(){

     @Override
     public String apply(NodeDto nodeDto) {
         return nodeDto.getLabel();
     }

     @Override
     public <V> Function<V, String> compose(Function<? super V, ? extends NodeDto> before) {
         return Function.super.compose(before);
     }

     @Override
     public <V> Function<NodeDto, V> andThen(Function<? super String, ? extends V> after) {
         return Function.super.andThen(after);
     }
 });


            exporter.setVertexAttributeProvider(new Function<NodeDto, Map<String, Attribute>>() {

                @Override
                public Map<String, Attribute> apply(NodeDto graphMember) {
                    Map<String, Attribute> toReturn = new HashMap<>();
                    toReturn.put("label", DefaultAttribute.createAttribute(graphMember.getLabel()));
                    toReturn.put("signature", DefaultAttribute.createAttribute(graphMember.getSignature()));
                    toReturn.put("type", DefaultAttribute.createAttribute(graphMember.getType()));

                    return toReturn;

                }

                @Override
                public <V> Function<V, Map<String, Attribute>> compose(Function<? super V, ? extends NodeDto> before) {
                    return Function.super.compose(before);
                }

                @Override
                public <V> Function<NodeDto, V> andThen(Function<? super Map<String, Attribute>, ? extends V> after) {
                    return Function.super.andThen(after);
                }
            });
            exporter.setEdgeAttributeProvider(
                    new Function<EdgeDto, Map<String, Attribute>>() {

                        @Override
                        public Map<String, Attribute> apply(EdgeDto edgeDto) {
                            Map<String, Attribute> toReturn = new HashMap<>();
                            toReturn.put("id", DefaultAttribute.createAttribute(edgeDto.getId()));
                            toReturn.put("sourceid", DefaultAttribute.createAttribute(edgeDto.getSource()));
                            toReturn.put("targetid", DefaultAttribute.createAttribute(edgeDto.getTarget()));
                            toReturn.put("type", DefaultAttribute.createAttribute(edgeDto.getType()));

                            return toReturn;

                        }

                        @Override
                        public <V> Function<V, Map<String, Attribute>> compose(Function<? super V, ? extends EdgeDto> before) {
                            return Function.super.compose(before);
                        }

                    }

            );


            exporter.registerAttribute("label", GraphMLExporter.AttributeCategory.NODE, AttributeType.STRING);
            exporter.registerAttribute("signature", GraphMLExporter.AttributeCategory.NODE, AttributeType.STRING);
            exporter.registerAttribute("type", GraphMLExporter.AttributeCategory.NODE, AttributeType.STRING);


            exporter.registerAttribute("id", GraphMLExporter.AttributeCategory.EDGE, AttributeType.STRING);
            exporter.registerAttribute("sourceid", GraphMLExporter.AttributeCategory.EDGE, AttributeType.STRING);
            exporter.registerAttribute("targetid", GraphMLExporter.AttributeCategory.EDGE, AttributeType.STRING);
            exporter.registerAttribute("type", GraphMLExporter.AttributeCategory.EDGE, AttributeType.STRING);
exporter.setExportEdgeLabels(true);

            try (OutputStream outputStream = new FileOutputStream("graph.graphml")) {
                exporter.exportGraph(graph, outputStream);

                Path fileName = Path.of("graph.graphml");

                // Now calling Files.readString() method to
                // read the file
                String str = Files.readString(fileName);
            res.setGraphml(str);

                System.out.println(res.getGraphml());

            } catch (IOException e) {
                e.printStackTrace();
                // Handle the file writing error, and send an error response to the client
                System.err.println("Error writing GraphML file: " + e.getMessage());

            }


            res.setGraph(graphData.toString());




            return res;

            // return graphData.toString();

        } catch (HttpClientErrorException ex) {
            System.out.println("Error response status code: " + ex.getRawStatusCode());
            System.out.println("Error response body: " + ex.getResponseBodyAsString());
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return res;

    }

}

