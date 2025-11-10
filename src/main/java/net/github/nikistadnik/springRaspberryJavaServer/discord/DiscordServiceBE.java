package net.github.nikistadnik.springRaspberryJavaServer.discord;

import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.cameras.ContinuousCaptureService;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorman.DoormanDoorBellEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;


@Slf4j
@Service
public class DiscordServiceBE {

    //private final DeviceRegistry deviceRegistry;
    private final WebClient client;

    public DiscordServiceBE(@Value("${discord.bot.token}") String botToken) {
        this.client = WebClient.builder()
                .baseUrl("https://discord.com/api/v10")
                .defaultHeader("Authorization", "Bot " + botToken)
                .build();
    }

    public void sendHardcodedMessage() {
        String channelId = "1435313713841442848";
        String content = "Hello from hardcoded Spring Boot service!";

        String imageUrl = "https://i.imgur.com/nhRH5Vu.jpeg"; // ← your image URL

        // Build payload as a Map (so WebClient converts it properly)
        Map<String, Object> embed = Map.of(
                "title", "Check out this picture!",
                "image", Map.of("url", imageUrl)
        );
        Map<String, Object> payload = Map.of(
                //"embeds", List.of(embed),
                "content", content
        );

        try {
            String response = client.post()
                    .uri("/channels/{channelId}/messages", channelId)
                    .bodyValue(payload)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, resp ->
                            resp.bodyToMono(String.class)
                                    .defaultIfEmpty("")
                                    .flatMap(body -> Mono.error(new DiscordApiException(
                                            Objects.requireNonNull(HttpStatus.resolve(resp.statusCode().value())), body)))
                    )
                    .bodyToMono(String.class)
                    .block();

            log.info("Discord response: {}", response);
        } catch (WebClientResponseException e) {
            log.error("Discord API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (DiscordApiException e) {
            log.info("Discord API error: {}", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @EventListener
    public void sendCamToDiscord(DoormanDoorBellEvent event) {
        String channelId = "1435643869197762683";
        String messageText = "Someone at the door!";
        if (event.held() > 0) messageText = "Someone at the door! Rang for: " + event.held() + " seconds";

        try {
            ByteArrayResource imageResource = new ByteArrayResource(ContinuousCaptureService.imageBytes) {
            //ByteArrayResource imageResource = new ByteArrayResource(DoorCam.imageBytes) {
                @Override
                public String getFilename() {
                    return "doorcam.jpg"; // required by Discord
                }
            };

            // Prepare payload (message text)
            Map<String, Object> payload = Map.of("content", messageText);

            // Send the request to Discord
            String response = client.post()
                    .uri("/channels/{channelId}/messages", channelId)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData("payload_json", payload)
                            .with("files[0]", imageResource))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, resp ->
                            resp.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new DiscordApiException(
                                            Objects.requireNonNull(HttpStatus.resolve(resp.statusCode().value())), body)))
                    )
                    .bodyToMono(String.class)
                    .block();

            log.info("Discord response: {}", response);
        } catch (WebClientResponseException e) {
            log.error("Discord API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (DiscordApiException e) {
            log.info("Discord API error: {}", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class DiscordApiException extends RuntimeException {
        private final HttpStatus status;

        public DiscordApiException(HttpStatus status, String body) {
            super("Discord API error " + status.value() + ": " + body);
            this.status = status;
        }

        public HttpStatus getStatus() {
            return status;
        }
    }


}