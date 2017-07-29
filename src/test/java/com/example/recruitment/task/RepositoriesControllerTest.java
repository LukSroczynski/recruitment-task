package com.example.recruitment.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.hamcrest.Matchers;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

import static org.hamcrest.Matchers.lessThan;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@WebAppConfiguration
public class RepositoriesControllerTest {

    @Autowired
    private WebApplicationContext context;

    private final String TEST_VALID_URL = "/repositories/lukSroczynski/Blog";
    private final String TEST_INVALID_URL = "/2q4r6tbv8h0rt9h75g5d3f1qw";

    private final Long MAX_RESPONSE_TIME = 7000L;

    @Before
    public void rest_assured_inicialization_with_the_web_application_context() {
        RestAssuredMockMvc.webAppContextSetup(context);
    }

    @After
    public void reset_rest_assured_after_each_test() {
        RestAssuredMockMvc.reset();
    }

    @Test
    public void validation_repository_details() {

        given().
                when().
                    get(TEST_VALID_URL).
                then().
                    statusCode(200).
                        body("full_name", Matchers.is("LukSroczynski/Blog")).
                        body("description", Matchers.is("Blog - template")).
                        body("clone_url", Matchers.is("https://github.com/LukSroczynski/Blog.git")).
                        body("stargazers_count", Matchers.is("0")).
                        body("created_at", Matchers.is("2016-11-06"));
    }

    @Test
    public void validate_response_time() {

        given().
                when().
                    get(TEST_VALID_URL).
                then().
                    statusCode(200).
                    time(lessThan(MAX_RESPONSE_TIME));
    }

    @Test
    public void validate_invalid_url() {

        given().
                when().
                    get(TEST_INVALID_URL).
                then().
                    statusCode(404);
    }
}