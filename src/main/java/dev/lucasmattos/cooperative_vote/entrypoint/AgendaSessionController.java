package dev.lucasmattos.cooperative_vote.entrypoint;

import dev.lucasmattos.cooperative_vote.core.domain.AgendaSession;
import dev.lucasmattos.cooperative_vote.core.usecase.agenda_session.CloseAndNotifyAgendaSessionFinished;
import dev.lucasmattos.cooperative_vote.core.usecase.agenda_session.OpenSessionInAgenda;
import io.swagger.v3.oas.annotations.Operation;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agenda")
@RequiredArgsConstructor
public class AgendaSessionController {

    private final OpenSessionInAgenda openSessionInAgenda;
    private final CloseAndNotifyAgendaSessionFinished closeAndNotifyAgendaSessionFinished;

    @Operation(summary = "Abrir uma sessão de votação em uma pauta", description = "O valor padrão para minutes é 1")
    @PostMapping("{agendaId}/session")
    public AgendaSession postOpenSessionInAgenda(
            @PathVariable final UUID agendaId, @RequestBody final OpenSessionInAgenda.Request request) {
        return openSessionInAgenda.execute(agendaId, request);
    }

    @Scheduled(fixedRate = 3000)
    public void closeAndNotifyAgendaSessionFinished() {
        closeAndNotifyAgendaSessionFinished.execute();
    }
}
