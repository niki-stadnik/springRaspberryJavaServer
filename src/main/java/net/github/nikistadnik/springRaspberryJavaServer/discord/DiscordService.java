package net.github.nikistadnik.springRaspberryJavaServer.discord;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;


/*
In POstman
POST http://192.168.88.12:1994/discord/messages
Headers Key:Content-Type Value:application/json
Body raw:
{
  "channelId": "1435313713841442848",
  "content": "Hello from Spring Boot!"
}
 */


@Slf4j
//@Service
public class DiscordService {

    //private final DeviceRegistry deviceRegistry;
    private final WebClient client;



    public DiscordService(@Value("${discord.bot.token}") String botToken) {
        this.client = WebClient.builder()
                .baseUrl("https://discord.com/api/v10")
                .defaultHeader("Authorization", "Bot " + botToken)
                .build();
    }

    // Minimal payload to create a message
    private record CreateMessageRequest(String content) {}

    // Subset of fields returned by Discord on message create
    public record MessageResponse(
            String id,
            @JsonProperty("channelid") String channelId,
            String content
    ) {}

    // Sends a message to a channel using the bot token
    public MessageResponse sendMessage(String channelId, String content) {
        try {
            return client.post()
                    .uri("/channels/{channelId}/messages", channelId)
                    .bodyValue(new CreateMessageRequest(content))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, resp ->
                            resp.bodyToMono(String.class)
                                    .defaultIfEmpty("")
                                    .flatMap(body -> Mono.error(new DiscordApiException(HttpStatus.resolve(resp.statusCode().value()), body)))
                    )
                    .bodyToMono(MessageResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new DiscordApiException(HttpStatus.valueOf(e.getRawStatusCode()), e.getResponseBodyAsString());
        }
    }

    public static class DiscordApiException extends RuntimeException {
        private final HttpStatus status;
        public DiscordApiException(HttpStatus status, String body) {
            super("Discord API error " + status.value() + ": " + body);
            this.status = status;
        }
        public HttpStatus getStatus() { return status; }
    }



}
