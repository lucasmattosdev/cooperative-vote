package dev.lucasmattos.cooperative_vote.infra.gateway.agenda_session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.lucasmattos.cooperative_vote.core.domain.AgendaSession;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaSessionStatus;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DatabaseAgendaSessionGatewayTest {

    @Mock
    AgendaSessionRepository agendaSessionRepository;

    @InjectMocks
    DatabaseAgendaSessionGateway databaseAgendaSessionGateway;

    @Test
    void shouldSave() {
        final AgendaSession agendaSession =
                AgendaSession.builder().id(UUID.randomUUID()).build();
        when(agendaSessionRepository.save(agendaSession)).thenReturn(agendaSession);

        final AgendaSession result = databaseAgendaSessionGateway.save(agendaSession);

        verify(agendaSessionRepository).save(agendaSession);
        assertEquals(agendaSession, result);
    }

    @Test
    void shouldFindAllWithStatusOpenAndEndAtBeforeNow() {
        final AgendaSession agendaSession =
                AgendaSession.builder().id(UUID.randomUUID()).build();
        final ZonedDateTime now = ZonedDateTime.now();

        try (MockedStatic<ZonedDateTime> mockedStatic = mockStatic(ZonedDateTime.class)) {
            mockedStatic.when(ZonedDateTime::now).thenReturn(now);
            when(agendaSessionRepository.findAllByStatusAndEndAtBefore(AgendaSessionStatus.OPEN, now))
                    .thenReturn(List.of(agendaSession));

            final List<AgendaSession> result = databaseAgendaSessionGateway.findAllWithStatusOpenAndEndAtBeforeNow();

            assertEquals(List.of(agendaSession), result);
        }
    }
}
