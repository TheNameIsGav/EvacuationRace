document.addEventListener("DOMContentLoaded", initializer());

var board;      //This will be the main board.

var Board = function (blueprint) {      //This is the board object.
    this.rows = blueprint.cols;
    this.cols = blueprint.rows;         //ok I've chased the rows/cols flip this far.
    this.typeMap = blueprint.map;       //someone else can finish it.
    this.hexes = [];
}

function initializer() {
    board = new Board(getMapBlueprint());       //starts the series of events that sets
    board.drawMap();                             //draws map.
}

function getMapBlueprint() {    //gets blueprint and sets up planetary system map
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:4567/map/getBoard", true);    //asks for the map
    xhr.onload = function (e) {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                console.log("gatMapBlueprint called and executed.")
                return JSON.parse(xhr.responseText);     //returns a new board parsed from the blueprints
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

Board.prototype.addHex = function (hex) {
    this.hexes[this.hexes.length] = hex;
}

Board.prototype.getHexByPosition = function (row, col) {
    for (var i = 0; i < this.hexes.length; i++) {
        if (this.hexes[i].row === row && this.hexes[i].col === col) {
            return this.hexes[i];
        }
    }
    return null;
}

Board.prototype.getHexById = function (id) {
    for (var i = 0; i < this.hexes.length; i++) {
        if (this.hexes[i].id === id) {
            return this.hexes[i];
        }
    }
    return null;
}

var Hex = function (row, col, type, id, backgroundHex) {
    this.row = row;
    this.col = col;
    this.type = type;
    this.building = "";
    this.units = [];
    this.backgroundHexId = backgroundHex;
    this.id = id;
}

Board.prototype.drawMap = function () {
    var map = document.getElementById("mapSVG");

    console.log("Cols : " + this.rows + " ; Rows : " + this.cols + " (These values are flipped damn it)");

    var w = (15 * this.rows) + 15; //original = 223
    var h = (17.4 * this.cols) + 18.7; //original = 191

    console.log("view box set to (0, 0, " + w + ", " + h + ")");
    map.setAttribute("viewBox","0 0 " + w + " " + h);

    var idCounter = 0;
    for (var rows = 0; rows < this.rows; rows++) {
        for (var cols = 0; cols < this.cols; cols++) {
            var hexElement = drawHex();
            var type = "";
            var backgroundHex = "";
            hexElement.id = idCounter + "H";
            positionStandard(rows, cols, hexElement);
            hexElement.style.stroke = "white";
            hexElement.style.strokeWidth = ".75";
            hexElement.addEventListener("click", unitTest);
            var backgroundHex;
            switch (board.typeMap[rows][cols]) {
                case 0: {
                    hexElement.style.fill = "#6A6D78";
                    type = "metalHex";
                    break;
                }
                case 1: {
                    hexElement.style.fill = "#73896E";
                    type = "organicsHex";
                    break;
                }
                case 2: {
                    hexElement.style.fill = "#DDDDDD";
                    type = "iceHex";
                    break;
                }
                case 3: {
                    hexElement.style.fill = "#435985";
                    type = "oceanHex";
                    break;
                }
                case 4: {
                    hexElement.style.fill = "url(#doubleMetal)";
                    type = "doubleMetal";
                    backgroundHex = hexElement.cloneNode(true);
                    backgroundHex.style.fill = "#6A6D78";
                    backgroundHex.id = idCounter;
                    map.appendChild(backgroundHex);
                    break;
                }
                case 5: {
                    hexElement.style.fill = "url(#iceMetal)";
                    backgroundHex = hexElement.cloneNode(true);
                    type = "iceMetal";
                    backgroundHex.style.fill = "#6A6D78";
                    backgroundHex.id = idCounter;
                    map.appendChild(backgroundHex);
                    break;
                }
                case 6: {
                    hexElement.style.fill = "url(#doubleIce)";
                    type = "doubleIce";
                    backgroundHex = hexElement.cloneNode(true);
                    backgroundHex.style.fill = "#DDDDDD";
                    backgroundHex.id = idCounter;
                    map.appendChild(backgroundHex);
                    break;
                }
                case 7: {
                    hexElement.style.fill = "#daa520";
                    type = "rareHex";
                    break;
                }
                case 8: {
                    hexElement.style.fill = "url(#doubleIce)";
                    type = "metalRare";
                    backgroundHex = hexElement.cloneNode(true);
                    backgroundHex.style.fill = "#DDDDDD";
                    backgroundHex.id = idCounter;
                    map.appendChild(backgroundHex);
                    break;
                }
                case 9: {
                    hexElement.style.fill = "url(#doubleIce)";
                    type = "IceRare";
                    backgroundHex = hexElement.cloneNode(true);
                    backgroundHex.style.fill = "#DDDDDD";
                    backgroundHex.id = idCounter;
                    map.appendChild(backgroundHex);
                    break;
                }
                case 10: {
                    hexElement.style.fill = "#3d8f3d";
                    type = "weakOrganicsHex";
                    break;
                }
                case 11: {
                    hexElement.style.fill = "#338099";
                    type = "extremiumHex";
                    break;
                }
                case 12: {
                    hexElement.style.fill = "#00997a";
                    type = "uraniumHex";
                    break;
                }
                case 13: {
                    hexElement.style.fill = "#ffdf80";
                    type = "hydrogenHex1";
                    break;
                }
                case 14: {
                    hexElement.style.fill = "#ffd24d";
                    type = "hydrogenHex2";
                    break;
                }
                case 15: {
                    hexElement.style.fill = "#ff8000";
                    type = "deuteriumHex";
                    break;
                }
                default: {
                    console.log("unrecognised value at (" + rows + "," + cols +") - " + board.typeMap[rows][cols]);
                }
            }
            var newHex = new Hex(rows, cols, type, idCounter + "H", (backgroundHex.id || -1) + "B");
            this.addHex(newHex);
            idCounter++;
            map.appendChild(hexElement);
        }
    }
}

function drawHex() {
    var hex = document.createElementNS("http://www.w3.org/2000/svg", "polygon");
    hex.setAttribute("fill", "#000000");
    hex.setAttribute("points", "15 0 20 8.7 15 17.4 5 17.4 0 8.7 5 0");
    return hex;
}