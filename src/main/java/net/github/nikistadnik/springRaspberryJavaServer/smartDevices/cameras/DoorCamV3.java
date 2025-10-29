package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.cameras;

import lombok.RequiredArgsConstructor;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.RebootDevice;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SendMessage;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.lightSwitch.LightSwitchService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicInteger;

//@Service
@RequiredArgsConstructor
public class DoorCamV3 {

    private final RebootDevice rebootDevice;

    // Snapshot endpoint (kept as-is)
    private static final URI IMAGE_URI = URI.create("http://192.168.88.54/picture/1/current/");
    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);
    private static final Base64.Encoder B64 = Base64.getEncoder();

    // Single reusable HTTP client (connection pooling, keep-alive)
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(CONNECT_TIMEOUT)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .version(HttpClient.Version.HTTP_1_1)
            .build();

    // Frame counters (thread-safe)
    private final AtomicInteger fpsCount = new AtomicInteger(0);
    private final AtomicInteger fpmCount = new AtomicInteger(0);

    // Published metrics (volatile for safe reads from other threads)
    private volatile int fps = 0;
    private volatile int fpm = 0;

    private static int resCounter = 10;
    private static int resInterval = 10; //minutes without frame before reset

    // Simple error backoff to ease pressure when camera is unavailable
    private final AtomicInteger consecutiveErrors = new AtomicInteger(0);
    private volatile long nextAllowedAtMillis = 0L;

    @Scheduled(fixedDelay = 1) //fixedRate = 40 // ~25 fps target; use fixedDelay if camera is slow
    public void getImage() {
        final long now = System.currentTimeMillis();
        if (now < nextAllowedAtMillis) {
            return; // Backing off due to recent errors
        }

        final HttpRequest request = HttpRequest.newBuilder(IMAGE_URI)
                .timeout(REQUEST_TIMEOUT)
                .GET()
                .build();

        try {
            HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() == 200) {
                byte[] body = response.body();
                if (body != null && body.length > 0) {
                    String imageBase64 = B64.encodeToString(body);

                    // Count frame
                    fpsCount.incrementAndGet();
                    fpmCount.incrementAndGet();

                    // Compact JSON payload to reduce allocations
                    String payload = "{\"image\":\"" + imageBase64 + "\"}";
                    SendMessage.sendMessage("/topic/imageDoorCam", payload);

                    // Reset backoff on success
                    consecutiveErrors.set(0);
                    nextAllowedAtMillis = now;
                } else {
                    handleError("Empty body from camera");
                }
            } else {
                handleError("HTTP " + response.statusCode());
            }
        } catch (Exception ex) {
            handleError(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
    }

    @Scheduled(fixedRate = 1000)
    private void fpsCounter() {
        fps = fpsCount.getAndSet(0);
        String data = "{\"fps\":" + fps + ",\"fpm\":" + fpm + "}";
        SendMessage.sendMessage("/topic/frameRate", data);
    }

    @Scheduled(fixedRate = 60000)
    private void fpmCounter() {
        fpm = fpmCount.getAndSet(0);
        //Self Reboot Logic
        if (fpm == 0) resCounter--;
        else resCounter = resInterval;
        if (resCounter == 0) rebootDevice.rebootDev("rebootDoorman");
    }

    private void handleError(String reason) {
        int errs = consecutiveErrors.incrementAndGet();
        long backoffMs = Math.min(10_000L, (long) Math.pow(2, Math.min(errs, 6)) * 100L); // 100ms..10s
        nextAllowedAtMillis = System.currentTimeMillis() + backoffMs;
        // Optionally log at debug/trace to avoid noisy logs
        // log.debug("DoorCam fetch error ({}); backing off {} ms", reason, backoffMs);
    }
}