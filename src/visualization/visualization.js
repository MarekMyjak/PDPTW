function parseText(text) {
    var json = JSON.parse(text);
    console.log(json);

    var scale = 12 ;
    var dataSet = [];
    dataSet.push({id: 0, label: "start", x: json[0].location.x*scale, y: json[0].location.y*scale});
    var edgeSet = [];
    json.forEach(function (vechicle) {
        var color = 'rgb(' + (Math.floor((256-99)*Math.random()) + 100) + ',' +
            (Math.floor((256-99)*Math.random()) + 100) + ',' +
            (Math.floor((256-99)*Math.random()) + 100) + ')';
        console.log(color);
        var from = 0;
        vechicle.routes.forEach(function (route) {
//            dataSet.push({id: route.id, label: "x:" + route.location.x + ", y:" + route.location.y, color: color,
        	 dataSet.push({id: route.id, label: "" + route.id , color: color,
                x: route.location.x*scale, y: route.location.y*scale, title: "arrivalTime:" + route.arrivalTime + ", type:" + route.type});
            edgeSet.push({from: from, to: route.id, color: color, arrows:'to'});
            from = route.id;
        })
    });

    var nodes = new vis.DataSet(dataSet);
    var edges = new vis.DataSet(edgeSet);

    $(document).ready(function () {
        var container = document.getElementById('mynetwork');
        var data = {
            nodes: nodes,
            edges: edges
        };
        options = {
            physics: {
                stabilization: {
                    onlyDynamicEdges: true
                }
            }
        };
        var network = new vis.Network(container, data, options);
        window.onload = function() {
            network.on("showPopup", function (params) {
                document.getElementById('eventSpan').innerHTML = '<h2>showPopup event: </h2>' + JSON.stringify(params, null, 4);
            });
        };

    });
}

var openFile = function (event) {
    var input = event.target;

    var reader = new FileReader();
    var text;
    reader.onload = function () {
        text = reader.result;
        parseText(text);
    };
    reader.readAsText(input.files[0]);
};


// $(document).ready(function() {
// //Example data
// var nodes = new vis.DataSet([
//     {id: 1, label: 'Node 1'},
//     {id: 2, label: 'Node 2'},
//     {id: 3, label: 'Node 3'},
//     {id: 4, label: 'Node 4'},
//     {id: 5, label: 'Node 5'}
// ]);
//
// // create an array with edges
// var edges = new vis.DataSet([
//     {from: 1, to: 3},
//     {from: 1, to: 2},
//     {from: 2, to: 4},
//     {from: 2, to: 5}
// ]);
//
// // create a network
// var container = document.getElementById('mynetwork');
//
// // provide the data in the vis format
// var data = {
//     nodes: nodes,
//     edges: edges
// };
// var options = {};
//
// // initialize your network!
// var network = new vis.Network(container, data, options);
//
//
// });