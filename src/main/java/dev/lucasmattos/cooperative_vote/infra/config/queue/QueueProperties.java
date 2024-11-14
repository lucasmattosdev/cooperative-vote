package dev.lucasmattos.cooperative_vote.infra.config.queue;

import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "queue")
@EnableConfigurationProperties
@Data
public class QueueProperties {
    private Map<String, String> topics;
}
