package dev.lucasmattos.cooperative_vote.infra.gateway.agenda_session;

import dev.lucasmattos.cooperative_vote.core.domain.AgendaSession;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaSessionGateway;
import dev.lucasmattos.cooperative_vote.infra.config.stereotype.Gateway;
import lombok.RequiredArgsConstructor;

@Gateway
@RequiredArgsConstructor
public class DatabaseAgendaSessionGateway implements AgendaSessionGateway {
    private final AgendaSessionRepository agendaSessionRepository;

    @Override
    public AgendaSession save(AgendaSession agendaSession) {
        return agendaSessionRepository.save(agendaSession);
    }
}
