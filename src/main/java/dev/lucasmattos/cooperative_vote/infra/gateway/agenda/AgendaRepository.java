package dev.lucasmattos.cooperative_vote.infra.gateway.agenda;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface AgendaRepository extends JpaRepository<Agenda, UUID> {}
