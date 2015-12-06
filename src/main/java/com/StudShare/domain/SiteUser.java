package com.StudShare.domain;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.List;


@Entity
@NamedQueries({@NamedQuery(name = "getPersonByUsername", query = "Select su from SiteUser su where su.username = :username"),
                @NamedQuery(name = "getPersonByEmail", query = "Select su from SiteUser su where su.email = :email")})
public class SiteUser
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idSiteUser;

    @Column(nullable = false, length = 32, unique = true)
    private String username;
    @Column(nullable = false, length = 32, unique = true)
    private String email;
    @Column(nullable = false)
    private String hash;
    @Column(nullable = false)
    private String salt;

    @OneToMany(mappedBy = "siteUser")
    private List<Token> tokens;

    public SiteUser()
    {
    }

    public SiteUser(String username, String email, String hash, String salt)
    {
        this.username = username;
        this.email = email;
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

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public List<Token> getTokens()
    {
        return tokens;
    }

    public void setTokens(List<Token> tokens)
    {
        this.tokens = tokens;
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
