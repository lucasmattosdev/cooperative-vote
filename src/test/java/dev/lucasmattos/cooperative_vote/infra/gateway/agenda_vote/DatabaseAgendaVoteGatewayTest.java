package dev.lucasmattos.cooperative_vote.infra.gateway.agenda_vote;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.lucasmattos.cooperative_vote.core.domain.AgendaVote;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DatabaseAgendaVoteGatewayTest {

    @Mock
    AgendaVoteRepository agendaVoteRepository;

    @InjectMocks
    DatabaseAgendaVoteGateway databaseAgendaVoteGateway;

    @Test
    void shouldFindAgendaByAgendaIdAndSessionOpenInDate() {
        final UUID agendaId = UUID.randomUUID();
        final UUID associateId = UUID.randomUUID();
        when(agendaVoteRepository.existsByAgenda_idAndAssociate_id(agendaId, associateId))
                .thenReturn(true);

        final boolean result = databaseAgendaVoteGateway.existsByAgendaAndAssociate(agendaId, associateId);

        assertThat(result).isTrue();
    }

    @Test
    void shouldSave() {
        final AgendaVote agendaVote = AgendaVote.builder().id(UUID.randomUUID()).build();
        when(agendaVoteRepository.save(agendaVote)).thenReturn(agendaVote);

        final AgendaVote result = databaseAgendaVoteGateway.save(agendaVote);

        verify(agendaVoteRepository).save(agendaVote);
        assertEquals(agendaVote, result);
    }
}
