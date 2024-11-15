package dev.lucasmattos.cooperative_vote.core.gateway;

import dev.lucasmattos.cooperative_vote.core.domain.AgendaSession;
import dev.lucasmattos.cooperative_vote.infra.config.stereotype.Gateway;
import java.util.List;

@Gateway
public interface AgendaSessionGateway {
    AgendaSession save(AgendaSession agendaSession);

    List<AgendaSession> findAllWithStatusOpenAndEndAtBeforeNow();
}
