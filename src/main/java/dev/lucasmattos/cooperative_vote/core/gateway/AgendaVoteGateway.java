package dev.lucasmattos.cooperative_vote.core.gateway;

import dev.lucasmattos.cooperative_vote.core.domain.AgendaVote;
import dev.lucasmattos.cooperative_vote.infra.config.stereotype.Gateway;
import java.util.UUID;

@Gateway
public interface AgendaVoteGateway {
    boolean existsByAgendaAndAssociate(UUID agendaId, UUID associateId);

    AgendaVote save(AgendaVote agendaVote);
}
