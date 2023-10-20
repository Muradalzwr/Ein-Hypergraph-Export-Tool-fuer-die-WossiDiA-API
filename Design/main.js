let rolesarr = [];
let addbtn = document.getElementById("addroles");
let nodecon = document.getElementById("nodecon");
let edgerole = document.getElementById("edgerole");
let roles = document.getElementById("roles");
let getgraphbtn = document.getElementById("getgraph");
let vrow = -1;
let graphML ;
addbtn.addEventListener("click", addcondrions);
getgraphbtn.addEventListener("click", getgraph);
function addcondrions() {
  let len = rolesarr.length;
  let role = {
    index: len,
    nod: nodecon.value,
    edg: edgerole.value,
  };
  rolesarr.push(role);
  console.log(rolesarr);
  showrole();
}

function showrole() {
  roles.innerHTML = "";

  for (let i = 0; i < rolesarr.length; i++) {
    let tr = document.createElement("tr");
    let rmsbtn = document.createElement("button");
    rmsbtn.innerHTML = "-";
    rmsbtn.onclick = () => {
      rolesarr.splice(i, 1);

      showrole();
    };
    let adsbtn = document.createElement("button");
    adsbtn.innerHTML = "+";
    adsbtn.onclick = () => {
      vrow = i;
      showrole();
    };
    let rmtd = document.createElement("td");
    rmtd.appendChild(rmsbtn);

    let nodetd = document.createElement("td");
    nodetd.innerHTML = rolesarr[i].nod;
    let edgetd = document.createElement("td");
    edgetd.innerHTML = rolesarr[i].edg;

    let adtd = document.createElement("td");
    adtd.appendChild(adsbtn);

    tr.appendChild(rmtd);
    tr.appendChild(nodetd);
    tr.appendChild(edgetd);
    tr.appendChild(adtd);
    roles.appendChild(tr);
    if (vrow === i) {
      addrolesrow();
    }
  }
}

function addrolesrow() {
  let len = rolesarr.length;
  let tr = document.createElement("tr");
  let nodein = document.createElement("input");
  let edgein = document.createElement("input");
  let rmsbtn = document.createElement("button");
  rmsbtn.innerHTML = "Cancel";
  rmsbtn.onclick = () => {
    vrow = -1;
    showrole();
  };
  let adsbtn = document.createElement("button");
  adsbtn.innerHTML = "Add";
  adsbtn.onclick = () => {
    let role = {
      index: len,
      nod: nodein.value,
      edg: edgein.value,
    };

    rolesarr.splice(vrow + 1, 0, role);
    vrow = -1;
    showrole();
  };
  let rmtd = document.createElement("td");
  rmtd.appendChild(rmsbtn);
  let nodetd = document.createElement("td");
  nodetd.appendChild(nodein);
  let edgetd = document.createElement("td");
  edgetd.appendChild(edgein);

  let adtd = document.createElement("td");
  adtd.appendChild(adsbtn);
  tr.appendChild(rmtd);
  tr.appendChild(nodetd);
  tr.appendChild(edgetd);
  tr.appendChild(adtd);
  roles.appendChild(tr);
}

let cy; // Declare cy variable

function getgraph() {
  let startnode = document.getElementById("startnode").value;
  let graphurl = "/graph/" + startnode + "/";
  for (let rol of rolesarr) {
    if (rol.edg) {
      graphurl += "<0:" + rol.edg + ":1>" + rol.nod;
    } else {
      graphurl += "<edge>" + rol.nod;
    }
  }


  fetch("http://localhost:8080/export-graph", {
    method: "POST",
    body: graphurl,
  })
    .then((response) => response.json())
    .then((data) => {

      console.log(data);
      graphML =data.graphml;
      console.log(graphML);
      const graphdata = JSON.parse(data.graph);
     
      const nodesData = graphdata.nodes;

      const edgesData = graphdata.edges;

      const cyNodes = Object.keys(nodesData).map((nodeId) => ({
        data: {
          id: nodeId,
          label: nodeId,
          sig: nodesData[nodeId].signature,
          type: Object.values(nodesData[nodeId].type)[0],
          attributes: nodesData[nodeId].attributes,
        },
      }));

      loadgraph(cyNodes);

      const cyEdges = Object.keys(edgesData).map((edgeId) => ({
        data: {
          id: edgeId,
          source: edgesData[edgeId].links[0].node.toString(),

          target: edgesData[edgeId].links[1].node.toString(),
          type: Object.values(edgesData[edgeId].type)[0],
        },
      }));

      loadgraphedges(cyEdges);
      if (cy) {
        cy.destroy(); // Destroy existing cytoscape instance if present
      }
      cy = cytoscape({
        container: document.getElementById("cx"),
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

function loadgraph(nodes) {
  let loaded = document.getElementById("Nodesinfo");

  loaded.innerHTML = "";

  for (let obj of nodes) {
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
    loaded.appendChild(row);
  }
}

function loadgraphedges(edges) {
  let loadededge = document.getElementById("Edgesinfo");

  loadededge.innerHTML = "";

  for (let obj of edges) {
    let row = document.createElement("tr");
    let rowid = document.createElement("td");
    rowid.innerHTML = obj.data.id;
    let rwosig = document.createElement("td");
    rwosig.innerHTML = obj.data.type;
    let tdtype2 = document.createElement("td");
    tdtype2.innerHTML = obj.data.source + "-" + obj.data.target;

    row.appendChild(rowid);
    row.appendChild(rwosig);
    row.appendChild(tdtype2);

    loadededge.appendChild(row);
  }
}



let downloadbtn = document.getElementById("dowmloaded");

downloadbtn.addEventListener("click",downloadgrapgml );


function downloadgrapgml() {

  const blob = new Blob([graphML], { type: 'application/xml' });

  // Create an <a> element to trigger the download
  const link = document.createElement('a');
  link.href = URL.createObjectURL(blob);
  link.download = 'graph.graphml';

  // Programmatically trigger the click event to start the download
  link.click();

}