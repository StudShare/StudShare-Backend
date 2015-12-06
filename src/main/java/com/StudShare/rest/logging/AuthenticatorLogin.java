package com.StudShare.rest.logging;


import com.StudShare.domain.SiteUser;
import com.StudShare.domain.Token;
import com.StudShare.rest.PasswordMatcher;
import com.StudShare.service.UserManagerDao;


import com.StudShare.service.UserManagerImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.security.*;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import javax.security.auth.login.LoginException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;

@Component
@ComponentScan(basePackageClasses = UserManagerDao.class)
public final class AuthenticatorLogin
{

    @Autowired
    private UserManagerDao userManager;

    public UserManagerDao getUserManager()
    {
        return userManager;
    }

    public void setUserManager(UserManagerDao userManager)
    {
        this.userManager = userManager;
    }

    public Response.ResponseBuilder login(String username, String password) throws JsonProcessingException
    {


        if(username != null && password != null)
        {
            SiteUser siteUser = userManager.findUserByUsername(username);

            if (siteUser != null)
            {
                PasswordMatcher passwordMatcher = new PasswordMatcher();

                String passwordToCheck = passwordMatcher.getSecurePassword(password, siteUser.getSalt());

                if (siteUser.getHash().equals(passwordToCheck))
                {
                    Map<String, String> map = new TreeMap<String, String>();
                    ObjectMapper mapper = new ObjectMapper();
                    Token token = new Token();
                    token.setSiteUser(siteUser);
                    String authToken;
                    Token tokenExist;

                    do
                    {
                        authToken = UUID.randomUUID().toString();
                        tokenExist = userManager.findTokenBySSID(authToken);
                    }
                    while (tokenExist != null);

                    token.setToken(authToken);
                    userManager.addToken(token);

                    map.put("username", username);
                    map.put("auth_token", authToken);
                    String jsonResponse = mapper.writeValueAsString(map);

                    return getNoCacheResponseBuilder(Response.Status.OK).entity(jsonResponse);
                }
                return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity("Haslo nie prawidlowe!");
            }
            return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity("Nie ma konta o podanej nazwie uzytkownika");
        }
        return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity("Nie uzupelniles wszystkich pol!");
    }


    public Response.ResponseBuilder logout(String username, String ssid) throws GeneralSecurityException
    {

        Token token = userManager.findTokenBySSID(ssid);

        userManager.deleteToken(token);
        return getNoCacheResponseBuilder(Response.Status.NO_CONTENT);

    }

    private Response.ResponseBuilder getNoCacheResponseBuilder(Response.Status status)
    {
        CacheControl cc = new CacheControl();
        cc.setNoCache(true);
        cc.setMaxAge(-1);
        cc.setMustRevalidate(true);

        return Response.status(status).cacheControl(cc);
    }

    public boolean isAuthTokenValid(String username, String ssid)
    {
        SiteUser siteUser = userManager.findUserByUsername(username);
        Token token = userManager.findTokenBySSID(ssid);

        if (token != null && siteUser != null)
        {
            if (token.getSiteUser().getIdSiteUser() == siteUser.getIdSiteUser())
            {
                return true;
            }
        }
        return false;
    }
}