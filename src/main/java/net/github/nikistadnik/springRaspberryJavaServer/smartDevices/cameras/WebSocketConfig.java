package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.cameras;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer, WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MyBinaryHandler(), "/user").setAllowedOrigins("*");
        registry.addHandler(new SocketTextHandler(), "/user2").setAllowedOrigins("*");
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        //container.setPort(8081);
        container.setMaxTextMessageBufferSize(5242880);
        container.setMaxBinaryMessageBufferSize(5242880);
        return container;
    }


    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        //factory.setPort(1994);
        //factory.setPort(1995);
    }
}