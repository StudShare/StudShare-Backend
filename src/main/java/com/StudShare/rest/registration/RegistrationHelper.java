package com.StudShare.rest.registration;

import com.StudShare.config.EmailConfig;
import com.StudShare.domain.RegToken;
import com.StudShare.domain.SiteUser;
import com.StudShare.rest.PasswordService;
import com.StudShare.service.SiteUserManagerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.UUID;
import java.util.regex.Pattern;


@Component
@ComponentScan(basePackageClasses = { SiteUserManagerDao.class, EmailConfig.class})
public class RegistrationHelper
{
    @Autowired
    private SiteUserManagerDao siteUserManager;

    @Autowired
    MailSender mailSender;


    public Response.ResponseBuilder registerUser(String login, String email, String repeatEmail, String password, String repeatPassword, UriInfo uriInfo)
            throws NoSuchProviderException, NoSuchAlgorithmException
    {

        Response.Status badrequest  = Response.Status.BAD_REQUEST;


        if (login == null || password == null || repeatPassword == null || email == null)
            return getNoCacheResponseBuilder(badrequest).entity("Nie uzupelniles wszystkich pol!");
        else if(!checkLogin(login))
            return getNoCacheResponseBuilder(badrequest).entity("Nazwa uzytkownika moze posiadac litery i cyfry oraz miec od 3 do 15 znakow ");
        else if(siteUserManager.findSiteUserByLogin(login) != null)
            return getNoCacheResponseBuilder(badrequest).entity("Nazwa uzytkowniak jest juz zajeta");
        else if(!checkEmail(email))
            return getNoCacheResponseBuilder(badrequest).entity("Wpisny email posiada nieprawidlowe znaki");
        else if(!email.equals(repeatEmail))
            return getNoCacheResponseBuilder(badrequest).entity("Podane emaile nie sa identyczne");
        else if(password.length() < 6)
            return getNoCacheResponseBuilder(badrequest).entity("Haslo musi posiadac wiecej niz 6 znakow");
        else if(!password.equals(repeatPassword))
            return getNoCacheResponseBuilder(badrequest).entity("Podane hasla nie sa identyczne");
        else if(siteUserManager.findSiteUserByEmail(email) != null)
            return getNoCacheResponseBuilder(badrequest).entity("Email jest zajety");
        else
        {
            PasswordService passwordMatcher = new PasswordService();
            String saltForPassword = passwordMatcher.generateSalt();
            String hashPassowrd = passwordMatcher.getSecurePassword(password, saltForPassword);


            //HERE WILL BE SENDING MAIL TO USERNAME
            String token = UUID.randomUUID().toString();

            SiteUser siteUser = siteUserManager.addSiteUser(new SiteUser(login, hashPassowrd, saltForPassword, email));




            return getNoCacheResponseBuilder(Response.Status.OK);
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
    private Response.ResponseBuilder getNoCacheResponseBuilder(Response.Status status)
    {
        CacheControl cc = new CacheControl();
        cc.setNoCache(true);
        cc.setMaxAge(-1);
        cc.setMustRevalidate(true);

        return Response.status(status).cacheControl(cc);
    }

    public void sendMail(String from, String to, String subject, String msg) {
        //creating message
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(msg);
        //sending message
        mailSender.send(message);
    }
}
