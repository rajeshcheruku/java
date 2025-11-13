package com.example.githubgistapi.service;

import com.example.githubgistapi.model.Gist;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class GistService {

    private final WebClient webClient;

    public GistService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    // Get all public gists
    public Flux<Gist> getGists() {
        return webClient.get()
                .uri("https://api.github.com/gists/public")
                .retrieve()
                .bodyToFlux(Gist.class);
    }

    // ✅ NEW METHOD: Get gists for a specific username
    public Flux<Gist> getGistsByUsername(String username) {
        return webClient.get()
                .uri("https://api.github.com/users/{username}/gists", username)
                .retrieve()
                .bodyToFlux(Gist.class);
    }
}
