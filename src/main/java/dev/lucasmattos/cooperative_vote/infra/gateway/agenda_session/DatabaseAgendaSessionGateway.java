package dev.lucasmattos.cooperative_vote.infra.gateway.agenda_session;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaSession;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaSessionGateway;
import dev.lucasmattos.cooperative_vote.infra.config.stereotype.Gateway;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Gateway
@RequiredArgsConstructor
public class DatabaseAgendaSessionGateway implements AgendaSessionGateway {
    private final AgendaSessionRepository agendaSessionRepository;

    @Override
    public Optional<Agenda> findAgendaByAgendaIdAndSessionOpenInDate(UUID agendaId, ZonedDateTime date) {
        return agendaSessionRepository.findAgendaByAgendaIdAndSessionOpenInDate(agendaId, date);
    }

    @Override
    public AgendaSession save(AgendaSession agendaSession) {
        return agendaSessionRepository.save(agendaSession);
    }
}
