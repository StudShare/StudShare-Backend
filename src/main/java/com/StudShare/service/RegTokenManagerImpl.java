package com.StudShare.service;

import com.StudShare.config.HibernateConfig;
import com.StudShare.domain.RegToken;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Service("registrationTokenManager")
@Rollback(value = false)
@Transactional
@ComponentScan(basePackageClasses = HibernateConfig.class)
public class RegTokenManagerImpl implements RegTokenManagerDao
{
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public RegToken addRegToken(RegToken regToken)
    {
        long idToken = ((Long) sessionFactory.getCurrentSession().save(regToken)).longValue();
        regToken.setIdRegToken(idToken);
        return regToken;
    }

    @Override
    public void deleteRegToken(RegToken regToken)
    {
        sessionFactory.getCurrentSession().delete(regToken);
    }

    @Override
    public RegToken findRegTokenByActivationKey(String activationKey)
    {
        return (RegToken) sessionFactory.getCurrentSession().getNamedQuery("registrationToken.byActivationKey")
                .setString("activationKey", activationKey).uniqueResult();
    }

    @Override
    public RegToken findRegTokenById(RegToken regToken)
    {
        return sessionFactory.getCurrentSession().get(RegToken.class, regToken.getIdRegToken());
    }
}
