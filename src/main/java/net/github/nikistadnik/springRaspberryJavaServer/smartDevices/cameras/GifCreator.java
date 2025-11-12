package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.cameras;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.LinkedList;

import org.bytedeco.javacv.FFmpegLogCallback;

import javax.imageio.ImageIO;

import static org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_GIF;


@Slf4j
@RequiredArgsConstructor
@Service
public class GifCreator {

    public static LinkedList<byte[]> frames = new LinkedList<byte[]>();
    private static int secondsLong = 6;
    private static int fps = 4;

    @Scheduled(fixedRate = 500)
    public synchronized void makeList (){
        if (ContinuousCaptureService.imageBytes == null) return;
        frames.add(ContinuousCaptureService.imageBytes);
        //log.info("added frame {}", ContinuousCaptureService.frame);
        while (frames.size() > secondsLong * fps){
            frames.removeFirst();
        }

    }

    public static ByteArrayResource createGifResource() throws Exception {
        FFmpegLogCallback.set();
        if (frames.isEmpty()) throw new IllegalArgumentException("Frame list is empty");

        // Take a thread-safe snapshot
        LinkedList<byte[]> framesCopy;
        synchronized (GifCreator.class) {
            framesCopy = new LinkedList<>(frames);
        }

        //log.info("Creating gif with {} frames", framesCopy.size());
        //log.info("frames: {}", framesCopy);
        //log.info("first frame: {}", framesCopy.getFirst().toString());


        File tempFile = File.createTempFile("animation", ".gif");

        Java2DFrameConverter converter = new Java2DFrameConverter();
        // decode first JPEG for size
        BufferedImage firstImage = ImageIO.read(new ByteArrayInputStream(framesCopy.getFirst()));
        int width = firstImage.getWidth();
        int height = firstImage.getHeight();

        try (FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(tempFile.getAbsolutePath(), width, height)) {
            recorder.setFormat("gif");
            recorder.setFrameRate(fps);
            recorder.setVideoCodec(AV_CODEC_ID_GIF);
            recorder.setPixelFormat(avutil.AV_PIX_FMT_BGR8);
            recorder.start();

            for (byte[] jpegBytes : framesCopy) {
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(jpegBytes));
                Frame frame = converter.convert(img);
                recorder.record(frame);
            }

            recorder.stop();
        }

        byte[] bytes = java.nio.file.Files.readAllBytes(tempFile.toPath());
        tempFile.delete();

        // Turn byte array into a Spring resource for multipart/form-data
        return new ByteArrayResource(bytes) {
            @Override
            public String getFilename() {
                return "animation.gif"; // Discord needs a name
            }
        };
    }

}
