package dev.lucasmattos.cooperative_vote.core.usecase.agenda;

import static dev.lucasmattos.cooperative_vote.core.domain.AgendaVoteValue.NO;
import static dev.lucasmattos.cooperative_vote.core.domain.AgendaVoteValue.YES;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaGateway;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaVoteGateway;
import dev.lucasmattos.cooperative_vote.core.usecase.exception.NotFoundException;
import dev.lucasmattos.cooperative_vote.infra.config.stereotype.UseCase;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindAgenda {
    private final AgendaGateway agendaGateway;
    private final AgendaVoteGateway agendaVoteGateway;

    @Schema(name = "FindAgenda.Response")
    public record Response(UUID id, long votedYes, long votedNo) {}

    public Response execute(final UUID agendaId) {
        final Agenda agenda =
                agendaGateway.findById(agendaId).orElseThrow(() -> new NotFoundException(Agenda.class, agendaId));
        long votedYes = agendaVoteGateway.countByAgendaAndValue(agenda.getId(), YES);
        long votedNo = agendaVoteGateway.countByAgendaAndValue(agenda.getId(), NO);
        return new Response(agenda.getId(), votedYes, votedNo);
    }
}
