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
        stompClient.subscribe('/topic/dimmersClient', function(message) {
            showDimmers(JSON.parse(message.body));
        });
        stompClient.subscribe('/topic/client/kitchenStrip', function(message) {
            showStripDimmer(JSON.parse(message.body));
        });
        stompClient.subscribe('/topic/client/bathroomStrip', function(message) {
            showStripDimmerB(JSON.parse(message.body));
        });
    });
    setTimeout(() => {stompClient.send("/app/client/lightDimmer", {}, JSON.stringify({'name': "getData", 'value': 200}));}, "100");
    setTimeout(() => {stompClient.send("/app/client/kitchenStrip", {}, JSON.stringify({'command': 3}));}, "100");
    setTimeout(() => {stompClient.send("/app/client/bathroomStrip", {}, JSON.stringify({'command': 3}));}, "100");

}

var flag = Array.from({length: 8}, (val,index) => false);


function showMessage(message) {
    if(!flag[0]) document.getElementById("0").checked = message.light0;
    if(!flag[1]) document.getElementById("1").checked = message.light1;
    if(!flag[2]) document.getElementById("2").checked = message.light2;
    if(!flag[3]) document.getElementById("3").checked = message.light3;
    if(!flag[4]) document.getElementById("4").checked = message.light4;
    if(!flag[5]) document.getElementById("5").checked = message.light5;
    if(!flag[6]) document.getElementById("6").checked = message.light6;
//    if(!flag[7]) document.getElementById("7").checked = message.light7;
}
function showDimmers(message) {
    document.getElementById(message.name).value = message.value;
}

function showStripDimmer(message) {
    document.getElementById("kitchenStrip").value = message.duty;
}
function showStripDimmerB(message) {
    document.getElementById("bathroomStrip").value = message.duty;
}

function switchLightOFF() {
    const NUM_LIGHTS = 7; // adjust to your setup
    const switchStateOf = Array(NUM_LIGHTS).fill(true);
    const stateLight = Array(NUM_LIGHTS).fill(false);
    stompClient.send("/app/client/lightSwitch", {}, JSON.stringify({switchStateOf, stateLight}));
    for (let i = 0; i < 8; i++)
    {
        flag[i] = true;
        setTimeout(() => {
            flag[i] = false;
        }, "2000");
    }
}
function switchLightON() {
    const NUM_LIGHTS = 7; // adjust to your setup
    const switchStateOf = Array(NUM_LIGHTS).fill(true);
    const stateLight = Array(NUM_LIGHTS).fill(true);
    stompClient.send("/app/client/lightSwitch", {}, JSON.stringify({switchStateOf, stateLight}));
    for (let i = 0; i < 8; i++)
    {
        flag[i] = true;
        setTimeout(() => {
            flag[i] = false;
        }, "2000");
    }
}

function switchLight(ele) {
    const NUM_LIGHTS = 7; // adjust to your setup
    const id = Number(ele.id);
    const checkBox = document.getElementById(ele.id);
    const switchStateOf = Array(NUM_LIGHTS).fill(false);
    const stateLight = Array(NUM_LIGHTS).fill(false);
    switchStateOf[id] = true;
    stateLight[id] = checkBox.checked;
    stompClient.send("/app/client/lightSwitch", {}, JSON.stringify({switchStateOf, stateLight}));
    flag[id] = true;
    setTimeout(() => {flag[id] = false;}, "2000");
}


function updateDimmer(ele) {
    var dimmer = document.getElementById(ele.id);
    stompClient.send("/app/client/lightDimmer", {}, JSON.stringify({'name': ele.id, 'value': dimmer.value}));
}

function kitchenStrip(ele) {
    var dimmer = document.getElementById(ele.id);
    var time = document.getElementById("time");
    stompClient.send("/app/client/kitchenStrip", {}, JSON.stringify({'command': 4, 'duty': dimmer.value, 'time': time.value}));
}
function bathroomStrip(ele) {
    var dimmer = document.getElementById(ele.id);
    var time = document.getElementById("timeB");
    const data = {
        command: 4,
        duty: dimmer.value,
        time: time.value
    };
    stompClient.send("/app/client/bathroomStrip", {}, JSON.stringify(data));
}