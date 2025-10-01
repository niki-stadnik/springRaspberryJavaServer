package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.cameras;

import java.io.*;
import java.net.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.SendMessage;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class DoorCamV2 {

    @PostConstruct
    public void init() {
        String cameraUrl = "http://192.168.88.54/picture/1/frame/";

        try {
            URL url = new URL(cameraUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.setConnectTimeout(5000); // Set a timeout of 10 seconds
            connection.setReadTimeout(5000);

            // Check if the response code is OK
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                // Start a separate thread to continuously read data from the input stream
                new Thread(() -> sendStreamToServer(inputStream)).start();
            } else {
                System.err.println("Failed to connect to camera stream. Response code: " + connection.getResponseCode());
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to continuously read data from the input stream and send it to the web app server
    private void sendStreamToServer(InputStream inputStream) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            System.out.println("here?");
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                System.out.println("here1?");
                // Convert the current buffer to Base64 and send it to the web app server
                String base64Data = Base64.getEncoder().encodeToString(outputStream.toByteArray());
                SendMessage.sendMessage("/topic/imageDoorCamV2", base64Data);
                // Reset the output stream for the next chunk of data
                outputStream.reset();
            }
            System.out.println("here2?");
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}