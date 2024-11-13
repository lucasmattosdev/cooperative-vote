package dev.lucasmattos.cooperative_vote.entrypoint;

import dev.lucasmattos.cooperative_vote.core.domain.AgendaVote;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaVoteValue;
import dev.lucasmattos.cooperative_vote.core.usecase.agenda_vote.AddVoteInAgenda;
import io.swagger.v3.oas.annotations.Operation;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agenda")
@RequiredArgsConstructor
public class AgendaVoteController {

    private final AddVoteInAgenda addVoteInAgenda;

    @Operation(summary = "Adicionar voto em uma pauta")
    @PostMapping("/{agendaId}/vote/{agendaVoteValue}")
    public AgendaVote postAddVoteInAgenda(
            @PathVariable final UUID agendaId,
            @PathVariable final AgendaVoteValue agendaVoteValue,
            @RequestHeader final UUID userId) {
        return addVoteInAgenda.execute(agendaId, agendaVoteValue, userId);
    }
}
