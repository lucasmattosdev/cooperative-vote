package dev.lucasmattos.cooperative_vote.core.usecase.agenda_session;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaSession;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaSessionStatus;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaVoteValue;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaSessionGateway;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaVoteGateway;
import dev.lucasmattos.cooperative_vote.infra.config.queue.QueueClient;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CloseAndNotifyAgendaSessionFinishedTest {
    @Mock
    private AgendaSessionGateway agendaSessionGateway;

    @Mock
    private AgendaVoteGateway agendaVoteGateway;

    @Mock
    private QueueClient votingSessionEndedQueueClient;

    @InjectMocks
    private CloseAndNotifyAgendaSessionFinished closeAndNotifyAgendaSessionFinished;

    @Captor
    ArgumentCaptor<AgendaSession> agendaSessionCaptor;

    @Test
    void shouldDoNothingWhenNotExistsOpen() {
        when(agendaSessionGateway.findAllWithStatusOpenAndEndAtBeforeNow()).thenReturn(List.of());

        assertDoesNotThrow(() -> closeAndNotifyAgendaSessionFinished.execute());
    }

    @Test
    void shouldCloseAndNotifyWhenExistsOpen() {
        final AgendaSession agendaSessionOne = AgendaSession.builder()
                .agenda(Agenda.builder().id(UUID.randomUUID()).build())
                .build();
        final AgendaSession agendaSessionTwo = AgendaSession.builder()
                .agenda(Agenda.builder().id(UUID.randomUUID()).build())
                .build();
        when(agendaSessionGateway.findAllWithStatusOpenAndEndAtBeforeNow())
                .thenReturn(List.of(agendaSessionOne, agendaSessionTwo));
        when(agendaVoteGateway.countByAgendaAndValue(
                        agendaSessionOne.getAgenda().getId(), AgendaVoteValue.YES))
                .thenReturn(10L);
        when(agendaVoteGateway.countByAgendaAndValue(
                        agendaSessionOne.getAgenda().getId(), AgendaVoteValue.NO))
                .thenReturn(20L);
        when(agendaVoteGateway.countByAgendaAndValue(
                        agendaSessionTwo.getAgenda().getId(), AgendaVoteValue.YES))
                .thenReturn(81723L);
        when(agendaVoteGateway.countByAgendaAndValue(
                        agendaSessionTwo.getAgenda().getId(), AgendaVoteValue.NO))
                .thenReturn(991029L);

        closeAndNotifyAgendaSessionFinished.execute();

        verify(agendaSessionGateway, times(2)).save(agendaSessionCaptor.capture());
        final List<AgendaSession> capturedAgendaSessions = agendaSessionCaptor.getAllValues();
        final AgendaSession capturedAgendaSessionOne = capturedAgendaSessions.get(0);
        final AgendaSession capturedAgendaSessionTwo = capturedAgendaSessions.get(1);
        assertEquals(AgendaSessionStatus.CLOSED, capturedAgendaSessionOne.getStatus());
        assertEquals(AgendaSessionStatus.CLOSED, capturedAgendaSessionTwo.getStatus());

        verify(votingSessionEndedQueueClient)
                .send(new CloseAndNotifyAgendaSessionFinished.Response(agendaSessionOne.getId(), 10L, 20L));
        verify(votingSessionEndedQueueClient)
                .send(new CloseAndNotifyAgendaSessionFinished.Response(agendaSessionTwo.getId(), 81723L, 991029L));
    }

    @Test
    void shouldExecuteFirstAndThrownExceptionBySecond() {
        final AgendaSession agendaSessionOne = AgendaSession.builder()
                .agenda(Agenda.builder().id(UUID.randomUUID()).build())
                .build();
        final AgendaSession agendaSessionTwo = AgendaSession.builder()
                .agenda(Agenda.builder().id(UUID.randomUUID()).build())
                .build();
        when(agendaSessionGateway.findAllWithStatusOpenAndEndAtBeforeNow())
                .thenReturn(List.of(agendaSessionOne, agendaSessionTwo));
        when(agendaVoteGateway.countByAgendaAndValue(
                        agendaSessionOne.getAgenda().getId(), AgendaVoteValue.YES))
                .thenReturn(10L);
        when(agendaVoteGateway.countByAgendaAndValue(
                        agendaSessionOne.getAgenda().getId(), AgendaVoteValue.NO))
                .thenReturn(20L);
        when(agendaVoteGateway.countByAgendaAndValue(
                        agendaSessionTwo.getAgenda().getId(), AgendaVoteValue.YES))
                .thenThrow(new RuntimeException("any Exception"));

        assertThatThrownBy(() -> closeAndNotifyAgendaSessionFinished.execute())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("any Exception");

        verify(agendaSessionGateway, times(1)).save(agendaSessionCaptor.capture());
        final List<AgendaSession> capturedAgendaSessions = agendaSessionCaptor.getAllValues();
        final AgendaSession capturedAgendaSessionOne = capturedAgendaSessions.get(0);
        assertEquals(AgendaSessionStatus.CLOSED, capturedAgendaSessionOne.getStatus());

        verify(votingSessionEndedQueueClient)
                .send(new CloseAndNotifyAgendaSessionFinished.Response(agendaSessionOne.getId(), 10L, 20L));
    }
}
