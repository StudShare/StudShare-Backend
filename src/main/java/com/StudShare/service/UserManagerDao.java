package com.StudShare.service;

import com.StudShare.domain.LoginToken;
import com.StudShare.domain.SiteUser;
import org.hibernate.SessionFactory;

public interface UserManagerDao
{
    SessionFactory getSessionFactory();

    void setSessionFactory(SessionFactory sessionFactory);

    SiteUser addUser(SiteUser siteUser);

    void deleteUser(SiteUser siteUser);

    LoginToken addToken(LoginToken loginToken);

    void deleteToken(LoginToken loginToken);

    SiteUser findUserByLogin(String login);

    SiteUser findUserByEmail(String email);

    SiteUser findUserById(SiteUser siteUser);

    LoginToken findTokenBySSID(String ssid);

    LoginToken findTokenById(LoginToken loginToken);
}
