package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorlock;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoorlockService {

    private final SimpMessageSendingOperations messaging;

    private static int position = 0;


    public void command(DoorlockClientModel data) {
        int move = data.getMove();
        messaging.convertAndSend("/topic/doorlock", new DoorlockClientModel(move));
        //messaging.convertAndSend("/topic/doorlock", data);
    }

    public void setData(DoorlockModel data) {
        position = data.getPosition();
    }

    @Scheduled(fixedRate = 1000)
    synchronized void lockUpdate() {
        messaging.convertAndSend("/topic/clientDoorlock", new DoorlockModel(position));
    }
}
