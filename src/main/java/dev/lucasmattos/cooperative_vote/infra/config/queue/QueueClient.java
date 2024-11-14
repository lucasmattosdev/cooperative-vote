package dev.lucasmattos.cooperative_vote.infra.config.queue;

public interface QueueClient {
    void send(Object message);
}
