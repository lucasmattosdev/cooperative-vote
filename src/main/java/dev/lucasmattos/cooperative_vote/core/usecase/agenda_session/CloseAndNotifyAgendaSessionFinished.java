package dev.lucasmattos.cooperative_vote.core.usecase.agenda_session;

import static dev.lucasmattos.cooperative_vote.core.domain.AgendaVoteValue.NO;
import static dev.lucasmattos.cooperative_vote.core.domain.AgendaVoteValue.YES;
import static java.util.Objects.nonNull;

import dev.lucasmattos.cooperative_vote.core.domain.AgendaSession;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaSessionStatus;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaSessionGateway;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaVoteGateway;
import dev.lucasmattos.cooperative_vote.infra.config.queue.QueueClient;
import dev.lucasmattos.cooperative_vote.infra.config.stereotype.UseCase;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public class CloseAndNotifyAgendaSessionFinished {
    private AgendaSessionGateway agendaSessionGateway;
    private AgendaVoteGateway agendaVoteGateway;
    private QueueClient votingSessionEndedQueueClient;

    public record Response(UUID agendaId, long votedYes, long votedNo) {}

    public void execute() {
        final List<AgendaSession> agendaSessionsToCloseAndNotify =
                agendaSessionGateway.findAllWithStatusOpenAndEndAtBeforeNow();

        AtomicReference<RuntimeException> exception = new AtomicReference<>();

        agendaSessionsToCloseAndNotify.parallelStream().forEach(agendaSession -> {
            try {
                agendaSession.setStatus(AgendaSessionStatus.CLOSED);

                final UUID agendaId = agendaSession.getAgenda().getId();
                final long votedYes = agendaVoteGateway.countByAgendaAndValue(agendaId, YES);
                final long votedNo = agendaVoteGateway.countByAgendaAndValue(agendaId, NO);
                votingSessionEndedQueueClient.send(new Response(agendaSession.getId(), votedYes, votedNo));

                agendaSessionGateway.save(agendaSession);
            } catch (RuntimeException e) {
                exception.set(e);
            }
        });

        if (nonNull(exception.get())) {
            throw exception.get();
        }
    }
}
