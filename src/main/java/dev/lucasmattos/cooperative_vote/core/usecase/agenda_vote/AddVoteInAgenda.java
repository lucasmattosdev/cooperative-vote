package dev.lucasmattos.cooperative_vote.core.usecase.agenda_vote;

import static dev.lucasmattos.cooperative_vote.core.domain.external.User.UserStatus.ABLE_TO_VOTE;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaSession;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaVote;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaVoteValue;
import dev.lucasmattos.cooperative_vote.core.domain.Associate;
import dev.lucasmattos.cooperative_vote.core.domain.external.User;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaGateway;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaVoteGateway;
import dev.lucasmattos.cooperative_vote.core.gateway.AssociateGateway;
import dev.lucasmattos.cooperative_vote.core.gateway.UserGateway;
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
    private final UserGateway userGateway;

    public AgendaVote execute(final UUID agendaId, final AgendaVoteValue voteValue, final UUID associateId) {
        final ZonedDateTime voteAt = ZonedDateTime.now();
        final Associate associate = associateGateway
                .findById(associateId)
                .orElseThrow(() -> new NotFoundException(Associate.class, associateId));

        checkAlreadyVoted(agendaId, associateId);
        checkAssociateIsAbleToVote(associate);

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

    private void checkAlreadyVoted(final UUID agendaId, final UUID associateId) {
        if (agendaVoteGateway.existsByAgendaAndAssociate(agendaId, associateId)) {
            throw new UseCaseException("associate-already-voted", "The associate has already voted in this agenda");
        }
    }

    private void checkAssociateIsAbleToVote(final Associate associate) {
        Optional<User> user = userGateway.findBy(associate.getDocument());
        if (user.isEmpty() || !user.get().status().equals(ABLE_TO_VOTE)) {
            throw new UseCaseException("associate-not-able-to-vote", "The associate is not able to vote");
        }
    }

    private void checkSessionIsStillOpen(final AgendaSession lastAgendaSession, final ZonedDateTime voteAt) {
        if (voteAt.isAfter(lastAgendaSession.getEndAt())) {
            throw new UseCaseException("session-already-end", "The session for this agenda is already end");
        }
    }
}
