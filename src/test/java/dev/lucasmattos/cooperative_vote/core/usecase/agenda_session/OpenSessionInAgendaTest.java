package dev.lucasmattos.cooperative_vote.core.usecase.agenda_session;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaSession;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaGateway;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaSessionGateway;
import dev.lucasmattos.cooperative_vote.core.usecase.exception.ConflictException;
import dev.lucasmattos.cooperative_vote.core.usecase.exception.NotFoundException;
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
class OpenSessionInAgendaTest {

    @Mock
    AgendaGateway agendaGateway;

    @Mock
    AgendaSessionGateway agendaSessionGateway;

    @InjectMocks
    OpenSessionInAgenda openSessionInAgenda;

    @Captor
    ArgumentCaptor<AgendaSession> agendaSessionCaptor;

    @Test
    void shouldThrownExceptionWhenAgendaNotFound() {
        final UUID agendaId = UUID.randomUUID();
        when(agendaGateway.findById(agendaId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> openSessionInAgenda.execute(agendaId, new OpenSessionInAgenda.Request(null)))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("The Agenda with id " + agendaId + " was not found");
    }

    @Test
    void shouldThrownExceptionWhenHasConflict() {
        final UUID agendaId = UUID.randomUUID();
        final Agenda agenda = Agenda.builder().id(agendaId).build();
        final UUID agendaSessionExistent = UUID.randomUUID();
        agenda.getAgendaSessions()
                .add(AgendaSession.builder()
                        .id(agendaSessionExistent)
                        .endAt(ZonedDateTime.now().plusMinutes(1))
                        .build());
        when(agendaGateway.findById(agendaId)).thenReturn(Optional.of(agenda));

        assertThatThrownBy(() -> openSessionInAgenda.execute(agendaId, new OpenSessionInAgenda.Request(0L)))
                .isInstanceOf(ConflictException.class)
                .hasMessage("There is already an open session with id " + agendaSessionExistent + " in this agenda");
    }

    @Test
    void shouldSaveWithSucess() {
        final UUID agendaId = UUID.randomUUID();
        final Agenda agenda = Agenda.builder().id(agendaId).build();
        final ZonedDateTime now = ZonedDateTime.now();
        agenda.getAgendaSessions()
                .add(AgendaSession.builder()
                        .id(UUID.randomUUID())
                        .endAt(ZonedDateTime.now().minusSeconds(1))
                        .build());
        when(agendaGateway.findById(agendaId)).thenReturn(Optional.of(agenda));

        try (MockedStatic<ZonedDateTime> mockedStatic = mockStatic(ZonedDateTime.class)) {
            mockedStatic.when(ZonedDateTime::now).thenReturn(now);

            openSessionInAgenda.execute(agendaId, new OpenSessionInAgenda.Request(10L));

            verify(agendaSessionGateway).save(agendaSessionCaptor.capture());
            final AgendaSession agendaSessionSaved = agendaSessionCaptor.getValue();
            assertEquals(agenda, agendaSessionSaved.getAgenda());
            assertEquals(10L, agendaSessionSaved.getDurationInMinutes());
            assertEquals(now, agendaSessionSaved.getStartAt());
            assertEquals(now.plusMinutes(10L), agendaSessionSaved.getEndAt());
        }
    }
}
