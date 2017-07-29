package stress.tests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import com.example.recruitment.task.Application;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

/**
 * Created by Lukasz S. on 18.05.2017.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@WebAppConfiguration
public class RepositoriesControllerStressTesting {

    @Autowired
    private WebApplicationContext context;

    private final String TEST_VALID_URL = "/repositories/lukSroczynski/Blog";

    @Before
    public void rest_assured_inicialization_with_the_web_application_context() {
        RestAssuredMockMvc.webAppContextSetup(context);
    }

    @After
    public void reset_rest_assured_after_each_test() {
        RestAssuredMockMvc.reset();
    }

    @Test
    public void stress_testing_for_rate_limit() {

        try {
            for (int i = 0; i < 100; i++) {
                given().when().async().get(TEST_VALID_URL);
            }
            given().when().async().get(TEST_VALID_URL);
        } catch (Exception e) {
            return;
        }
    }
}
