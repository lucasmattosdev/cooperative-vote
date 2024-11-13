package dev.lucasmattos.cooperative_vote.core.usecase.agenda;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaGateway;
import dev.lucasmattos.cooperative_vote.infra.config.stereotype.UseCase;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class CreateAgenda {
    private final AgendaGateway agendaGateway;

    @Data
    public static class Response {
        UUID id;

        public Response(final Agenda agenda) {
            this.id = agenda.getId();
        }
    }

    public Response execute() {
        final Agenda agendaToSave = Agenda.builder().build();

        return new Response(agendaGateway.save(agendaToSave));
    }
}
