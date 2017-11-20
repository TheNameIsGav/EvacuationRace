document.addEventListener("DOMContentLoaded", initializer());

var board;      //This will be the main board.

var Board = function (blueprint) {      //This is the board object.
    this.rows = blueprint.cols;
    this.cols = blueprint.rows;         //ok I've chased the rows/cols flip this far.
    this.typeMap = blueprint.map;       //someone else can finish it.
    this.hexes = [];
}

function getSystemBlueprint() {    //gets blueprint and sets up planetary system map
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:4567/map/getBoard", true);    //asks for the map
    xhr.onload = function (e) {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                console.log("gatMapBlueprint called and executed.")
                return JSON.parse(xhr.responseText);     //returns a new parsed board blueprint
            } else {
                console.error(xhr.status);
            }
        }
    };
    xhr.onerror = function (e) {
        console.error(xhr.statusText);
    };
    xhr.send(null);
}



/////////////////////////////////////////////////////////////////////////

function initializer() {
    board = new Board(getSystemBlueprint());       //starts the series of events that sets
    board.drawMap();                             //draws map.
}