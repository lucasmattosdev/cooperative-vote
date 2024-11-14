package dev.lucasmattos.cooperative_vote.entrypoint;

import dev.lucasmattos.cooperative_vote.core.usecase.agenda.CreateAgenda;
import dev.lucasmattos.cooperative_vote.core.usecase.agenda.FindAgenda;
import io.swagger.v3.oas.annotations.Operation;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/agenda")
@RequiredArgsConstructor
public class AgendaV2Controller {

    private final CreateAgenda createAgenda;
    private final FindAgenda findAgenda;

    @Operation(summary = "Cadastrar uma nova pauta")
    @PostMapping
    public CreateAgenda.Response postCreateAgenda() {
        return createAgenda.execute();
    }

    @Operation(summary = "Mostra o resultado da pauta contabilizando todos os votos")
    @GetMapping("/{agendaId}")
    public FindAgenda.ResponseV2 getFindAgenda(@PathVariable final UUID agendaId) {
        return findAgenda.executeV2(agendaId);
    }
}
