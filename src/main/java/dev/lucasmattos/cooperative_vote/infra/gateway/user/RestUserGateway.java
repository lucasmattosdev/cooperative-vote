package dev.lucasmattos.cooperative_vote.infra.gateway.user;

import dev.lucasmattos.cooperative_vote.core.domain.external.User;
import dev.lucasmattos.cooperative_vote.core.gateway.UserGateway;
import dev.lucasmattos.cooperative_vote.infra.config.stereotype.Gateway;
import dev.lucasmattos.cooperative_vote.infra.gateway.exception.GatewayException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Gateway
@RequiredArgsConstructor
public class RestUserGateway implements UserGateway {
    private final RestClient userInfoRestClient;

    @Override
    public Optional<User> findBy(String document) {
        try {
            ResponseEntity<User> response =
                    userInfoRestClient.get().uri("/" + document).retrieve().toEntity(User.class);

            if (response.getStatusCode().value() == HttpStatus.OK.value()) {
                return Optional.ofNullable(response.getBody());
            }
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new GatewayException(
                    "user-gateway", "An error occurred while processing the request on user gateway", e);
        }

        throw new GatewayException("user-gateway", "The user gateway returned an unprocessable status");
    }
}
