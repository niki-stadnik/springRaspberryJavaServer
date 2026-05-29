function initConfigCameraViewers() {

    let overlay = document.getElementById('overlay');
    if (overlay) {
        overlay.remove(); // remove it from its current trapped position
    }
    overlay = document.createElement('div');
    overlay.id = 'overlay';
    overlay.style.cssText = 'display:none; position:fixed; inset:0; background:rgba(0,0,0,0.92); z-index:9999; align-items:center; justify-content:center;';
    overlay.innerHTML = '<img id="fullImg" style="max-width:100%; max-height:100%; object-fit:contain;">';
    document.documentElement.appendChild(overlay);

    const fullImg = document.getElementById('fullImg');

    let scale = 1, startDist = 0, startScale = 1;
    let translateX = 0, translateY = 0;
    let startMidX = 0, startMidY = 0;
    let startTX = 0, startTY = 0;

    function applyTransform() {
        fullImg.style.transform = `translate(${translateX}px, ${translateY}px) scale(${scale})`;
    }

    function resetTransform() {
        scale = 1; translateX = 0; translateY = 0;
        fullImg.style.transition = 'transform .2s';
        applyTransform();
        setTimeout(() => fullImg.style.transition = '', 200);
    }

    function dist(t) { return Math.hypot(t[0].clientX - t[1].clientX, t[0].clientY - t[1].clientY); }
    function mid(t) { return { x: (t[0].clientX + t[1].clientX) / 2, y: (t[0].clientY + t[1].clientY) / 2 }; }

    overlay.addEventListener('touchstart', e => {
        if (e.touches.length === 2) {
            e.preventDefault();
            startDist = dist(e.touches);
            startScale = scale;
            const m = mid(e.touches);
            startMidX = m.x; startMidY = m.y;
            startTX = translateX; startTY = translateY;
        }
    }, { passive: false });

    overlay.addEventListener('touchmove', e => {
        if (e.touches.length === 2) {
            e.preventDefault();
            scale = Math.min(5, Math.max(1, startScale * (dist(e.touches) / startDist)));
            const m = mid(e.touches);
            translateX = startTX + (m.x - startMidX);
            translateY = startTY + (m.y - startMidY);
            applyTransform();
        }
    }, { passive: false });

    overlay.addEventListener('touchend', e => {
        if (e.touches.length < 2 && scale < 1.05) resetTransform();
    }, { passive: true });

    document.getElementById('camList').addEventListener('click', e => {
        const img = e.target.closest('.camera-image');
        if (!img) return;
        fullImg.src = img.src;
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
}