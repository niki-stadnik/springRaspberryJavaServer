(function () {

    /* ══════════════════════════════════════════════════
       TILE DEFINITIONS
       - id     : matches <div id="cpanel-{id}">
       - link   : set instead of id to open a URL directly
       - status : 'active' = glowing dot
    ══════════════════════════════════════════════════ */
    const TILES = [
        {
            id:'bathroom', label:'Баня', sub:'24° · 80%', status:'active',
            icon:`<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
        <path d="M4 12h16M4 12V8a4 4 0 0 1 8 0M4 12v5a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2v-5"/>
        <path d="M10 17v2M14 17v2"/></svg>`
        },
        {
            id:'devices', label:'Устройства', sub:'6 онлайн', status:'active',
            icon:`<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
        <rect x="2" y="3" width="20" height="14" rx="2"/><path d="M8 21h8M12 17v4"/>
        <path d="M6 8h.01M6 11h.01"/><path d="M10 8h8M10 11h4"/></svg>`
        },
        {
            id:'cameras', label:'Камери', sub:'1 активна', status:'active',
            icon:`<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
        <path d="M15 10l4.553-2.069A1 1 0 0 1 21 8.87v6.26a1 1 0 0 1-1.447.894L15 14M3 8a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8z"/></svg>`
        },
        {
            id:'herbpot', label:'Саксии', sub:'Босилек · Мента', status:'',
            icon:`<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
        <path d="M12 22V12M12 12C12 12 7 10 7 5a5 5 0 0 1 10 0c0 5-5 7-5 7z"/>
        <path d="M5 22h14"/></svg>`
        },
        {
            id:'doorlock', label:'Брава', sub:'Заключена', status:'',
            icon:`<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
        <rect x="3" y="11" width="18" height="11" rx="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/>
        <circle cx="12" cy="16" r="1" fill="currentColor"/></svg>`
        },
        {
            /* CONSOLE: no panel, just opens a URL */
            id:'console', label:'Конзола', sub:'Отвори →', status:'active',
            link:'/pages/console.html',
            icon:`<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
        <rect x="2" y="6" width="20" height="12" rx="3"/>
        <path d="M6 10v4M4 12h4M14 11h.01M16 13h.01"/></svg>`
        },
    ];



    /* ══════════════════════════════════════════════════
       CAMERAS  — add objects here for more cameras
    ══════════════════════════════════════════════════ */
    const CAMERAS = [
        { id:'cam1', name:'Входна врата', location:'CAM-01 · Exterior',stream:'/camera.frame', fps:24, fpm:1440 , link:'http://192.168.88.54/' },
    ];

    /* ══════════════════════════════════════════════════
       INIT
    ══════════════════════════════════════════════════ */
    function initControls(root) {
        const $  = s => root.querySelector(s);
        const $$ = s => root.querySelectorAll(s);

        const grid    = $('#ctrlGrid');
        const backBar = $('#ctrlBackBar');
        const backBtn = $('#ctrlBackBtn');
        const title   = $('#ctrlPanelTitle');
        let   current = null;

        /* ── Build tiles ── */
        TILES.forEach(tile => {
            const el = document.createElement('div');
            el.className = 'ctrl-tile';
            el.dataset.status = tile.status || '';
            el.innerHTML = `
        <div class="ctrl-tile-icon">${tile.icon}<span class="ctrl-tile-dot"></span></div>
        <div class="ctrl-tile-label">${tile.label}</div>
        ${tile.sub ? `<div class="ctrl-tile-sub">${tile.sub}</div>` : ''}`;
            el.addEventListener('click', () => {
                if (tile.link) { window.location.href = tile.link; return; }
                openPanel(tile);
            });
            grid.appendChild(el);
        });

        /* ── Navigation ── */
        function openPanel(tile) {
            if (current) $('#cpanel-' + current).classList.remove('visible');
            current = tile.id;
            title.textContent = tile.label;
            grid.classList.add('hidden');
            backBar.classList.add('visible');
            requestAnimationFrame(() => $('#cpanel-' + tile.id).classList.add('visible'));
        }

        backBtn.addEventListener('click', () => {
            if (!current) return;
            $('#cpanel-' + current).classList.remove('visible');
            grid.classList.remove('hidden');
            backBar.classList.remove('visible');
            current = null;
        });

        /* ── Generic toggle switches ── */
        $$('[data-sw]').forEach(sw => sw.addEventListener('click', () => sw.classList.toggle('on')));

        /* ════════════════
           BATHROOM
        ════════════════ */
/*
        const fanBtns = $$('.cp-fan-btn');
        const autoCfg = $('#bth-auto-cfg');
        fanBtns.forEach(btn => {
            btn.addEventListener('click', () => {
                fanBtns.forEach(b => b.classList.remove('active'));
                btn.classList.add('active');
                //autoCfg.style.display = btn.dataset.fan === 'auto' ? '' : 'none';
            });
        });

        const bthSave = $('#bth-save-btn');
        if (bthSave) {
            bthSave.addEventListener('click', () => {
                bthSave.textContent = 'Запазено ✓';
                setTimeout(() => bthSave.textContent = 'Запази', 1800);
            });
        }


 */




        /* ════════════════
           DEVICES
        ════════════════ */
        const devList = $('#devList');
        DEVICES.forEach(dev => {
            const row = document.createElement('div');
            row.className = 'cp-toggle-row';
            row.innerHTML = `
        <div>
          <div class="cp-toggle-label" style="display:flex;align-items:center;gap:8px">
            <span class="cp-conn-dot ${dev.on ? 'online' : 'offline'}"></span>${dev.name}
          </div>
        </div>
        <button class="cp-btn small" onclick="rebootDevice('${dev.name}')">Reboot</button>
         <div class="cp-section">Auto<br>Manage</div>
        <div class="cp-sw ${dev.on ? 'on' : ''}" data-sw></div>`;
            const sw = row.querySelector('[data-sw]');
            const dot = row.querySelector('.cp-conn-dot');
            dev.dot = dot;
            dev.sw = sw;
            sw.addEventListener('click', () => {
                sw.classList.toggle('on');
                toggleRebootMonitoring( dev.name, sw.classList.contains('on'));
            });
            devList.appendChild(row);
        });



        /* ════════════════
           CAMERAS
        ════════════════ */
        const camList = $('#camList');
        CAMERAS.forEach(cam => {
            const wrap = document.createElement('div');
            wrap.setAttribute('data-cam', cam.id);
            wrap.innerHTML = `
        <div class="cp-section">${cam.name}</div>
        <div class="cp-cam">
          <div class="cp-cam-rec"></div>
          <span>${cam.location}</span>
          <img class="camera-image" src="${cam.stream}" alt="Live Camera Stream">
        </div>
        <div class="cp-cam-indicators">
          <div class="cp-cam-ind">
            <span class="cp-cam-ind-val" data-fps>${cam.fps}</span>
            <span class="cp-cam-ind-lbl">FPS</span>
          </div>
          <div class="cp-cam-ind">
            <span class="cp-cam-ind-val" data-fpm>${cam.fpm}</span>
            <span class="cp-cam-ind-lbl">FPM</span>
          </div>
          <button class="cp-cam-action-btn" onclick="window.location.href='${cam.link}';">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" width="18" height="18">
              <circle cx="12" cy="12" r="3"/>
              <path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 1 1-2.83 2.83l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 1 1-4 0v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 1 1-2.83-2.83l.06-.06A1.65 1.65 0 0 0 4.68 15a1.65 1.65 0 0 0-1.51-1H3a2 2 0 1 1 0-4h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 1 1 2.83-2.83l.06.06A1.65 1.65 0 0 0 9 4.68a1.65 1.65 0 0 0 1-1.51V3a2 2 0 1 1 4 0v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 1 1 2.83 2.83l-.06.06A1.65 1.65 0 0 0 19.4 9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 1 1 0 4h-.09a1.65 1.65 0 0 0-1.51 1z"/>
            </svg>
            Настройки
          </button>
        </div>`;
            camList.appendChild(wrap);
        });

        /* ════════════════
           HERB POT
        ════════════════ */
        const lightSw  = $('#light-sw');
        const lightTxt = $('#light-status-txt');
        if (lightSw) {
            lightSw.addEventListener('click', () => {
                lightTxt.textContent = lightSw.classList.contains('on') ? 'Включена' : 'Изключена';
                herbLight();
            });
        }

        const waterSw  = $('#water-sw');
        const waterTxt = $('#water-status-txt');
        if (waterSw) {
            waterSw.addEventListener('click', () => {
                waterTxt.textContent = waterSw.classList.contains('on') ? 'Активно' : 'Изключено';
                water();
            });
        }

        ['light-submit', 'water-submit'].forEach(id => {
            const btn = $('#' + id);
            if (!btn) return;
            btn.addEventListener('click', () => {
                const orig = btn.textContent;
                btn.textContent = 'Запазено ✓';
                setTimeout(() => btn.textContent = orig, 1800);
            });
        });



        /* ════════════════
           DOOR LOCK
        ════════════════ */
        const lockIcon  = $('#lockIcon');
        const lockState = $('#lockState');
        const lockSub   = $('#lockSub');
        const unlockBtn = $('#unlockBtn');
        const lockBtn   = $('#lockBtn');

        function setLock(locked) {
            if (locked) {
                lockIcon.style.color  = 'rgba(0,255,200,0.75)';
                lockIcon.style.filter = 'drop-shadow(0 0 12px rgba(0,255,200,0.5))';
                lockState.textContent = 'ЗАКЛЮЧЕНА';
                lockState.style.color = 'rgba(0,255,200,0.85)';
                lockSub.textContent   = 'Последна промяна: точно сега';
                lockBtn.disabled      = true;
                unlockBtn.disabled    = false;
            } else {
                lockIcon.style.color  = 'rgba(255,130,80,0.85)';
                lockIcon.style.filter = 'drop-shadow(0 0 12px rgba(255,100,60,0.5))';
                lockState.textContent = 'ОТКЛЮЧЕНА';
                lockState.style.color = 'rgba(255,150,80,0.95)';
                lockSub.textContent   = 'Последна промяна: точно сега';
                lockBtn.disabled      = false;
                unlockBtn.disabled    = true;
            }
        }

        if (unlockBtn) {
            unlockBtn.addEventListener('click', () => setLock(false));
            lockBtn.addEventListener('click',   () => setLock(true));
            setLock(true);
        }
    }

    /* ══════════════════════════════════════════════════
       BOOTSTRAP
    ══════════════════════════════════════════════════ */
    function boot() {
        const spaRoot = document.getElementById('view-controls');
        const root    = spaRoot || document;
        if (root.querySelector('#ctrlGrid')) initControls(root);
        getDeviceStatuses();
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', boot);
    } else {
        boot();
    }

})();