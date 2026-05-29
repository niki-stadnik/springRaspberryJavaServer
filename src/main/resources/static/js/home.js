/* ── CONFIG ──
link to weather api
* https://open-meteo.com/en/docs?latitude=42.6975&longitude=23.3241&timezone=auto&hourly=temperature_2m,precipitation_probability,precipitation,wind_speed_10m,weather_code&daily=weather_code,temperature_2m_max,temperature_2m_min,rain_sum,showers_sum,snowfall_sum,precipitation_probability_max,precipitation_sum&forecast_days=1*/
const MJPEG_URL  = "";
const API_URL    = "https://api.open-meteo.com/v1/forecast?latitude=42.6975&longitude=23.3241&daily=weather_code,temperature_2m_max,temperature_2m_min,rain_sum,showers_sum,snowfall_sum,precipitation_probability_max,precipitation_sum&hourly=temperature_2m,precipitation_probability,precipitation,wind_speed_10m,weather_code&timezone=auto&forecast_days=1";
const REFRESH_MS = 15 * 60 * 1000;
const TARGET_HRS = [9, 12, 15, 18, 21];

/* ── HELPERS ── */
const DAYS   = ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];
const MONTHS = ["January","February","March","April","May","June","July","August","September","October","November","December"];
function pad(n) { return String(n).padStart(2,"0"); }

/* ── WMO ── */
const WMO = {
    0:["Clear sky","☀️"],1:["Mainly clear","🌤️"],2:["Partly cloudy","⛅"],3:["Overcast","☁️"],
    45:["Foggy","🌫️"],48:["Icy fog","🌫️"],51:["Light drizzle","🌦️"],53:["Drizzle","🌦️"],
    55:["Heavy drizzle","🌧️"],61:["Slight rain","🌧️"],63:["Rain","🌧️"],65:["Heavy rain","🌧️"],
    71:["Slight snow","🌨️"],73:["Snow","🌨️"],75:["Heavy snow","❄️"],77:["Snow grains","🌨️"],
    80:["Slight showers","🌦️"],81:["Showers","🌦️"],82:["Heavy showers","⛈️"],
    85:["Snow showers","🌨️"],86:["Heavy snow showers","❄️"],
    95:["Thunderstorm","⛈️"],96:["Thunderstorm+hail","⛈️"],99:["Thunderstorm+hail","⛈️"]
};
function wmo(c) { return WMO[c] || ["Unknown","🌡️"]; }
function fmt(n, d=1) { return (n===null||n===undefined) ? "—" : Number(n).toFixed(d); }

/* ── STATE ── */
let clockTimer   = null;
let countdownTimer = null;
let weatherTimer = null;
let nextAt       = 0;

/* ── CLOCK ── */
function tick() {
    const el = document.getElementById("wxClock");
    if (!el) return;
    const n = new Date();
    el.textContent = `${pad(n.getHours())}:${pad(n.getMinutes())}:${pad(n.getSeconds())}`;
    const d = document.getElementById("wxDate");
    if (d) d.innerHTML = "<span class='wx-date-day'>" + DAYS[n.getDay()] + "</span><span class='wx-date-rest'>&nbsp;" + n.getDate() + " " + MONTHS[n.getMonth()] + "</span>";
}

function startClock() {
    clearInterval(clockTimer);
    tick();
    clockTimer = setInterval(tick, 1000);
}

/* ── COUNTDOWN ── */
function startCountdown() {
    clearInterval(countdownTimer);
    countdownTimer = setInterval(() => {
        const el = document.getElementById("wxNext");
        if (!el) {
            clearInterval(countdownTimer); // Clean up if we left the home page
            return;
        }
        const left = Math.max(0, nextAt - Date.now());
        el.textContent = `${pad(Math.floor(left/60000))}:${pad(Math.floor((left%60000)/1000))}`;
    }, 1000);
}

/* ── RENDER ── */
function render(data) {
    const container = document.getElementById("hourlyRows");
    if (!container) return;

    const d = data.daily, h = data.hourly;
    const [cond, icon] = wmo(d.weather_code[0]);

    const set = (id, html, prop='innerHTML') => { const el = document.getElementById(id); if (el) el[prop] = html; };

    set("wxIcon",  icon, 'textContent');
    set("wxMax",   `${fmt(d.temperature_2m_max[0],0)}<span>°C</span>`);
    set("wxMin",   `Low ${fmt(d.temperature_2m_min[0],0)}°`, 'textContent');
    set("wxCond",  cond, 'textContent');
    set("wsPre",   `${fmt(d.precipitation_probability_max[0])}<span class="u">%&nbsp;&nbsp;&nbsp;</span>`);
    set("wsTotal", `${fmt(d.precipitation_sum[0])}<span class="u">mm</span>`);

    const maxWind = Math.max(...h.wind_speed_10m.filter(v => v !== null));
    set("wsWind", `${fmt(maxWind,0)}<span class="u">km/h</span>`);

    const prob = d.precipitation_probability_max[0] || 0;
    set("probFill", `${prob}%`, 'style.width'); // won't work via set(), do directly:
    const pf = document.getElementById("probFill");
    if (pf) pf.style.width = `${prob}%`;
    set("probNum", `${prob}%`, 'textContent');

    const rain = d.rain_sum[0]||0, shower = d.showers_sum[0]||0, snow = d.snowfall_sum[0]||0;
    const tot  = rain + shower + snow;
    if (tot > 0) {
        const pr = rain/tot*100, ps = shower/tot*100, pn = snow/tot*100;
        ["barR","barS","barN"].forEach((id,i) => {
            const el = document.getElementById(id);
            if (el) el.style.width = `${[pr,ps,pn][i]}%`;
        });
        set("pctR", `${Math.round(pr)}%`, 'textContent');
        set("pctS", `${Math.round(ps)}%`, 'textContent');
        set("pctN", `${Math.round(pn)}%`, 'textContent');
    } else {
        const barR = document.getElementById("barR");
        if (barR) { barR.style.width = "100%"; barR.style.opacity = "0.12"; }
        ["pctR","pctS","pctN"].forEach(id => set(id, "0%", 'textContent'));
    }

    container.innerHTML = "";
    TARGET_HRS.forEach(hr => {
        const idx = h.time.findIndex(t => new Date(t).getHours() === hr);
        if (idx === -1) return;
        const temp   = h.temperature_2m[idx];
        const precip = h.precipitation[idx];
        const wind   = h.wind_speed_10m[idx];
        const pp     = h.precipitation_probability[idx] || 0;
        const [, hicon] = wmo(h.weather_code[idx]);
        const card = document.createElement("div");
        card.className = "hour-card";
        card.innerHTML = `
            <div class="hc-time">${pad(hr)}:00</div>
            <div class="hc-icon">${hicon}</div>
            <div class="hc-temp">${fmt(temp,0)}<span>°</span></div>
            <div class="hc-cell"><span class="hc-k"><svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round" style="vertical-align:middle;margin-right:2px"><path d="M12 22a7 7 0 0 0 7-7c0-4.3-7-9-7-9s-7 4.7-7 9a7 7 0 0 0 7 7z"/></svg>Precip</span><span class="hc-v ${precip>0?'wet':''}">${fmt(precip)}mm</span></div>
            <div class="hc-cell"><span class="hc-k">Rain%</span><span class="hc-v">${pp}%</span></div>
            <div class="hc-cell"><span class="hc-k"><svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round" style="vertical-align:middle;margin-right:2px"><path d="M9.59 4.59A2 2 0 1 1 11 8H2m10.59 11.41A2 2 0 1 0 14 16H2m15.73-8.27A2.5 2.5 0 1 1 19.5 12H2"/></svg>Wind</span><span class="hc-v">${fmt(wind,0)}km/h</span></div>`;
        container.appendChild(card);
    });
}

/* ── FETCH ── */
async function fetchWeather() {
    try {
        const res = await fetch(API_URL);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data = await res.json();
        localStorage.setItem("wxcache", JSON.stringify({ data, ts: Date.now() }));
        render(data);
        const el = document.getElementById("wxError");
        if (el) el.style.display = "none";
        const n = new Date();
        const fetched = document.getElementById("wxFetched");
        if (fetched) fetched.textContent = `${pad(n.getHours())}:${pad(n.getMinutes())}`;
        const status = document.getElementById("wxStatus");
        if (status) status.textContent = "Live · auto-refresh";
        nextAt = Date.now() + REFRESH_MS;
    } catch(e) {
        const cached = JSON.parse(localStorage.getItem("wxcache")||"null");
        const err = document.getElementById("wxError");
        if (cached) {
            render(cached.data);
            if (err) err.textContent = `⚠ Cached — ${Math.round((Date.now()-cached.ts)/60000)}min ago (${e.message})`;
            const status = document.getElementById("wxStatus");
            if (status) status.textContent = "Cached data";
        } else {
            if (err) err.textContent = `⚠ ${e.message}`;
            const status = document.getElementById("wxStatus");
            if (status) status.textContent = "Error";
        }
        if (err) err.style.display = "block";
    } finally {
        const overlay = document.getElementById("loadingOverlay");
        if (overlay) overlay.style.display = "none";
    }
}

/* ── CAMERA ── */
function restartCamera() {
    const oldCam = document.getElementById('homeScreenCam');
    if (!oldCam) return;
    const newCam = oldCam.cloneNode(true);
    oldCam.replaceWith(newCam);
}

/* ── INIT HOME PAGE ── */
function initHome() {
    // Clear everything before re-initialising
    clearInterval(clockTimer);
    clearInterval(countdownTimer);
    clearInterval(weatherTimer);

    startClock();
    startCountdown();
    initCameraViewer();

    fetchWeather();
    weatherTimer = setInterval(() => {
        fetchWeather();
        nextAt = Date.now() + REFRESH_MS;
    }, REFRESH_MS);
}

/* ── LISTEN FOR PAGE LOAD ── */
// Fires when the router injects a page for the first time
document.addEventListener('pageinit', e => {
    if (e.target.id === 'view-home') initHome();
});

// Also fires when navigating BACK to home (view already exists, pageinit won't fire again)
document.addEventListener('pageshow', e => {
    if (e.detail?.page === 'home') initHome();
    handlePageShow();
});
document.addEventListener("visibilitychange", () => {
    if (document.visibilityState === "visible") handlePageShow();
});

function handlePageShow() {
    console.log("stompClient:", stompClient, socketConnectionFlag);
    if(stompClient && socketConnectionFlag) {
        console.log("App resumed. Checking server connection...");
        if (!stompClient || !stompClient.connected) {
            console.log("STOMP is not connected. Reconnecting...");
            connect();
            restartCamera();
        } else console.log("Server is alive! Restarting camera feed...");
    }
}
