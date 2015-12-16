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

        SiteUser userToTests = siteUserManager.findSiteUserById(user);

        assertNotNull(userToTests);
        assertEquals(userToTests.getIdSiteUser(), user.getIdSiteUser());
        assertEquals(userToTests.getLogin(), user.getLogin());
        assertEquals(userToTests.getHash(), user.getHash());
        assertEquals(userToTests.getSalt(), user.getSalt());
        assertEquals(userToTests.getEmail(), user.getEmail());

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
        assertEquals(userToTests.getSalt(), user.getSalt());
        assertEquals(userToTests.getEmail(), user.getEmail());

        siteUserManager.deleteSiteUser(userToTests);

        userToTests = siteUserManager.findSiteUserById(user);

        assertNull(userToTests);

    }

    @Test
    public void checkUpdatingSiteUser()
    {
        SiteUser user = siteUserManager.addSiteUser(new SiteUser("mateusz", "password", "salt", "example1@com.pl"));

        user.setLogin("andrzejek");
        user.setHash("hash");
        user.setSalt("other_salt");
        user.setEmail("otheremail@com.pl");

        SiteUser userToTests = siteUserManager.updateUser(user);

        assertNotNull(userToTests);
        assertEquals(userToTests.getLogin(), "andrzejek");
        assertEquals(userToTests.getHash(), "hash");
        assertEquals(userToTests.getSalt(), "other_salt");
        assertEquals(userToTests.getEmail(), "otheremail@com.pl");

    }




    @Test
    public void checkFindingSiteUserByLogin()
    {
        SiteUser user = siteUserManager.addSiteUser(new SiteUser("KOFLXYHBSA", "password", "salt", "example5@com.pl"));
        SiteUser userToTests = siteUserManager.findSiteUserByLogin("KOFLXYHBSA");

        assertEquals(userToTests.getIdSiteUser(), user.getIdSiteUser());
        assertEquals(userToTests.getLogin(), user.getLogin());
        assertEquals(userToTests.getHash(), user.getHash());
        assertEquals(userToTests.getSalt(), user.getSalt());
        assertEquals(userToTests.getEmail(), user.getEmail());
    }

    @Test
    public void checkFindingSiteUserById()
    {
        SiteUser user = siteUserManager.addSiteUser(new SiteUser("UserMaster", "password", "salt", "example6@com.pl"));
        SiteUser userToTests = siteUserManager.findSiteUserById(user);

        assertEquals(userToTests.getIdSiteUser(), user.getIdSiteUser());
        assertEquals(userToTests.getLogin(), user.getLogin());
        assertEquals(userToTests.getHash(), user.getHash());
        assertEquals(userToTests.getSalt(), user.getSalt());
        assertEquals(userToTests.getEmail(), user.getEmail());
    }

    @Test
    public void checkFindingSiteUserByEmail()
    {
        SiteUser user = siteUserManager.addSiteUser(new SiteUser("userexample1", "password", "salt", "testemail@com.pl"));
        SiteUser userToTests = siteUserManager.findSiteUserByEmail("testemail@com.pl");

        assertEquals(userToTests.getIdSiteUser(), user.getIdSiteUser());
        assertEquals(userToTests.getLogin(), user.getLogin());
        assertEquals(userToTests.getHash(), user.getHash());
        assertEquals(userToTests.getSalt(), user.getSalt());
        assertEquals(userToTests.getEmail(), user.getEmail());
    }
}
