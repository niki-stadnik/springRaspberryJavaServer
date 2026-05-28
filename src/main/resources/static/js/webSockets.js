// ------ communication logic
var stompClient = null;

window.onload = function() {
    connect();
}


function connect() {
    console.log("connected3");
    var socket = new SockJS('/stomp-endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/lightsClient', function(message) {
            const card = document.querySelector(`.lamp-card[data-id="l1"]`);
            if (card) {
                showLights(JSON.parse(message.body));
            } else {
                console.log(`Lamp l1 is not currently rendered in the SPA. UI skip applied.`);
            }
        });
        stompClient.subscribe('/topic/dimmersClient', function(message) {
            const card = document.querySelector(`.dim-row[data-id="l1"]`);
            if (card) {
                showDimmers(JSON.parse(message.body));
            } else {
                console.log(`Lamp l1 is not currently rendered in the SPA. UI skip applied.`);
            }
        });
        stompClient.subscribe('/topic/client/kitchenStrip', function(message) {
            const card = document.querySelector(`.dim-row[data-id="l1"]`);
            if (card) {
                showStripDimmer(JSON.parse(message.body));
            } else {
                console.log(`Lamp l1 is not currently rendered in the SPA. UI skip applied.`);
            }
        });
        stompClient.subscribe('/topic/client/bathroomStrip', function(message) {
            const card = document.querySelector(`.dim-row[data-id="l1"]`);
            if (card) {
                showStripDimmerB(JSON.parse(message.body));
            } else {
                console.log(`Lamp l1 is not currently rendered in the SPA. UI skip applied.`);
            }
        });
        stompClient.subscribe('/topic/client/bathroomFan', function(message) {
            const card = document.getElementById("sendBFAuto");
            if (card) {
                showBFan(JSON.parse(message.body));
            } else {
                console.log(`sendBFAuto is not currently rendered in the SPA. UI skip applied.`);
            }
        });
        stompClient.subscribe('/topic/state', function (message) {
            if (DEVICES.some(d => d.dot)) {
                showState(JSON.parse(message.body));
            } else {
                console.log('Device list is not currently rendered in the SPA. UI skip applied.');
            }
        });
        stompClient.subscribe('/topic/frameRate', function(message) {
            const card = document.querySelector(`#camList [data-cam="cam1"]`);
            if (card) {
                showFrameRate(JSON.parse(message.body));
            } else {
                console.log(`cam1 is not currently rendered in the SPA. UI skip applied.`);
            }
        });
        stompClient.subscribe('/topic/client/herbPot', function(message) {
            const card = document.getElementById("light-start");
            if (card) {
                showPotData(JSON.parse(message.body));
            } else {
                console.log(`herbPot is not currently rendered in the SPA. UI skip applied.`);
            }
        });
        stompClient.subscribe('/topic/clientDoorlock', function(message) {
            const card = document.getElementById("position");
            if (card) {
                showDoorlock(JSON.parse(message.body));
            } else {
                console.log(`position is not currently rendered in the SPA. UI skip applied.`);
            }
        });

    });
    console.log("connected");
}