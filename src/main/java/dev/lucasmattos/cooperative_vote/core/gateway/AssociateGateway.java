package dev.lucasmattos.cooperative_vote.core.gateway;

import dev.lucasmattos.cooperative_vote.core.domain.Associate;
import dev.lucasmattos.cooperative_vote.infra.config.stereotype.Gateway;
import java.util.Optional;
import java.util.UUID;

@Gateway
public interface AssociateGateway {
    Optional<Associate> findById(UUID id);

    Associate save(Associate associate);
}
