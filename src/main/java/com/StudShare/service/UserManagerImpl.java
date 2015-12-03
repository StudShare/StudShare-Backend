package com.StudShare.service;

import com.StudShare.config.HibernateConfig;
import com.StudShare.domain.SiteUser;
import com.StudShare.domain.Token;
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
    public SiteUser findUserByUsername(String username)
    {
        return (SiteUser) sessionFactory.getCurrentSession().getNamedQuery("getPersonByUsername").setString("username", username).uniqueResult();
    }

    @Override
    public SiteUser findUserById(SiteUser siteUser)
    {
        return (SiteUser) sessionFactory.getCurrentSession().get(SiteUser.class, siteUser.getIdSiteUser());
    }


    @Override
    public Token findTokenBySSID(String ssid)
    {
        return (Token) sessionFactory.getCurrentSession().getNamedQuery("getTokenBySSID").setString("ssid", ssid).uniqueResult();
    }
    @Override
    public Token findTokenById(Token token)
    {
        return (Token) sessionFactory.getCurrentSession().get(Token.class, token.getIdToken());
    }

    @Override
    public SiteUser addUser(SiteUser siteUser)
    {
        long idUser = ((Long)sessionFactory.getCurrentSession().save(siteUser)).longValue();
        siteUser.setIdSiteUser(idUser);
        return siteUser;
    }
    @Override
    public void deleteUser(SiteUser siteUser)
    {
        sessionFactory.getCurrentSession().delete(siteUser);
    }

    @Override
    public Token addToken(Token token)
    {
        long idToken = ((Long)sessionFactory.getCurrentSession().save(token)).longValue();
        token.setIdToken(idToken);
        return token;
    }

    @Override
    public void deleteToken(Token token)
    {
        sessionFactory.getCurrentSession().delete(token);
    }

}
