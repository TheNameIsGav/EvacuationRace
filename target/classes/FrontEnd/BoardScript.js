document.addEventListener("DOMContentLoaded", getMapBlueprint);

document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("nextPhase").addEventListener("click", loadMovementPhase);
});

var phase = "building";
function loadMovementPhase() {
    document.getElementById("phase").innerHTML = "Movement";
    document.getElementById("nextPhase").removeEventListener("click", loadMovementPhase);
    document.getElementById("nextPhase").addEventListener("click", loadCombatPhase);
    phase = "movement";
    for (var i = 0; i < board.hexes.length; i++) {
        for (var j = 0; j < board.hexes[i].units.length; j++) {
            document.getElementById(board.hexes[i].units[j].id).style.stroke = "black";
        }
        if (board.hexes[i].building.componentElements) {
            for (var j = 0; j < board.hexes[i].building.componentElements.length; j++) {
                document.getElementById(board.hexes[i].building.componentElements[j]).style.stroke = "black";
            }
        }
    }
}

function loadCombatPhase() {
    document.getElementById("phase").innerHTML = "Combat";
    document.getElementById("nextPhase").removeEventListener("click", loadCombatPhase);
    document.getElementById("nextPhase").addEventListener("click", loadTurnComplete);
    phase = "combat";
}

function loadTurnComplete() {
    document.getElementById("nextPhase").removeEventListener("click", loadTurnComplete);
    document.getElementById("nextPhase").addEventListener("click", loadBuildingPhase);
    document.getElementById("phase").innerHTML = "Turn Complete";
    document.getElementById("phase").title = "It's not your turn right now, come back later!";
    phase = "turnComplete";
}

function loadBuildingPhase() {
    document.getElementById("phase").title = "Current phase";
    document.getElementById("phase").innerHTML = "Building";
    document.getElementById("nextPhase").removeEventListener("click", loadBuildingPhase);
    document.getElementById("nextPhase").addEventListener("click", loadMovementPhase);
    phase = "building";
}

var board;
function getMapBlueprint() {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:4567/map/getBoard", true);
    xhr.onload = function (e) {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                board = new Board(JSON.parse(xhr.responseText));
                board.drawMap();
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

var Board = function (blueprint) {
    this.rows = blueprint.rows;
    this.cols = blueprint.cols;
    this.typeMap = blueprint.map;
    this.hexes = [];
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
    hex.setAttribute("fill", "#ff0000");
    hex.setAttribute("points", "15 0 20 8.7 15 17.4 5 17.4 0 8.7 5 0");
    return hex;
}

function hoverEnter() {
    var target = event.target;
    target.addEventListener("click", focus);
    target.addEventListener("mouseleave", hoverLeave);
    target.removeEventListener("mouseenter", hoverEnter);
    var transform = target.getAttribute("transform") || "rotate(0)";
    var stroke = target.style.stroke;
    target.setAttribute("transform", "rotate(0)");
    target.style.stroke = "grey";
    function hoverLeave() {
        target.addEventListener("mouseenter", hoverEnter);
        target.removeEventListener("mouseleave", hoverLeave);
        target.removeEventListener("click", focus);
        target.setAttribute("transform", transform);
        target.style.stroke = stroke;
    }
    function focus() {
        if (phase !== "movement" || event.ctrlKey) {
            return;
        }
        var target = event.target;
        console.log(target);
        document.addEventListener("mousemove", drag);
        target.removeEventListener("mouseleave", hoverLeave);
        target.removeEventListener("click", focus);
        target.setAttribute("transform", "rotate(0)");
        target.style.pointerEvents = "none";
        var currentX = event.pageX;
        var currentY = event.pageY;
        var hexes = document.getElementById("mapSVG").childNodes;
        for (var i = 0; i < board.hexes.length; i++) {
            document.getElementById(board.hexes[i].id).addEventListener("click", appendUnit);
            document.getElementById(board.hexes[i].id).removeEventListener("click", unitTest);
            for (var j = 0; j < board.hexes[i].units.length; j++) {
                document.getElementById(board.hexes[i].units[j].id).removeEventListener("mouseenter", hoverEnter);
            }
        }
        var bool = true;
        function drag() {
            if (bool) {
                document.addEventListener("click", deFocus);
                document.addEventListener("keydown", deFocusKey);
                var shadow = target.cloneNode(true);
                shadow.id = "shadow";
                shadow.style.opacity = ".35";
                document.getElementById("mapSVG").appendChild(shadow);
                bool = false;
            }
            var width = document.getElementById("page").offsetWidth;
            var c = 1 / (width * .92 / 223);
            var x = (event.pageX - currentX) * c;
            var y = (event.pageY - currentY) * c;
            translate(x, y, target);
            currentX = event.pageX;
            currentY = event.pageY;
            if (currentX > 223 / c || currentY < document.getElementById("page").offsetHeight * .06) {
                deFocus(event);
            }
        };
        function deFocusKey() {
            if (event.key === "Escape") {
                deFocus(event);
            }
        }
        function deFocus() {
            target.addEventListener("mouseenter", hoverEnter);
            document.removeEventListener("click", deFocus);
            document.removeEventListener("keydown", deFocusKey);
            document.removeEventListener("mousemove", drag);
            var op = Number.parseInt(document.getElementById("shadow").style.opacity);
            document.getElementById("mapSVG").removeChild(document.getElementById("shadow"));
            target.style.pointerEvents = "auto";
            target.style.stroke = stroke;
            for (var i = 0; i < board.hexes.length; i++) {
                document.getElementById(board.hexes[i].id).addEventListener("click", unitTest);
                document.getElementById(board.hexes[i].id).removeEventListener("click", appendUnit);
                for (var j = 0; j < board.hexes[i].units.length; j++) {
                    document.getElementById(board.hexes[i].units[j].id).addEventListener("mouseenter", hoverEnter);
                }
            }
            if (!hexClicked) {
                var id = target.id;
                var unit;
                for (var i = 0; i < board.hexes.length; i++) {
                    for (var j = 0; j < board.hexes[i].units.length; j++) {
                        if (board.hexes[i].units[j].id === id) {
                            unit = board.hexes[i].units[j];
                        }
                    }
                }
                positionUnits(board.getHexByPosition(unit.row, unit.col));
            }
        }
        var hexClicked = false;
        function appendUnit() {
            event.preventDefault();
            hexClicked = true;
            var id = target.id;
            var unit;
            for (var i = 0; i < board.hexes.length; i++) {
                for (var j = 0; j < board.hexes[i].units.length; j++) {
                    if (board.hexes[i].units[j].id === id) {
                        unit = board.hexes[i].units[j];
                    }
                }
            }
            var oldHex = board.getHexByPosition(unit.row, unit.col);
            var newHex = board.getHexById(event.target.id);
            if (newHex.id === oldHex.id) {
                positionUnits(oldHex);
                return;
            }
            unit.row = newHex.row;
            unit.col = newHex.col;
            oldHex.units.splice(Number.parseInt(unit.unitNumber), 1);
            newHex.units.push(unit);
            positionUnits(newHex);
            positionUnits(oldHex);
        }
    }
}

function hasClass(classes, target) {
    classes = classes.split(" ");
    for (var i = 0; i < classes.length; i++) {
        if (classes[i] === target) {
            return true;
        }
    }
    return false;
}

function positionStandard(rows, cols, img) {
    var points = img.points;
    for (var i = 0; i < points.length; i++) {
        if (rows % 2 === 1) {
            points[i].y += + 17.4 * cols + 17.4 * .5 + 4;
        } else {
            points[i].y += + 17.4 * cols + 4;
        }
        points[i].x += + 20 * rows * .75 + 4;
    }
    return img;
}

function translate(xTran, yTran, img) {
    var points = img.points;
    for (var i = 0; i < points.length; i++) {
        points[i].x += xTran;
        points[i].y += yTran;
    }
    return img;
}

function fireClick(node) {
    if (document.createEvent) {
        var evt = document.createEvent('MouseEvents');
        evt.initEvent('mouseup', true, false);
        node.dispatchEvent(evt);
    }
}

function unitTest() {
    var hex = board.getHexById(event.target.id);
    var unit;
    if (!hex.building && event.ctrlKey) {
        addBuilding(hex, "settlement");
    }
}

var Unit = function (row, col, type, id, unitNumber) {
    this.row = row;
    this.col = col;
    this.type = type;
    this.id = id;
    this.unitNumber = unitNumber;
}

Board.prototype.getUnitCount = function () {
    var count = 0;
    for (var i = 0; i < this.hexes.length; i++) {
        count += this.hexes[i].units.length;
    }
    return count;
}

function addUnit(hex, unitType) {
    if (hex.units.length >= 34) {
        alert("Unit capacity for this hex has been reached!");
        return;
    }
    var unitElement = drawUnit(unitType);
    unitElement.addEventListener("mouseenter", hoverEnter);
    document.getElementById("mapSVG").appendChild(unitElement);
    unitElement.id = board.getUnitCount() + "U";
    hex = board.getHexById(hex.id);
    var unit = new Unit(hex.row, hex.col, unitType, board.getUnitCount() + "U", hex.units.length);
    hex.units[hex.units.length] = unit;
    unitElement.style.pointerEvents = "fill";
    positionUnits(hex);
    unitElement.addEventListener("click", createMenu);
    function createMenu() {
        hex = board.getHexByPosition(unit.row, unit.col);
        if (!((phase === "building") || (phase === "movement" && event.ctrlKey))) {
            return;
        }
        unitElement.removeEventListener("click", createMenu);
        var el = document.createElement("div");
        el.id = "menu";
        el.style.width = "100";
        el.style.height = "auto";
        el.style.zIndex = "1";
        el.style.position = "absolute";
        el.style.marginLeft = event.pageX - 2;
        el.style.marginTop = event.pageY - document.getElementById("page").offsetHeight * .06 - 2;
        document.getElementById("map").insertBefore(el, document.getElementById("mapSVG"));
        var ul = document.createElement("ul");
        function createButton(type) {
            var bu = document.createElement("button");
            bu.id = "menuButton";
            bu.style.width = "125";
            bu.style.opacity = ".75";
            bu.style.background = "#f9f9f9"
            bu.style.font = "Arial";
            bu.style.boxShadow = "0px 8px 16px 0px rgba(0,0,0,0.2)";
            bu.style.fontSize = "1em";
            bu.style.textAlign = "center";
            bu.style.height = "20";
            bu.parentId = hex.id;
            bu.buildingType = type;
            var innerHTML;
            switch (type) {
                case "settlement": innerHTML = "Settlement"; break;
                case "largeMine": innerHTML = "Large Mine"; break;
                case "smallMine": innerHTML = "Small Mine"; break;
            }
            bu.innerHTML = innerHTML;
            bu.style.padding = "17px 21px";
            bu.addEventListener("mouseenter", function () {
                bu.style.background = "#f1f1f1";
            });
            bu.addEventListener("mouseleave", function () {
                bu.style.background = "#f9f9f9";
            });
            bu.addEventListener("click", createBuilding)
            return bu;
        }
        if (!hex.building) {
            ul.appendChild(createButton("settlement"));
            ul.appendChild(createButton("largeMine"));
            ul.appendChild(createButton("smallMine"));
            el.appendChild(ul);
        } else {
            var div = document.createElement("div");
            div.id = "menuButton";
            div.innerHTML = "Hex is full";
            div.style.width = "125";
            div.style.opacity = ".75";
            div.style.background = "#f9f9f9"
            div.style.font = "Arial";
            div.style.boxShadow = "0px 8px 16px 0px rgba(0,0,0,0.2)";
            div.style.fontSize = "1em";
            div.style.textAlign = "center";
            div.style.height = "20";
            ul.appendChild(div);
            el.appendChild(ul);
        }
        ul.addEventListener("mouseleave", function () {
            if (document.elementFromPoint(event.clientX, event.clientY).id !== unitElement.id) {
                removeMenu(event);
                unitElement.removeEventListener("mouseleave", leaveUnit)
            } else {
                unitElement.addEventListener("mouseleave", leaveUnit);
            }
        });
        function leaveUnit() {
            if (document.elementFromPoint(event.clientX, event.clientY).id !== "menuButton") {
                removeMenu(event);
                unitElement.removeEventListener("mouseleave", leaveUnit)
            }
        }
        document.addEventListener("click", removeMenu);
        created = false;
    }
    function removeMenu() {
        if (((created && event.target.id !== "menu" && event.target.id !== "menuButton") || event.type === "mouseleave") && document.getElementById("menu")) {
            unitElement.addEventListener("click", createMenu);
            document.getElementById("map").removeChild(document.getElementById("menu"));
            document.removeEventListener("click", removeMenu);
            document.removeEventListener("mouseleave", removeMenu);
        }
        created = true;
    }

    function createBuilding() {
        var building = addBuilding(board.getHexByPosition(unit.row, unit.col), this.buildingType);
        for (var i = 0; i < building.componentElements.length; i++) {
            document.getElementById(building.componentElements[i]).style.stroke = "white";
        }
    }
    return unit;
}

function drawUnit(unitType) {
    var unit = document.createElementNS("http://www.w3.org/2000/svg", "polygon");
    unit.setAttribute("stroke-width", ".25");
    unit.setAttribute("stroke", "black");
    unit.setAttribute("fill", "transparent");
    unit.setAttribute("stroke-linejoin", "round");
    if (unitType === "infantry") {
        unit.setAttribute("points", "0 0 0 2 2 2 2 0");
    } else if (unitType === "crawler") {
        unit.setAttribute("points", "0 0 0 2 4 2 4 0");
    } else if (unitType === "shuttle") {
        unit.setAttribute("points", "0 0 3 0 1.5 -3");
    }
    switch (Math.floor(Math.random() * 6) + 1) {
        case 6: unit.style.fill = "red"; break;
        case 5: unit.style.fill = "blue"; break;
        case 4: unit.style.fill = "green"; break;
        case 3: unit.style.fill = "yellow"; break;
        case 2: unit.style.fill = "purple"; break;
        case 1: unit.style.fill = "pink"; break;
    }
    return unit;
}

function redrawSVG(svg) {
    console.log(svg);
    var newSVG = svg.cloneNode(true);
    var parent = svg.parentNode;
    parent.removeChild(svg);
    parent.appendChild(newSVG);
    newSVG.addEventListener("mouseenter", hoverEnter);
    console.log(svg)
    return newSVG;
}

function switchxy(unit) {
    var points = document.getElementById(unit.id).points;
    for (var i = 0; i < points.length; i++) {
        var x = points[i].x;
        points[i].x = points[i].y;
        points[i].y = x
    }
}

function positionUnits(hex) {
    var units = hex.units;
    for (var i = 0; i < units.length; i++) {
        units[i].unitNumber = i;
    }
    resetUnitPositions(units);
    var circle;
    if (hex.units.length > 6) {
        var overflowUnits = [];
        for (var i = 6; i < units.length; i++) {
            overflowUnits[i - 6] = units[i];
        }
        circle = drawCircle(hex.row, hex.col);
        document.getElementById("mapSVG").appendChild(circle);
        circle.addEventListener("mouseenter", expandCircle);
        for (var i = 0; i < overflowUnits.length; i++) {
            redrawSVG(document.getElementById(overflowUnits[i].id));
        }
        positionRadially(overflowUnits, circle);
        var remainingUnits = 6;
    }
    switch (remainingUnits || hex.units.length) {
        case 6: {
            positionUnit(units[5], 3);
        }
        case 5: {
            positionUnit(units[4], 0)
        }
        case 4: {
            positionUnit(units[0], 5);
            positionUnit(units[1], 4);
            positionUnit(units[2], 2);
            positionUnit(units[3], 1)
            break;
        }
        case 3: {
            positionUnit(units[0], 0);
            positionUnit(units[1], 4);
            positionUnit(units[2], 2);
            break;
        }
        case 2: {
            if (hex.building) {
                positionUnit(units[0], 0);
                positionUnit(units[1], 3);
            } else {
                positionUnit(units[0], .5);
                positionUnit(units[1], 3.5);
            }
            break;
        }
        case 1: {
            if (hex.building) {
                positionUnit(units[0], 0);
            } else {
                positionUnit(units[0], 0);
            }
            break;
        }
    }
    function drawCircle(row, col) {
        var circle = document.createElementNS("http://www.w3.org/2000/svg", "circle");
        circle.setAttribute("fill", "#f1f1f1");
        circle.setAttribute("cx", positionCircleX(row) - 8.5);
        circle.setAttribute("cy", positionCircleY(row, col) - 8);
        var radius = 2;
        circle.setAttribute("r", radius);
        circle.id = "circleMenu";
        return circle;
    }
    var finalRadius;
    if (circle) {
        if (circleLevels === 3) {
            finalRadius = circleLevels * 10;
        }
        else {
            finalRadius = circleLevels * 5;
        }
    } else {
        finalRadius = 2;
    }
    var radiusIncrement;
    var expandInterval;
    var isOpen = false;
    document.addEventListener("mousemove", checkToClose)
    function expandCircle() {
        isOpen = true;
        radiusIncrement = Math.pow(circleLevels, .25);
        var delay = 10;
        expandInterval = window.setInterval(circleExpander, delay);
        function circleExpander() {
            circle.setAttribute("r", (Number.parseFloat(circle.getAttribute("r")) + radiusIncrement));
            if (Math.floor(Number.parseFloat(circle.getAttribute("r")) + radiusIncrement) > finalRadius) {
                circle.setAttribute("r", finalRadius);
                clearInterval(expandInterval);
            }
            positionRadially(overflowUnits, circle);
        }
        circle.removeEventListener("mouseenter", expandCircle);
        circle.addEventListener("mouseleave", possibleCollapseCircle);
    }

    function collapseCircle() {
        isOpen = false;
        var delay = 10;
        var interval = window.setInterval(circleExpander, delay);
        function circleExpander() {
            var finalRadius = 2;
            circle.setAttribute("r", (Number.parseFloat(circle.getAttribute("r")) - radiusIncrement));
            if (Math.floor(Number.parseFloat(circle.getAttribute("r")) < finalRadius)) {
                circle.setAttribute("r", finalRadius);
                clearInterval(interval);
            }
            for (var i = 0; i < overflowUnits.length; i++) {
                var element = document.getElementById(overflowUnits[i].id);
                var oldRotation = Number.parseFloat(element.getAttribute("transform").substring(7) || "0");
            }
            positionRadially(overflowUnits, circle);
        }
        circle.addEventListener("mouseenter", expandCircle);
    }

    function checkToClose() {
        if (isOpen && !isOverflowUnit(document.elementFromPoint(event.clientX, event.clientY)) && document.elementFromPoint(event.clientX, event.clientY).id !== "circleMenu" && document.elementFromPoint(event.clientX, event.clientY).id !== "menuButton") {
            clearInterval(expandInterval);
            collapseCircle(event);
        }
    }
    function possibleCollapseCircle() {
        if (!isOverflowUnit(document.elementFromPoint(event.clientX, event.clientY)) && document.elementFromPoint(event.clientX, event.clientY).id !== "circleMenu" && document.elementFromPoint(event.clientX, event.clientY).id !== "menuButton") {
            clearInterval(expandInterval)
            collapseCircle(event);
        }
    }

    function isOverflowUnit(element) {
        for (var i = 0; i < overflowUnits.length; i++) {
            if (element.id === overflowUnits[i].id) {
                return true;
            }
        }
        return false;
    }
}

var circleLevels = 1;
var radius = 3;
function positionRadially(units, referenceElement) {
    var levels = 1;
    var total = 0;
    function getLevelNumber(start, number) {
        total += start;
        if (total >= number) {
            return;
        }
        levels++;
        return start + getLevelNumber(start * 2, number);
    }
    getLevelNumber(4, units.length);
    var index = 0;
    resetUnitSize(units);
    var scale = Number.parseFloat(referenceElement.getAttribute("r")) * levels / 2;
    if (levels === 1) {
        scale = 1;
    }
    if (scale / 6 < 1) {
        changeUnitSize(units, scale / 6);
    }
    position();
    function position() {
        for (var levelNum = 1; levelNum <= levels; levelNum++) {
            if (index >= units.length) {
                break;
            }
            for (var space = 0; space < Math.pow(2, levelNum + 1); space++) {
                var cx = Number.parseFloat(document.getElementById("circleMenu").getAttribute("cx"));
                var cy = Number.parseFloat(document.getElementById("circleMenu").getAttribute("cy"));
                var unitNumber;
                if (levelNum === levels) {
                    unitNumber = units.length - getUnitsByLevel(levelNum - 1);
                } else {
                    unitNumber = Math.pow(2, levelNum + 1);
                }

                var theta = (270 + 360 / unitNumber * space) % 360;
                radius = 2 * (levelNum * scale / 6 + 2 / scale);
                if (levels === 3) {
                    radius /= 4;
                }
                if (levelNum === 1) {
                    radius /= 2;
                }
                var thetaRad = theta / 180 * Math.PI;
                if (index >= units.length) {
                    break;
                }
                positionByCenter(units[index], cx + radius * Math.cos(thetaRad), cy + radius * Math.sin(thetaRad));
                document.getElementById(units[index].id).setAttribute("transform", "rotate(" + theta + " " + (cx + radius * Math.cos(thetaRad)) + " " + (cy + radius * Math.sin(thetaRad)) + ")");
                index++;
            }
        }
    }
    circleLevels = levels;
}

function getUnitsByLevel(level) {
    var doubler = 4;
    var unitCount = 0;
    for (var i = 0; i < level; i++) {
        unitCount += doubler;
        doubler *= 2;
    }
    return unitCount;
}
function getCenterX(unit) {
    var points = document.getElementById(unit.id).points;
    switch (unit.type) {
        case "infantry": {
            return (points[0].x + points[3].x) / 2;
        }
    }
}

function getCenterY(unit) {
    var points = document.getElementById(unit.id).points;
    switch (unit.type) {
        case "infantry": {
            return (points[3].y + points[2].y) / 2;
        }
    }
}

function changeUnitSize(units, scale) {
    for (var i = 0; i < units.length; i++) {
        switch (units[i].type) {
            case "infantry": {
                var points = document.getElementById(units[i].id).points;
                var length = points[3].x - points[0].x;
                var height = points[1].y - points[0].y;
                var lengthChange = scale * length / 2;
                if (scale < 1) {
                    lengthChange *= -1;
                }
                points[0].x -= lengthChange;
                points[1].x -= lengthChange;
                points[3].x += lengthChange;
                points[2].x += lengthChange;
                points[0].y -= lengthChange;
                points[1].y += lengthChange;
                points[3].y -= lengthChange;
                points[2].y += lengthChange;
                break;
            }
        }
    }
}

function resetUnitSize(units) {
    for (var i = 0; i < units.length; i++) {
        switch (units[i].type) {
            case "infantry": document.getElementById(units[i].id).setAttribute("points", "0 0, 0 2, 2 2, 2 0"); break;
            case "crawler": document.getElementById(units[i].id).setAttribute("points", "0 0 0 2 4 2 4 0"); break;
            case "shuttle": document.getElementById(units[i].id).setAttribute("points", "0 0 3 0 1.5 -3"); break;
        }
    }
}

function positionByCenter(unit, xTran, yTran) {
    var points = document.getElementById(unit.id).points;
    var xChange;
    var yChange;
    switch (unit.type) {
        case "infantry": {
            var cx = (points[0].x + points[3].x) / 2;
            var cy = (points[3].y + points[2].y) / 2;
            xChange = xTran - cx;
            yChange = yTran - cy;
            break;
        }
    }
    for (var i = 0; i < points.length; i++) {
        points[i].x += xChange;
        points[i].y += yChange;
    }
}

function resetUnitPositions(units) {
    for (var i = 0; i < units.length; i++) {
        switch (units[i].type) {
            case "infantry": document.getElementById(units[i].id).setAttribute("points", "0 0, 0 2, 2 2, 2 0"); break;
            case "crawler": document.getElementById(units[i].id).setAttribute("points", "0 0 0 2 4 2 4 0"); break;
            case "shuttle": document.getElementById(units[i].id).setAttribute("points", "0 0 3 0 1.5 -3"); break;
        }
        positionStandard(units[i].row, units[i].col, document.getElementById(units[i].id));
        document.getElementById(units[i].id).setAttribute("transform", "rotate(0)");
    }
}

function positionUnit(unit, space) {
    var unitElement = document.getElementById(unit.id);
    switch (space) {
        case 0: {
            switch (unit.type) {
                case "infantry": {
                    translate(9, 1.4, unitElement);
                    break;
                }
                case "crawler": {
                    translate(8, 13.5, unitElement);
                    break;
                }
                case "shuttle": {
                    translate(8.5, 15.5, unitElement);
                    break;
                }
            }
            break;
        }
        case .5: {
            switch (unit.type) {
                case "infantry": {
                    translate(9, 4.4, unitElement);
                    break;
                }
                case "crawler": {
                }
                case "shuttle": {

                    break;
                }
            }
            break;
        }
        case 1: {
            switch (unit.type) {
                case "infantry": {
                    var infantry = unitElement;
                    translate(14.3, 4, infantry);
                    var centerX = infantry.points[3].x - 1;
                    var centerY = infantry.points[1].y - 1;
                    infantry.setAttribute("transform", "rotate(-31 " + centerX + " " + centerY + ")");
                    break;
                }
                case "crawler": {
                    translate(3, 10.5, unitElement);
                    var centerX = unitElement.points[3].x - 2;
                    var centerY = unitElement.points[1].y - 1;
                    unitElement.setAttribute("transform", "rotate(-120 " + centerX + " " + centerY + ")");
                    break;
                }
                case "shuttle": {
                    translate(3.5, 12, unitElement);
                    var centerX = unitElement.points[1].x - 2;
                    var centerY = unitElement.points[1].y - 1;
                    unitElement.setAttribute("transform", "rotate(60 " + centerX + " " + centerY + ")");
                    break;
                }
            }
            break;
        }
        case 2: {
            switch (unit.type) {
                case "infantry": {
                    var infantry = unitElement;
                    translate(14.5, 11, infantry);
                    var centerX = infantry.points[3].x - 1;
                    var centerY = infantry.points[1].y - 1;
                    infantry.setAttribute("transform", "rotate(31 " + centerX + " " + centerY + ")");
                    break;
                }
                case "crawler": {
                    translate(13, 10.5, unitElement);
                    var centerX = unitElement.points[3].x - 2;
                    var centerY = unitElement.points[1].y - 1;
                    unitElement.setAttribute("transform", "rotate(120 " + centerX + " " + centerY + ")");
                    break;
                }
                case "shuttle": {
                    translate(13.2, 12.5, unitElement);
                    var centerX = unitElement.points[1].x - 1.5;
                    var centerY = unitElement.points[2].y + 1.5;
                    unitElement.setAttribute("transform", "rotate(-60 " + centerX + " " + centerY + ")");
                    break;
                }
            }
            break;
        }
        case 3: {
            switch (unit.type) {
                case "infantry": {
                    translate(9, 14.2, unitElement);
                    break;
                }
                case "crawler": {
                    translate(3, 4.7, unitElement);
                    var centerX = unitElement.points[3].x - 2;
                    var centerY = unitElement.points[1].y - 1;
                    unitElement.setAttribute("transform", "rotate(120 " + centerX + " " + centerY + ")");
                    break;
                }
                case "shuttle": {
                    translate(4, 6.5, unitElement);
                    var centerX = unitElement.points[1].x - 2;
                    var centerY = unitElement.points[1].y - 1;
                    unitElement.setAttribute("transform", "rotate(120 " + centerX + " " + centerY + ")");
                    break;
                }
            }
            break;
        }
        case 3.5: {
            switch (unit.type) {
                case "infantry": {
                    translate(9, 10.2, unitElement);
                    break;
                }
                case "crawler": {

                    break;
                }
                case "shuttle": {

                    break;
                }
            }
            break;
        }
        case 4: {
            switch (unit.type) {
                case "infantry": {
                    var infantry = unitElement;
                    translate(3.5, 11, infantry);
                    var centerX = infantry.points[3].x - 1;
                    var centerY = infantry.points[1].y - 1;
                    infantry.setAttribute("transform", "rotate(-31 " + centerX + " " + centerY + ")");
                    break;
                }
                case "crawler": {
                    translate(8, 2, unitElement);
                    break;
                }
                case "shuttle": {
                    translate(9.5, 3.5, unitElement);
                    var centerX = unitElement.points[1].x - 2;
                    var centerY = unitElement.points[1].y - 1;
                    unitElement.setAttribute("transform", "rotate(180 " + centerX + " " + centerY + ")");
                    break;
                }
            }
            break;
        }
        case 5: {
            switch (unit.type) {
                case "infantry": {
                    var infantry = unitElement;
                    translate(3.7, 4.2, infantry);
                    var centerX = infantry.points[3].x - 1;
                    var centerY = infantry.points[1].y - 1;
                    infantry.setAttribute("transform", "rotate(31 " + centerX + " " + centerY + ")");
                    break;
                }
                case "crawler": {
                    translate(13, 4.5, unitElement);
                    var centerX = unitElement.points[3].x - 2;
                    var centerY = unitElement.points[1].y - 1;
                    unitElement.setAttribute("transform", "rotate(-120 " + centerX + " " + centerY + ")");
                    break;
                }
                case "shuttle": {
                    translate(14.5, 7.5, unitElement);
                    var centerX = unitElement.points[1].x - 2;
                    var centerY = unitElement.points[1].y - 1;
                    unitElement.setAttribute("transform", "rotate(-120 " + centerX + " " + centerY + ")");
                    break;
                }
            }

            break;
        }
        case 6: {
            switch (unit.type) {
                case "infantry": {
                    translate(9, 7.8, unitElement);
                    break;
                }
                case "crawler": {

                    break;
                }
                case "shuttle": {

                    break;
                }
            }
            break;
        }
    }
}


function positionCrawler(unit, homeHex) {
    var points = unit.points;
    var rows = homeHex.id.slice(0, homeHex.id.indexOf(" "));
    var cols = homeHex.id.slice(homeHex.id.indexOf((" ")));
    positionStandard(rows, cols, unit);
    if (homeHex.getAttribute("unitNumber") === "0") {
        translate(8, 13.5, unit);
    } if (homeHex.getAttribute("unitNumber") === "1") {
        translate(3, 10.5, unit);
        var centerX = unit.points[3].x - 2;
        var centerY = unit.points[1].y - 1;
        unit.setAttribute("transform", "rotate(-120 " + centerX + " " + centerY + ")");
    } if (homeHex.getAttribute("unitNumber") === "2") {
        translate(13, 10.5, unit);
        var centerX = unit.points[3].x - 2;
        var centerY = unit.points[1].y - 1;
        unit.setAttribute("transform", "rotate(120 " + centerX + " " + centerY + ")");
    } if (homeHex.getAttribute("unitNumber") === "3") {
        translate(3, 4.7, unit);
        var centerX = unit.points[3].x - 2;
        var centerY = unit.points[1].y - 1;
        unit.setAttribute("transform", "rotate(120 " + centerX + " " + centerY + ")");
    } if (homeHex.getAttribute("unitNumber") === "4") {
        translate(8, 2, unit);
    } if (homeHex.getAttribute("unitNumber") === "5") {
        translate(13, 4.5, unit);
        var centerX = unit.points[3].x - 2;
        var centerY = unit.points[1].y - 1;
        unit.setAttribute("transform", "rotate(-120 " + centerX + " " + centerY + ")");
    }
}

var Building = function (row, col, type, id, components) {
    this.row = row;
    this.col = col;
    this.type = type;
    this.id = id;
    this.componentElements = components;
}

Board.prototype.getBuildingCount = function () {
    var count = 0;
    for (var i = 0; i < this.hexes.length; i++) {
        if (this.hexes[i].building) {
            count++;
        }
    }
    return count;
}

function addBuilding(hex, buildingType) {
    var buildingElement;
    var components = [];
    var building = new Building(hex.row, hex.col, buildingType, board.getBuildingCount(), components);
    hex.building = building;
    switch (buildingType) {
        case "settlement": {
            buildingElement = drawBuilding("settlement", hex);
            buildingElement.id = board.getBuildingCount() + "S";
            components[components.length] = buildingElement.id;
            document.getElementById("mapSVG").appendChild(buildingElement);
            building = new Building(hex.row, hex.col, buildingType, board.getBuildingCount(), components);
            hex.building = building;
        }
        case "large": {
            buildingElement = drawBuilding("large", hex);
            buildingElement.id = board.getBuildingCount() + "LM";
            components[components.length] = buildingElement.id;
            document.getElementById("mapSVG").appendChild(buildingElement);
            building = new Building(hex.row, hex.col, buildingType, board.getBuildingCount(), components);
            hex.building = building;
        }
        case "small": {
            buildingElement = drawBuilding("small", hex);
            buildingElement.id = board.getBuildingCount() + "SM";
            components[components.length] = buildingElement.id;
            document.getElementById("mapSVG").appendChild(buildingElement);
            building = new Building(hex.row, hex.col, buildingType, board.getBuildingCount(), components);
            hex.building = building;
        }
    }
    for (var i = 0; i < components.length; i++) {
        document.getElementById(components[i]).addEventListener("click", createMenu);
    }
    var created;
    function createMenu() {
        if (phase !== "building") {
            return;
        }
        for (var i = 0; i < components.length; i++) {
            document.getElementById(components[i]).removeEventListener("click", createMenu);
        }
        var el = document.createElement("div");
        el.id = "menu";
        el.style.width = "100";
        el.style.height = "auto";
        el.style.zIndex = "1";
        el.style.position = "absolute";
        el.style.marginLeft = event.pageX - 2;
        el.style.marginTop = event.pageY - document.getElementById("page").offsetHeight * .06 - 2;
        document.getElementById("map").insertBefore(el, document.getElementById("mapSVG"));
        var ul = document.createElement("ul");
        el.appendChild(ul);
        function createButton(type) {
            var bu = document.createElement("button");
            bu.id = "menuButton";
            bu.style.width = "125";
            bu.style.opacity = ".75";
            bu.style.background = "#f9f9f9"
            bu.style.font = "Arial";
            bu.style.boxShadow = "0px 8px 16px 0px rgba(0,0,0,0.2)";
            bu.style.fontSize = "1em";
            bu.style.textAlign = "center";
            bu.style.height = "20";
            bu.parentId = hex.id;
            bu.unitType = type;
            var innerHTML;
            switch (type) {
                case "infantry": innerHTML = "Infantry"; break;
                case "crawler": innerHTML = "Crawler"; break;
                case "shuttle": innerHTML = "Shuttle"; break;
            }
            bu.innerHTML = innerHTML;
            bu.style.padding = "17px 21px";
            bu.addEventListener("mouseenter", function () {
                bu.style.background = "#f1f1f1";
            });
            bu.addEventListener("mouseleave", function () {
                bu.style.background = "#f9f9f9";
            });
            bu.addEventListener("click", createUnit)
            return bu;
        }
        ul.appendChild(createButton("infantry"));
        ul.appendChild(createButton("crawler"));
        ul.appendChild(createButton("shuttle"));
        function componentLeave() {
            if (!isPartOfSettlement(document.elementFromPoint(event.clientX, event.clientY)) && document.elementFromPoint(event.clientX, event.clientY).id !== "menuButton") {
                for (var i = 0; i < components.length; i++) {
                    document.getElementById(components[i]).removeEventListener("mouseleave", componentLeave);
                }
                removeMenu(event);
            }
        }
        ul.addEventListener("mouseleave", function () {
            if (!isPartOfSettlement(document.elementFromPoint(event.clientX, event.clientY))) {
                removeMenu(event);
                for (var i = 0; i < components.length; i++) {
                    document.getElementById(components[i]).removeEventListener("mouseleave", componentLeave);
                }
            }
            for (var i = 0; i < components.length; i++) {
                document.getElementById(components[i]).addEventListener("mouseleave", componentLeave);
            }
        });
        function isPartOfSettlement(element) {
            for (var i = 0; i < components.length; i++) {
                if (components[i] === element.id) {
                    return true;
                }
            }
            return false;
        }
        document.addEventListener("click", removeMenu);
        created = false;
    }
    function removeMenu() {
        if ((created && event.target.id !== "menuButton") || event.type === "mouseleave") {
            if (document.getElementById("menu")) {
                document.getElementById("map").removeChild(document.getElementById("menu"));
            }
            for (var i = 0; i < components.length; i++) {
                document.getElementById(components[i]).addEventListener("click", createMenu);
            }
            document.removeEventListener("click", removeMenu);
            document.removeEventListener("mouseleave", removeMenu);
        }
        created = true;
    }
    positionUnits(hex);
    for (var i = 0; i < board.hexes.length; i++) {
        if (board.hexes[i].id === hex.id) {
            board.hexes[i] = hex;
        }
    }
    return building;
}

function createUnit() {
    var unit = addUnit(board.getHexById(this.parentId), this.unitType);
    if (unit === undefined) {
        return;
    }
    document.getElementById(unit.id).style.stroke = "white";
    document.getElementById(unit.id).style.strokeWidth = ".25";
}

function drawBuilding(buildingType, homeHex) {
    var row = homeHex.row;
    var col = homeHex.col;
    var circle = document.createElementNS("http://www.w3.org/2000/svg", "circle");
    circle.setAttribute("stroke-width", ".5");
    circle.setAttribute("stroke", "white");
    circle.setAttribute("fill", "transparent");
    circle.setAttribute("cx", positionCircleX(row));
    circle.setAttribute("cy", positionCircleY(row, col));
    var radius = 0;
    if (buildingType === "small") {
        radius = 1.5;
    } else if (buildingType === "large") {
        radius = 3;
    } else if (buildingType = "settlement") {
        radius = 4.5;
        switch (Math.floor(Math.random() * 6) + 1) {
            case 6: circle.style.fill = "red"; break;
            case 5: circle.style.fill = "blue"; break;
            case 4: circle.style.fill = "green"; break;
            case 3: circle.style.fill = "yellow"; break;
            case 2: circle.style.fill = "purple"; break;
            case 1: circle.style.fill = "pink"; break;
        }
    }
    circle.setAttribute("r", radius);
    return circle;
}


function positionCircleY(rows, cols) {
    if (rows % 2 === 1) {
        return 17.4 * cols + 17.4 * .5 + 4 + 8.8;
    } else {
        return 17.4 * cols + 4 + 8.8;
    }
}

function positionCircleX(rows) {
    return 20 * rows * .75 + 4 + 10;
}
