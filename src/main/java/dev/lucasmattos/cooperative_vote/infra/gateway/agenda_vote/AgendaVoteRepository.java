package dev.lucasmattos.cooperative_vote.infra.gateway.agenda_vote;

import dev.lucasmattos.cooperative_vote.core.domain.AgendaVote;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaVoteValue;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AgendaVoteRepository extends JpaRepository<AgendaVote, UUID> {
    boolean existsByAgenda_idAndAssociate_id(UUID agendaId, UUID associateId);

    long countByAgenda_idAndValue(UUID agendaId, AgendaVoteValue value);
}
