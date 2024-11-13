package dev.lucasmattos.cooperative_vote.core.usecase.agenda;

import static dev.lucasmattos.cooperative_vote.core.domain.AgendaVoteValue.NO;
import static dev.lucasmattos.cooperative_vote.core.domain.AgendaVoteValue.YES;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaVote;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaGateway;
import dev.lucasmattos.cooperative_vote.core.usecase.exception.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindAgendaTest {

    @Mock
    AgendaGateway agendaGateway;

    @InjectMocks
    FindAgenda findAgenda;

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

        final FindAgenda.Response result = findAgenda.execute(agendaId);

        assertEquals(agendaId, result.id());
        assertEquals(0L, result.votedYes());
        assertEquals(0L, result.votedNo());
    }

    @Test
    void shouldFindAgendaWithVotes() {
        final UUID agendaId = UUID.randomUUID();
        final Agenda agenda = Agenda.builder().id(agendaId).build();
        agenda.getAgendaVotes()
                .addAll(List.of(
                        AgendaVote.builder().value(YES).build(),
                        AgendaVote.builder().value(YES).build(),
                        AgendaVote.builder().value(NO).build(),
                        AgendaVote.builder().value(NO).build(),
                        AgendaVote.builder().value(NO).build()));
        when(agendaGateway.findById(agendaId)).thenReturn(Optional.of(agenda));

        final FindAgenda.Response result = findAgenda.execute(agendaId);

        assertEquals(agendaId, result.id());
        assertEquals(2L, result.votedYes());
        assertEquals(3L, result.votedNo());
    }
}
