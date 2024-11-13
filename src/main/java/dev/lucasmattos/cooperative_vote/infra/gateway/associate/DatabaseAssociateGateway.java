package dev.lucasmattos.cooperative_vote.infra.gateway.associate;

import dev.lucasmattos.cooperative_vote.core.domain.Associate;
import dev.lucasmattos.cooperative_vote.core.gateway.AssociateGateway;
import dev.lucasmattos.cooperative_vote.infra.config.stereotype.Gateway;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Gateway
@RequiredArgsConstructor
public class DatabaseAssociateGateway implements AssociateGateway {
    private final AssociateRepository associateRepository;

    @Override
    public Optional<Associate> findById(UUID id) {
        return associateRepository.findById(id);
    }

    @Override
    public Associate save(Associate associate) {
        return associateRepository.save(associate);
    }
}
