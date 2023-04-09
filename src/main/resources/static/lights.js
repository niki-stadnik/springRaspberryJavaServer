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
        stompClient.subscribe('/topic/lightsClient', function(message) {
            showMessage(JSON.parse(message.body));
        });
    });
}

var flag = Array.from({length: 8}, (val,index) => false);


function showMessage(message) {
    if(!flag[0]) document.getElementById("0").checked = message.light0;
    document.getElementById("1").checked = message.light1;
    document.getElementById("2").checked = message.light2;
    document.getElementById("3").checked = message.light3;
    document.getElementById("4").checked = message.light4;
    document.getElementById("5").checked = message.light5;
    document.getElementById("6").checked = message.light6;
    document.getElementById("7").checked = message.light7;
}



function switchLight(ele) {
    var id = +ele.id;
    var checkBox = document.getElementById(ele.id);
    stompClient.send("/app/clientLightSwitch", {}, JSON.stringify({'light': id, 'state': checkBox.checked}));
    flag[id] = true;
    if(checkBox.checked) setTimeout(() => {flag[id] = false;}, "1000");
    else setTimeout(() => {flag[id] = false;}, "5000");
}