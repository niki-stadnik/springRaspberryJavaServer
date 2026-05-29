function initSliders() {
    stompClient.send("/app/client/lightDimmer", {}, JSON.stringify({'name': "getData", 'value': 200}));
    stompClient.send("/app/client/kitchenStrip", {}, JSON.stringify({'command': 3}));
    stompClient.send("/app/client/bathroomStrip", {}, JSON.stringify({'command': 3}));
}


var flag = Array.from({length: 8}, (val, index) => false);

function showLights(message) {
    for (const key in message) {
        const numericId = parseInt(key.replace('light', ''), 10);
        const status = message[key];
        const stringId = `l${numericId}`;
        const card = document.querySelector(`.lamp-card[data-id="${stringId}"]`);
        if (card && !flag[numericId] && numericId < 7) {
            card.classList.toggle('on', status);
        }
    }
}


function showDimmers(message) {
    const numericId = parseInt((message.name).replace('dim', ''), 10);
    const percentage = (message.value / 255) * 100;
    const newPct = percentage.toFixed(1);
    const lampId = `l${numericId}`;

    // brightness
    document.querySelector(`#dimList .dim-row[data-id="${lampId}"] .dim-slider`).value = message.value;
    document.querySelector(`#dimList .dim-row[data-id="${lampId}"] .dim-fill`).style.width = percentage + '%';
    document.querySelector(`#dimList .dim-row[data-id="${lampId}"] .dim-pct`).textContent = newPct + '%';
    // fade time
    //document.querySelector(`#dimList .dim-row[data-id="${lampId}"] .dim-fade`).value = newFade;
}

function showStripDimmer(message) {
    const percentage = (message.duty / 255) * 100;
    const newPct = percentage.toFixed(1);
    document.querySelector(`#dimList .dim-row[data-id="l7"] .dim-slider`).value = message.duty;
    document.querySelector(`#dimList .dim-row[data-id="l7"] .dim-fill`).style.width = percentage + '%';
    document.querySelector(`#dimList .dim-row[data-id="l7"] .dim-pct`).textContent = newPct + '%';
    const status = message.duty != 0;
    const card = document.querySelector(`.lamp-card[data-id="l7"]`);
    if (card && !flag[7]) {
        card.classList.toggle('on', status);
    }
}

function showStripDimmerB(message) {
    const percentage = (message.duty / 255) * 100;
    const newPct = percentage.toFixed(1);
    document.querySelector(`#dimList .dim-row[data-id="l8"] .dim-slider`).value = message.duty;
    document.querySelector(`#dimList .dim-row[data-id="l8"] .dim-fill`).style.width = percentage + '%';
    document.querySelector(`#dimList .dim-row[data-id="l8"] .dim-pct`).textContent = newPct + '%';
    const status = message.duty != 0;
    const card = document.querySelector(`.lamp-card[data-id="l8"]`);
    if (card && !flag[8]) {
        card.classList.toggle('on', status);
    }
}


//-----Sending-----

function switchLightOFF() {
    const NUM_LIGHTS = 7; // adjust to your setup
    const switchStateOf = Array(NUM_LIGHTS).fill(true);
    const stateLight = Array(NUM_LIGHTS).fill(false);
    stompClient.send("/app/client/lightSwitch", {}, JSON.stringify({switchStateOf, stateLight}));
    for (let i = 0; i < 8; i++) {
        flag[i] = true;
        setTimeout(() => {
            flag[i] = false;
        }, "1000");
    }
}

function switchLightON() {
    const NUM_LIGHTS = 7; // adjust to your setup
    const switchStateOf = Array(NUM_LIGHTS).fill(true);
    const stateLight = Array(NUM_LIGHTS).fill(true);
    stompClient.send("/app/client/lightSwitch", {}, JSON.stringify({switchStateOf, stateLight}));
    for (let i = 0; i < 8; i++) {
        flag[i] = true;
        setTimeout(() => {
            flag[i] = false;
        }, "1000");
    }
}

function switchLight(id, state) {
    if (id < 7) {
        const NUM_LIGHTS = 7; // adjust to your setup
        const switchStateOf = Array(NUM_LIGHTS).fill(false);
        const stateLight = Array(NUM_LIGHTS).fill(false);
        switchStateOf[id] = true;
        stateLight[id] = state;
        stompClient.send("/app/client/lightSwitch", {}, JSON.stringify({switchStateOf, stateLight}));
    } else if (id === 7) {
        if (state) stompClient.send("/app/client/kitchenStrip", {}, JSON.stringify({
            'command': 4,
            'duty': 255,
            'time': 4
        }));
        else stompClient.send("/app/client/kitchenStrip", {}, JSON.stringify({'command': 4, 'duty': 0, 'time': 4}));
    } else if (id === 8) {
        if (state) stompClient.send("/app/client/bathroomStrip", {}, JSON.stringify({
            'command': 4,
            'duty': 255,
            'time': 4
        }));
        else stompClient.send("/app/client/bathroomStrip", {}, JSON.stringify({'command': 4, 'duty': 0, 'time': 4}));
    }
    flag[id] = true;
    setTimeout(() => {
        flag[id] = false;
    }, "1000");
}

function updateDimmer(ele, slider, fade) {
    if (ele < 7) {
        stompClient.send("/app/client/lightDimmer", {}, JSON.stringify({'name': ele, 'value': slider, 'fade': fade}));
    } else if (ele === 7) {
        stompClient.send("/app/client/kitchenStrip", {}, JSON.stringify({'command': 4, 'duty': slider, 'time': fade}));
    } else if (ele === 8) {
        stompClient.send("/app/client/bathroomStrip", {}, JSON.stringify({'command': 4, 'duty': slider, 'time': fade}));
    }
}