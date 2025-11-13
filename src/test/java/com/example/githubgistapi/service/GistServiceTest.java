package com.example.githubgistapi.service;

import com.example.githubgistapi.model.Gist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class GistServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec<?> uriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> headersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private GistService gistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(webClientBuilder.build()).thenReturn(webClient);
        gistService = new GistService(webClientBuilder);
    }

    @Test
    void testGetGists() {
        Gist gist1 = new Gist();
        gist1.setId("1");
        Gist gist2 = new Gist();
        gist2.setId("2");

        // Raw-type workaround to avoid generics clash
        when(webClient.get()).thenReturn((WebClient.RequestHeadersUriSpec) uriSpec);
        when(uriSpec.uri("https://api.github.com/gists/public")).thenReturn((WebClient.RequestHeadersSpec) headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(Gist.class)).thenReturn(Flux.just(gist1, gist2));

        StepVerifier.create(gistService.getGists())
                .expectNext(gist1)
                .expectNext(gist2)
                .verifyComplete();
    }

    @Test
    void testGetGistsByUsername() {
        String username = "rajesh";
        Gist gist = new Gist();
        gist.setId("abc");

        when(webClient.get()).thenReturn((WebClient.RequestHeadersUriSpec) uriSpec);
        when(uriSpec.uri("https://api.github.com/users/{username}/gists", username))
                .thenReturn((WebClient.RequestHeadersSpec) headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(Gist.class)).thenReturn(Flux.just(gist));

        StepVerifier.create(gistService.getGistsByUsername(username))
                .expectNext(gist)
                .verifyComplete();
    }
}
