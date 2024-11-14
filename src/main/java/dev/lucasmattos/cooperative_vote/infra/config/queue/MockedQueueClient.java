package dev.lucasmattos.cooperative_vote.infra.config.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class MockedQueueClient implements QueueClient {
    /*
     *  Esse é apenas um exemplo de como poderia ser a implementação de um cliente de fila
     *  que envia mensagens para um tópico.
     *
     *  A implementação real depende da tecnologia de fila que será utilizada.
     */

    final String topic;
    final ObjectMapper objectMapper = new ObjectMapper();

    public void send(Object message) {
        try {
            log.info("Sending message to topic " + topic + " with message " + objectMapper.writeValueAsString(message));
        } catch (Exception e) {
            log.error("Error sending message to topic " + topic, e);
        }
    }
}
