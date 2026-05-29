const CACHE = 'hud-v1';

const PRECACHE = [
  '/',
  '/colors.css',
  '/style.css',
  '/css/home.css',
  '/css/doorCam.css',
  '/css/lights.css',
  '/css/controls.css',
  '/css/console.css',
  '/spa-router.js',
  '/js/home.js',
  '/js/webSockets.js',
  '/js/lightsComs.js',
  '/js/lights.js',
  '/js/controlsComs.js',
  '/js/doorCam.js',
  '/js/allCams.js',
  '/js/controls.js',
  '/js/console.js',
  '/pages/home.html',
  '/pages/lights.html',
  '/pages/climate.html',
  '/pages/controls.html',
  '/pages/console.html',
  '/icons/icon-512.png',
  '/icons/icon-192.png',
  '/manifest.json',
];

self.addEventListener('install', e => {
  e.waitUntil(
    caches.open(CACHE).then(c => c.addAll(PRECACHE)).then(() => self.skipWaiting())
  );
});

self.addEventListener('activate', e => {
  e.waitUntil(
    caches.keys()
      .then(keys => Promise.all(keys.filter(k => k !== CACHE).map(k => caches.delete(k))))
      .then(() => self.clients.claim())
  );
});

self.addEventListener('fetch', e => {
  const { request } = e;
  const url = new URL(request.url);

  // Never intercept WebSocket upgrades or API calls
  if (request.headers.get('upgrade') === 'websocket') return;
  if (url.pathname.startsWith('/api/') || url.pathname.startsWith('/ws')) return;

  // webjars and external resources: network with cache fallback
  if (url.pathname.startsWith('/webjars/') || url.origin !== self.location.origin) {
    e.respondWith(
      fetch(request)
        .then(res => {
          const clone = res.clone();
          caches.open(CACHE).then(c => c.put(request, clone));
          return res;
        })
        .catch(() => caches.match(request))
    );
    return;
  }

  // SPA navigation: network-first, fallback to cached index
  if (request.mode === 'navigate') {
    e.respondWith(
      fetch(request)
        .catch(() => caches.match('/'))
    );
    return;
  }

  // Static assets: cache-first, update in background
  e.respondWith(
    caches.match(request).then(cached => {
      const network = fetch(request).then(res => {
        caches.open(CACHE).then(c => c.put(request, res.clone()));
        return res;
      });
      return cached || network;
    })
  );
});
