package dev.lucasmattos.cooperative_vote.core.gateway;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import dev.lucasmattos.cooperative_vote.infra.config.stereotype.Gateway;
import java.util.Optional;
import java.util.UUID;

@Gateway
public interface AgendaGateway {
    Optional<Agenda> findById(UUID id);

    Agenda save(Agenda agenda);
}
