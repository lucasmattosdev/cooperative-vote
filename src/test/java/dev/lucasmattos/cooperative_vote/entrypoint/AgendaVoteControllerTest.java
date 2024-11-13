package dev.lucasmattos.cooperative_vote.entrypoint;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.lucasmattos.cooperative_vote.core.domain.AgendaVote;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaVoteValue;
import dev.lucasmattos.cooperative_vote.core.usecase.agenda_vote.AddVoteInAgenda;
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
@ContextConfiguration(classes = {AgendaVoteController.class})
class AgendaVoteControllerTest {
    @MockBean
    private AddVoteInAgenda addVoteInAgenda;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldPostOpenSessionInAgenda() throws Exception {
        final UUID agendaId = UUID.randomUUID();
        final AgendaVoteValue value = AgendaVoteValue.YES;
        final UUID userId = UUID.randomUUID();
        final AgendaVote expectedResult =
                AgendaVote.builder().id(UUID.randomUUID()).build();
        when(addVoteInAgenda.execute(agendaId, value, userId)).thenReturn(expectedResult);

        mockMvc.perform(post("/agenda/" + agendaId + "/vote/" + value)
                        .contentType(APPLICATION_JSON)
                        .header("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(Matchers.is(expectedResult.getId().toString())));
    }
}
