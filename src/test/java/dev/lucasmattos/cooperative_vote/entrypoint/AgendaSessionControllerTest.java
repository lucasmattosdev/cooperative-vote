package dev.lucasmattos.cooperative_vote.entrypoint;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.lucasmattos.cooperative_vote.core.domain.AgendaSession;
import dev.lucasmattos.cooperative_vote.core.usecase.agenda_session.CloseAndNotifyAgendaSessionFinished;
import dev.lucasmattos.cooperative_vote.core.usecase.agenda_session.OpenSessionInAgenda;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@WebAppConfiguration
@ContextConfiguration(classes = {AgendaSessionController.class})
class AgendaSessionControllerTest {
    @MockBean
    private OpenSessionInAgenda openSessionInAgenda;

    @MockBean
    private CloseAndNotifyAgendaSessionFinished closeAndNotifyAgendaSessionFinished;

    @Autowired
    private AgendaSessionController agendaSessionController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldPostOpenSessionInAgenda() throws Exception {
        final UUID agendaId = UUID.randomUUID();
        final OpenSessionInAgenda.Request request = new OpenSessionInAgenda.Request(10L);
        final AgendaSession expectedResult =
                AgendaSession.builder().id(UUID.randomUUID()).build();
        when(openSessionInAgenda.execute(agendaId, request)).thenReturn(expectedResult);

        mockMvc.perform(post("/agenda/" + agendaId + "/session")
                        .contentType(APPLICATION_JSON)
                        .content("{\"minutes\": 10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(Matchers.is(expectedResult.getId().toString())));
    }

    @Test
    void shouldCloseAndNotifyAgendaSessionFinished() {
        agendaSessionController.closeAndNotifyAgendaSessionFinished();

        verify(closeAndNotifyAgendaSessionFinished).execute();
    }
}
