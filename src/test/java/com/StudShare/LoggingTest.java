package com.StudShare;

import com.StudShare.domain.SiteUser;
import com.StudShare.utils.PasswordService;
import com.StudShare.rest.logging.LoginAuthenticator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
    LoginAuthenticator loginAuthenticator;

    private static final String urlHost = "http://localhost:8080/rest/";
    String tokenSSID;


    @Transactional
    @Rollback(value = false)
    public SiteUser addExampleUser(String login, String password, String email) throws NoSuchProviderException, NoSuchAlgorithmException
    {

        SiteUser user;
        PasswordService passwordMatcher = new PasswordService();
        String salt = passwordMatcher.generateSalt();
        String hashPassword = passwordMatcher.getSecurePassword(password, salt);
        user = loginAuthenticator.getSiteUserManager().addSiteUser(new SiteUser(login, hashPassword, salt, email));

        return user;
    }


    @Test
    public void checkLoginTest() throws NoSuchProviderException, NoSuchAlgorithmException
    {

        String login = "user1",
                password = "pass1",
                email = "exampleemail1@com.pl";

        SiteUser siteUser = loginAuthenticator.getSiteUserManager().findSiteUserByLogin(login);
        if( siteUser == null)
            siteUser = addExampleUser(login, password, email);

        expect().statusCode(401).when().with().headers("login", "ghghgh", "password", "notmatchpassword").post(urlHost + "user/login");
        tokenSSID = given().headers("login", login, "password", password).when().post(urlHost + "user/login").then().statusCode(200).extract().path("auth_token");

        loginAuthenticator.getLogTokenManager().deleteLogToken(loginAuthenticator.getLogTokenManager().findLogTokenBySSID(tokenSSID)); //FIRST WE MUST DELETE TOKEN WHICH WAS CREATED WITH LOGGING.
        //TOKEN HAVE ALSO CONSTRAINT KEY idSiteUser
        loginAuthenticator.getSiteUserManager().deleteSiteUser(siteUser);

    }

    @Test
    public void checkLogoutTest() throws NoSuchProviderException, NoSuchAlgorithmException
    {
        String login = "user2",
                password = "pass2",
                email = "exampleemail2@com.pl";

        SiteUser siteUser = loginAuthenticator.getSiteUserManager().findSiteUserByLogin("user2");
        if(siteUser == null )
            siteUser = addExampleUser(login, password, email);


        given().headers("login", "bumbaja", "auth_token", "EXAMPLE_AUTH_TOKEN").when().post(urlHost + "user/logout").then().statusCode(401);

        tokenSSID = given().headers("login", login, "password", password).when().post(urlHost + "user/login").then().statusCode(200).extract().path("auth_token");
        given().headers("login", login, "auth_token", tokenSSID).when().post(urlHost + "user/logout").then().statusCode(204);


        //HERE WE DONT REMOVE TOKEN, BECAUSE TOKEN IS DELETING WHEN USER HAS BEEN LOGOUT
        loginAuthenticator.getSiteUserManager().deleteSiteUser(siteUser);

    }

}
