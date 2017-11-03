document.getElementById("chatTextArea").addEventListener("keypress", newChatMessage);

document.getElementById("chatSendButton").addEventListener("click", newChatMessage);

function newChatMessage() {
    if (event.type === "click" || (event.type === "keypress" && event.keyCode === 13)) {
        event.preventDefault();
        var message = {
            "userID" : "12345",
            "message" : document.getElementById("chatTextArea").value
        }
        document.getElementById("chatTextArea").value = "";

           message = JSON.stringify(message);

            var xhr = new XMLHttpRequest();
            xhr.open("POST", "http://localhost:4567/chat/createMessage", true);
            xhr.onload = function (e) {
                if (xhr.readyState === 4) {

                    if (xhr.status === 200) {
                             console.log("reached output");
                             console.log(JSON.parse(xhr.responseText));
                             document.getElementById("messages").innerHTML(JSON.parse(xhr.responseText));
                    } else {

                        console.error(xhr.status);
                    }
                }
            };
            xhr.onerror = function (e) {
                console.error(xhr.statusText);
            };
            xhr.send(message);

    }
}


function POST(string, destination) {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:4567" + destination);
    xhr.send(string);
    return "";
}

function GET(destination, elementID){
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:4567" + destination, true);
    return "";
}

var tradeDropdownNL = document.getElementsByClassName("tradeDropdown");

for (var i = 0; i < tradeDropdownNL.length; i++) {
    tradeDropdownNL.item(i).addEventListener("click", newTradeTarget);
}

function newTradeTarget() {
    var span = document.createElement("SPAN");
    var text = document.createTextNode(event.target.innerHTML);
    span.appendChild(text);
    span.className = event.target.className;
    if (document.getElementById("dropdownButton").childElementCount === 0) {
        document.getElementById("dropdownButton").appendChild(span);
    } else {
        var oldChild = document.getElementById("dropdownButton").childNodes[1];
        document.getElementById("dropdownButton").replaceChild(span, oldChild);
    }
}


document.getElementById("sendButton").addEventListener("click", newTrade);

var tradeTextAreaNL = document.getElementsByClassName("tradeTextArea");

for (var i = 0; i < tradeTextAreaNL.length; i++) {
    tradeTextAreaNL.item(i).addEventListener("keypress", validateEntryNumeric);
}

function validateEntryNumeric() {
    if (event.keyCode < 48 || event.keyCode > 57) {
        event.preventDefault();
    }
}

function newTrade() {
    var NLSender = document.getElementsByClassName("sender");
    var tradeRequestValuesSender = {
        "metal" : NLSender.item(0).value || 0,
        "ice" : NLSender.item(1).value || 0,
        "organics" : NLSender.item(2).value || 0,
        "hydrogen" : NLSender.item(3).value || 0,
        "rare" : NLSender.item(4).value || 0,
        "xrare" : NLSender.item(5).value || 0,
        "iridium" : NLSender.item(6).value || 0,
        "uranium" : NLSender.item(7).value || 0
    }
    for (var i = 0; i < NLSender.length; i++) {
        NLSender.item(i).value = "";
    }
    var NLReciever = document.getElementsByClassName("reciever");
    var tradeRequestValuesReciever = {
        "metal" : NLReciever.item(0).value || 0,
        "ice" : NLReciever.item(1).value || 0,
        "organics" : NLReciever.item(2).value || 0,
        "hydrogen" : NLReciever.item(3).value || 0,
        "rare" : NLReciever.item(4).value || 0,
        "xrare" : NLReciever.item(5).value || 0,
        "iridium" : NLReciever.item(6).value || 0,
        "uranium" : NLReciever.item(7).value || 0
    }
    for (var i = 0; i < NLReciever.length; i++) {
        NLReciever.item(i).value = "";
    }
    var trade = {
        "senderID" : "12345", //sender here
        "recieverID" : "12345", //reciever here
        "senderTradeValues" : tradeRequestValuesSender,
        "recieverTradeValues" : tradeRequestValuesReciever 
    }
    POST(JSON.stringify(trade), "/trade/createRequest")
}
