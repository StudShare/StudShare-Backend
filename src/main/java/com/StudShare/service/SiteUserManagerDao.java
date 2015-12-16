package com.StudShare.service;

import com.StudShare.domain.SiteUser;
import org.hibernate.SessionFactory;

public interface SiteUserManagerDao
{
    SessionFactory getSessionFactory();

    void setSessionFactory(SessionFactory sessionFactory);

    SiteUser addSiteUser(SiteUser siteSiteUser);

    void deleteSiteUser(SiteUser siteSiteUser);

    SiteUser updateUser(SiteUser siteUser);

    SiteUser findSiteUserByLogin(String login);

    SiteUser findSiteUserByEmail(String email);

    SiteUser findSiteUserById(SiteUser siteSiteUser);

}
