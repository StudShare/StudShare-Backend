package com.StudShare;

import com.StudShare.domain.LoginToken;
import com.StudShare.domain.SiteUser;
import com.StudShare.service.UserManagerDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
    UserManagerDao userManager;


    @Test
    public void checkAddingUser()
    {
        SiteUser user = userManager.addUser(new SiteUser("mateusz", "password", "salt", "example1@com.pl"));

        SiteUser userToTest = userManager.findUserById(user);

        assertNotNull(userToTest);
        assertEquals(userToTest.getIdSiteUser(), user.getIdSiteUser());
        assertEquals(userToTest.getLogin(), user.getLogin());
        assertEquals(userToTest.getHash(), user.getHash());


    }

    @Test
    public void checkDeletingUser()
    {
        SiteUser user = userManager.addUser(new SiteUser("user1993", "password", "salt", "example2@com.pl"));

        SiteUser userToTests = userManager.findUserById(user);

        assertNotNull(userToTests);
        assertEquals(userToTests.getIdSiteUser(), user.getIdSiteUser());
        assertEquals(userToTests.getLogin(), user.getLogin());
        assertEquals(userToTests.getHash(), user.getHash());


        userManager.deleteUser(userToTests);

        userToTests = userManager.findUserById(user);

        assertNull(userToTests);

    }

    @Test
    public void checkAddingToken()
    {
        SiteUser user = userManager.addUser(new SiteUser("henio", "password", "salt", "example3@com.pl"));
        LoginToken loginToken = userManager.addToken(new LoginToken(user, "EXAMPLE-SSID-TOKEN"));
        LoginToken loginTokenToTests = userManager.findTokenById(loginToken);

        assertNotNull(loginTokenToTests);
        assertEquals(loginTokenToTests.getSiteUser().getIdSiteUser(), loginToken.getSiteUser().getIdSiteUser());
        assertEquals(loginTokenToTests.getSsid(), loginToken.getSsid());
    }

    @Test
    public void checkDeletingToken()
    {
        SiteUser user = userManager.addUser(new SiteUser("GoodBoy", "password", "salt", "example4@com.pl"));
        LoginToken loginToken = userManager.addToken(new LoginToken(user, "EXAMPLE_SSID_TOKEN_FOR_DELETING"));

        LoginToken loginTokenToTests = userManager.findTokenById(loginToken);

        assertNotNull(loginTokenToTests);

        userManager.deleteToken(loginTokenToTests);

        loginTokenToTests = userManager.findTokenById(loginToken);

        assertNull(loginTokenToTests);
    }

    @Test
    public void checkFindingUserByLogin()
    {
        SiteUser user = userManager.addUser(new SiteUser("KOFLXYHBSA", "password", "salt", "example5@com.pl"));
        SiteUser userToTests = userManager.findUserByLogin("KOFLXYHBSA");

        assertEquals(userToTests.getIdSiteUser(), user.getIdSiteUser());
        assertEquals(userToTests.getLogin(), user.getLogin());
        assertEquals(userToTests.getHash(), user.getHash());

    }

    @Test
    public void checkFindingUserById()
    {
        SiteUser user = userManager.addUser(new SiteUser("UserMaster", "password", "salt", "example6@com.pl"));
        SiteUser userToTests = userManager.findUserById(user);

        assertEquals(userToTests.getIdSiteUser(), user.getIdSiteUser());
        assertEquals(userToTests.getLogin(), user.getLogin());
        assertEquals(userToTests.getHash(), user.getHash());
    }

    @Test
    public void checkFindingTokenBySSID()
    {
        SiteUser user = userManager.addUser(new SiteUser("Kasia", "password", "salt", "example7@com.pl"));
        LoginToken loginToken = userManager.addToken(new LoginToken(user, "FAJNY TOKEN"));
        LoginToken loginTokenToTests = userManager.findTokenBySSID("FAJNY TOKEN");

        assertEquals(loginTokenToTests.getIdToken(), loginToken.getIdToken());
        assertEquals(loginTokenToTests.getSsid(), loginToken.getSsid());
        assertEquals(loginTokenToTests.getSiteUser().getIdSiteUser(), loginToken.getSiteUser().getIdSiteUser());
    }
}
