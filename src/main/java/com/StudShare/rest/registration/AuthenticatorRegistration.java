package com.StudShare.rest.registration;

import com.StudShare.domain.SiteUser;
import com.StudShare.rest.PasswordMatcher;
import com.StudShare.service.UserManagerDao;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    public Response.ResponseBuilder registration(String username, String password, String repeatPassword, String email) throws NoSuchProviderException, NoSuchAlgorithmException
    {
        if (username != null && password != null && repeatPassword != null && email != null)
        {
            if(checkUsername(username))
            {
                SiteUser siteUser;
                siteUser = getUserManager().findUserByUsername(username);

                if (siteUser == null)
                {
                    if(password.equals(repeatPassword))
                    {
                        if(checkEmail(email))
                        {
                            siteUser = userManager.findUserByEmail(email);

                            if (siteUser == null)
                            {
                                PasswordMatcher passwordMatcher = new PasswordMatcher();
                                String saltForPassword = passwordMatcher.generateSalt();
                                String hashPassowrd = passwordMatcher.getSecurePassword(password, saltForPassword);
                                //HERE WILL BE SENDING MAIL TO USERNAME
                                siteUser = new SiteUser(username, email, hashPassowrd, saltForPassword);
                                userManager.addUser(siteUser);
                                return getNoCacheResponseBuilder(Response.Status.OK).entity("Rejestracja przebiegla pomyslnie!");
                            }
                            return getNoCacheResponseBuilder(Response.Status.BAD_REQUEST).entity("Email jest zajety");
                        }
                        return getNoCacheResponseBuilder(Response.Status.BAD_REQUEST).entity("Wpisny email posiada nieprawidlowe znaki");
                    }
                    return getNoCacheResponseBuilder(Response.Status.BAD_REQUEST).entity("Podane hasla nie sa identyczne");
                }
                return getNoCacheResponseBuilder(Response.Status.BAD_REQUEST).entity("Nazwa uzytkowniak jest juz zajeta");
            }
            return getNoCacheResponseBuilder(Response.Status.BAD_REQUEST).entity("Nazwa uzytkownika moze posiadac litery i cyfry oraz miec od 3 do 15 znakow ");
        }
        return getNoCacheResponseBuilder(Response.Status.BAD_REQUEST).entity("Nie uzupelniles wszystkich pol!");
    }


    private boolean checkEmail(String email)
    {
        return new EmailValidator().isValid(email, null);
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
