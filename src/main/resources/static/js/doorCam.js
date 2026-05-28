function initCameraViewer() {
    const overlay = document.getElementById('overlay');
    const fullImg = document.getElementById('fullImg');
    const cameraImg = document.querySelector('.camera-image');


    let scale = 1, startDist = 0, startScale = 1;
    let translateX = 0, translateY = 0;
    let startMidX = 0, startMidY = 0;
    let startTX = 0, startTY = 0;

    function applyTransform() {
        fullImg.style.transform = `translate(${translateX}px, ${translateY}px) scale(${scale})`;
    }

    function resetTransform() {
        scale = 1;
        translateX = 0;
        translateY = 0;
        fullImg.style.transition = 'transform .2s';
        applyTransform();
        setTimeout(() => fullImg.style.transition = '', 200);
    }

    function dist(t) {
        return Math.hypot(t[0].clientX - t[1].clientX, t[0].clientY - t[1].clientY);
    }

    function mid(t) {
        return {x: (t[0].clientX + t[1].clientX) / 2, y: (t[0].clientY + t[1].clientY) / 2};
    }

    overlay.addEventListener('touchstart', e => {
        if (e.touches.length === 2) {
            e.preventDefault();
            startDist = dist(e.touches);
            startScale = scale;
            const m = mid(e.touches);
            startMidX = m.x;
            startMidY = m.y;
            startTX = translateX;
            startTY = translateY;
        }
    }, {passive: false});

    overlay.addEventListener('touchmove', e => {
        if (e.touches.length === 2) {
            e.preventDefault();
            const d = dist(e.touches);
            scale = Math.min(5, Math.max(1, startScale * (d / startDist)));
            const m = mid(e.touches);
            translateX = startTX + (m.x - startMidX);
            translateY = startTY + (m.y - startMidY);
            applyTransform();
        }
    }, {passive: false});

    overlay.addEventListener('touchend', e => {
        if (e.touches.length < 2 && scale < 1.05) {
            resetTransform();
        }
    }, {passive: true});

    cameraImg.addEventListener('click', () => {
        overlay.style.display = 'flex';
        document.body.style.overflow = 'hidden';
    });

    overlay.addEventListener('click', () => {
        overlay.style.display = 'none';
        resetTransform();
        document.body.style.overflow = '';
    });

    document.addEventListener('keydown', e => {
        if (e.key === 'Escape' && overlay.style.display === 'flex') {
            overlay.style.display = 'none';
            resetTransform();
            document.body.style.overflow = '';
        }
    });
};