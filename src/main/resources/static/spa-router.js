(function () {
  'use strict';

  const PAGES_DIR = 'pages/';
  const DEFAULT   = 'home';
  const area      = document.getElementById('pageArea');
  const loader    = document.getElementById('pageLoader');

  const cache  = {};
  let current  = null;

  const wait = ms => new Promise(r => setTimeout(r, ms));

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

    document.querySelectorAll('.nav-btn').forEach(btn =>
        btn.classList.toggle('active', btn.dataset.page === name));

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
  }

  document.querySelectorAll('.nav-btn[data-page]').forEach(btn =>
      btn.addEventListener('click', () => navigate(btn.dataset.page)));

  window.addEventListener('popstate', e =>
      navigate(e.state?.page ?? DEFAULT));

  navigate(DEFAULT);
  history.replaceState({ page: DEFAULT }, '', '#home');

  window.spaRouter = { navigate };
})();
