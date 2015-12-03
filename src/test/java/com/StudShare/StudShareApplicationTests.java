package com.StudShare;

import com.StudShare.rest.logging.Authenticator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(StudShareApplication.class)
public class StudShareApplicationTests {

	@Autowired
	Authenticator authenticator;

	@Test
	public void contextLoads()
	{
		assertNotNull(authenticator);
		assertNotNull(authenticator.getUserManager());
		assertNotNull(authenticator.getUserManager().getSessionFactory());

	}

}
