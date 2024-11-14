package dev.lucasmattos.cooperative_vote.core.usecase.agenda;

import static dev.lucasmattos.cooperative_vote.core.domain.AgendaVoteValue.NO;
import static dev.lucasmattos.cooperative_vote.core.domain.AgendaVoteValue.YES;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaSession;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaGateway;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaVoteGateway;
import dev.lucasmattos.cooperative_vote.core.usecase.exception.NotFoundException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindAgendaTest {

    @Mock
    AgendaGateway agendaGateway;

    @Mock
    AgendaVoteGateway agendaVoteGateway;

    @InjectMocks
    FindAgenda findAgenda;

    @Nested
    class v1 {
        @Test
        void shouldThrownExceptionWhenNotFoundAgenda() {
            final UUID agendaId = UUID.randomUUID();
            when(agendaGateway.findById(agendaId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> findAgenda.execute(agendaId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("The Agenda with id " + agendaId + " was not found");
        }

        @Test
        void shouldFindAgendaWithoutVotes() {
            final UUID agendaId = UUID.randomUUID();
            final Agenda agenda = Agenda.builder().id(agendaId).build();
            when(agendaGateway.findById(agendaId)).thenReturn(Optional.of(agenda));
            when(agendaVoteGateway.countByAgendaAndValue(agendaId, YES)).thenReturn(0L);
            when(agendaVoteGateway.countByAgendaAndValue(agendaId, NO)).thenReturn(0L);

            final FindAgenda.Response result = findAgenda.execute(agendaId);

            assertEquals(agendaId, result.id());
            assertEquals(0L, result.votedYes());
            assertEquals(0L, result.votedNo());
        }

        @Test
        void shouldFindAgendaWithVotes() {
            final UUID agendaId = UUID.randomUUID();
            final Agenda agenda = Agenda.builder().id(agendaId).build();
            when(agendaGateway.findById(agendaId)).thenReturn(Optional.of(agenda));
            when(agendaVoteGateway.countByAgendaAndValue(agendaId, YES)).thenReturn(2L);
            when(agendaVoteGateway.countByAgendaAndValue(agendaId, NO)).thenReturn(3L);

            final FindAgenda.Response result = findAgenda.execute(agendaId);

            assertEquals(agendaId, result.id());
            assertEquals(2L, result.votedYes());
            assertEquals(3L, result.votedNo());
        }
    }

    @Nested
    class v2 {
        @Test
        void shouldThrownExceptionWhenNotFoundAgenda() {
            final UUID agendaId = UUID.randomUUID();
            when(agendaGateway.findById(agendaId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> findAgenda.executeV2(agendaId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("The Agenda with id " + agendaId + " was not found");
        }

        @Test
        void shouldFindAgendaWithoutVotes() {
            final UUID agendaId = UUID.randomUUID();
            final AgendaSession lastSession = AgendaSession.builder().build();
            final Agenda agenda =
                    Agenda.builder().id(agendaId).lastAgendaSession(lastSession).build();
            when(agendaGateway.findById(agendaId)).thenReturn(Optional.of(agenda));
            when(agendaVoteGateway.countByAgendaAndValue(agendaId, YES)).thenReturn(0L);
            when(agendaVoteGateway.countByAgendaAndValue(agendaId, NO)).thenReturn(0L);

            final FindAgenda.ResponseV2 result = findAgenda.executeV2(agendaId);

            assertEquals(agendaId, result.id());
            assertEquals(lastSession.getId(), result.lastSession().getId());
            assertEquals(0L, result.votedYes());
            assertEquals(0L, result.votedNo());
        }

        @Test
        void shouldFindAgendaWithVotes() {
            final UUID agendaId = UUID.randomUUID();
            final AgendaSession lastSession = AgendaSession.builder().build();
            final Agenda agenda =
                    Agenda.builder().id(agendaId).lastAgendaSession(lastSession).build();
            when(agendaGateway.findById(agendaId)).thenReturn(Optional.of(agenda));
            when(agendaVoteGateway.countByAgendaAndValue(agendaId, YES)).thenReturn(2L);
            when(agendaVoteGateway.countByAgendaAndValue(agendaId, NO)).thenReturn(3L);

            final FindAgenda.ResponseV2 result = findAgenda.executeV2(agendaId);

            assertEquals(agendaId, result.id());
            assertEquals(lastSession.getId(), result.lastSession().getId());
            assertEquals(2L, result.votedYes());
            assertEquals(3L, result.votedNo());
        }
    }
}
