package com.StudShare.service;

import com.StudShare.domain.SiteUser;
import com.StudShare.domain.Token;
import org.hibernate.SessionFactory;

public interface UserManagerDao
{
    SessionFactory getSessionFactory();

    void setSessionFactory(SessionFactory sessionFactory);

    SiteUser addUser(SiteUser siteUser);

    void deleteUser(SiteUser siteUser);

    Token addToken(Token token);

    void deleteToken(Token token);

    SiteUser findUserByUsername(String username);

    SiteUser findUserById(SiteUser siteUser);

    Token findTokenBySSID(String ssid);

    Token findTokenById(Token token);
}
