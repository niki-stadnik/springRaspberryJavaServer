package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorlock;


import lombok.RequiredArgsConstructor;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorman.DoormanDoorButtonEvent;
import net.github.nikistadnik.springRaspberryJavaServer.smartDevices.doorman.DoormanDoorRfidEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class DoorlockService {

    private final SimpMessageSendingOperations messaging;

    private static int position = 0;
    private static boolean door = false;

    private CompletableFuture<Boolean> statusDoorFuture = new CompletableFuture<>();

    DoorlockClientModel unlock = new DoorlockClientModel(11762, 30000, 1500, 15000, 3000, 4000, 6200, 7150, 9350, 10150);
    DoorlockClientModel lock = new DoorlockClientModel(2162, 30000, 1500, 15000, 3300, 4200, 6500, 7250, 9500, 10300);



    public void command(DoorlockClientModel data) {
        //int move = data.getMove();
        //messaging.convertAndSend("/topic/doorlock", new DoorlockClientModel(move));
        //messaging.convertAndSend("/topic/doorlock", data);
    }

    public void setData(DoorlockModel data) {
        position = data.getPosition();
        door = data.isDoor();
        statusDoorFuture.complete(door);
    }

    @Scheduled(fixedRate = 1000)
    synchronized void lockUpdate() {
        messaging.convertAndSend("/topic/clientDoorlock", new DoorlockModel(position, door));
    }

    @EventListener
    public void rfidMonitor(DoormanDoorRfidEvent event) {
        unlockAndLockAfter();
    }


    @EventListener
    @Async
    public void moveDoorLock(DoormanDoorButtonEvent event) {
        if (event.held() > 25)
            unlockAndLockAfter();
    }

    public void unlockAndLockAfter() {
        moveLock(false);
        try {
            boolean result = statusDoorFuture.orTimeout(60000, java.util.concurrent.TimeUnit.MILLISECONDS).get();
            System.out.println("Variable changed to: " + result);
        } catch (Exception e) {
            // Triggers if 30 seconds pass without complete() being called
            System.out.println("Timed out waiting for variable change after 30 seconds.");
        }finally {
            // Reset the future for the next run if necessary
            statusDoorFuture = new CompletableFuture<>();
        }
        while (!door) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        while (door) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (!door && position > 3000){
            moveLock(true);
        }
        //todo trigger a method, that checks if anyone is left home, and turn off lights and so on if not.
    }


    public void moveLock(boolean move){
        if (move)
            messaging.convertAndSend("/topic/doorlock", lock);
        else
            messaging.convertAndSend("/topic/doorlock", unlock);

    }
}
