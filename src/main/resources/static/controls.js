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
        stompClient.subscribe('/topic/modeBathroomFan', function(message) {
            showMode(JSON.parse(message.body));
        });
    });
    setTimeout(() => {stompClient.send("/app/clientInitBF", {}, JSON.stringify({}));}, "100");
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
    document.getElementById("hum").textContent = message.bathHum + 20;
    document.getElementById("light").textContent = message.bathLight;
}
function showMode(message) {
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