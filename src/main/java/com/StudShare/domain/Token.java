package com.StudShare.domain;


import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({@NamedQuery(name = "getTokenBySSID", query = "Select t from Token t where t.ssid = :ssid")})
public class Token
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idToken;

    @ManyToOne
    @JoinColumn(name="idSiteUser")
    private SiteUser siteUser;

    @Column(nullable = false, length = 36, unique = true)
    private String ssid;

    public Token(){}
    public Token(SiteUser siteUser, String token)
    {
        this.siteUser = siteUser;
        this.ssid = token;
    }

    public long getIdToken()
    {
        return idToken;
    }

    public void setIdToken(long idToken)
    {
        this.idToken = idToken;
    }

    public SiteUser getSiteUser()
    {
        return siteUser;
    }

    public void setSiteUser(SiteUser person)
    {
        this.siteUser = person;
    }

    public String getToken()
    {
        return ssid;
    }

    public void setToken(String token)
    {
        this.ssid = token;
    }


}
