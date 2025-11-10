package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.cameras;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
public class CameraStreamController {

    private static final String BOUNDARY = "boundarydonotcross";


    @GetMapping(value = "/camera.frame", produces = "multipart/x-mixed-replace;boundary=" + BOUNDARY)
    public void streamFrame(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, private");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Connection", "keep-alive");
        response.setContentType("multipart/x-mixed-replace; boundary=" + BOUNDARY);

        try (ServletOutputStream out = response.getOutputStream()) {

            while (true) {
                    //byte[] imageBytes = DoorCam.imageBytes; // latest JPEG bytes
                    byte[] imageBytes = ContinuousCaptureService.imageBytes;

                    if (imageBytes != null && imageBytes.length > 0) {
                        out.write(("--" + BOUNDARY + "\r\n").getBytes());
                        out.write("Content-Type: image/jpeg\r\n".getBytes());
                        out.write(("Content-Length: " + imageBytes.length + "\r\n\r\n").getBytes());
                        out.write(imageBytes);
                        out.write("\r\n".getBytes());
                        out.flush();
                    }

                Thread.sleep(30); // ~33ms = ~30 FPS
            }

        } catch (Exception e) {
            //e.printStackTrace();
            log.info("Failed to get output stream: {}", e.getMessage());
        }
    }
}

