var stompClient = null;

window.onload = function () {
    var reloading = sessionStorage.getItem("reloading");
    connect();
    getDeviceStatuses();
    if (reloading) {
        sessionStorage.removeItem("reloading");
    }
}


function connect() {
    var socket = new SockJS('/stomp-endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/state', function (message) {
            showState(JSON.parse(message.body));
        });
    });
}


function getDeviceStatuses() {
    fetch('/reboot/monitoring')  // your backend endpoint
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.json(); // parse JSON
        })
        .then(data => {
                console.log("Received device statuses:", data);

                // Example: iterate over key-value pairs
                Object.entries(data).forEach(([device, status]) => {
                    console.log(`Device: ${device}, Status: ${status}`);

                    device = device + "Monitoring";
                    const element = document.getElementById(device);// Get the element
                    if (element) {
                        document.getElementById(device).checked = status;
                    } else {
                        console.warn(`Element with ID '${device}' not found.`); // Log a warning if element is missing

                    }
                });
            }
        )
        .catch(error => {
            console.error("Fetch error:", error);
        });
}


function showState(message) {
    for (const [key, value] of Object.entries(message)) {
        console.log("Key:", key);
        console.log("Value:", value);
        const element = document.getElementById(key); // Get the element
        if (element) { // Check if the element exists
            if (value) {
                document.getElementById(key).style.backgroundColor = '#009933';
            } else {
                document.getElementById(key).style.backgroundColor = '#cacaca57';
            }
        } else {
            console.warn(`Element with ID '${key}' not found.`); // Log a warning if element is missing
        }
    }
}


function toggleRebootMonitoring(device, monitor) {
    fetch(`/reboot/monitoring/${device}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(monitor)
    })
        .then(response => {
            if (!response.ok) {
                console.error("Request failed");
            }
        });
}

function rebootDevice(device) {
    fetch(`/reboot/rebooting/${device}`, {
        method: 'POST'
    })
        .then(response => {
            if (!response.ok) {
                console.error("Request failed");
            }
        });
}