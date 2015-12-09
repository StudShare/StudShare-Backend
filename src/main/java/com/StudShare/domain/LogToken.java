package com.StudShare.domain;


import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({@NamedQuery(name = "loginToken.bySSID", query = "Select l from LogToken l where l.ssid = :ssid")})
public class LogToken
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idLogToken;

    @ManyToOne
    @JoinColumn(name = "idSiteUser")
    private SiteUser siteUser;

    @Column(nullable = false, unique = true)
    private String ssid;

    public LogToken()
    {
    }

    public LogToken(SiteUser siteUser, String token)
    {
        this.siteUser = siteUser;
        this.ssid = token;
    }

    public long getIdLogToken()
    {
        return idLogToken;
    }

    public void setIdLogToken(long idLogToken)
    {
        this.idLogToken = idLogToken;
    }

    public SiteUser getSiteUser()
    {
        return siteUser;
    }

    public void setSiteUser(SiteUser person)
    {
        this.siteUser = person;
    }

    public String getSsid()
    {
        return ssid;
    }

    public void setSsid(String token)
    {
        this.ssid = token;
    }


}
