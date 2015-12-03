package com.StudShare.domain;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({@NamedQuery(name = "getPersonByUsername", query = "Select su from SiteUser su where su.username = :username")})
public class SiteUser
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idSiteUser;

    @Column(nullable = false, length = 32, unique = true)
    private String username;
    @Column(nullable = false)
    private String hash;
    @Column(nullable = false)
    private String salt;

    public SiteUser() {}
    public SiteUser(String username, String hash, String salt)
    {
        this.username = username;
        this.hash = hash;
        this.salt = salt;
    }


    public long getIdSiteUser()
    {
        return idSiteUser;
    }

    public void setIdSiteUser(long idSiteUser)
    {
        this.idSiteUser = idSiteUser;
    }
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String name)
    {
        this.username = name;
    }

    public String getHash()
    {
        return hash;
    }

    public void setHash(String hash)
    {
        this.hash = hash;
    }

    public String getSalt()
    {
        return salt;
    }

    public void setSalt(String salt)
    {
        this.salt = salt;
    }
}
