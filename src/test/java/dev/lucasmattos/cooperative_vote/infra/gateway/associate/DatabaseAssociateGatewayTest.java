package dev.lucasmattos.cooperative_vote.infra.gateway.associate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.lucasmattos.cooperative_vote.core.domain.Associate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DatabaseAssociateGatewayTest {

    @Mock
    AssociateRepository associateRepository;

    @InjectMocks
    DatabaseAssociateGateway databaseAssociateGateway;

    @Test
    void shouldFindById() {
        final Associate associate = Associate.builder().id(UUID.randomUUID()).build();
        when(associateRepository.findById(associate.getId())).thenReturn(Optional.of(associate));

        final Optional<Associate> result = databaseAssociateGateway.findById(associate.getId());

        assertThat(result).isPresent();
        assertThat(result).contains(associate);
    }

    @Test
    void shouldSave() {
        final Associate associate = Associate.builder().id(UUID.randomUUID()).build();
        when(associateRepository.save(associate)).thenReturn(associate);

        final Associate result = databaseAssociateGateway.save(associate);

        verify(associateRepository).save(associate);
        assertEquals(associate, result);
    }
}
