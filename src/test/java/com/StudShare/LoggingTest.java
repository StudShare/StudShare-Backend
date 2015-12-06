package com.StudShare;

import com.StudShare.domain.SiteUser;
import com.StudShare.rest.PasswordMatcher;
import com.StudShare.rest.logging.AuthenticatorLogin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(StudShareApplication.class)
public class LoggingTest
{
    @Autowired
    @Qualifier("authenticatorLogin")
    AuthenticatorLogin authenticatorLogin;

    private static final String urlHost = "http://localhost:8080/rest/";
    String tokenSSID;


    @Transactional
    @Rollback(value = false)
    public SiteUser addExampleUser(String username, String password, String email) throws NoSuchProviderException, NoSuchAlgorithmException
    {
        SiteUser user;
        PasswordMatcher passwordMatcher = new PasswordMatcher();
        String salt = passwordMatcher.generateSalt();
        String hashPassword = passwordMatcher.getSecurePassword(password, salt);
        user = authenticatorLogin.getUserManager().addUser(new SiteUser(username, hashPassword, salt , email));

        return user;
    }


    @Test
    public void checkLoginTest() throws NoSuchProviderException, NoSuchAlgorithmException
    {
        String username = "user1",
                password = "pass1",
                email = "exampleemail1@com.pl";

        SiteUser siteUser = authenticatorLogin.getUserManager().findUserByUsername("user1");
        if( siteUser == null)
            siteUser = addExampleUser(username, password, email);

        expect().statusCode(401).when().with().headers("username", "ghghgh", "password", "notmatchpassword").post(urlHost + "user/login");
        tokenSSID = given().headers("username", username, "password", password).when().post(urlHost + "user/login").then().statusCode(200).extract().path("auth_token");

        authenticatorLogin.getUserManager().deleteToken(authenticatorLogin.getUserManager().findTokenBySSID(tokenSSID)); //FIRST WE MUST DELETE TOKEN WHICH WAS CREATED WITH LOGGING.
        //TOKEN HAVE ALSO CONSTRAINT KEY idSiteUser
        authenticatorLogin.getUserManager().deleteUser(siteUser);

    }

    @Test
    public void checkLogoutTest() throws NoSuchProviderException, NoSuchAlgorithmException
    {
        String username = "user2",
                password = "pass2",
                email = "exampleemail2@com.pl";

        SiteUser siteUser = authenticatorLogin.getUserManager().findUserByUsername("user2");
        if(siteUser == null )
            siteUser = addExampleUser(username, password, email);


        given().headers("username", "bumbaja", "auth_token", "EXAMPLE_AUTH_TOKEN").when().post(urlHost + "user/logout").then().statusCode(401);

        tokenSSID = given().headers("username", username, "password", password).when().post(urlHost + "user/login").then().statusCode(200).extract().path("auth_token");
        given().headers("username", username, "auth_token", tokenSSID).when().post(urlHost + "user/logout").then().statusCode(204);


        //HERE WE DONT REMOVE TOKEN, BECAUSE TOKEN IS DELETING WHEN USER HAS BEEN LOGOUT
        authenticatorLogin.getUserManager().deleteUser(siteUser);

    }

}
