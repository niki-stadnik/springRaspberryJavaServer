package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.cameras;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.RebootDevice;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.Base64;


@Slf4j
@RequiredArgsConstructor
@Service
public class ContinuousCaptureService {


    private final VideoRecorderService videoRecorderService;
    private final GifCreator gifCreator;
    private final SimpMessageSendingOperations messaging;
    private final RebootDevice rebootDevice;


    private static final String STREAM_URL = "http://192.168.88.54:8081/"; // your MJPEG stream
    private static final boolean SAVE_EVERY_FRAME = true; // set true to save every frame (can be huge!)

    private volatile boolean running = true;
    private Thread captureThread;

    public static Frame frame;

    @PostConstruct
    public void startCapture() {
        running = true;
        captureThread = new Thread(this::captureLoop, "MotionEye-Capture-Thread");
        captureThread.start();
    }

    public volatile static byte[] imageBytes;

    private void captureLoop() {
        //stops the spam of: "[swscaler @ 000001fd1dcd5d80] deprecated pixel format used, make sure you did set range correctly"
        avutil.av_log_set_level(avutil.AV_LOG_ERROR);

        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(STREAM_URL)) {
            grabber.start();
            log.info("Started capturing from {}", STREAM_URL);

            //Frame frame;
            while (running && (frame = grabber.grabImage()) != null) {
                //log.info("Frame captured");
                if (SAVE_EVERY_FRAME) {
                    fpsCount++;
                    fpmCount++;
                    videoRecorderService.setImage(frame);

                    imageBytes = FrameToBytes.frameToJpegBytes(frame);
                    //String imageString = Base64.getEncoder().encodeToString(imageBytes);
                    //messaging.convertAndSend("/topic/imageDoorCam", "{\"image\":\"" + imageString + "\" }");
                }
            }
            log.info("Stopped capturing from {}", STREAM_URL);
        } catch (Exception e) {
            log.info("Url not reachable: {}", STREAM_URL);
            //e.printStackTrace();
        }
    }

    @PreDestroy
    public void stopCapture() {
        running = false;
        if (captureThread != null) {
            captureThread.interrupt();
        }
    }

    private static int  fpsCount = 0;
    private static int  fpmCount = 0;
    private static int fps = 0;
    private static int fpm = 0;

    private static int resCounter = 3;
    private static int resInterval = 3; //minutes without frame before reset

    private static boolean resFlag = false;

    @Scheduled(fixedRate = 1000)
    private synchronized void fpsCounter() {
        fps = fpsCount;
        fpsCount = 0;
        //log.info("fps: {}", fps);
        messaging.convertAndSend("/topic/frameRate", "{\"fps\":" + fps + ",\"fpm\":" + fpm + " }");
    }

    @Scheduled(fixedRate = 60000)
    private synchronized void fpmCounter() {
        fpm = fpmCount;
        fpmCount = 0;
        //log.info("fpm: {}", fpm);
        //log.info("fpmCount: {}", fpmCount);

        //Self Reboot Logic
        if (fpm == 0) resCounter--;
        else resCounter = resInterval;
        if (resCounter ==0) {
                rebootDevice.rebootDev(RebootDevice.destination.DOORMAN);
        }
    }

    @Scheduled(initialDelay = 60000, fixedRate = 7000)
    private synchronized void restartCapture() {
        if (fpmCount == 0) {
            if (resFlag) {
                resFlag = false;
                log.info("Restart capture...");
                stopCapture();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                startCapture();
                log.info("Restart capture done...");
            }else resFlag = true;
        }else resFlag = false;
    }
}