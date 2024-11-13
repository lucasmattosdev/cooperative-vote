package dev.lucasmattos.cooperative_vote.core.usecase.agenda_vote;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaSession;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaVote;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaVoteValue;
import dev.lucasmattos.cooperative_vote.core.domain.Associate;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaGateway;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaVoteGateway;
import dev.lucasmattos.cooperative_vote.core.gateway.AssociateGateway;
import dev.lucasmattos.cooperative_vote.core.usecase.exception.NotFoundException;
import dev.lucasmattos.cooperative_vote.core.usecase.exception.UseCaseException;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddVoteInAgendaTest {

    @Mock
    AgendaVoteGateway agendaVoteGateway;

    @Mock
    AssociateGateway associateGateway;

    @Mock
    AgendaGateway agendaGateway;

    @InjectMocks
    AddVoteInAgenda addVoteInAgenda;

    @Captor
    ArgumentCaptor<AgendaVote> agendaVoteCaptor;

    @Test
    void shouldThrownExceptionWhenAssociateNotFound() {
        final UUID agendaId = UUID.randomUUID();
        final UUID associateId = UUID.randomUUID();
        when(associateGateway.findById(associateId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> addVoteInAgenda.execute(agendaId, AgendaVoteValue.YES, associateId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("The Associate with id " + associateId + " was not found");
    }

    @Test
    void shouldThrownExceptionWhenAssociateAlreadyVoted() {
        final UUID agendaId = UUID.randomUUID();
        final UUID associateId = UUID.randomUUID();
        when(associateGateway.findById(associateId))
                .thenReturn(Optional.of(Associate.builder().build()));
        when(agendaVoteGateway.existsByAgendaAndAssociate(agendaId, associateId))
                .thenReturn(true);

        assertThatThrownBy(() -> addVoteInAgenda.execute(agendaId, AgendaVoteValue.YES, associateId))
                .isInstanceOf(UseCaseException.class)
                .hasMessage("The associate has already voted in this agenda");
    }

    @Test
    void shouldThrownExceptionWhenNotFoundAgenda() {
        final UUID agendaId = UUID.randomUUID();
        final UUID associateId = UUID.randomUUID();
        final ZonedDateTime now = ZonedDateTime.now();
        when(associateGateway.findById(associateId))
                .thenReturn(Optional.of(Associate.builder().build()));
        when(agendaVoteGateway.existsByAgendaAndAssociate(agendaId, associateId))
                .thenReturn(false);
        when(agendaGateway.findById(agendaId)).thenReturn(Optional.empty());

        try (MockedStatic<ZonedDateTime> mockedStatic = mockStatic(ZonedDateTime.class)) {
            mockedStatic.when(ZonedDateTime::now).thenReturn(now);
            assertThatThrownBy(() -> addVoteInAgenda.execute(agendaId, AgendaVoteValue.YES, associateId))
                    .isInstanceOf(UseCaseException.class)
                    .hasMessage("The Agenda with id " + agendaId + " was not found");
        }
    }

    @Test
    void shouldThrownExceptionWhenNotFoundAgendaSession() {
        final UUID agendaId = UUID.randomUUID();
        final UUID associateId = UUID.randomUUID();
        final ZonedDateTime now = ZonedDateTime.now();
        when(associateGateway.findById(associateId))
                .thenReturn(Optional.of(Associate.builder().build()));
        when(agendaVoteGateway.existsByAgendaAndAssociate(agendaId, associateId))
                .thenReturn(false);
        when(agendaGateway.findById(agendaId))
                .thenReturn(Optional.of(Agenda.builder().build()));

        try (MockedStatic<ZonedDateTime> mockedStatic = mockStatic(ZonedDateTime.class)) {
            mockedStatic.when(ZonedDateTime::now).thenReturn(now);
            assertThatThrownBy(() -> addVoteInAgenda.execute(agendaId, AgendaVoteValue.YES, associateId))
                    .isInstanceOf(UseCaseException.class)
                    .hasMessage("There is no open session for this agenda");
        }
    }

    @Test
    void shouldThrownExceptionWhenAgendaSessionEnd() {
        final UUID agendaId = UUID.randomUUID();
        final UUID associateId = UUID.randomUUID();
        final ZonedDateTime now = ZonedDateTime.now();
        when(associateGateway.findById(associateId))
                .thenReturn(Optional.of(Associate.builder().build()));
        when(agendaVoteGateway.existsByAgendaAndAssociate(agendaId, associateId))
                .thenReturn(false);
        when(agendaGateway.findById(agendaId))
                .thenReturn(Optional.of(Agenda.builder()
                        .lastAgendaSession(AgendaSession.builder()
                                .endAt(now.minusSeconds(1))
                                .build())
                        .build()));

        try (MockedStatic<ZonedDateTime> mockedStatic = mockStatic(ZonedDateTime.class)) {
            mockedStatic.when(ZonedDateTime::now).thenReturn(now);
            assertThatThrownBy(() -> addVoteInAgenda.execute(agendaId, AgendaVoteValue.YES, associateId))
                    .isInstanceOf(UseCaseException.class)
                    .hasMessage("The session for this agenda is already end");
        }
    }

    @Test
    void shouldSaveWithSucess() {
        final UUID agendaId = UUID.randomUUID();
        final UUID associateId = UUID.randomUUID();
        final ZonedDateTime now = ZonedDateTime.now();
        final Agenda agenda = Agenda.builder()
                .lastAgendaSession(
                        AgendaSession.builder().endAt(now.plusSeconds(1)).build())
                .build();
        final Associate associate = Associate.builder().build();
        final AgendaVote expectedResult =
                AgendaVote.builder().id(UUID.randomUUID()).build();
        when(associateGateway.findById(associateId)).thenReturn(Optional.of(associate));
        when(agendaVoteGateway.existsByAgendaAndAssociate(agendaId, associateId))
                .thenReturn(false);
        when(agendaGateway.findById(agendaId)).thenReturn(Optional.of(agenda));
        when(agendaVoteGateway.save(agendaVoteCaptor.capture())).thenReturn(expectedResult);

        try (MockedStatic<ZonedDateTime> mockedStatic = mockStatic(ZonedDateTime.class)) {
            mockedStatic.when(ZonedDateTime::now).thenReturn(now);
            final AgendaVote result = addVoteInAgenda.execute(agendaId, AgendaVoteValue.YES, associateId);

            final AgendaVote agendaVoteSaved = agendaVoteCaptor.getValue();
            assertEquals(agenda, agendaVoteSaved.getAgenda());
            assertEquals(AgendaVoteValue.YES, agendaVoteSaved.getValue());
            assertEquals(now, agendaVoteSaved.getVotedAt());
            assertEquals(associate, agendaVoteSaved.getAssociate());

            assertEquals(expectedResult, result);
        }
    }
}
