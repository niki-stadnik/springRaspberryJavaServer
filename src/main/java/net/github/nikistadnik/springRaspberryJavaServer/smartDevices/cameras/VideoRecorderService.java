package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.cameras;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.RebootDevice;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
@Service
public class VideoRecorderService {


    Path primaryPath = Paths.get("/media/DoorCam");
    Path fallbackPath = Paths.get("C:/Users/nikis/Videos/Captures");

    private static Path chooseExistingPath(Path primary, Path fallback) {
        if (Files.exists(primary)) {
            return primary;
        } else if (Files.exists(fallback)) {
            return fallback;
        } else {
            throw new IllegalStateException("Neither primary nor fallback file exists or is readable.");
        }
    }

    public VideoRecorderService() {
        outputPath = String.valueOf(chooseExistingPath(primaryPath, fallbackPath));
        log.info("Using: {}", outputPath);
    }

    // === Configurable parameters ===
    @Setter
    private double targetFps = 30.0;               // reference fps (for video header) 24
    @Setter
    private long videoLengthMs = 600_000_000;           // 60_000_000 = 1min  //now 10 min 600_000_000
    @Setter @Getter
    public static String outputPath;

    private FFmpegFrameRecorder recorder;
    private AtomicBoolean recording = new AtomicBoolean(false);
    private long startTime = 0; // in epoch ms
    private long lastTimestamp = 0;
    private long secondKeeper = 0;
    private long counter = 0;
    private long counter2 = 0;

    private FrameConverter<Mat> converter = new OpenCVFrameConverter.ToMat();

    private int frameWidthCheck = 0;
    private int frameHeightCheck = 0;



    private synchronized String generateNewFilename() {
        String timestamp = java.time.LocalDateTime.now()
                .toString()
                .replace(":", "-")
                .replace("T", "_T_")
                .replace(".", "_");
        return outputPath + "/doorcam_" + timestamp + ".mp4";
    }



    public synchronized void startRecording(int width, int height) throws FrameRecorder.Exception {
        if (recording.get()) return;

        frameWidthCheck = width;
        frameHeightCheck = height;

        String fileName = generateNewFilename();
        recorder = new FFmpegFrameRecorder(fileName, width, height);
        recorder.setFormat("mp4");
        recorder.setVideoCodecName("libx264");
        recorder.setFrameRate(targetFps);
        recorder.setVideoBitrate(2_000_000);
        recorder.start();

        startTime = System.nanoTime() / 1000;
        lastTimestamp = 0;
        log.info("startTime: {} ", startTime);
        recording.set(true);

        log.info("Recording started: {} ", outputPath);
    }


    public synchronized void stopRecording() {
        if (!recording.get()) return;
        try {
            recorder.stop();
            recorder.release();
            log.info("Recording stopped: {} ", outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            recording.set(false);
        }
    }


 //   public synchronized void setImage(byte[] imageBytes) {
    public synchronized void setImage(Frame frame) {


        try {
            /*
            Mat mat = opencv_imgcodecs.imdecode(new Mat(imageBytes), opencv_imgcodecs.IMREAD_COLOR);
            if (mat == null || mat.empty()) {
                log.info("Skipped invalid image frame");
                return;
            }

            Frame frame = converter.convert(mat);
             */

            if (!recording.get()) {
                log.info("image receivet but not recording");
                startRecording(frame.imageWidth, frame.imageHeight);
                return;
            }

            long currentTime = System.nanoTime() / 1000;
            //log.info("last time: {} ", lastTimestamp);
            long timestampUs = (currentTime - startTime); // microseconds
            //log.info("timestamp: {} ", timestampUs);

            //if (timestampUs <= lastTimestamp + 41800) {

            if (timestampUs > secondKeeper + 1_000_000) {
                secondKeeper += 1_000_000;
                counter = 0;
                //log.info("counter2: {} ", counter2);
                counter2 = 0;
                if (counter != counter2){
                    log.info("counter not equal to counter2: {} : {}", counter, counter2);
                }
            }
            counter++;
            //log.info("counter: {} ", counter);

            if (timestampUs <= lastTimestamp + 33333) {
                if (timestampUs > currentTime-startTime + 60000) {
                    long diff = timestampUs - (currentTime-startTime);
                    log.info("dropped frame. diff:  {} microseconds\n\n\n", diff);
                    return;
                }
                timestampUs = lastTimestamp + 33333;
                    //log.info("new timestamp:             {} ", timestampUs);

                //}else {
                    //log.info("dropped: {} \n", timestampUs);
                    //return;
                //}
            }



            if (frameWidthCheck != frame.imageWidth || frameHeightCheck != frame.imageHeight) {
                log.info("image size wrong: {} ", timestampUs);
            }

            recorder.setTimestamp(timestampUs);
            if (recording.get()) {
                //log.info("recording: {} ", timestampUs);
                counter2++;
                recorder.record(frame);
            }

            lastTimestamp = timestampUs;

            // Stop after reaching max video length
            if ((currentTime - startTime) >= videoLengthMs) {
                log.info("Starting new recording...");
                new Thread(() -> {
                    synchronized (this) {
                        stopRecording();
                        try {
                            // Small delay ensures FFmpeg releases file handles properly
                            Thread.sleep(300);
                            startRecording(frame.imageWidth, frame.imageHeight);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // === Getters/Setters for configuration ===

    public boolean isRecording() {
        return recording.get();
    }
}