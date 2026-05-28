//BathroomFan

function showBFan(message) {
    if (message.bathFan){
        document.getElementById("sendBFOn").classList.add('active');
        document.getElementById("sendBFOff").classList.remove('active');
    }else{
        document.getElementById("sendBFOff").classList.add('active');
        document.getElementById("sendBFOn").classList.remove('active');
    }
    document.getElementById("bathTemp1").textContent = message.bathTemp1;
    document.getElementById("bathTemp2").textContent = message.bathTemp2;
    document.getElementById("bathHum1").textContent = message.bathHum1;
    document.getElementById("bathHum2").textContent = message.bathHum2;
    if(message.auto) document.getElementById("sendBFAuto").classList.add('active');
    else document.getElementById("sendBFAuto").classList.remove('active');
}


function sendBFOn() {
    document.getElementById("sendBFOn").classList.add('active');
    document.getElementById("sendBFOff").classList.remove('active');
    document.getElementById("sendBFAuto").classList.remove('active');
    stompClient.send("/app/client/bathroomFan", {}, JSON.stringify({'bathFanCommand': true,'auto': false}));
}
function sendBFOff() {
    document.getElementById("sendBFOff").classList.add('active');
    document.getElementById("sendBFOn").classList.remove('active');
    document.getElementById("sendBFAuto").classList.remove('active');
    stompClient.send("/app/client/bathroomFan", {}, JSON.stringify({'bathFanCommand': false,'auto': false}));
}
function sendBFAuto() {
    document.getElementById("sendBFAuto").classList.add('active');
    document.getElementById("sendBFOn").classList.remove('active');
    document.getElementById("sendBFOff").classList.remove('active');
    stompClient.send("/app/client/bathroomFan", {}, JSON.stringify({'bathFanCommand': false,'auto': true}));
}


//Door lock

function sendlock() {
    stompClient.send("/app/clientDoorlock", {}, JSON.stringify({'move': 2162}));
}
function sendunlock() {
    stompClient.send("/app/clientDoorlock", {}, JSON.stringify({'move': 11762}));
}

function showDoorlock(message){
    document.getElementById("position").textContent = message.position;
}


//Camera

function showFrameRate(message){
    document.querySelector(`#camList [data-cam="cam1"] [data-fps]`).textContent = message.fps;
    document.querySelector(`#camList [data-cam="cam1"] [data-fpm]`).textContent = message.fpm;
}



//Herb pot

function setLightSwitch(isOn) {
    const lightSw  = document.getElementById('light-sw');
    const lightTxt = document.getElementById('light-status-txt');
    if (!lightSw || !lightTxt) return;
    lightSw.classList.toggle('on', isOn);
    lightTxt.textContent = isOn ? 'Включена' : 'Изключена';
}

function setWaterSwitch(isOn) {
    const waterSw  = document.getElementById('water-sw');
    const waterTxt = document.getElementById('water-status-txt');
    if (!waterSw) return;
    waterSw.classList.toggle('on', isOn);
    waterTxt.textContent = isOn ? 'Активно' : 'Изключено';
}

function showPotData(message){
    document.getElementById("temp1").textContent = (message.temp1 + "°C");
    document.getElementById("temp2").textContent = (message.temp2 + "°C");
    document.getElementById("moisture1").textContent = (message.moisture1 + "%");
    document.getElementById("moisture2").textContent = (message.moisture2 + "%");
    const startInput = document.getElementById('light-start');
    if (!startInput.value) document.getElementById('light-start').value = message.herbLightStartTime;
    const endInput = document.getElementById('light-end');
    if (!endInput.value) document.getElementById('light-end').value = message.herbLightEndTime;
    setLightSwitch(message.herbLight);
    //setWaterSwitch(message.waterAuto);
}


function water(){
    stompClient.send("/app/client/herbPot", {}, JSON.stringify({'command': 3}));
}

function herbLight(){
    stompClient.send("/app/client/herbPot", {}, JSON.stringify({'command': 4}));
}

function readTimes() {
    const start = document.getElementById("light-start").value;
    const end = document.getElementById("light-end").value;
    stompClient.send("/app/client/herbPot", {}, JSON.stringify({'command': 5, 'herbLightStartTime': start, 'herbLightEndTime': end}));
    console.log(start); // e.g. "22:00"
    console.log(end);   // e.g. "02:00"
}



//devices reboot

/* ══════════════════════════════════════════════════
       DEVICES  — add/remove objects freely
    ══════════════════════════════════════════════════ */
const DEVICES = [
    { name:'lightSwitch',        sub:'lightSwitch',     on:false },
    { name:'doorman',            sub:'doorman',         on:false },
    { name:'kitchenStrip',       sub:'kitchenStrip',    on:false },
    { name:'kitchen2',           sub:'kitchen2',        on:false },
    { name:'bathroomStrip',      sub:'bathroomStrip',   on:false },
    { name:'bathroomFan',        sub:'bathroomFan',     on:false },
    { name:'herbPot',            sub:'herbPot',         on:false },
    { name:'lightDimmer',        sub:'lightDimmer',     on:false },
];



function setDeviceMonitoringStatus(name, isOnline) {
    const dev = DEVICES.find(d => d.name === name);
    if (!dev?.sw) return;
    dev.sw.classList.toggle('on', isOnline);
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
                    setDeviceMonitoringStatus(device, status);
                });
            }
        )
        .catch(error => {
            console.error("Fetch error:", error);
        });
}


function setDeviceStatus(name, isOnline) {
    const dev = DEVICES.find(d => d.name === name);
    if (!dev?.dot) return;
    dev.dot.classList.toggle('online', isOnline);
    dev.dot.classList.toggle('offline', !isOnline);
}
function showState(message) {
    for (const [key, value] of Object.entries(message)) {
        setDeviceStatus(key, value);
    }
}


function toggleRebootMonitoring(device, monitor) {
    console.log(device, monitor);
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
    console.log("Rebooting device:", device);
    fetch(`/reboot/rebooting/${device}`, {
        method: 'POST'
    })
        .then(response => {
            if (!response.ok) {
                console.error("Request failed");
            }
        });
}