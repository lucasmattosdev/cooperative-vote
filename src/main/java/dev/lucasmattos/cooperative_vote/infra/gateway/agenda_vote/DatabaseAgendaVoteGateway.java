package dev.lucasmattos.cooperative_vote.infra.gateway.agenda_vote;

import dev.lucasmattos.cooperative_vote.core.domain.AgendaVote;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaVoteGateway;
import dev.lucasmattos.cooperative_vote.infra.config.stereotype.Gateway;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Gateway
@RequiredArgsConstructor
public class DatabaseAgendaVoteGateway implements AgendaVoteGateway {
    private final AgendaVoteRepository agendaVoteRepository;

    @Override
    public boolean existsByAgendaAndAssociate(UUID agendaId, UUID associateId) {
        return agendaVoteRepository.existsByAgenda_idAndAssociate_id(agendaId, associateId);
    }

    @Override
    public AgendaVote save(AgendaVote agendaVote) {
        return agendaVoteRepository.save(agendaVote);
    }
}
