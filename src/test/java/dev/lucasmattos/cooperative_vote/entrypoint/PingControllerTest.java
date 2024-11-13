package dev.lucasmattos.cooperative_vote.entrypoint;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@WebAppConfiguration
@ContextConfiguration(classes = {PingController.class})
class PingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnPongWhenCallPing() throws Exception {
        mockMvc.perform(get("/ping").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));
    }
}
