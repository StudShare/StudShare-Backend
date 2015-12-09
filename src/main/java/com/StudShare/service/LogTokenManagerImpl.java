package com.StudShare.service;

import com.StudShare.config.HibernateConfig;
import com.StudShare.domain.LogToken;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Service("loginTokenManager")
@Rollback(value = false)
@Transactional
@ComponentScan(basePackageClasses = HibernateConfig.class)
public class LogTokenManagerImpl implements LogTokenManagerDao
{

    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public LogToken addLogToken(LogToken logToken)
    {
        long idToken = ((Long) sessionFactory.getCurrentSession().save(logToken)).longValue();
        logToken.setIdLogToken(idToken);
        return logToken;
    }

    @Override
    public void deleteLogToken(LogToken logToken)
    {
        sessionFactory.getCurrentSession().delete(logToken);
    }


    @Override
    public LogToken findLogTokenBySSID(String ssid)
    {
        return (LogToken) sessionFactory.getCurrentSession().getNamedQuery("loginToken.bySSID").setString("ssid", ssid).uniqueResult();
    }

    @Override
    public LogToken findLogTokenById(LogToken logToken)
    {
        return sessionFactory.getCurrentSession().get(LogToken.class, logToken.getIdLogToken());
    }
}
