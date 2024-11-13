package dev.lucasmattos.cooperative_vote.core.usecase.agenda_vote;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaSession;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaVote;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaVoteValue;
import dev.lucasmattos.cooperative_vote.core.domain.Associate;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaGateway;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaVoteGateway;
import dev.lucasmattos.cooperative_vote.core.gateway.AssociateGateway;
import dev.lucasmattos.cooperative_vote.core.usecase.exception.NotFoundException;
import dev.lucasmattos.cooperative_vote.core.usecase.exception.UseCaseException;
import dev.lucasmattos.cooperative_vote.infra.config.stereotype.UseCase;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class AddVoteInAgenda {
    private final AgendaVoteGateway agendaVoteGateway;
    private final AssociateGateway associateGateway;
    private final AgendaGateway agendaGateway;

    public AgendaVote execute(final UUID agendaId, final AgendaVoteValue voteValue, final UUID associateId) {
        final ZonedDateTime voteAt = ZonedDateTime.now();
        final Associate associate = associateGateway
                .findById(associateId)
                .orElseThrow(() -> new NotFoundException(Associate.class, associateId));

        checkAlreacyVoted(agendaId, associateId);

        final Agenda agenda =
                agendaGateway.findById(agendaId).orElseThrow(() -> new NotFoundException(Agenda.class, agendaId));

        final AgendaSession lastAgendaSession = Optional.ofNullable(agenda.getLastAgendaSession())
                .orElseThrow(
                        () -> new UseCaseException("session-not-opened", "There is no open session for this agenda"));

        checkSessionIsStillOpen(lastAgendaSession, voteAt);

        final AgendaVote agendaVoteToSave = AgendaVote.builder()
                .agenda(agenda)
                .value(voteValue)
                .votedAt(voteAt)
                .associate(associate)
                .build();

        return agendaVoteGateway.save(agendaVoteToSave);
    }

    private void checkAlreacyVoted(final UUID agendaId, final UUID associateId) {
        if (agendaVoteGateway.existsByAgendaAndAssociate(agendaId, associateId)) {
            throw new UseCaseException("associate-already-voted", "The associate has already voted in this agenda");
        }
    }

    private void checkSessionIsStillOpen(final AgendaSession lastAgendaSession, final ZonedDateTime voteAt) {
        if (voteAt.isAfter(lastAgendaSession.getEndAt())) {
            throw new UseCaseException("session-already-end", "The session for this agenda is already end");
        }
    }
}
