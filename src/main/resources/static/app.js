var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/stomp-endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/client', function (greeting) {
            showGreeting(JSON.parse(greeting.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendBFOn() {
    //stompClient.send("/app/bathroomFan", {}, JSON.stringify({'name': $("#name").val()}));
    stompClient.send("/app/clientBathroomFan", {}, JSON.stringify({'bathFanCommand': true,'auto': false}));
}

function sendBFOff() {
    stompClient.send("/app/clientBathroomFan", {}, JSON.stringify({'bathFanCommand': false,'auto': false}));
}
function sendBFAuto() {
    stompClient.send("/app/clientBathroomFan", {}, JSON.stringify({'bathFanCommand': false,'auto': true}));
}
function lightBath() {
    stompClient.send("/app/clientLightSwitch", {}, JSON.stringify({'light0': false,'light1': true,'light2': false,'light3': false,'light4': false,'light5': false,'light6': false,'light7': false}));
}
function lightBed() {
    stompClient.send("/app/clientLightSwitch", {}, JSON.stringify({'light0': true,'light1': false,'light2': false,'light3': false,'light4': false,'light5': false,'light6': false,'light7': false}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message.message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#sendBFOn" ).click(function() { sendBFOn(); });
    $( "#sendBFOff" ).click(function() { sendBFOff(); });
    $( "#sendBFAuto" ).click(function() { sendBFAuto(); });
    $( "#lightBath" ).click(function() { lightBath(); });
    $( "#lightBed" ).click(function() { lightBed(); });
});
