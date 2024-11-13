package dev.lucasmattos.cooperative_vote.infra.gateway.associate;

import dev.lucasmattos.cooperative_vote.core.domain.Associate;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociateRepository extends JpaRepository<Associate, UUID> {}
