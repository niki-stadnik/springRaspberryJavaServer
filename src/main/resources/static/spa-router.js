(function () {
  'use strict';

  const PAGES_DIR = 'pages/';
  const DEFAULT   = 'home';
  const area      = document.getElementById('pageArea');
  const loader    = document.getElementById('pageLoader');

  const cache  = {};
  let current  = null;

  const wait = ms => new Promise(r => setTimeout(r, ms));

  /* ══════════════════════════════════════════════════
    SHARED CAMERA OVERLAY
    Created once here — before any page loads — so
    neither home.js nor controls.js can accidentally
    destroy and recreate it, which was the root cause
    of the cameras-screen viewer dying after a home
    navigation.
 ══════════════════════════════════════════════════ */
  (function initOverlay() {
    const overlay = document.createElement('div');
    overlay.id = 'overlay';
    overlay.style.cssText = [
      'display:none',
      'position:fixed',
      'inset:0',
      'background:rgba(0,0,0,0.92)',
      'z-index:9999',
      'align-items:center',
      'justify-content:center',
    ].join(';');
    overlay.innerHTML = `
      <img id="fullImg" style="max-width:100%;max-height:100%;object-fit:contain;">
      <div style="position:absolute;bottom:24px;left:0;right:0;text-align:center;
                  font-family:monospace;font-size:.7rem;color:rgba(255,255,255,.3);
                  letter-spacing:.1em;">
        Pinch to zoom &nbsp;·&nbsp; Tap to close
      </div>`;
    document.documentElement.appendChild(overlay);

    const fullImg = document.getElementById('fullImg');

    // ── Pinch-zoom state ──
    let scale = 1, startDist = 0, startScale = 1;
    let translateX = 0, translateY = 0;
    let startMidX  = 0, startMidY  = 0;
    let startTX    = 0, startTY    = 0;

    function applyTransform() {
      fullImg.style.transform = `translate(${translateX}px,${translateY}px) scale(${scale})`;
    }
    function resetTransform() {
      scale = 1; translateX = 0; translateY = 0;
      fullImg.style.transition = 'transform .2s';
      applyTransform();
      setTimeout(() => fullImg.style.transition = '', 200);
    }
    function dist(t) {
      return Math.hypot(t[0].clientX - t[1].clientX, t[0].clientY - t[1].clientY);
    }
    function mid(t) {
      return { x: (t[0].clientX + t[1].clientX) / 2, y: (t[0].clientY + t[1].clientY) / 2 };
    }

    overlay.addEventListener('touchstart', e => {
      if (e.touches.length !== 2) return;
      e.preventDefault();
      startDist  = dist(e.touches);
      startScale = scale;
      const m = mid(e.touches);
      startMidX = m.x; startMidY = m.y;
      startTX   = translateX; startTY = translateY;
    }, { passive: false });

    overlay.addEventListener('touchmove', e => {
      if (e.touches.length !== 2) return;
      e.preventDefault();
      scale = Math.min(5, Math.max(1, startScale * (dist(e.touches) / startDist)));
      const m = mid(e.touches);
      translateX = startTX + (m.x - startMidX);
      translateY = startTY + (m.y - startMidY);
      applyTransform();
    }, { passive: false });

    overlay.addEventListener('touchend', e => {
      if (e.touches.length < 2 && scale < 1.05) resetTransform();
    }, { passive: true });

    // Close on tap or Escape — registered once, never duplicated
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

    // ── Public helper used by both home.js and controls.js ──
    window.openCameraOverlay = function (src) {
      fullImg.src = src;
      overlay.style.display = 'flex';
      document.body.style.overflow = 'hidden';
    };
  })();


  /* ══════════════════════════════════════════════════
     ROUTER
  ══════════════════════════════════════════════════ */

  async function fetchPage(name) {
    if (cache[name]) return cache[name];
    loader.classList.add('show');
    try {
      const res = await fetch(`${PAGES_DIR}${name}.html`);
      cache[name] = res.ok ? await res.text()
          : `<div class="page-error">⚠ Failed to load "${name}"</div>`;
    } catch (e) {
      cache[name] = `<div class="page-error">⚠ ${e.message}</div>`;
    }
    loader.classList.remove('show');
    return cache[name];
  }

  async function navigate(name) {
    if (!name) name = DEFAULT;
    //if (name === current) return;
    const isSame = name === current;

    if (current && !isSame) {
      const old = document.getElementById(`view-${current}`);
      if (old) {
        old.classList.remove('visible');
        await wait(220);
        old.classList.remove('active');
      }
    }

    let view = document.getElementById(`view-${name}`);
    if (!view) {
      const html = await fetchPage(name);
      view = document.createElement('div');
      view.id        = `view-${name}`;
      view.className = 'page-view';
      view.innerHTML = html;
      area.appendChild(view);
      // Re-execute scripts (innerHTML doesn't run them)
      view.querySelectorAll('script').forEach(orig => {
        const s = document.createElement('script');
        [...orig.attributes].forEach(a => s.setAttribute(a.name, a.value));
        s.textContent = orig.textContent;
        orig.replaceWith(s);
      });
      view.dispatchEvent(new CustomEvent('pageinit', { bubbles: true }));
    }

    current = name;

    if (!isSame) {
      view.classList.add('active');
      requestAnimationFrame(() => {
        view.classList.add('visible');
      });
      history.pushState({ page: name }, '', `#${name}`);
    }

    // Fire pageshow so already-cached pages can re-init (pageinit only fires on first inject)
    document.dispatchEvent(new CustomEvent('pageshow', { detail: { page: name } }));


    // This must be the last thing:
    document.querySelectorAll('.nav-btn').forEach(btn =>
        btn.classList.toggle('active', btn.dataset.page === name));
  }

  document.querySelectorAll('.nav-btn[data-page]').forEach(btn =>
      btn.addEventListener('click', () => navigate(btn.dataset.page)));
/*
  window.addEventListener('popstate', e =>
      navigate(e.state?.page ?? DEFAULT));
 */
  window.addEventListener('popstate', e => {
    // Let the controls page handle its own back if it has a panel open
    if (typeof window.__controlsHandleBack === 'function' && window.__controlsHandleBack()) {
      return; // controls consumed the back press
    }
    // Otherwise: always go home unless already there
    if (current !== DEFAULT) {
      clearStuckHover();
      navigate(DEFAULT);
    }
    document.activeElement?.blur();
  });


  navigate(DEFAULT);
  history.replaceState({ page: DEFAULT }, '', '#home');

  window.spaRouter = { navigate };
})();


// Force browser to clear stuck hover by dispatching a touch on body
function clearStuckHover() {
  const t = document.createElement('div');
  t.style.cssText = 'position:fixed;top:0;left:0;width:1px;height:1px;';
  document.body.appendChild(t);
  t.dispatchEvent(new TouchEvent('touchstart', { bubbles: true }));
  t.dispatchEvent(new TouchEvent('touchend', { bubbles: true }));
  document.body.removeChild(t);
}