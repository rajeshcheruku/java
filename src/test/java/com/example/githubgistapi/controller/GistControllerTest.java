package com.example.githubgistapi.controller;

import com.example.githubgistapi.model.Gist;
import com.example.githubgistapi.service.GistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(GistController.class)
class GistControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GistService gistService;

    @Test
    void testGetGists_Success() {
        Gist gist1 = new Gist("1", "First gist", "https://gist.github.com/1");
        Gist gist2 = new Gist("2", "Second gist", "https://gist.github.com/2");
        
        when(gistService.getGists()).thenReturn(Flux.just(gist1, gist2));

        webTestClient.get()
                .uri("/api/gists")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Gist.class)
                .hasSize(2)
                .contains(gist1, gist2);

        verify(gistService).getGists();
    }

    @Test
    void testGetGists_Empty() {
        when(gistService.getGists()).thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/api/gists")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Gist.class)
                .hasSize(0);

        verify(gistService).getGists();
    }

    @Test
    void testGetGists_SingleGist() {
        Gist gist = new Gist("123", "Single", "url");
        when(gistService.getGists()).thenReturn(Flux.just(gist));

        webTestClient.get()
                .uri("/api/gists")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Gist.class)
                .hasSize(1)
                .contains(gist);
    }

    @Test
    void testGetGists_MultipleGists() {
        Gist g1 = new Gist("1", "First", "url1");
        Gist g2 = new Gist("2", "Second", "url2");
        Gist g3 = new Gist("3", "Third", "url3");
        Gist g4 = new Gist("4", "Fourth", "url4");
        
        when(gistService.getGists()).thenReturn(Flux.just(g1, g2, g3, g4));

        webTestClient.get()
                .uri("/api/gists")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Gist.class)
                .hasSize(4);
    }

    @Test
    void testGetGistsByUsername_Success() {
        Gist gist = new Gist("1", "User gist", "https://gist.github.com/1");
        when(gistService.getGistsByUsername("testuser")).thenReturn(Flux.just(gist));

        webTestClient.get()
                .uri("/api/gists/user/testuser")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Gist.class)
                .hasSize(1)
                .contains(gist);

        verify(gistService).getGistsByUsername("testuser");
    }

    @Test
    void testGetGistsByUsername_Empty() {
        when(gistService.getGistsByUsername(anyString())).thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/api/gists/user/nonexistent")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Gist.class)
                .hasSize(0);

        verify(gistService).getGistsByUsername("nonexistent");
    }

    @Test
    void testGetGistsByUsername_MultipleGists() {
        Gist g1 = new Gist("1", "First", "url1");
        Gist g2 = new Gist("2", "Second", "url2");
        when(gistService.getGistsByUsername("octocat")).thenReturn(Flux.just(g1, g2));

        webTestClient.get()
                .uri("/api/gists/user/octocat")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Gist.class)
                .hasSize(2)
                .contains(g1, g2);
    }

    @Test
    void testGetGists_ServiceError() {
        when(gistService.getGists())
                .thenReturn(Flux.error(new RuntimeException("Service error")));

        webTestClient.get()
                .uri("/api/gists")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void testGetGistsByUsername_ServiceError() {
        when(gistService.getGistsByUsername(anyString()))
                .thenReturn(Flux.error(new RuntimeException("User not found")));

        webTestClient.get()
                .uri("/api/gists/user/baduser")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void testGetGistsByUsername_SpecialCharacters() {
        Gist gist = new Gist("1", "Special user gist", "https://gist.github.com/1");
        when(gistService.getGistsByUsername("user-with-dash")).thenReturn(Flux.just(gist));

        webTestClient.get()
                .uri("/api/gists/user/user-with-dash")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Gist.class)
                .hasSize(1);
    }

    @Test
    void testGetGistsByUsername_NumbersInUsername() {
        Gist gist = new Gist("1", "Test", "url");
        when(gistService.getGistsByUsername("user123")).thenReturn(Flux.just(gist));

        webTestClient.get()
                .uri("/api/gists/user/user123")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Gist.class)
                .hasSize(1);
    }

    @Test
    void testGetGists_NullDescription() {
        Gist gist = new Gist("1", null, "url");
        when(gistService.getGists()).thenReturn(Flux.just(gist));

        webTestClient.get()
                .uri("/api/gists")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Gist.class)
                .hasSize(1);
    }

    @Test
    void testApiBasePath() {
        when(gistService.getGists()).thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/api/gists")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testUserEndpointPattern() {
        Gist gist = new Gist("1", "Test", "url");
        when(gistService.getGistsByUsername("test")).thenReturn(Flux.just(gist));

        webTestClient.get()
                .uri("/api/gists/user/test")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Gist.class)
                .hasSize(1);

        verify(gistService).getGistsByUsername("test");
    }
}