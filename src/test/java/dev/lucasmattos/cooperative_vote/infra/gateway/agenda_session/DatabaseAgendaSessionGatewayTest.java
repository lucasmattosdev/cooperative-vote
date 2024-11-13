package dev.lucasmattos.cooperative_vote.infra.gateway.agenda_session;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaSession;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DatabaseAgendaSessionGatewayTest {

    @Mock
    AgendaSessionRepository agendaSessionRepository;

    @InjectMocks
    DatabaseAgendaSessionGateway databaseAgendaSessionGateway;

    @Test
    void shouldFindAgendaByAgendaIdAndSessionOpenInDate() {
        final Agenda agenda = Agenda.builder().id(UUID.randomUUID()).build();
        final ZonedDateTime now = ZonedDateTime.now();
        when(agendaSessionRepository.findAgendaByAgendaIdAndSessionOpenInDate(agenda.getId(), now))
                .thenReturn(Optional.of(agenda));

        final Optional<Agenda> result =
                databaseAgendaSessionGateway.findAgendaByAgendaIdAndSessionOpenInDate(agenda.getId(), now);

        assertThat(result).isPresent();
        assertThat(result).contains(agenda);
    }

    @Test
    void shouldSave() {
        final AgendaSession agendaSession =
                AgendaSession.builder().id(UUID.randomUUID()).build();
        when(agendaSessionRepository.save(agendaSession)).thenReturn(agendaSession);

        final AgendaSession result = databaseAgendaSessionGateway.save(agendaSession);

        verify(agendaSessionRepository).save(agendaSession);
        assertEquals(agendaSession, result);
    }
}
