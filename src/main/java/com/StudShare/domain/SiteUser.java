package com.StudShare.domain;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.List;


@Entity
@NamedQueries({@NamedQuery(name = "getPersonByLogin", query = "Select su from SiteUser su where su.login = :login"),
                @NamedQuery(name = "getPersonByEmail", query = "Select su from SiteUser su where su.email = :email")})
public class SiteUser
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idSiteUser;

    @Column(nullable = false, length = 32, unique = true)
    private String login;
    @Column(nullable = false, length = 64, unique = true)
    private String email;
    @Column(nullable = false)
    private String hash;
    @Column(nullable = false)
    private String salt;
    @Column(nullable = false)
    private boolean enabled;

    @OneToMany(mappedBy = "siteUser", cascade = CascadeType.REMOVE)
    private List<LogToken> logTokens;

    @OneToOne(mappedBy = "siteUser", cascade = CascadeType.REMOVE)
    private RegToken regToken;

    public SiteUser() {}

    public SiteUser(String login, String hash, String salt, String email)
    {
        this.login = login;
        this.hash = hash;
        this.salt = salt;
        this.email = email;
        this.enabled = false;
    }


    public long getIdSiteUser()
    {
        return idSiteUser;
    }

    public void setIdSiteUser(long idSiteUser)
    {
        this.idSiteUser = idSiteUser;
    }

    public String getLogin()
    {
        return login;
    }

    public void setLogin(String name)
    {
        this.login = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
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

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

}
