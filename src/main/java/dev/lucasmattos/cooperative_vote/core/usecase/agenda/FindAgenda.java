package dev.lucasmattos.cooperative_vote.core.usecase.agenda;

import static dev.lucasmattos.cooperative_vote.core.domain.AgendaVoteValue.NO;
import static dev.lucasmattos.cooperative_vote.core.domain.AgendaVoteValue.YES;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaSession;
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

    /**
     * @deprecated Refatoring frontends to use find agenda with last session
     */
    @Deprecated(since = "v2")
    @Schema(name = "FindAgenda.Response")
    public record Response(UUID id, long votedYes, long votedNo) {}

    @Schema(name = "FindAgenda.ResponseV2")
    public record ResponseV2(UUID id, AgendaSession lastSession, long votedYes, long votedNo) {}

    /**
     * @deprecated Refatoring frontends to use find agenda with last session
     */
    @Deprecated(since = "v2")
    public Response execute(final UUID agendaId) {
        final Agenda agenda =
                agendaGateway.findById(agendaId).orElseThrow(() -> new NotFoundException(Agenda.class, agendaId));
        long votedYes = agendaVoteGateway.countByAgendaAndValue(agenda.getId(), YES);
        long votedNo = agendaVoteGateway.countByAgendaAndValue(agenda.getId(), NO);
        return new Response(agenda.getId(), votedYes, votedNo);
    }

    public ResponseV2 executeV2(final UUID agendaId) {
        final Agenda agenda =
                agendaGateway.findById(agendaId).orElseThrow(() -> new NotFoundException(Agenda.class, agendaId));
        long votedYes = agendaVoteGateway.countByAgendaAndValue(agenda.getId(), YES);
        long votedNo = agendaVoteGateway.countByAgendaAndValue(agenda.getId(), NO);
        return new ResponseV2(agenda.getId(), agenda.getLastAgendaSession(), votedYes, votedNo);
    }
}
