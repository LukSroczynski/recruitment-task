package parallel.tests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import com.example.recruitment.task.Application;
import com.example.recruitment.task.domain.GithubCredentials;
import com.example.recruitment.task.service.RepositoriesService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.hamcrest.Matchers;

import java.util.concurrent.ExecutionException;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

import stress.tests.RepositoriesControllerStressTesting;

import static org.junit.Assert.assertThat;

/**
 * Created by Lukasz S. on 03.06.2017.
 */

/**
 * Class created to simulate/test requests that are handled in parallel
 * NOTE: Make sure that GithubCreadentials still exists
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@WebAppConfiguration
public class ParallelTest {

    @Autowired
    private WebApplicationContext context;

    private static final Logger logger = LoggerFactory.getLogger(RepositoriesControllerStressTesting.class);

    private final String TEST_USER1_USERNAME = "getify";
    private final String TEST_USER1_REPO = "You-Dont-Know-JS";

    private final String TEST_USER2_USERNAME ="shiffman";
    private final String TEST_USER2_REPO = "The-Nature-of-Code-Examples";

    private final String TEST_USER3_USERNAME ="lukSroczynski";
    private final String TEST_USER3_REPO = "Notes";

    private final Long MAX_PARALLEL_RESPONSE_TIME = 500L;

    @Autowired
    private RepositoriesService gitHubLookupService;

    @Before
    public void rest_assured_inicialization_with_the_web_application_context() {
        RestAssuredMockMvc.webAppContextSetup(context);
    }

    @After
    public void reset_rest_assured_after_each_test() {
        RestAssuredMockMvc.reset();
    }

    /**
     * NOTE: If you want to see that async works just turn off @EnableAsync annotation from WebConfig. Much more time will elapse to perform requests.
     * e.g.
     * With async: 4-10 [ms]
     * Without async: 1000+ [ms]
     */
    @Test
    public void async_test() throws ExecutionException, InterruptedException {

        long start = System.currentTimeMillis();

        gitHubLookupService.getRepository(new GithubCredentials(TEST_USER1_USERNAME, TEST_USER1_REPO));
        gitHubLookupService.getRepository(new GithubCredentials(TEST_USER2_USERNAME, TEST_USER2_REPO));
        gitHubLookupService.getRepository(new GithubCredentials(TEST_USER3_USERNAME, TEST_USER3_REPO));

        long diff = System.currentTimeMillis() - start;

        assertThat(diff, Matchers.lessThan(MAX_PARALLEL_RESPONSE_TIME));

    }
}
