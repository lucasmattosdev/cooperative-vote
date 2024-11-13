package dev.lucasmattos.cooperative_vote.core.gateway;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaSession;
import dev.lucasmattos.cooperative_vote.infra.config.stereotype.Gateway;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Gateway
public interface AgendaSessionGateway {
    Optional<Agenda> findAgendaByAgendaIdAndSessionOpenInDate(UUID agendaId, ZonedDateTime date);

    AgendaSession save(AgendaSession agendaSession);
}
