package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.cameras;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;


@Slf4j
@Service
public class VideoFileCleaner {

    public VideoFileCleaner() {
        targetDirectory = Path.of(VideoRecorderService.outputPath);
    }

    private final Path targetDirectory;

    // How long to keep recordings
    private int keepDays = 7;
    private int keepHours = 0;
    private int keepMinutes = 0;

    public void setKeepDays(int days)     { this.keepDays = Math.max(days, 0); }
    public void setKeepHours(int hours)   { this.keepHours = Math.max(hours, 0); }
    public void setKeepMinutes(int mins)  { this.keepMinutes = Math.max(mins, 0); }


    public void cleanOldFiles() {
        try {
            if (!Files.exists(targetDirectory)) {
                log.info("Directory not found: {}", targetDirectory);
                return;
            }

            Duration retention = Duration.ofDays(keepDays)
                    .plusHours(keepHours)
                    .plusMinutes(keepMinutes);
            Instant cutoff = Instant.now().minus(retention);

            File[] files = targetDirectory.toFile().listFiles((dir, name) -> name.toLowerCase().endsWith(".mp4"));
            if (files == null) return;

            int deleted = 0;
            for (File file : files) {
                Instant lastModified = Instant.ofEpochMilli(file.lastModified());
                if (lastModified.isBefore(cutoff)) {
                    if (file.delete()) {
                        deleted++;
                        log.info("Deleted: {}", file.getName());
                    } else {
                        log.info("Failed to delete: {}", file.getName());
                    }
                }
            }

            if (deleted > 0) {
                log.info("Cleanup complete: {} files removed", deleted);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Scheduled(fixedRate = 60 * 60 * 1000)  //every hour
    public void scheduledCleanup() {
        cleanOldFiles();
    }
}
