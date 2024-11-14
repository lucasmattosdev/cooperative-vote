package dev.lucasmattos.cooperative_vote.infra.gateway.user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import dev.lucasmattos.cooperative_vote.core.domain.external.User;
import dev.lucasmattos.cooperative_vote.infra.gateway.exception.GatewayException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

@ExtendWith(MockitoExtension.class)
class RestUserGatewayTest {
    private final RestClient.Builder restClient = RestClient.builder();

    private final MockRestServiceServer server =
            MockRestServiceServer.bindTo(restClient).build();

    private final RestUserGateway restUserGateway = new RestUserGateway(restClient.build());

    @Test
    void shouldFindWithSuccess() {
        final String document = "12345678900";
        final User expectedUser = new User(User.UserStatus.ABLE_TO_VOTE);

        this.server
                .expect(requestTo("/" + document))
                .andRespond(withSuccess("{\"status\":\"ABLE_TO_VOTE\"}", MediaType.APPLICATION_JSON));

        final Optional<User> result = restUserGateway.findBy(document);

        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
    }

    @Test
    void shouldReturnEmptyWhenReturnNotFound() {
        final String document = "12345678900";

        this.server
                .expect(requestTo("/" + document))
                .andRespond(withStatus(HttpStatus.NOT_FOUND)
                        .body("{\"message\":\"not found user\"}")
                        .contentType(MediaType.APPLICATION_JSON));

        final Optional<User> result = restUserGateway.findBy(document);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldThrownExceptionWhenStatusIsInternalError() {
        final String document = "12345678900";

        this.server
                .expect(requestTo("/" + document))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"message\":\"internal error\"}")
                        .contentType(MediaType.APPLICATION_JSON));

        assertThatThrownBy(() -> restUserGateway.findBy(document))
                .isInstanceOf(GatewayException.class)
                .hasMessage("An error occurred while processing the request on user gateway");
    }

    @Test
    void shouldThrownExceptionWhenStatusIsNoContent() {
        final String document = "12345678900";

        this.server.expect(requestTo("/" + document)).andRespond(withStatus(HttpStatus.NO_CONTENT));

        assertThatThrownBy(() -> restUserGateway.findBy(document))
                .isInstanceOf(GatewayException.class)
                .hasMessage("The user gateway returned an unprocessable status");
    }
}
