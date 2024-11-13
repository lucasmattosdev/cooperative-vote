package dev.lucasmattos.cooperative_vote.core.usecase.agenda;

import static dev.lucasmattos.cooperative_vote.core.domain.AgendaVoteValue.NO;
import static dev.lucasmattos.cooperative_vote.core.domain.AgendaVoteValue.YES;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaGateway;
import dev.lucasmattos.cooperative_vote.core.usecase.exception.NotFoundException;
import dev.lucasmattos.cooperative_vote.infra.config.stereotype.UseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindAgenda {
    private final AgendaGateway agendaGateway;

    public record Response(UUID id, long votedYes, long votedNo) {
        public Response(final Agenda agenda) {
            this(
                    agenda.getId(),
                    agenda.getAgendaVotes().stream()
                            .filter(el -> el.getValue().equals(YES))
                            .count(),
                    agenda.getAgendaVotes().stream()
                            .filter(el -> el.getValue().equals(NO))
                            .count());
        }
    }

    public Response execute(final UUID agendaId) {
        return new Response(
                agendaGateway.findById(agendaId).orElseThrow(() -> new NotFoundException(Agenda.class, agendaId)));
    }
}
