package com.example.recruitment.task.web;


import com.example.recruitment.task.domain.GithubCredentials;
import com.example.recruitment.task.domain.GithubRepositoryDetails;
import com.example.recruitment.task.service.RepositoriesService;
import com.example.recruitment.task.util.CredentialsErrorHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Lukasz S. on 09.05.2017.
 */

@RestController
public class RepositoriesController {

	@Autowired
	private RepositoriesService repositoriesService;

    private CredentialsErrorHandler credentialsErrorHandler = new CredentialsErrorHandler();

    private final static Logger LOGGER = Logger.getLogger(RepositoriesController.class.getName());

    @RequestMapping(value = "/repositories/{owner}/{repository-name}", method = RequestMethod.GET)
	public ResponseEntity<?> getRepositoryDetails(
            @PathVariable String owner,
            @PathVariable("repository-name") String repositoryName) {

	        try {

                Future<GithubRepositoryDetails> future = repositoriesService.getRepository(
                        new GithubCredentials(owner, repositoryName));

                while (true)
                    if (future.isDone())
                        return new ResponseEntity<>(future.get(), HttpStatus.OK);

	        } catch (ExecutionException e) {

                LOGGER.log(Level.WARNING, e.getMessage());

                credentialsErrorHandler.setMessage("GitHub credentials incorrect or API calls exceeded.");
                credentialsErrorHandler.setDetailed_message(e.getMessage());

                return new ResponseEntity<>(credentialsErrorHandler, HttpStatus.NOT_FOUND);

            } catch (Exception e) {

                LOGGER.log(Level.WARNING, e.getMessage());

                credentialsErrorHandler.setMessage("Unidentified error occurred");
                credentialsErrorHandler.setDetailed_message(e.getMessage());

                return new ResponseEntity<>(credentialsErrorHandler, HttpStatus.NOT_FOUND);
            }
    }
}