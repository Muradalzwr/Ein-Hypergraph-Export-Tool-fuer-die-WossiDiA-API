let cy; // Declare cy variable

function getgraph() {
  fetch("http://localhost:8080/export-graph", {
    method: "POST",
    body: document.getElementById("url").value,
  })
    .then((response) => response.json())
    .then((data) => {
    const graphdata = JSON.parse(data.graph);
    console.log(graphdata )
      const nodesData = graphdata.nodes;

      const edgesData = graphdata.edges;

      const cyNodes = Object.keys(nodesData).map((nodeId) => ({
      
        data: {
          id: nodeId,
          label: nodeId ,
          sig : nodesData[nodeId].signature ,
          type: Object.values(nodesData[nodeId].type)[0], 
          attributes: nodesData[nodeId].attributes
        },
      }));
      console.log(cyNodes);
      loadgraph(cyNodes);

      const cyEdges = Object.keys(edgesData).map((edgeId) => ({
        data: {
          id: edgeId,
          source: edgesData[edgeId].links[0].node.toString(),
          target: edgesData[edgeId].links[1].node.toString(),
        },
      }));
      if (cy) {
        cy.destroy(); // Destroy existing cytoscape instance if present
      }
      cy = cytoscape({
        container: document.getElementById("cy"),
        elements: {
          nodes: cyNodes,
          edges: cyEdges,
        },
        style: [
          {
            selector: "node", // Apply the style to nodes
            style: {
              label: "data(label)", // Show the label from the data
              "text-valign": "center", // Center-align the label
              "text-halign": "center",
            },
          },
        ],
        layout: {
          name: "cose", // Choose a layout algorithm
        },
      });
    })
    .catch((error) => console.error("Error fetching data:", error));
}




document.getElementById('downloadButton').addEventListener('click', function() {
    // Send a GET request to the backend to download the GraphML
    fetch('/download-graphml')
        .then(response => response.text())
        .then(graphML => {
            // Create a Blob from the GraphML content
            const blob = new Blob([graphML], { type: 'application/xml' });

            // Create an <a> element to trigger the download
            const link = document.createElement('a');
            link.href = URL.createObjectURL(blob);
            link.download = 'graph.graphml';

            // Programmatically trigger the click event to start the download
            link.click();
        });
});

function loadgraph(nodes) {

  let  loaded = document.getElementById("loaded");

   let tables =document.createElement("table");
   let headr = document.createElement("tr");
   let tdid = document.createElement("td");
   tdid.innerHTML = " Node id";
   let tdsig = document.createElement("td");
   tdsig.innerHTML = "signature";
   let tdtype = document.createElement("td");
   tdtype.innerHTML = "type";
   let tdattri = document.createElement("td");
   tdattri.innerHTML = "attributes";
   headr.appendChild(tdid);
   headr.appendChild(tdsig);
   headr.appendChild(tdtype);
   headr.appendChild(tdattri);
   tables.appendChild(headr);

   for(let obj of nodes) {
    let row = document.createElement("tr");
    let rowid = document.createElement("td");
    rowid.innerHTML = obj.data.label;
    let rwosig = document.createElement("td");
    rwosig.innerHTML = obj.data.sig;
    let tdtype2 = document.createElement("td");
    tdtype2.innerHTML = obj.data.type;
    let tdattriee = document.createElement("td");
    tdattriee.innerHTML = JSON.stringify(obj.data.attributes);
    row.appendChild(rowid);
    row.appendChild(rwosig);
    row.appendChild(tdtype2);
    row.appendChild(tdattriee);
    tables.appendChild(row);

   }

   loaded.appendChild(tables);


}
let roles =[];
function addorles() {
  let rolestable = document.getElementById("roles");
  let inrole = document.getElementById("inrole").value;
  let outrole = document.getElementById("outrole").value;
  let tr = document.createElement("tr");
  let tdin = document.createElement("td");
  let tdout = document.createElement("td");
  let tddel = document.createElement("td");
  let btndel = document.createElement("button");
  btndel.innerHTML = "-";
  btndel.onclick = 
  tdin.innerHTML = inrole;
  tdout.innerHTML = outrole;
  tddel.appendChild(btndel);
  tr.appendChild(tdin);
  tr.appendChild(tdout);
  tr.appendChild(tddel);
  rolestable.appendChild(tr);

}

function showroles () {
  let rolestable = document.getElementById("roles");
  rolestable.innerHTML = "";
for(let role in roles){
  let tr = document.createElement("tr");
  let tdin = document.createElement("td");
  let tdout = document.createElement("td");
  let tddel = document.createElement("td");
  let btndel = document.createElement("button");
  btndel.innerHTML = "-";
  btndel.onclick = () => {
    roles.splice(role);
  }
  tdin.innerHTML = inrole;
  tdout.innerHTML = outrole;
  tddel.appendChild(btndel);
  tr.appendChild(tdin);
  tr.appendChild(tdout);
  tr.appendChild(tddel);
  rolestable.appendChild(tr);
}
}