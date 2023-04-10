var stompClient = null;

function myFunction() {
	document.getElementById("welcome").textContent = "Welcome back!";
}

window.onload = function() {
    var reloading = sessionStorage.getItem("reloading");
    connect();
    if (reloading) {
        sessionStorage.removeItem("reloading");
        myFunction();
    }
}

function reloadP() {
    sessionStorage.setItem("reloading", "true");
    document.location.reload();
}



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
            showGreeting2(JSON.parse(greeting.body));
        });
    });
}



function showGreeting(test) {
    $("#greetings").append("<tr><td>" + test.test + "</td></tr>");
}
function showGreeting2(wtf) {
   // $("#greetings").append("<tr><td>" + wtf.wtf + "</td></tr>");
    document.getElementById("teststr").textContent = wtf.wtf;
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
function lightBedOn() {
    stompClient.send("/app/clientLightSwitch", {}, JSON.stringify({'light': 0, 'state': true}));
}
function lightBedOff() {
    stompClient.send("/app/clientLightSwitch", {}, JSON.stringify({'light': 0, 'state': false}));
}
function lightBathOn() {
    stompClient.send("/app/clientLightSwitch", {}, JSON.stringify({'light': 1, 'state': true}));
}
function lightBathOff() {
    stompClient.send("/app/clientLightSwitch", {}, JSON.stringify({'light': 1, 'state': false}));
}
function adax1() {
    stompClient.send("/app/adax", {}, JSON.stringify({'id': 161132 ,'temp': 2100}));
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
    $( "#lightBathOn" ).click(function() { lightBathOn(); });
    $( "#lightBathOff" ).click(function() { lightBathOff(); });
    $( "#lightBedOn" ).click(function() { lightBedOn(); });
    $( "#lightBedOff" ).click(function() { lightBedOff(); });
    $( "#adax1" ).click(function() { adax1(); });
});
