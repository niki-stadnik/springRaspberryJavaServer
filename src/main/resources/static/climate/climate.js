var stompClient = null;

window.onload = function() {
    var reloading = sessionStorage.getItem("reloading");
    connect();
    if (reloading) {
        sessionStorage.removeItem("reloading");
    }
}

function connect() {
    var socket = new SockJS('/stomp-endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/adax', function(message) {
            showMessage(JSON.parse(message.body));
        });
    });
    setTimeout(() => {stompClient.send("/app/adaxInit", {}, JSON.stringify({}));}, "1000");
}

var BedTarget;

function showMessage(message) {
    switch(message.roomId){
        case 161132:    //bedroom
            document.getElementById("BedTemp").textContent = message.currentTemperature;
            document.getElementById("update").textContent = message.targetTemperature;
            BedTarget = message.targetTemperature;
            break;
    }
}
function update() {
    stompClient.send("/app/adaxInit", {}, JSON.stringify({}));
}

function BedDecrease() {
    var dec = (BedTarget - 1) * 100;
    stompClient.send("/app/adax", {}, JSON.stringify({'id': 161132 ,'temp': dec}));    //bedroom
    document.getElementById("update").textContent = dec;
}
function BedIncrease() {
    var dec = (BedTarget + 1) * 100;
    stompClient.send("/app/adax", {}, JSON.stringify({'id': 161132 ,'temp': dec}));    //bedroom
    document.getElementById("update").textContent = dec;
}