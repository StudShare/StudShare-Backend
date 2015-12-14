package com.StudShare.rest.logging;


import com.StudShare.domain.LogToken;
import com.StudShare.domain.SiteUser;
import com.StudShare.service.LogTokenManagerDao;
import com.StudShare.service.SiteUserManagerDao;
import com.StudShare.utils.NoCacheResponse;
import com.StudShare.utils.PasswordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

@Component
@ComponentScan(basePackageClasses = {LogTokenManagerDao.class, SiteUserManagerDao.class, NoCacheResponse.class})
public final class LoginAuthenticator
{

    @Autowired
    private LogTokenManagerDao logTokenManager;

    @Autowired
    private SiteUserManagerDao siteUserManager;

    @Autowired
    private NoCacheResponse noCacheResponse;

    public LogTokenManagerDao getLogTokenManager()
    {
        return logTokenManager;
    }

    public SiteUserManagerDao getSiteUserManager()
    {
        return siteUserManager;
    }

    public Response.ResponseBuilder login(String login, String password) throws JsonProcessingException
    {


        if (login == null || login.length() == 0 || password == null || password.length() == 0)
        {
            return noCacheResponse.getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity("Nie uzupelniles wszystkich pol!");
        }
        else if (siteUserManager.findSiteUserByLogin(login) == null)
        {
            return noCacheResponse.getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity("Nie ma konta o podanej nazwie uzytkownika");
        }
        else
        {
            SiteUser siteUser = siteUserManager.findSiteUserByLogin(login);
            PasswordService passwordMatcher = new PasswordService();
            String passwordToCheck = passwordMatcher.getSecurePassword(password, siteUser.getSalt());

            if (siteUser.getHash().equals(passwordToCheck))
            {
                Map<String, String> map = new TreeMap<String, String>();
                ObjectMapper mapper = new ObjectMapper();
                LogToken logToken = new LogToken();
                logToken.setSiteUser(siteUser);
                String authToken;
                LogToken logTokenExist;

                do
                {
                    authToken = UUID.randomUUID().toString();
                    logTokenExist = logTokenManager.findLogTokenBySSID(authToken);
                }
                while (logTokenExist != null);

                logToken.setSsid(authToken);
                logTokenManager.addLogToken(logToken);

                map.put("login", login);
                map.put("auth_token", authToken);
                String jsonResponse = mapper.writeValueAsString(map);

                return noCacheResponse.getNoCacheResponseBuilder(Response.Status.OK).entity(jsonResponse);
            }
            return noCacheResponse.getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity("Haslo nie prawidlowe!");
        }


    }


    public Response.ResponseBuilder logout(String login, String ssid) throws GeneralSecurityException
    {

        LogToken logToken = logTokenManager.findLogTokenBySSID(ssid);

        logTokenManager.deleteLogToken(logToken);
        return noCacheResponse.getNoCacheResponseBuilder(Response.Status.NO_CONTENT);

    }


    public boolean isAuthTokenValid(String login, String ssid)
    {
        SiteUser siteUser = siteUserManager.findSiteUserByLogin(login);
        LogToken logToken = logTokenManager.findLogTokenBySSID(ssid);

        if (logToken == null || siteUser == null)
        {
            return false;
        }
        else if (!(logToken.getSiteUser().getIdSiteUser() == siteUser.getIdSiteUser()))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}