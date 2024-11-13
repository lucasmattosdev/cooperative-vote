package dev.lucasmattos.cooperative_vote.core.gateway;

import dev.lucasmattos.cooperative_vote.infra.config.stereotype.Gateway;

@Gateway
public interface DocumentGateway {
    boolean isValid(String document);
}
