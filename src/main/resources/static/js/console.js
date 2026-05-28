var stompClient = null;
var ansi_up = null;

window.onload = function() {
    var reloading = sessionStorage.getItem("reloading");
    // Initialize the converter
    ansi_up = new AnsiUp();
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
    var textElement = document.getElementById("text");
    // Convert ANSI to HTML and append
    textElement.innerHTML += ansi_up.ansi_to_html(message);

    console.log(isAutoScrollEnabled);
    if (isAutoScrollEnabled) {
        setTimeout(scrollToBottom, 0);
    }
}

// Function to scroll textarea to the bottom
function scrollToBottom() {
    var scrollableElement = document.getElementById("text");
    scrollableElement.scrollTop = scrollableElement.scrollHeight;
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
