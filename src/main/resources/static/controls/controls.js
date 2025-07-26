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
        stompClient.subscribe('/topic/clientBathroomFan', function(message) {
            showMessage(JSON.parse(message.body));
        });
        stompClient.subscribe('/topic/frameRate', function(message) {
            showFrameRate(JSON.parse(message.body));
        });
        stompClient.subscribe('/topic/clientHerbPot', function(message) {
            showPotData(JSON.parse(message.body));
        });
        stompClient.subscribe('/topic/console', function(message) {
                    //showFrameRate(JSON.parse(message.body));
                });
        stompClient.subscribe('/topic/clientDoorlock', function(message) {
            showDoorlock(JSON.parse(message.body));
        });
    });
}

//var flag = Array.from({length: 8}, (val,index) => false);


function showMessage(message) {
    if (message.bathFan){
        document.getElementById("sendBFOn").style.backgroundColor = '#009933';
        document.getElementById("sendBFOff").style.backgroundColor = '#cacaca57';
    }else{
        document.getElementById("sendBFOff").style.backgroundColor = '#009933';
        document.getElementById("sendBFOn").style.backgroundColor = '#cacaca57';
    }
    document.getElementById("temp").textContent = message.bathTemp;
    document.getElementById("hum").textContent = message.bathHum;
    document.getElementById("light").textContent = message.bathLight;
    if(message.auto) document.getElementById("sendBFAuto").style.backgroundColor = '#009933';
    else document.getElementById("sendBFAuto").style.backgroundColor = '#cacaca57';
}


function sendBFOn() {
    stompClient.send("/app/clientBathroomFan", {}, JSON.stringify({'bathFanCommand': true,'auto': false}));
}
function sendBFOff() {
    stompClient.send("/app/clientBathroomFan", {}, JSON.stringify({'bathFanCommand': false,'auto': false}));
}
function sendBFAuto() {
    stompClient.send("/app/clientBathroomFan", {}, JSON.stringify({'bathFanCommand': false,'auto': true}));
}


function sendResLight() {
    stompClient.send("/app/clientDoorman", {}, JSON.stringify({'command': 1}));
}
function sendResDoor() {
    stompClient.send("/app/clientDoorman", {}, JSON.stringify({'command': 2}));
}
function sendResKitchenStrip() {
    stompClient.send("/app/clientKitchenStrip", {}, JSON.stringify({'command': 2}));
}
function sendResKitchen2() {
    stompClient.send("/app/clientKitchenStrip", {}, JSON.stringify({'command': 1}));
}


function sendlock() {
    stompClient.send("/app/clientDoorlock", {}, JSON.stringify({'move': 2162}));
}
function sendunlock() {
    stompClient.send("/app/clientDoorlock", {}, JSON.stringify({'move': 11762}));
}



function showFrameRate(message){
    document.getElementById("fps").textContent = message.fps;
    document.getElementById("fpm").textContent = message.fpm;
}

function showPotData(message){
    document.getElementById("temp1").textContent = message.temp1;
    document.getElementById("temp2").textContent = message.temp2;
    document.getElementById("moisture1").textContent = message.moisture1;
    document.getElementById("moisture2").textContent = message.moisture2;
}

function showDoorlock(message){
    document.getElementById("posit").textContent = message.posit;
}
