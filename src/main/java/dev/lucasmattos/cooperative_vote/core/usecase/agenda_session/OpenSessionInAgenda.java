package dev.lucasmattos.cooperative_vote.core.usecase.agenda_session;

import static java.lang.String.format;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaSession;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaGateway;
import dev.lucasmattos.cooperative_vote.core.gateway.AgendaSessionGateway;
import dev.lucasmattos.cooperative_vote.core.usecase.exception.ConflictException;
import dev.lucasmattos.cooperative_vote.core.usecase.exception.NotFoundException;
import dev.lucasmattos.cooperative_vote.infra.config.stereotype.UseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.validation.annotation.Validated;

@UseCase
@Validated
@RequiredArgsConstructor
public class OpenSessionInAgenda {
    final AgendaGateway agendaGateway;
    final AgendaSessionGateway agendaSessionGateway;

    @Data
    public static class Request {
        @Positive
        Long minutes;
    }

    public AgendaSession execute(final UUID agendaId, @Valid final Request request) {
        final Agenda agenda =
                agendaGateway.findById(agendaId).orElseThrow(() -> new NotFoundException(Agenda.class, agendaId));
        final ZonedDateTime startAt = ZonedDateTime.now();
        final long minutes = firstNonNull(request.minutes, 1L);

        checkConflictSession(agenda, startAt);

        final AgendaSession agendaSessionToSave = AgendaSession.builder()
                .agenda(agenda)
                .durationInMinutes(minutes)
                .startAt(startAt)
                .endAt(startAt.plusMinutes(minutes))
                .build();

        return agendaSessionGateway.save(agendaSessionToSave);
    }

    private void checkConflictSession(final Agenda agenda, final ZonedDateTime startAt) {
        agenda.getAgendaSessions().forEach(agendaSession -> {
            if (agendaSession.getEndAt().isAfter(startAt)) {
                throw new ConflictException(
                        AgendaSession.class,
                        format("There is already an open session with id %s in this agenda", agendaSession.getId()));
            }
        });
    }
}
