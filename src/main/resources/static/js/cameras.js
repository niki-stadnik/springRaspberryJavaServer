function restartCamera() {
    const oldCam = document.getElementById('homeScreenCam');
    if (!oldCam) return;
    const newCam = oldCam.cloneNode(true);
    // Re-attach the click listener on the fresh element
    newCam.addEventListener('click', () => window.openCameraOverlay(newCam.src));
    oldCam.replaceWith(newCam);
}
function restartControlsCamera() {
    const camList = document.getElementById('camList');
    if (!camList) return;
    camList.querySelectorAll('.camera-image').forEach(oldCam => {
        // Clear old intervals before replacing
        if (oldCam._intervals) oldCam._intervals.forEach(clearInterval);
        const newCam = oldCam.cloneNode(true);
        oldCam.replaceWith(newCam);
        // click is handled by delegation on #camList so no listener needed here
    });
    // Re-attach counters to each wrap after all images are replaced
    camList.querySelectorAll('[data-cam]').forEach(wrap => startFrameCounter(wrap));
}
function startFrameCounter(wrap) {
    const img = wrap.querySelector('.camera-image');
    if (!img) return;

    let frameCount = 0;
    let frameCountMin = 0;

    const canvas = document.createElement('canvas');
    canvas.width = 4;
    canvas.height = 4;
    const ctx = canvas.getContext('2d', { willReadFrequently: true });
    let lastPixel = '';

    const poll = setInterval(() => {
        try {
            ctx.drawImage(img, 0, 0, 4, 4);
            const pixel = ctx.getImageData(0, 0, 4, 4).data.toString();
            if (pixel !== lastPixel) {
                lastPixel = pixel;
                frameCount++;
                frameCountMin++;
            }
        } catch (e) {}
    }, 16);

    const fpsTick = setInterval(() => {
        const el = wrap.querySelector('[data-fps-live]');
        if (el) el.textContent = frameCount;
        frameCount = 0;
    }, 1000);

    const fpmTick = setInterval(() => {
        const el = wrap.querySelector('[data-fpm-live]');
        if (el) el.textContent = frameCountMin;
        frameCountMin = 0;
    }, 60000);

    // Store interval IDs on the element so we can clear them on next restart
    img._intervals = [poll, fpsTick, fpmTick];
}