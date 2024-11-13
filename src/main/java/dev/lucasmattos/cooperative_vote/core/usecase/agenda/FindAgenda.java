package dev.lucasmattos.cooperative_vote.core.usecase.agenda;

import static dev.lucasmattos.cooperative_vote.core.domain.AgendaVoteValue.NO;
import static dev.lucasmattos.cooperative_vote.core.domain.AgendaVoteValue.YES;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaVote;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaGateway;
import dev.lucasmattos.cooperative_vote.core.usecase.exception.NotFoundException;
import dev.lucasmattos.cooperative_vote.infra.config.stereotype.UseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@UseCase
@RequiredArgsConstructor
public class FindAgenda {
    private final AgendaGateway agendaGateway;

    @Value
    public static class Response {
        UUID id;
        long votedYes;
        long votedNo;

        public Response(final Agenda agenda) {
            this.id = agenda.getId();

            long countVotedYes = 0L;
            long countVotedNo = 0L;
            for (final AgendaVote agendaVote : agenda.getAgendaVotes()) {
                if (agendaVote.getValue().equals(YES)) {
                    countVotedYes++;
                }
                if (agendaVote.getValue().equals(NO)) {
                    countVotedNo++;
                }
            }
            this.votedYes = countVotedYes;
            this.votedNo = countVotedNo;
        }
    }

    public Response execute(final UUID agendaId) {
        return new Response(
                agendaGateway.findById(agendaId).orElseThrow(() -> new NotFoundException(Agenda.class, agendaId)));
    }
}
