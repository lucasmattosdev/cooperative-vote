package dev.lucasmattos.cooperative_vote.infra.gateway.agenda;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaGateway;
import dev.lucasmattos.cooperative_vote.infra.config.stereotype.Gateway;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Gateway
@RequiredArgsConstructor
public class DatabaseAgendaGateway implements AgendaGateway {
    private final AgendaRepository agendaRepository;

    @Override
    public Optional<Agenda> findById(UUID id) {
        return agendaRepository.findById(id);
    }

    @Override
    public Agenda save(Agenda agenda) {
        return agendaRepository.save(agenda);
    }
}
