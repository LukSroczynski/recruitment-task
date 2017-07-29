package com.example.recruitment.task.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.recruitment.task.domain.GithubCredentials;
import com.example.recruitment.task.domain.GithubRepositoryDetails;

import java.util.concurrent.Future;

/**
 * Created by Lukasz S. on 09.05.2017.
 */

@Service
public class RepositoriesService {

    private final String REST_API = "https://api.github.com/";
    private final String REPOS = "repos/";

    private final RestTemplate restTemplate;

    public RepositoriesService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async
    public Future<GithubRepositoryDetails> getRepository(GithubCredentials githubCredentials) {

        String url =
                String.format(
                        REST_API + REPOS + "%s/%s",
                        githubCredentials.getOwner(),
                        githubCredentials.getRepositoryName());

        GithubRepositoryDetails results = restTemplate.getForObject(url, GithubRepositoryDetails.class);

        return new AsyncResult<>(results);
    }
}