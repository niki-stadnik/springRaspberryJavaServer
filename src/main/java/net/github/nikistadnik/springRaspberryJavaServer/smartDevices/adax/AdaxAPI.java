package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.adax;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.github.scribejava.apis.openid.OpenIdJsonTokenExtractor;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.json.JSONArray;
import org.json.JSONObject;

import jakarta.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdaxAPI {
    @Value("${adax.apiUrl}")
    private String apiUrl;
    @Value("${adax.clientId}")
    private String clientId; // replace with your client ID (see Adax WiFi app, Account Section)
    @Value("${adax.clientSecret}")
    private String clientSecret; // replace with your client secret (see Adax WiFi app, Account Section -> Third party integrations -> Remote user client API)

    private String token;


    private boolean coolDown;

    private JSONObject tempBedroom;

    protected final SimpMessageSendingOperations messaging;

    @PostConstruct
    private void init() throws Exception {
        token = getToken();
    }

    public void command(AdaxAPIModel data) {
        // Change the temperature to 24 C in the room with an Id of 196342
        // Replace the 196342 with the room id from the getHomesInfo output
        int temp = data.getTemp();
        int id = data.getId();
        coolDown = true;
        if (id == 161132) tempBedroom.put("targetTemperature", temp / 100);
        updateClient();
        changeTemp(id, temp);
    }

    private void changeTemp(int id, int temp) { //room id for Bedroom is 161132
        try {
            setRoomTargetTemperature(id, temp, token);  //temp is 4 digit 23* is 2300
        } catch (Exception e) {
            System.out.println("exep");
            try {
                Thread.sleep(2500);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            changeTemp(id, temp);
        }
    }

    /**
     * Sets target temperature of the room
     *
     * @throws IOException
     */
    private void setRoomTargetTemperature(int roomId, int temperature, String token) throws Exception {
        String postData = "{ \"rooms\": [{ \"id\": " + roomId + ", \"targetTemperature\": " + temperature + " }] }";

        HttpsURLConnection connection = (HttpsURLConnection) (new URL(apiUrl + "/rest/v1/control/").openConnection());
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json");

        byte[] input = postData.getBytes("utf-8");
        connection.setFixedLengthStreamingMode(input.length);

        connection.connect();
        connection.getOutputStream().write(input);
        connection.getOutputStream().close();

        String response = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
        connection.getInputStream().close();
        System.out.println(response);
    }

    public void updateClient() {
        if (tempBedroom != null) {
            //SendMessage.sendMessage("/topic/adax", String.valueOf(tempBedroom));
            messaging.convertAndSend("/topic/adax", String.valueOf(tempBedroom));
        }
    }


    @Scheduled(fixedRate = 600000)  //every 10 minutes
    //@Scheduled(fixedRate = 60000)  //every 1 minutes
    private void getData() {
        try {
            getHomesInfo(token);
            coolDown = false;
        } catch (Exception e) {     //when heater is off (--) it gives exception
            //System.out.println("exep..");
            JSONObject jo = new JSONObject();
            jo.put("roomName", "Bedroom");
            jo.put("roomId", 161132);
            jo.put("targetTemperature", "--");
            jo.put("currentTemperature", "--");
            String dataOut = jo.toString();
            System.out.println(dataOut);
            if (!coolDown) {
                        tempBedroom = jo;
                        //SendMessage.sendMessage("/topic/adax", dataOut);
                        messaging.convertAndSend("/topic/adax", dataOut);

            }
            coolDown = false;
        }
    }

    private void getHomesInfo(String token) throws Exception {
        HttpsURLConnection connection = (HttpsURLConnection) (new URL(apiUrl + "/rest/v1/content/").openConnection());

        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        connection.connect();

        String response = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
        connection.getInputStream().close();

        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("rooms");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject roomData = jsonArray.getJSONObject(i);

            String roomName = roomData.getString("name");
            int roomId = roomData.getInt("id");
            int targetTemperature = roomData.getInt("targetTemperature") / 100;
            double currentTemperature = 0;

            if (!roomData.isNull("temperature")) {
                currentTemperature = roomData.getDouble("temperature") / 100;
            }

            //send to client
            JSONObject jo = new JSONObject();
            jo.put("roomName", roomName);
            jo.put("roomId", roomId);
            jo.put("targetTemperature", targetTemperature);
            jo.put("currentTemperature", currentTemperature);
            String dataOut = jo.toString();
            System.out.println(dataOut);


            if (!coolDown) {
                switch (roomId) {
                    case 161132:    //bedroom
                        tempBedroom = jo;
                        //SendMessage.sendMessage("/topic/adax", dataOut);
                        messaging.convertAndSend("/topic/adax", dataOut);
                        break;
                }
            }

            //double test = roomData.getDouble("temperature");
            //System.out.println(test);
            //System.out.println("Room: " + roomName + ", Target: " + targetTemperature + ", Temperature: " + currentTemperature + ", id: " + roomId);
            //System.out.println(String.format("Room: %15s, Target: %5d, Temperature: %5d, id: %5d", roomName, targetTemperature, currentTemperature, roomId));
        }
    }

    /**
     * Authenticate and obtain JWT token
     */
    private String getToken() throws Exception {
        DefaultApi20 api = new DefaultApi20() {
            public String getAccessTokenEndpoint() {
                return apiUrl + "/auth/token";
            }

            public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
                return OpenIdJsonTokenExtractor.instance();
            }

            protected String getAuthorizationBaseUrl() {
                throw new UnsupportedOperationException();
            }

            public String getRevokeTokenEndpoint() {
                throw new UnsupportedOperationException();
            }
        };

        OAuth20Service service = new ServiceBuilder(this.clientId).apiSecret(this.clientSecret).build(api);

        return service.getAccessTokenPasswordGrant(this.clientId, this.clientSecret).getAccessToken();
    }
}
