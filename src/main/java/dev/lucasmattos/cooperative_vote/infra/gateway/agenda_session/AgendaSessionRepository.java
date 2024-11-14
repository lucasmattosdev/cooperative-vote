package dev.lucasmattos.cooperative_vote.infra.gateway.agenda_session;

import dev.lucasmattos.cooperative_vote.core.domain.AgendaSession;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaSessionStatus;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AgendaSessionRepository extends JpaRepository<AgendaSession, UUID> {
    List<AgendaSession> findAllByStatusAndEndAtBefore(AgendaSessionStatus status, ZonedDateTime now);
}
