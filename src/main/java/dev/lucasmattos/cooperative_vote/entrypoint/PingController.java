package dev.lucasmattos.cooperative_vote.entrypoint;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @Operation(summary = "Checar a saúde da aplicação")
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
