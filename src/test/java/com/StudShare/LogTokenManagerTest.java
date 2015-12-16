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

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(StudShareApplication.class)
@Rollback(value = true)
@Transactional
public class LogTokenManagerTest
{
    @Autowired
    SiteUserManagerDao siteUserManager;

    @Autowired
    LogTokenManagerDao loginTokenManager;


    @Test
    public void checkAddingLogToken()
    {
        String authToken = UUID.randomUUID().toString();
        SiteUser user = siteUserManager.addSiteUser(new SiteUser("henio", "password", "salt", "example3@com.pl"));
        LogToken logToken = loginTokenManager.addLogToken(new LogToken(authToken, user));
        LogToken logTokenToTests = loginTokenManager.findLogTokenById(logToken);

        assertNotNull(logTokenToTests);
        assertEquals(logTokenToTests.getSiteUser().getIdSiteUser(), logToken.getSiteUser().getIdSiteUser());
        assertEquals(logTokenToTests.getSsid(), logToken.getSsid());
    }

    @Test
    public void checkDeletingLogToken()
    {
        String authToken = UUID.randomUUID().toString();
        SiteUser user = siteUserManager.addSiteUser(new SiteUser("GoodBoy", "password", "salt", "example4@com.pl"));
        LogToken logToken = loginTokenManager.addLogToken(new LogToken(authToken, user));

        LogToken logTokenToTests = loginTokenManager.findLogTokenById(logToken);

        assertNotNull(logTokenToTests);

        loginTokenManager.deleteLogToken(logTokenToTests);

        logTokenToTests = loginTokenManager.findLogTokenById(logToken);

        assertNull(logTokenToTests);
    }

    @Test
    public void checkFindingLogTokenBySSID()
    {
        String authToken = UUID.randomUUID().toString();
        SiteUser user = siteUserManager.addSiteUser(new SiteUser("Kasia", "password", "salt", "example7@com.pl"));
        LogToken logToken = loginTokenManager.addLogToken(new LogToken(authToken, user));
        LogToken logTokenToTests = loginTokenManager.findLogTokenBySSID(authToken);

        assertEquals(logTokenToTests.getIdLogToken(), logToken.getIdLogToken());
        assertEquals(logTokenToTests.getSsid(), logToken.getSsid());
        assertEquals(logTokenToTests.getSiteUser().getIdSiteUser(), logToken.getSiteUser().getIdSiteUser());
    }

    @Test
    public void checkFindingLogTokenById()
    {

        SiteUser user = siteUserManager.addSiteUser(new SiteUser("Kasia", "password", "salt", "example8@com.pl"));
        LogToken logToken = loginTokenManager.addLogToken(new LogToken("example_ssid", user));
        LogToken logTokenToTests = loginTokenManager.findLogTokenById(logToken);



        assertEquals(logTokenToTests.getIdLogToken(), logToken.getIdLogToken());
        assertEquals(logTokenToTests.getSsid(), logToken.getSsid());
        assertEquals(logTokenToTests.getSiteUser().getIdSiteUser(), logToken.getSiteUser().getIdSiteUser());
    }
}
