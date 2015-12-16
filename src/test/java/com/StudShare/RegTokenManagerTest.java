package com.StudShare;

import com.StudShare.domain.RegToken;
import com.StudShare.domain.SiteUser;
import com.StudShare.service.RegTokenManagerDao;
import com.StudShare.service.SiteUserManagerDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(StudShareApplication.class)
@Rollback(value = true)
@Transactional
public class RegTokenManagerTest
{
    @Autowired
    SiteUserManagerDao siteUserManager;

    @Autowired
    RegTokenManagerDao registrationTokenManager;

    @Test
    public void checkAddingRegToken()
    {
        String authToken = UUID.randomUUID().toString();

        SiteUser user = siteUserManager.addSiteUser(new SiteUser("henio", "password", "salt", "example3@com.pl"));
        RegToken regToken = registrationTokenManager.addRegToken(new RegToken(authToken, user));
        RegToken regTokenToTests = registrationTokenManager.findRegTokenById(regToken);

        assertNotNull(regTokenToTests);
        assertEquals(regTokenToTests.getSiteUser().getIdSiteUser(), regToken.getSiteUser().getIdSiteUser());
        assertEquals(regTokenToTests.getActivationKey(), regToken.getActivationKey());
    }

    @Test
    public void checkDeletingRegToken()
    {
        String authToken = UUID.randomUUID().toString();
        SiteUser user = siteUserManager.addSiteUser(new SiteUser("GoodBoy", "password", "salt", "example4@com.pl"));
        RegToken regToken = registrationTokenManager.addRegToken(new RegToken(authToken, user));

        RegToken regTokenToTests = registrationTokenManager.findRegTokenById(regToken);

        assertNotNull(regTokenToTests);

        registrationTokenManager.deleteRegToken(regTokenToTests);

        regTokenToTests = registrationTokenManager.findRegTokenById(regToken);

        assertNull(regTokenToTests);
    }

    @Test
    public void checkFindingRegTokenByActivationKey()
    {

        SiteUser user = siteUserManager.addSiteUser(new SiteUser("Kasia", "password", "salt", "example7@com.pl"));
        RegToken regToken = registrationTokenManager.addRegToken(new RegToken("activation_key", user));
        RegToken regTokenToTests = registrationTokenManager.findRegTokenByActivationKey("activation_key");

        assertEquals(regTokenToTests.getIdRegToken(), regToken.getIdRegToken());
        assertEquals(regTokenToTests.getActivationKey(), regToken.getActivationKey());
        assertEquals(regTokenToTests.getSiteUser().getIdSiteUser(), regToken.getSiteUser().getIdSiteUser());
    }

    @Test
    public void checkFindingRegTokenById()
    {
        SiteUser user = siteUserManager.addSiteUser(new SiteUser("Kasia", "password", "salt", "example8@com.pl"));
        RegToken regToken = registrationTokenManager.addRegToken(new RegToken("example_activation_key", user));
        RegToken regTokenToTests = registrationTokenManager.findRegTokenById(regToken);



        assertEquals(regTokenToTests.getIdRegToken(), regToken.getIdRegToken());
        assertEquals(regTokenToTests.getActivationKey(), regToken.getActivationKey());
        assertEquals(regTokenToTests.getSiteUser().getIdSiteUser(), regToken.getSiteUser().getIdSiteUser());
    }
}
