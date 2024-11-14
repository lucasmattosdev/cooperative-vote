package dev.lucasmattos.cooperative_vote.infra.config.rest;

import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rest")
@EnableConfigurationProperties
@Data
public class RestProperties {
    private Map<String, String> apis;
}
