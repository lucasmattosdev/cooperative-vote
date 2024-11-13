package dev.lucasmattos.cooperative_vote.core.usecase.agenda;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaGateway;
import dev.lucasmattos.cooperative_vote.infra.config.stereotype.UseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CreateAgenda {
    private final AgendaGateway agendaGateway;

    public record Response(UUID id) {
        public Response(final Agenda agenda) {
            this(agenda.getId());
        }
    }

    public Response execute() {
        final Agenda agendaToSave = Agenda.builder().build();

        return new Response(agendaGateway.save(agendaToSave));
    }
}
