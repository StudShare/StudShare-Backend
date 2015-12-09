package com.StudShare;

import com.StudShare.domain.LogToken;
import com.StudShare.domain.SiteUser;
import com.StudShare.service.LogTokenManagerDao;
import com.StudShare.service.SiteUserManagerDao;
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
    SiteUserManagerDao siteUserManager;

    @Autowired
    LogTokenManagerDao loginTokenManager;

    @Test
    public void checkAddingSiteUser()
    {
        SiteUser user = siteUserManager.addSiteUser(new SiteUser("mateusz", "password", "salt", "example1@com.pl"));

        SiteUser userToTest = siteUserManager.findSiteUserById(user);

        assertNotNull(userToTest);
        assertEquals(userToTest.getIdSiteUser(), user.getIdSiteUser());
        assertEquals(userToTest.getLogin(), user.getLogin());
        assertEquals(userToTest.getHash(), user.getHash());


    }

    @Test
    public void checkDeletingSiteUser()
    {
        SiteUser user = siteUserManager.addSiteUser(new SiteUser("user1993", "password", "salt", "example2@com.pl"));

        SiteUser userToTests = siteUserManager.findSiteUserById(user);

        assertNotNull(userToTests);
        assertEquals(userToTests.getIdSiteUser(), user.getIdSiteUser());
        assertEquals(userToTests.getLogin(), user.getLogin());
        assertEquals(userToTests.getHash(), user.getHash());


        siteUserManager.deleteSiteUser(userToTests);

        userToTests = siteUserManager.findSiteUserById(user);

        assertNull(userToTests);

    }

    @Test
    public void checkAddingLogToken()
    {
        SiteUser user = siteUserManager.addSiteUser(new SiteUser("henio", "password", "salt", "example3@com.pl"));
        LogToken logToken = loginTokenManager.addLogToken(new LogToken(user, "EXAMPLE-SSID-TOKEN"));
        LogToken logTokenToTests = loginTokenManager.findLogTokenById(logToken);

        assertNotNull(logTokenToTests);
        assertEquals(logTokenToTests.getSiteUser().getIdSiteUser(), logToken.getSiteUser().getIdSiteUser());
        assertEquals(logTokenToTests.getSsid(), logToken.getSsid());
    }

    @Test
    public void checkDeletingLogToken()
    {
        SiteUser user = siteUserManager.addSiteUser(new SiteUser("GoodBoy", "password", "salt", "example4@com.pl"));
        LogToken logToken = loginTokenManager.addLogToken(new LogToken(user, "EXAMPLE_SSID_TOKEN_FOR_DELETING"));

        LogToken logTokenToTests = loginTokenManager.findLogTokenById(logToken);

        assertNotNull(logTokenToTests);

        loginTokenManager.deleteLogToken(logTokenToTests);

        logTokenToTests = loginTokenManager.findLogTokenById(logToken);

        assertNull(logTokenToTests);
    }

    @Test
    public void checkFindingSiteUserByLog()
    {
        SiteUser user = siteUserManager.addSiteUser(new SiteUser("KOFLXYHBSA", "password", "salt", "example5@com.pl"));
        SiteUser userToTests = siteUserManager.findSiteUserByLogin("KOFLXYHBSA");

        assertEquals(userToTests.getIdSiteUser(), user.getIdSiteUser());
        assertEquals(userToTests.getLogin(), user.getLogin());
        assertEquals(userToTests.getHash(), user.getHash());

    }

    @Test
    public void checkFindingSiteUserById()
    {
        SiteUser user = siteUserManager.addSiteUser(new SiteUser("UserMaster", "password", "salt", "example6@com.pl"));
        SiteUser userToTests = siteUserManager.findSiteUserById(user);

        assertEquals(userToTests.getIdSiteUser(), user.getIdSiteUser());
        assertEquals(userToTests.getLogin(), user.getLogin());
        assertEquals(userToTests.getHash(), user.getHash());
    }

    @Test
    public void checkFindingLogTokenBySSID()
    {
        SiteUser user = siteUserManager.addSiteUser(new SiteUser("Kasia", "password", "salt", "example7@com.pl"));
        LogToken logToken = loginTokenManager.addLogToken(new LogToken(user, "FAJNY TOKEN"));
        LogToken logTokenToTests = loginTokenManager.findLogTokenBySSID("FAJNY TOKEN");

        assertEquals(logTokenToTests.getIdLogToken(), logToken.getIdLogToken());
        assertEquals(logTokenToTests.getSsid(), logToken.getSsid());
        assertEquals(logTokenToTests.getSiteUser().getIdSiteUser(), logToken.getSiteUser().getIdSiteUser());
    }
}
