package com.StudShare.rest.registration;

import com.StudShare.domain.SiteUser;
import com.StudShare.rest.PasswordMatcher;
import com.StudShare.service.UserManagerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.regex.Pattern;


@Component
@ComponentScan(basePackageClasses = UserManagerDao.class)
public class AuthenticatorRegistration
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

    public Response.ResponseBuilder registration(String username, String email, String repeatEmail, String password, String repeatPassword )
            throws NoSuchProviderException, NoSuchAlgorithmException
    {

        if (username == null || password == null || repeatPassword == null || email == null)
            return getNoCacheResponseBuilder(Response.Status.BAD_REQUEST).entity("Nie uzupelniles wszystkich pol!");
        else if(!checkUsername(username))
            return getNoCacheResponseBuilder(Response.Status.BAD_REQUEST).entity("Nazwa uzytkownika moze posiadac litery i cyfry oraz miec od 3 do 15 znakow ");
        else if(getUserManager().findUserByUsername(username) != null)
            return getNoCacheResponseBuilder(Response.Status.BAD_REQUEST).entity("Nazwa uzytkowniak jest juz zajeta");
        else if(!checkEmail(email))
            return getNoCacheResponseBuilder(Response.Status.BAD_REQUEST).entity("Wpisny email posiada nieprawidlowe znaki");
        else if(!email.equals(repeatEmail))
            return getNoCacheResponseBuilder(Response.Status.BAD_REQUEST).entity("Podane emaile nie sa identyczne");
        else if(password.length() < 6)
            return getNoCacheResponseBuilder(Response.Status.BAD_REQUEST).entity("Haslo musi posiadac wiecej niz 6 znakow");
        else if(!password.equals(repeatPassword))
            return getNoCacheResponseBuilder(Response.Status.BAD_REQUEST).entity("Podane hasla nie sa identyczne");
        else if(userManager.findUserByEmail(email) != null)
            return getNoCacheResponseBuilder(Response.Status.BAD_REQUEST).entity("Email jest zajety");
        else
        {
            PasswordMatcher passwordMatcher = new PasswordMatcher();
            String saltForPassword = passwordMatcher.generateSalt();
            String hashPassowrd = passwordMatcher.getSecurePassword(password, saltForPassword);
            //HERE WILL BE SENDING MAIL TO USERNAME
            SiteUser siteUser = new SiteUser(username, email, hashPassowrd, saltForPassword);
            userManager.addUser(siteUser);

            return getNoCacheResponseBuilder(Response.Status.OK);
        }
    }


    private boolean checkEmail(String email)
    {
        return Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").matcher(email).matches();
    }
    private boolean checkUsername(String username)
    {
        return Pattern.compile("^[a-z0-9_-]{3,15}$").matcher(username).matches();
    }
    private Response.ResponseBuilder getNoCacheResponseBuilder(Response.Status status)
    {
        CacheControl cc = new CacheControl();
        cc.setNoCache(true);
        cc.setMaxAge(-1);
        cc.setMustRevalidate(true);

        return Response.status(status).cacheControl(cc);
    }
}
