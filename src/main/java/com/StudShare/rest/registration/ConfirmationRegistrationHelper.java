package com.StudShare.rest.registration;

import com.StudShare.domain.RegToken;
import com.StudShare.domain.SiteUser;
import com.StudShare.service.RegTokenManagerDao;
import com.StudShare.service.SiteUserManagerDao;
import com.StudShare.utils.NoCacheResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;

@Component
@ComponentScan(basePackageClasses = {SiteUserManagerDao.class, RegTokenManagerDao.class, NoCacheResponse.class})
public class ConfirmationRegistrationHelper
{
    @Autowired
    SiteUserManagerDao siteUserManager;

    @Autowired
    RegTokenManagerDao regTokenManager;


    public Response.ResponseBuilder confirmRegisterUser(long idSiteUser, String activationKey, String serverPath) throws URISyntaxException
    {

        SiteUser s = new SiteUser();
        s.setIdSiteUser(idSiteUser);
        SiteUser siteUser = siteUserManager.findSiteUserById(s);
        RegToken regToken = regTokenManager.findRegTokenByActivationKey(activationKey);

        String urlRegistration = "/registration.html";
        URI redirect = new URI(serverPath + urlRegistration);
        Calendar cal = Calendar.getInstance();

        Cookie cookie;

        if (regToken == null || siteUser == null)
        {
            cookie = new Cookie("message", "Nieprawidlowe zapytanie http", urlRegistration , "" );
            return Response.temporaryRedirect(redirect).cookie(new NewCookie(cookie, "", 60, false));

        }
        else if (regToken.getSiteUser().getIdSiteUser() != siteUser.getIdSiteUser())
        {
            cookie = new Cookie("message", "Token nie należy do użytkownika", urlRegistration , "" );
            return Response.temporaryRedirect(redirect).cookie(new NewCookie(cookie, "", 60, false));
        }
        else if ((regToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0)
        {
            siteUserManager.deleteSiteUser(siteUser);
            cookie = new Cookie("message", "Twoj token wygasl. Sprobuj zarejestrowac swoje konto jeszcze raz", urlRegistration , "" );

            return Response.temporaryRedirect(redirect).cookie(new NewCookie(cookie, "", 60, false));
        }
        else
        {
            siteUser.setEnabled(true);
            siteUserManager.updateUser(siteUser);
            regTokenManager.deleteRegToken(regToken);

            cookie = new Cookie("message", "Twoje konto zostalo pomyslnie aktywowane", urlRegistration , "" );
            return Response.temporaryRedirect(redirect).cookie(new NewCookie(cookie, "", 60, false));
        }

    }
}
