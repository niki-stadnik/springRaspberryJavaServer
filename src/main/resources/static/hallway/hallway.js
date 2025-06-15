var stompClient = null;

window.onload = function() {
    var reloading = sessionStorage.getItem("reloading");
    connect();
    if (reloading) {
        sessionStorage.removeItem("reloading");
    }
}
function reloadP() {
    sessionStorage.setItem("reloading", "true");
    document.location.reload();
}
function controls() {
     window.location.href="/controls/controls.html";
}
function lights() {
     window.location.href="/lights/lights.html";
}
function climate() {
     window.location.href="/climate/climate.html";
}

function connect() {
    var socket = new SockJS('/stomp-endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/imageDoorCam', function(message) {
            showImage(JSON.parse(message.body));
        });
    });
}

function showImage(image) {
    var dd = image.image;
    const imageData = document.getElementById('image');
    imageData.src = 'data:image/jpeg;base64,' + dd;
}