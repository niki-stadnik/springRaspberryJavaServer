(function () {

    /* ══════════════════════════════════════════════════
       TILE DEFINITIONS
       - id     : matches <div id="cpanel-{id}">
       - link   : set instead of id to open a URL directly
       - status : 'active' = glowing dot
    ══════════════════════════════════════════════════ */
    const TILES = [{
        id: 'bathroom', label: 'Баня', sub: '24° · 80%', status: 'active', icon: `<svg viewBox="0 0 24 24" fill="none">
        <path fill-rule="evenodd" clip-rule="evenodd" d="M12 3.48154C7.29535 3.48154 3.48148 7.29541 3.48148 12.0001C3.48148 16.7047 7.29535 20.5186 12 20.5186C16.7046 20.5186 20.5185 16.7047 20.5185 12.0001C20.5185 7.29541 16.7046 3.48154 12 3.48154ZM2 12.0001C2 6.47721 6.47715 2.00006 12 2.00006C17.5228 2.00006 22 6.47721 22 12.0001C22 17.5229 17.5228 22.0001 12 22.0001C6.47715 22.0001 2 17.5229 2 12.0001Z"/>
<path d="M12 11.3C11.8616 11.3 11.7262 11.3411 11.6111 11.418C11.496 11.4949 11.4063 11.6042 11.3533 11.7321C11.3003 11.86 11.2864 12.0008 11.3134 12.1366C11.3405 12.2724 11.4071 12.3971 11.505 12.495C11.6029 12.5929 11.7277 12.6596 11.8634 12.6866C11.9992 12.7136 12.14 12.6997 12.2679 12.6467C12.3958 12.5937 12.5051 12.504 12.582 12.3889C12.6589 12.2738 12.7 12.1385 12.7 12C12.7 11.8144 12.6262 11.6363 12.495 11.505C12.3637 11.3738 12.1857 11.3 12 11.3ZM12.35 5.00002C15.5 5.00002 15.57 7.49902 13.911 8.32502C13.6028 8.50778 13.3403 8.75856 13.1438 9.05822C12.9473 9.35787 12.8218 9.69847 12.777 10.054C13.1117 10.1929 13.4073 10.4116 13.638 10.691C16.2 9.29102 19 9.84401 19 12.35C19 15.5 16.494 15.57 15.675 13.911C15.4869 13.6029 15.232 13.341 14.9291 13.1448C14.6262 12.9485 14.283 12.8228 13.925 12.777C13.7844 13.1108 13.566 13.406 13.288 13.638C14.688 16.221 14.128 19 11.622 19C8.5 19 8.423 16.494 10.082 15.668C10.3852 15.4828 10.644 15.2332 10.84 14.9368C11.036 14.6404 11.1644 14.3046 11.216 13.953C10.8729 13.8188 10.5711 13.5967 10.341 13.309C7.758 14.695 5 14.149 5 11.65C5 8.50002 7.478 8.42302 8.304 10.082C8.48945 10.3888 8.74199 10.6496 9.04265 10.8448C9.34332 11.0399 9.68431 11.1645 10.04 11.209C10.1748 10.8721 10.3971 10.5772 10.684 10.355C9.291 7.80001 9.844 5.00002 12.336 5.00002H12.35Z"/>
        </svg>`
    }, {
        id: 'devices', label: 'Устройства', sub: '6 онлайн', status: 'active', icon: `<svg viewBox="0 0 496 496" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
        <path d="M92.688,0L48,44.688V80H0v112h48v128H0v64h48v112h448V0H92.688z M16,176V96h96v80H16z M16,368v-32h96v32H16z M480,480H64
				v-96h64v-24h16v-16h-16v-24H64V192h64v-16h16v-16h-16v-48h16V96h-16V80H64V51.312L99.312,16H480V480z"/>
			<path d="M304,448h112v-16h16v-16h-16v-16h16v-16h-16v-16h16v-16h-16v-16h16v-16h-16v-16h16v-16h-16v-16h16v-16h-16v-16h16v-16
				h-16v-16h16v-16h-16v-16h16v-16h-16v-16h16v-16h-16v-16h16V96h-16V80h16V64h-16V48H304v16h-16v16h16v16h-16v16h16v16h-16v16h16
				v16h-16v16h16v16h-16v16h16v16h-16v16h16v16h-16v16h16v16h-16v16h16v16h-16v16h16v16h-16v16h16v16h-16v16h16v16h-16v16h16V448z
				 M320,64h80v368h-80V64z"/>
			<circle cx="360" cy="88" r="8"/>
			<path d="M256,64h-64v48h64V64z M240,96h-32V80h32V96z"/>
			<path d="M256,144h-64v48h64V144z M240,176h-32v-16h32V176z"/>
			<path d="M144,216v16h-16v-16h-16v16H96v48h16v16h16v-16h16v16h16v-16h16v16h16v-16h16v-48h-16v-16h-16v16h-16v-16H144z M192,248
				v16h-80v-16H192z"/>
			<path d="M112,400c-17.648,0-32,14.352-32,32s14.352,32,32,32s32-14.352,32-32S129.648,400,112,400z M112,448
				c-8.824,0-16-7.176-16-16c0-8.824,7.176-16,16-16c8.816,0,16,7.176,16,16C128,440.824,120.816,448,112,448z"/>
			<path d="M192,400c-17.648,0-32,14.352-32,32s14.352,32,32,32s32-14.352,32-32S209.648,400,192,400z M192,448
				c-8.824,0-16-7.176-16-16c0-8.824,7.176-16,16-16c8.816,0,16,7.176,16,16C208,440.824,200.816,448,192,448z"/>
			<rect x="176" y="336" width="16" height="32"/>
			<rect x="208" y="336" width="16" height="32"/>
			<rect x="240" y="336" width="16" height="32"/>
			<circle cx="96" cy="48" r="8"/>
        </svg>`
    }, {
        id: 'cameras', label: 'Камери', sub: '1 активна', status: 'active', icon: `<svg viewBox="0 0 510.453 510.453" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
<path d="M495.12,152.145c-1.6-5.312-5.84-9.408-11.184-10.848L80.752,33.185c-4.08-1.088-8.464-0.528-12.144,1.6
			c-3.68,2.112-6.368,5.616-7.456,9.712L20.656,195.649c-2.288,8.544,2.768,17.312,11.312,19.6l300.784,80.656
			c5.28,1.424,10.688,2.144,16.032,2.144c16.064,0,31.488-6.448,44.256-18.976l98.752-111.728
			C495.456,163.185,496.72,157.441,495.12,152.145z M369.84,257.041c-7.904,7.728-18.48,10.704-28.8,7.952L55.696,188.481
			L87.92,68.225l362.768,97.28L369.84,257.041z"/>
			<path d="M508.304,179.185c-2.112-3.68-5.616-6.368-9.712-7.456l-33.632-8.992c-8.592-2.352-17.312,2.8-19.6,11.312
			c-2.288,8.56,2.784,17.312,11.312,19.6l18.176,4.864l-19.904,74.288l-70.048-18.784c-8.512-2.32-17.312,2.768-19.6,11.312
			c-2.288,8.544,2.768,17.312,11.312,19.6l85.504,22.928c1.36,0.352,2.752,0.544,4.144,0.544c2.768,0,5.536-0.736,8-2.176
			c3.68-2.112,6.368-5.616,7.456-9.712l28.192-105.184C511.008,187.233,510.432,182.849,508.304,179.185z"/>
			<path d="M56.544,333.809H16c-8.832,0-16,7.152-16,16v112c0,8.848,7.168,16,16,16h40.544C96,477.809,96,438.689,96,425.825v-51.68
			C96,349.249,89.392,333.809,56.544,333.809z M64,425.825c0,19.984-2.8,19.984-7.456,19.984H32v-80h24.544
			c3.52,0,5.776,0.192,7.168,0.384c0.16,1.504,0.288,3.952,0.288,7.952V425.825z"/>
			<path d="M217.92,248.865c-8.8-1.664-17.04,4.112-18.656,12.816l-25.248,136.128H96c-8.832,0-16,7.152-16,16
			c0,8.848,7.168,16,16,16h91.328c7.696,0,14.32-5.504,15.728-13.088l27.68-149.2C232.336,258.833,226.608,250.481,217.92,248.865z"
			/>
</svg>`
    }, {
        id: 'herbpot', label: 'Саксии', sub: 'Босилек · Мента', status: '', icon: `<svg viewBox="0 0 512.034 512.034" fill="none" stroke="currentColor" stroke-width="1.9" stroke-linecap="round" stroke-linejoin="round">
        <path d="M412.457,314.914H251.863c1.862-3.609,3.995-7.984,6.24-13.026c0.765-0.389,1.559-0.879,2.461-1.48
			c3.413-3.413,7.68-9.387,12.8-15.36c9.953-12.441,22.329-28.087,36.335-37.575c42.372-0.589,73.473-10.843,92.519-31.545
			c23.893-23.893,23.04-56.32,23.04-68.267v-2.56c0-5.12-3.413-8.533-8.533-8.533h-1.707c-49.493-0.853-75.947,15.36-90.453,29.013
			c-17.149,17.149-26.459,40.564-27.231,69.469c-8.948,6.523-16.922,14.579-24.04,22.835
			c10.637-42.479,13.597-99.851-17.887-158.339c-0.399-30.632-9.738-55.51-27.268-72.205c-13.653-12.8-38.4-28.16-82.773-27.307
			h-1.707c-5.12,0-7.68,10.24-7.68,10.24c-0.853,11.947-1.707,46.08,23.893,71.68c17.533,18.368,45.691,27.745,81.267,28.141
			c49.99,95.567,0.922,188.432-8.829,204.819H97.577c-11.947,0-22.187,10.24-22.187,22.187v7.68
			c0,11.947,10.24,22.187,22.187,22.187h5.483l45.609,128H92.457c-5.12,0-8.533,3.413-8.533,8.533s3.413,8.533,8.533,8.533h62.293
			h199.68h53.76c5.12,0,8.533-3.413,8.533-8.533s-3.413-8.533-8.533-8.533h-47.742l45.137-128h6.018
			c11.947,0,22.187-10.24,23.04-22.187v-7.68C434.644,325.154,424.404,314.914,412.457,314.914z M336.51,177.527
			c11.093-11.093,32.427-23.893,71.68-23.893c0,12.8-1.707,34.133-17.067,51.2c-14.258,13.507-35.118,22.392-63.734,24.92
			c11.132-10.467,24.743-21.612,33.014-24.92c5.12-1.707,6.827-6.827,5.12-11.093c-1.707-5.12-6.827-6.827-11.093-5.12
			c-10.58,4.232-25.747,16.66-37.783,27.937C319.543,200.351,326.43,187.608,336.51,177.527z M152.19,17.101
			c34.133,0.853,52.907,11.947,64,22.187c9.161,9.16,15.246,20.958,18.601,35.363c-10.945-11.054-22.611-20.067-35.667-27.683
			c-4.267-1.707-9.387-0.853-11.947,3.413c-1.707,4.267-0.853,9.387,3.413,11.947c13.708,8.1,25.591,18.03,36.656,30.102
			c-24.015-2.208-42.687-9.679-55.429-22.422C154.75,52.941,152.19,29.901,152.19,17.101z M166.697,494.967l-25.6-75.947
			l6.827,5.973c3.413,2.56,13.653,10.24,30.72,10.24c8.533,0,15.36-4.267,21.333-8.533c2.56-2.56,5.12-4.267,7.68-5.12
			c13.653-5.12,17.067-4.267,23.893,2.56l33.28,34.987c5.973,6.827,16.213,11.093,28.16,10.24c10.24,0,20.48-4.267,29.867-11.947
			l12.8-11.093c3.413-2.56,7.68-3.413,11.947-0.853l10.24,5.973l-15.36,43.52H166.697z M363.347,435.921l-8.916-4.954
			c-9.387-5.973-22.187-5.12-31.573,2.56l-12.8,11.093c-4.267,3.413-11.093,8.533-19.627,8.533c-5.973,0-11.947-1.707-14.507-5.12
			l-33.28-35.84c-14.507-13.653-26.453-12.8-42.667-5.973c-4.267,1.707-7.68,4.267-11.093,6.827
			c-4.267,3.413-8.533,5.973-11.947,5.973c-11.093,0-17.067-5.12-19.627-6.827l-28.776-25.234l-7.064-19.992h266.24L363.347,435.921
			z M417.577,344.781c0,2.56-2.56,5.12-5.12,5.12h-0.853H97.577c-2.56,0-5.12-2.56-5.12-4.267v-7.68c0-6.827,5.12-5.973,5.12-5.973
			h314.88c2.56,0,5.12,2.56,5.12,5.12V344.781z"/></svg>`
    }, {
        id: 'doorlock', label: 'Брава', sub: 'Заключена', status: '', icon: `<svg viewBox="0 0 24 24" fill="none">
        <path d="M12,13a1.49,1.49,0,0,0-1,2.61V17a1,1,0,0,0,2,0V15.61A1.49,1.49,0,0,0,12,13Zm5-4V7A5,5,0,0,0,7,7V9a3,3,0,0,0-3,3v7a3,3,0,0,0,3,3H17a3,3,0,0,0,3-3V12A3,3,0,0,0,17,9ZM9,7a3,3,0,0,1,6,0V9H9Zm9,12a1,1,0,0,1-1,1H7a1,1,0,0,1-1-1V12a1,1,0,0,1,1-1H17a1,1,0,0,1,1,1Z"/>
        </svg>`
    }, {
        id: 'applinks', label: 'Приложения', sub: 'Линкове', status: '', icon: `<svg viewBox="0 0 502 502" fill="none">
        <g>
	<g>
		<path d="M176.062,232.224H54.963C24.656,232.224,0,207.567,0,177.261V56.162C0,25.855,24.656,1.199,54.963,1.199h121.099
			c30.307,0,54.963,24.656,54.963,54.963v121.099C231.025,207.567,206.369,232.224,176.062,232.224z M54.963,21.199
			C35.684,21.199,20,36.884,20,56.162v121.099c0,19.278,15.684,34.963,34.963,34.963h121.099c19.279,0,34.963-15.685,34.963-34.963
			V56.162c0-19.278-15.684-34.963-34.963-34.963H54.963z"/>
	</g>
	<g>
		<path d="M46.9,182c-5.523,0-10-4.478-10-10v-23c0-5.522,4.477-10,10-10s10,4.478,10,10v23C56.9,177.522,52.423,182,46.9,182z"/>
	</g>
	<g>
		<path d="M46.9,122c-5.523,0-10-4.478-10-10V65.1c0-14.888,12.112-27,27-27H86c5.523,0,10,4.478,10,10s-4.477,10-10,10H63.9
			c-3.86,0-7,3.141-7,7V112C56.9,117.522,52.423,122,46.9,122z"/>
	</g>
	<g>
		<path d="M447.037,232.224H325.938c-30.307,0-54.963-24.656-54.963-54.963V56.162c0-30.307,24.656-54.963,54.963-54.963h121.099
			C477.344,1.199,502,25.855,502,56.162v121.099C502,207.567,477.344,232.224,447.037,232.224z M325.938,21.199
			c-19.279,0-34.963,15.685-34.963,34.963v121.099c0,19.278,15.684,34.963,34.963,34.963h121.099
			c19.279,0,34.963-15.685,34.963-34.963V56.162c0-19.278-15.684-34.963-34.963-34.963H325.938z"/>
	</g>
	<g>
		<path d="M176.062,500.801H54.963C24.656,500.801,0,476.145,0,445.838V324.739c0-30.307,24.656-54.963,54.963-54.963h121.099
			c30.307,0,54.963,24.656,54.963,54.963v121.099C231.025,476.145,206.369,500.801,176.062,500.801z M54.963,289.776
			C35.684,289.776,20,305.461,20,324.739v121.099c0,19.278,15.684,34.963,34.963,34.963h121.099
			c19.279,0,34.963-15.685,34.963-34.963V324.739c0-19.278-15.684-34.963-34.963-34.963H54.963z"/>
	</g>
	<g>
		<path d="M447.037,500.801H325.938c-30.307,0-54.963-24.656-54.963-54.963V324.739c0-30.307,24.656-54.963,54.963-54.963h121.099
			c30.307,0,54.963,24.656,54.963,54.963v121.099C502,476.145,477.344,500.801,447.037,500.801z M325.938,289.776
			c-19.279,0-34.963,15.685-34.963,34.963v121.099c0,19.278,15.684,34.963,34.963,34.963h121.099
			c19.279,0,34.963-15.685,34.963-34.963V324.739c0-19.278-15.684-34.963-34.963-34.963H325.938z"/>
	</g>
</g>
        </svg>`
    }, {
        /* CONSOLE: no panel, just opens a URL */
        id: 'console', label: 'Конзола', sub: 'Отвори →', status: 'active', link: '/pages/console.html', icon: `<svg viewBox="0 0 36 36" fill="none">
        <path d="M32,5H4A2,2,0,0,0,2,7V29a2,2,0,0,0,2,2H32a2,2,0,0,0,2-2V7A2,2,0,0,0,32,5ZM4,7H32V9.2H4ZM4,29V10.8H32V29Z" class="clr-i-outline clr-i-outline-path-1"></path><rect x="17" y="23" width="6" height="2" class="clr-i-outline clr-i-outline-path-2"></rect><polygon points="7 15.68 13.79 18.8 7 21.91 7 24.11 16.6 19.7 16.6 17.89 7 13.48 7 15.68" class="clr-i-outline clr-i-outline-path-3"></polygon>
    <rect x="0" y="0" width="36" height="36" fill-opacity="0"/>
    </svg>`
    },];


    /* ══════════════════════════════════════════════════
       CAMERAS  — add objects here for more cameras
    ══════════════════════════════════════════════════ */
    const CAMERAS = [{
        id: 'cam1',
        name: 'Входна врата',
        location: 'CAM-01 · Exterior',
        stream: '/camera.frame',
        fps: 24,
        fpm: 1440,
        link: 'http://192.168.88.54/'
    },];

    /* ══════════════════════════════════════════════════
   CAMERA VIEWER (cameras panel)
   No overlay is created here. The overlay lives in
   spa-router.js and is initialised once at app boot.
   This function only wires the delegated click
   trigger on the #camList container.

   Called once from boot() below — it is NOT called
   on every panel open, so there is no risk of
   duplicate listeners accumulating over time.
══════════════════════════════════════════════════ */
    function initConfigCameraViewers(root) {
        const camList = root.querySelector('#camList');
        if (!camList) return;

        camList.addEventListener('click', e => {
            const img = e.target.closest('.camera-image');
            if (img) window.openCameraOverlay(img.src);
        });
    }

    /* ══════════════════════════════════════════════════
       INIT
    ══════════════════════════════════════════════════ */
    function initControls(root) {
        const $ = s => root.querySelector(s);
        const $$ = s => root.querySelectorAll(s);

        const grid = $('#ctrlGrid');
        const backBar = $('#ctrlBackBar');
        const backBtn = $('#ctrlBackBtn');
        const title = $('#ctrlPanelTitle');
        let current = null;

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
                if (tile.link) {
                    window.location.href = tile.link;
                    return;
                }
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
            // Push a history entry so back gesture is interceptable
            history.pushState({ page: 'controls', panel: tile.id }, '', '#controls');
        }

        function closePanel() {
            if (!current) return false;
            $('#cpanel-' + current).classList.remove('visible');
            grid.classList.remove('hidden');
            backBar.classList.remove('visible');
            current = null;
            return true;
        }

        backBtn.addEventListener('click', () => {
            closePanel();
            // Push state back so the router's history buffer stays consistent
            history.pushState({ page: 'controls' }, '', '#controls');
        });
        // Expose to the router so it can delegate back presses
        window.__controlsHandleBack = () => closePanel();

        /* ── Generic toggle switches ── */
        $$('[data-sw]').forEach(sw => sw.addEventListener('click', () => sw.classList.toggle('on')));

        /* ════════════════
           BATHROOM
        ════════════════ */

        document.getElementById('bth-hum-min').addEventListener('input', function(e) {
            updateFanTriggers(e.target.value, 'minHum1');
        });
        document.getElementById('bth-hum-max').addEventListener('input', function(e) {
            updateFanTriggers(e.target.value, 'maxHum1');
        });
        document.getElementById('bth-hum-min2').addEventListener('input', function(e) {
            updateFanTriggers(e.target.value, 'minHum2');
        });
        document.getElementById('bth-hum-max2').addEventListener('input', function(e) {
            updateFanTriggers(e.target.value, 'maxHum2');
        });
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
                toggleRebootMonitoring(dev.name, sw.classList.contains('on'));
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
        <div class="cp-cam">
          <div class="cp-cam-rec"></div>
          <span>${cam.location}</span>
          <img class="camera-image" src="${cam.stream}" alt="Live Camera Stream">
          <div class="cp-cam-top">
  <span class="cp-section">${cam.name}</span>
  <div style="display:flex; gap:6px; margin-left:auto;">
  <div class="cp-cam-ind cp-cam-ind--overlay">
    <span class="cp-cam-ind-val" data-fps-live>–</span>
    <span class="cp-cam-ind-lbl">FPS</span>
  </div>
  <div class="cp-cam-ind cp-cam-ind--overlay">
      <span class="cp-cam-ind-val" data-fpm-live>–</span>
      <span class="cp-cam-ind-lbl">FPM</span>
    </div>
    </div>
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
          </div>
        </div>`;
            camList.appendChild(wrap);
            const img = wrap.querySelector('.camera-image');
            img.addEventListener('load', () => {
                wrap.querySelector('.cp-cam').style.aspectRatio = `${img.naturalWidth} / ${img.naturalHeight}`;
            });


            // REAL FPS COUNTER
            img.addEventListener('load', () => {
                wrap.querySelector('.cp-cam').style.aspectRatio = `${img.naturalWidth} / ${img.naturalHeight}`;
            });
            startFrameCounter(wrap);
        });

        /* ════════════════
           HERB POT
        ════════════════ */
        const lightSw = $('#light-sw');
        const lightTxt = $('#light-status-txt');
        if (lightSw) {
            lightSw.addEventListener('click', () => {
                lightTxt.textContent = lightSw.classList.contains('on') ? 'Включена' : 'Изключена';
                herbLight();
            });
        }

        const waterSw = $('#water-sw');
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
        const lockIcon = $('#lockIcon');
        const lockState = $('#lockState');
        const lockSub = $('#lockSub');
        const unlockBtn = $('#unlockBtn');
        const lockBtn = $('#lockBtn');

    }

    /* ══════════════════════════════════════════════════
       BOOTSTRAP
    ══════════════════════════════════════════════════ */
    function boot() {
        const spaRoot = document.getElementById('view-controls');
        const root = spaRoot || document;
        if (root.querySelector('#ctrlGrid')) {
            initControls(root);
            initConfigCameraViewers(root);
        }
        getDeviceStatuses();
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', boot);
    } else {
        boot();
    }

})();