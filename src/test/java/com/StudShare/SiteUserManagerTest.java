package com.StudShare;

import com.StudShare.config.HibernateConfig;
import com.StudShare.domain.SiteUser;
import com.StudShare.domain.Token;
import com.StudShare.service.UserManagerDao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(StudShareApplication.class)
@Rollback(value = true)
@Transactional
public class SiteUserManagerTest
{

    @Autowired
    @Qualifier("userManager")
    UserManagerDao userManager;


    @Test
    public void checkAddingUser()
    {
        SiteUser user = userManager.addUser(new SiteUser("mateusz", "password", "salt", "example1@com.pl"));

        SiteUser userToTest = userManager.findUserById(user);

        assertNotNull(userToTest);
        assertEquals(userToTest.getIdSiteUser(), user.getIdSiteUser());
        assertEquals(userToTest.getUsername(), user.getUsername());
        assertEquals(userToTest.getHash(), user.getHash());


    }

    @Test
    public void checkDeletingUser()
    {
        SiteUser user = userManager.addUser(new SiteUser("user1993", "password", "salt", "example2@com.pl"));

        SiteUser userToTests = userManager.findUserById(user);

        assertNotNull(userToTests);
        assertEquals(userToTests.getIdSiteUser(), user.getIdSiteUser());
        assertEquals(userToTests.getUsername(), user.getUsername());
        assertEquals(userToTests.getHash(), user.getHash());


        userManager.deleteUser(userToTests);

        userToTests = userManager.findUserById(user);

        assertNull(userToTests);

    }

    @Test
    public void checkAddingToken()
    {
        SiteUser user = userManager.addUser(new SiteUser("henio", "password", "salt", "example3@com.pl"));
        Token token = userManager.addToken(new Token(user, "EXAMPLE-SSID-TOKEN"));
        Token tokenToTests = userManager.findTokenById(token);

        assertNotNull(tokenToTests);
        assertEquals(tokenToTests.getSiteUser().getIdSiteUser(), token.getSiteUser().getIdSiteUser());
        assertEquals(tokenToTests.getToken(), token.getToken());
    }

    @Test
    public void checkDeletingToken()
    {
        SiteUser user = userManager.addUser(new SiteUser("GoodBoy", "password", "salt", "example4@com.pl"));
        Token token = userManager.addToken(new Token(user, "EXAMPLE_SSID_TOKEN_FOR_DELETING"));

        Token tokenToTests = userManager.findTokenById(token);

        assertNotNull(tokenToTests);

        userManager.deleteToken(tokenToTests);

        tokenToTests = userManager.findTokenById(token);

        assertNull(tokenToTests);
    }

    @Test
    public void checkFindingUserByUsername()
    {
        SiteUser user = userManager.addUser(new SiteUser("KOFLXYHBSA", "password", "salt", "example5@com.pl"));
        SiteUser userToTests = userManager.findUserByUsername("KOFLXYHBSA");

        assertEquals(userToTests.getIdSiteUser(), user.getIdSiteUser());
        assertEquals(userToTests.getUsername(), user.getUsername());
        assertEquals(userToTests.getHash(), user.getHash());

    }

    @Test
    public void checkFindingUserById()
    {
        SiteUser user = userManager.addUser(new SiteUser("UserMaster", "password", "salt", "example6@com.pl"));
        SiteUser userToTests = userManager.findUserById(user);

        assertEquals(userToTests.getIdSiteUser(), user.getIdSiteUser());
        assertEquals(userToTests.getUsername(), user.getUsername());
        assertEquals(userToTests.getHash(), user.getHash());
    }

    @Test
    public void checkFindingTokenBySSID()
    {
        SiteUser user = userManager.addUser(new SiteUser("Kasia", "password", "salt", "example7@com.pl"));
        Token token = userManager.addToken(new Token(user, "FAJNY TOKEN"));
        Token tokenToTests = userManager.findTokenBySSID("FAJNY TOKEN");

        assertEquals(tokenToTests.getIdToken(), token.getIdToken());
        assertEquals(tokenToTests.getToken(), token.getToken());
        assertEquals(tokenToTests.getSiteUser().getIdSiteUser(), token.getSiteUser().getIdSiteUser());
    }
}
