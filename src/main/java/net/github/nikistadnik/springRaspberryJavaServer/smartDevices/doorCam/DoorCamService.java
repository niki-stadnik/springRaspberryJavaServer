package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorCam;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class DoorCamService {

    public DoorCamClientModel setData(DoorCamModel data) throws IOException {
        byte[] imageData = data.getImageData();
        String base64ImageData = Base64.getEncoder().encodeToString(imageData);
        return new DoorCamClientModel(base64ImageData);
    }

}
