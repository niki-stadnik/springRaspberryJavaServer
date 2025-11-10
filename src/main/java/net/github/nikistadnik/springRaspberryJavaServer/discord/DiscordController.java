package net.github.nikistadnik.springRaspberryJavaServer.discord;



import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorman.DoormanDoorBellEvent;
import net.github.nikistadnik.springRaspberryJavaServer.storage.Storage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/discord")
@RequiredArgsConstructor
public class DiscordController {

    //private final DiscordService discordService;
    private final DiscordServiceBE discordServiceBE;


/*
    // POST /discord/messages
    @PostMapping("/messages")
    public ResponseEntity<DiscordService.MessageResponse> createMessage(
            @Valid @RequestBody DiscordMessageRequest request
    ) {
        var response = discordService.sendMessage(request.channelId(), request.content());
        return ResponseEntity.ok(response);
    }

*/

    // Request DTO for creating a message
    public record DiscordMessageRequest(
            @NotBlank String channelId,
            @NotBlank @Size(max = 2000) String content
    ) {}

    @PostMapping("/message")
    public void test(){
        //discordServiceBE.sendHardcodedMessage();
        DoormanDoorBellEvent event = new DoormanDoorBellEvent(this, 0);
        discordServiceBE.sendCamToDiscord(event);
    }

}
