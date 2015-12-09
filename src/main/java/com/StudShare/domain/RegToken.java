package com.StudShare.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
@NamedQueries({@NamedQuery(name = "registrationToken.byActivationKey", query = "Select r from RegToken r where r.activationKey = :activationKey")})
public class RegToken
{
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRegToken;

    @Column(name = "activation_key", nullable = false, unique = true)
    private String activationKey;

    @OneToOne(targetEntity = SiteUser.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "idSiteUser")
    private SiteUser siteUser;

    private Date expiryDate;

    public RegToken() {}

    public RegToken(String activationKey, SiteUser siteUser)
    {
        this.activationKey = activationKey;
        this.siteUser = siteUser;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public Long getIdRegToken()
    {
        return idRegToken;
    }

    public void setIdRegToken(Long idRegToken)
    {
        this.idRegToken = idRegToken;
    }

    public String getActivationKey()
    {
        return activationKey;
    }

    public void setActivationKey(String activationKey)
    {
        this.activationKey = activationKey;
    }

    public SiteUser getSiteUser()
    {
        return siteUser;
    }

    public void setSiteUser(SiteUser siteUser)
    {
        this.siteUser = siteUser;
    }

    public Date getExpiryDate()
    {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate)
    {
        this.expiryDate = expiryDate;
    }
}
