package com.StudShare.service;

import com.StudShare.config.HibernateConfig;
import com.StudShare.domain.LoginToken;
import com.StudShare.domain.SiteUser;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


@Service("userManager")
@Rollback(value = false)
@Transactional
@ComponentScan(basePackageClasses = HibernateConfig.class)
public class UserManagerImpl implements UserManagerDao
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
    public SiteUser findUserByLogin(String login)
    {
        return (SiteUser) sessionFactory.getCurrentSession().getNamedQuery("getPersonByLogin").setString("login", login).uniqueResult();
    }
    @Override
    public SiteUser findUserByEmail(String email)
    {
        return (SiteUser) sessionFactory.getCurrentSession().getNamedQuery("getPersonByEmail").setString("email", email).uniqueResult();
    }
    @Override
    public SiteUser findUserById(SiteUser siteUser)
    {
        return sessionFactory.getCurrentSession().get(SiteUser.class, siteUser.getIdSiteUser());
    }


    @Override
    public LoginToken findTokenBySSID(String ssid)
    {
        return (LoginToken) sessionFactory.getCurrentSession().getNamedQuery("getTokenBySSID").setString("ssid", ssid).uniqueResult();
    }

    @Override
    public LoginToken findTokenById(LoginToken loginToken)
    {
        return sessionFactory.getCurrentSession().get(LoginToken.class, loginToken.getIdToken());
    }

    @Override
    public SiteUser addUser(SiteUser siteUser)
    {
        long idUser = ((Long) sessionFactory.getCurrentSession().save(siteUser)).longValue();
        siteUser.setIdSiteUser(idUser);
        return siteUser;
    }

    @Override
    public void deleteUser(SiteUser siteUser)
    {
        sessionFactory.getCurrentSession().delete(siteUser);
    }

    @Override
    public LoginToken addToken(LoginToken loginToken)
    {
        long idToken = ((Long) sessionFactory.getCurrentSession().save(loginToken)).longValue();
        loginToken.setIdToken(idToken);
        return loginToken;
    }

    @Override
    public void deleteToken(LoginToken loginToken)
    {
        sessionFactory.getCurrentSession().delete(loginToken);
    }

}
