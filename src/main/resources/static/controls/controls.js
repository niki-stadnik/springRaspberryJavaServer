var stompClient = null;

window.onload = function() {
    var reloading = sessionStorage.getItem("reloading");
    connect();
    if (reloading) {
        sessionStorage.removeItem("reloading");
    }
}

function reboot() {
    window.location.href="/controls/reboot/reboot.html";
}

function connect() {
    var socket = new SockJS('/stomp-endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/client/bathroomFan', function(message) {
            showMessage(JSON.parse(message.body));
        });
        stompClient.subscribe('/topic/frameRate', function(message) {
            showFrameRate(JSON.parse(message.body));
        });
        stompClient.subscribe('/topic/client/herbPot', function(message) {
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
    document.getElementById("bathTemp1").textContent = message.bathTemp1;
    document.getElementById("bathTemp2").textContent = message.bathTemp2;
    document.getElementById("bathHum1").textContent = message.bathHum1;
    document.getElementById("bathHum2").textContent = message.bathHum2;
    if(message.auto) document.getElementById("sendBFAuto").style.backgroundColor = '#009933';
    else document.getElementById("sendBFAuto").style.backgroundColor = '#cacaca57';
}


function sendBFOn() {
    stompClient.send("/app/client/bathroomFan", {}, JSON.stringify({'bathFanCommand': true,'auto': false}));
}
function sendBFOff() {
    stompClient.send("/app/client/bathroomFan", {}, JSON.stringify({'bathFanCommand': false,'auto': false}));
}
function sendBFAuto() {
    stompClient.send("/app/client/bathroomFan", {}, JSON.stringify({'bathFanCommand': false,'auto': true}));
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
    const startInput = document.getElementById('startHerbPotLight');
    if (!startInput.value) document.getElementById('startHerbPotLight').value = message.herbLightStartTime;
    const endInput = document.getElementById('endHerbPotLight');
    if (!endInput.value) document.getElementById('endHerbPotLight').value = message.herbLightEndTime;
    if(message.herbLight) document.getElementById("herbLight").style.backgroundColor = '#009933';
    else document.getElementById("herbLight").style.backgroundColor = '#cacaca57';
}

function showDoorlock(message){
    document.getElementById("position").textContent = message.position;
}

function water(){
    stompClient.send("/app/client/herbPot", {}, JSON.stringify({'command': 3}));
}

function herbLight(){
    stompClient.send("/app/client/herbPot", {}, JSON.stringify({'command': 4}));
}

function readTimes() {
    const start = document.getElementById("startHerbPotLight").value;
    const end = document.getElementById("endHerbPotLight").value;
    stompClient.send("/app/client/herbPot", {}, JSON.stringify({'command': 5, 'herbLightStartTime': start, 'herbLightEndTime': end}));
    console.log(start); // e.g. "22:00"
    console.log(end);   // e.g. "02:00"
}
