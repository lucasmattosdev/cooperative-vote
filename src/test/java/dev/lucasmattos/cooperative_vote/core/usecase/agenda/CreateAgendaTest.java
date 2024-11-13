package dev.lucasmattos.cooperative_vote.core.usecase.agenda;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaGateway;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateAgendaTest {

    @Mock
    AgendaGateway agendaGateway;

    @InjectMocks
    CreateAgenda createAgenda;

    @Test
    void shouldCreateAgenda() {
        final Agenda agenda = Agenda.builder().id(UUID.randomUUID()).build();
        when(agendaGateway.save(new Agenda())).thenReturn(agenda);

        final CreateAgenda.Response result = createAgenda.execute();

        verify(agendaGateway).save(new Agenda());
        assertEquals(agenda.getId(), result.getId());
    }
}
