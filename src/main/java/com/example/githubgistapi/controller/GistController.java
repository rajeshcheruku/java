package com.example.githubgistapi.controller;

import com.example.githubgistapi.model.Gist;
import com.example.githubgistapi.service.GistService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class GistController {

    private final GistService gistService;

    public GistController(GistService gistService) {
        this.gistService = gistService;
    }

    @GetMapping("/gists")
    public Flux<Gist> getGists() {
        return gistService.getGists();
    }

    @GetMapping("/gists/user/{username}")
    public Flux<Gist> getGistsByUsername(@PathVariable String username) {
        return gistService.getGistsByUsername(username);
    }
}