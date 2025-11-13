package com.example.githubgistapi;

import com.example.githubgistapi.service.GistService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class GithubGistApiApplicationTest {

    // Mock the service so Spring doesn't need to create the real bean
    @MockBean
    private GistService gistService;

    @Test
    void contextLoads() {
        // just checks if Spring context loads successfully
    }

    @Test
    void mainMethodTest() {
        GithubGistApiApplication.main(new String[]{});
    }
}
