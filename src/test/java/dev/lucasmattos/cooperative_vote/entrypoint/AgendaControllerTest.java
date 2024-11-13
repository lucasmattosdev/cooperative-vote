package dev.lucasmattos.cooperative_vote.entrypoint;

import static dev.lucasmattos.cooperative_vote.core.domain.AgendaVoteValue.NO;
import static dev.lucasmattos.cooperative_vote.core.domain.AgendaVoteValue.YES;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.lucasmattos.cooperative_vote.core.domain.Agenda;
import dev.lucasmattos.cooperative_vote.core.domain.AgendaVote;
import dev.lucasmattos.cooperative_vote.core.usecase.agenda.CreateAgenda;
import dev.lucasmattos.cooperative_vote.core.usecase.agenda.FindAgenda;
import java.util.List;
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
@ContextConfiguration(classes = {AgendaController.class})
class AgendaControllerTest {
    @MockBean
    private CreateAgenda createAgenda;

    @MockBean
    private FindAgenda findAgenda;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateAgenda() throws Exception {
        final CreateAgenda.Response response =
                new CreateAgenda.Response(Agenda.builder().id(UUID.randomUUID()).build());
        when(createAgenda.execute()).thenReturn(response);

        mockMvc.perform(post("/agenda").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Matchers.is(response.id().toString())));
    }

    @Test
    void shouldGetAgenda() throws Exception {
        final Agenda agenda = Agenda.builder().id(UUID.randomUUID()).build();
        agenda.getAgendaVotes()
                .addAll(List.of(
                        AgendaVote.builder().value(NO).build(),
                        AgendaVote.builder().value(NO).build(),
                        AgendaVote.builder().value(NO).build(),
                        AgendaVote.builder().value(YES).build(),
                        AgendaVote.builder().value(YES).build()));
        final FindAgenda.Response response = new FindAgenda.Response(agenda);
        when(findAgenda.execute(agenda.getId())).thenReturn(response);

        mockMvc.perform(get("/agenda/" + agenda.getId()).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Matchers.is(agenda.getId().toString())))
                .andExpect(jsonPath("$.votedYes").value(Matchers.is(2)))
                .andExpect(jsonPath("$.votedNo").value(Matchers.is(3)));
    }
}
