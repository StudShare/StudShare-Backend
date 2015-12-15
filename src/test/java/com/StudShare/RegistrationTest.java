package com.StudShare;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jayway.restassured.RestAssured.expect;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(StudShareApplication.class)
public class RegistrationTest
{
    private static final String urlHost = "http://localhost:8080/rest/";


    @Test
    public void checkRegistrationUser()
    {
        expect().statusCode(400).when().with().headers("login", "user").post(urlHost + "user/register");

        expect().statusCode(400).when().with().headers("login", "user", "password", "password").post(urlHost + "user/register");

        expect().statusCode(400).when().with().headers("login", "user", "password", "password",
                "repeatPassword", "repeatPassword").post(urlHost + "user/register");

        expect().statusCode(400).when().with().headers("login", "user", "password", "password",
                "repeat_password", "password", "email", "email@com.com").post(urlHost + "user/register");

        expect().statusCode(400).when().with().headers("login", "user", "password", "password",
                "repeat_password", "password", "email", "email@com.com", "repeat_email", "email").post(urlHost + "user/register");

        String activationLink = expect().statusCode(200).when().with().headers("login", "user", "password", "password",
                "repeat_password", "password", "email", "email@com.com", "repeat_email", "email@com.com").post(urlHost + "user/register").asString();

        expect().statusCode(200).when().get(activationLink);

    }

}
