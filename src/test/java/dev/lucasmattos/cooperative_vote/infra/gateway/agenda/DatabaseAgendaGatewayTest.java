package dev.lucasmattos.cooperative_vote.infra.gateway.agenda;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DatabaseAgendaGatewayTest {

    @Mock
    AgendaRepository agendaRepository;

    @InjectMocks
    DatabaseAgendaGateway databaseAgendaGateway;

    @Test
    void shouldFindById() {
        final Agenda agenda = Agenda.builder().id(UUID.randomUUID()).build();
        when(agendaRepository.findById(agenda.getId())).thenReturn(Optional.of(agenda));

        final Optional<Agenda> result = databaseAgendaGateway.findById(agenda.getId());

        assertThat(result).isPresent();
        assertThat(result).contains(agenda);
    }

    @Test
    void shouldSave() {
        final Agenda agenda = Agenda.builder().id(UUID.randomUUID()).build();
        when(agendaRepository.save(agenda)).thenReturn(agenda);

        final Agenda result = databaseAgendaGateway.save(agenda);

        verify(agendaRepository).save(agenda);
        assertEquals(agenda, result);
    }
}
