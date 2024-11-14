package dev.lucasmattos.cooperative_vote.infra.gateway.agenda_session;

import dev.lucasmattos.cooperative_vote.core.domain.AgendaSession;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaSessionStatus;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaSessionGateway;
import dev.lucasmattos.cooperative_vote.infra.config.stereotype.Gateway;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Gateway
@RequiredArgsConstructor
public class DatabaseAgendaSessionGateway implements AgendaSessionGateway {
    private final AgendaSessionRepository agendaSessionRepository;

    @Override
    public AgendaSession save(AgendaSession agendaSession) {
        return agendaSessionRepository.save(agendaSession);
    }

    @Override
    public List<AgendaSession> findAllWithStatusOpenAndEndAtBeforeNow() {
        return agendaSessionRepository.findAllByStatusAndEndAtBefore(AgendaSessionStatus.OPEN, ZonedDateTime.now());
    }
}
