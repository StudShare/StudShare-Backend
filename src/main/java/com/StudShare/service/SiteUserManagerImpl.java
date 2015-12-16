package com.StudShare.service;

import com.StudShare.config.HibernateConfig;
import com.StudShare.domain.SiteUser;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


@Service("siteUserManager")
@Rollback(value = false)
@Transactional
@ComponentScan(basePackageClasses = HibernateConfig.class)
public class SiteUserManagerImpl implements SiteUserManagerDao
{

    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public SiteUser findSiteUserByLogin(String login)
    {
        return (SiteUser) sessionFactory.getCurrentSession().getNamedQuery("getPersonByLogin").setString("login", login).uniqueResult();
    }

    @Override
    public SiteUser findSiteUserByEmail(String email)
    {
        return (SiteUser) sessionFactory.getCurrentSession().getNamedQuery("getPersonByEmail").setString("email", email).uniqueResult();
    }

    @Override
    public SiteUser findSiteUserById(SiteUser siteUser)
    {
        return sessionFactory.getCurrentSession().get(SiteUser.class, siteUser.getIdSiteUser());
    }


    @Override
    public SiteUser addSiteUser(SiteUser siteUser)
    {
        long idUser = ((Long) sessionFactory.getCurrentSession().save(siteUser)).longValue();
        siteUser.setIdSiteUser(idUser);
        return siteUser;
    }

    @Override
    public void deleteSiteUser(SiteUser siteUser)
    {
        sessionFactory.getCurrentSession().delete(siteUser);
    }

    @Override
    public SiteUser updateUser(SiteUser siteUser)
    {
        return (SiteUser) sessionFactory.getCurrentSession().merge(siteUser);
    }


}
