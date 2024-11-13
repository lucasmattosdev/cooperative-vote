package dev.lucasmattos.cooperative_vote.infra.gateway.agenda_session;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaSession;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
interface AgendaSessionRepository extends JpaRepository<AgendaSession, UUID> {
    @Query(
            "select a.agenda from AgendaSession a where a.agenda.id = :agendaId and a.startAt <= :date and a.endAt >= :date")
    Optional<Agenda> findAgendaByAgendaIdAndSessionOpenInDate(UUID agendaId, ZonedDateTime date);
}
