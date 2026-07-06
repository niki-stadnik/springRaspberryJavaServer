package net.github.nikistadnik.springRaspberryJavaServer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig {

    private final ThreadPoolTaskExecutor executor;

    public AsyncConfig() {
        this.executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(50);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
    }

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        return executor;
    }

    @Scheduled(initialDelay = 10000, fixedRate = 10000)
    public void checkPoolStatus() {
        log.info("[POOL STATUS] Active Threads: {}, Pool Size: {}, Queue Size: {}",
                executor.getActiveCount(),
                executor.getPoolSize(),
                executor.getThreadPoolExecutor().getQueue().size());
    }
}