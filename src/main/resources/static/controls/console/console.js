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
        stompClient.subscribe('/topic/console', function(message) {
            showMessage(message.body);
        });
    });
}


var isAutoScrollEnabled = true;


function showMessage(message) {
    document.getElementById("text").textContent = message;
    console.log(isAutoScrollEnabled);
    if (isAutoScrollEnabled) {
        scrollToBottom();
    }
}

// Function to scroll textarea to the bottom
function scrollToBottom() {
    var textarea = document.getElementById("text");
    textarea.scrollTop = textarea.scrollHeight;
}

function enableScroll(ele){
    var id = +ele.id;
    var checkBox = document.getElementById(ele.id);
    if (checkBox.checked) {
        isAutoScrollEnabled = true;
    }
    else{
        isAutoScrollEnabled = false;
    }
}
