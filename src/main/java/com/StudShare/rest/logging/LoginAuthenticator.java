package com.StudShare.rest.logging;


import com.StudShare.domain.LoginToken;
import com.StudShare.domain.SiteUser;
import com.StudShare.rest.PasswordService;
import com.StudShare.service.UserManagerDao;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.security.*;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;

@Component
@ComponentScan(basePackageClasses = UserManagerDao.class)
public final class LoginAuthenticator
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

    public Response.ResponseBuilder login(String login, String password) throws JsonProcessingException
    {


        if(login == null || password == null)
            return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity("Nie uzupelniles wszystkich pol!");
        else if(userManager.findUserByLogin(login) == null)
            return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity("Nie ma konta o podanej nazwie uzytkownika");
        else
        {
            SiteUser siteUser = userManager.findUserByLogin(login);
            PasswordService passwordMatcher = new PasswordService();
            String passwordToCheck = passwordMatcher.getSecurePassword(password, siteUser.getSalt());

            if (siteUser.getHash().equals(passwordToCheck))
            {
                Map<String, String> map = new TreeMap<String, String>();
                ObjectMapper mapper = new ObjectMapper();
                LoginToken loginToken = new LoginToken();
                loginToken.setSiteUser(siteUser);
                String authToken;
                LoginToken loginTokenExist;

                do
                {
                    authToken = UUID.randomUUID().toString();
                    loginTokenExist = userManager.findTokenBySSID(authToken);
                }
                while (loginTokenExist != null);

                loginToken.setSsid(authToken);
                userManager.addToken(loginToken);

                map.put("login", login);
                map.put("auth_token", authToken);
                String jsonResponse = mapper.writeValueAsString(map);

                return getNoCacheResponseBuilder(Response.Status.OK).entity(jsonResponse);
            }
            return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity("Haslo nie prawidlowe!");
        }



    }


    public Response.ResponseBuilder logout(String login, String ssid) throws GeneralSecurityException
    {

        LoginToken loginToken = userManager.findTokenBySSID(ssid);

        userManager.deleteToken(loginToken);
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

    public boolean isAuthTokenValid(String login, String ssid)
    {
        SiteUser siteUser = userManager.findUserByLogin(login);
        LoginToken loginToken = userManager.findTokenBySSID(ssid);

        if (loginToken == null || siteUser == null)
            return false;
        else if (!(loginToken.getSiteUser().getIdSiteUser() == siteUser.getIdSiteUser()))
            return false;
        else
            return true;
    }
}