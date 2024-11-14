package dev.lucasmattos.cooperative_vote.core.gateway;

import dev.lucasmattos.cooperative_vote.core.domain.external.User;
import dev.lucasmattos.cooperative_vote.infra.config.stereotype.Gateway;
import java.util.Optional;

@Gateway
public interface UserGateway {
    Optional<User> findBy(final String document);
}
