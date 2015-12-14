package com.StudShare.rest.registration;

import com.StudShare.domain.RegToken;
import com.StudShare.domain.SiteUser;
import com.StudShare.service.RegTokenManagerDao;
import com.StudShare.service.SiteUserManagerDao;
import com.StudShare.utils.MailHelper;
import com.StudShare.utils.NoCacheResponse;
import com.StudShare.utils.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.UUID;
import java.util.regex.Pattern;


@Component
@ComponentScan(basePackageClasses = { SiteUserManagerDao.class, MailHelper.class,
        RegTokenManagerDao.class, NoCacheResponse.class })
public class RegistrationHelper
{

    @Autowired
    SiteUserManagerDao siteUserManager;

    @Autowired
    RegTokenManagerDao regTokenManager;

    @Autowired
    MailHelper mailHelper;

    @Autowired
    NoCacheResponse noCacheResponse;

    public Response.ResponseBuilder registerUser(String login, String email, String repeatEmail, String password,
                                                 String repeatPassword, UriInfo uriInfo)
            throws NoSuchProviderException, NoSuchAlgorithmException, URISyntaxException
    {

        Response.Status badRequest  = Response.Status.BAD_REQUEST;


        if (login == null || password == null || repeatPassword == null || email == null || repeatEmail == null)
            return noCacheResponse.getNoCacheResponseBuilder(badRequest).entity("Nie uzupelniles wszystkich pol!");
        else if(login.length() == 0 || password.length() == 0 || repeatPassword.length() ==0 || email.length() == 0 || repeatEmail.length() == 0)
            return noCacheResponse.getNoCacheResponseBuilder(badRequest).entity("Nie uzupelniles wszystkich pol!");
        else if(!checkLogin(login))
            return noCacheResponse.getNoCacheResponseBuilder(badRequest).entity("Nazwa uzytkownika moze posiadac litery i cyfry oraz miec od 3 do 15 znakow ");
        else if(siteUserManager.findSiteUserByLogin(login) != null)
            return noCacheResponse.getNoCacheResponseBuilder(badRequest).entity("Nazwa uzytkowniak jest juz zajeta");
        else if (!checkEmail(email))
            return noCacheResponse.getNoCacheResponseBuilder(badRequest).entity("Wpisny email posiada nieprawidlowe znaki");
        else if(!email.equals(repeatEmail))
            return noCacheResponse.getNoCacheResponseBuilder(badRequest).entity("Podane emaile nie sa identyczne");
        else if(password.length() < 6)
            return noCacheResponse.getNoCacheResponseBuilder(badRequest).entity("Haslo musi posiadac wiecej niz 6 znakow");
        else if(!password.equals(repeatPassword))
            return noCacheResponse.getNoCacheResponseBuilder(badRequest).entity("Podane hasla nie sa identyczne");
        else if(siteUserManager.findSiteUserByEmail(email) != null)
            return noCacheResponse.getNoCacheResponseBuilder(badRequest).entity("Email jest zajety");
        else
        {
            PasswordService passwordMatcher = new PasswordService();
            String saltForPassword = passwordMatcher.generateSalt();
            String hashPassword = passwordMatcher.getSecurePassword(password, saltForPassword);


            //HERE WILL BE SENDING MAIL TO USERNAME
            String token = UUID.randomUUID().toString();

            SiteUser siteUser = siteUserManager.addSiteUser(new SiteUser(login, hashPassword, saltForPassword, email));
            RegToken regToken = regTokenManager.addRegToken(new RegToken(token, siteUser));



            mailHelper.sendMail("Studshare.pl", email, "Witamy na Studshare.pl",
                    "Twoje konto jest obecnie nieaktywne." +
                    " Aby aktywować konto musisz odwiedzić poniższą stronę: \n"
                    + uriInfo.getAbsolutePath()+ "?u=" + regToken.getSiteUser().getIdSiteUser()
                    + "&activation_key="+ regToken.getActivationKey());

            String urlRegistration = "/registration.html";


            Cookie cookie = new Cookie("email", siteUser.getEmail() , urlRegistration , "" );

            return noCacheResponse.getNoCacheResponseBuilder(Response.Status.OK).cookie(new NewCookie(cookie, "", 60, false));

        }

    }

    private boolean checkEmail(String email)
    {
        return Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").matcher(email).matches();
    }
    private boolean checkLogin(String login)
    {
        return Pattern.compile("^[a-z0-9_-]{3,15}$").matcher(login).matches();
    }



}
