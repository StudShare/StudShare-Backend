package com.StudShare;

import com.StudShare.rest.logging.LoginAuthenticator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(StudShareApplication.class)
public class StudShareApplicationTests
{

    @Autowired
    LoginAuthenticator loginAuthenticator;

    @Test
    public void contextLoads()
    {
        assertNotNull(loginAuthenticator);
        assertNotNull(loginAuthenticator.getSiteUserManager());
        assertNotNull(loginAuthenticator.getSiteUserManager().getSessionFactory());

    }

}
