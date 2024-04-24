package dev.zydy.poll.commands.configuration;

import lombok.RequiredArgsConstructor;
import org.axonframework.config.EventProcessingConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AxonConfig {

    @Autowired
    public void configure(EventProcessingConfigurer config) {
        config.usingSubscribingEventProcessors();
    }

}
