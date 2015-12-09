package com.StudShare.rest.registration;

import com.StudShare.rest.HTTPHeaderNames;
import com.StudShare.rest.logging.LoginAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Path("/user")
@ComponentScan(basePackageClasses = LoginAuthenticator.class)
public class RegistrationManager
{
    @Autowired
    RegistrationHelper registrationHelper;

    @POST
    @Path("/register")
    public Response registration(@Context HttpHeaders httpHeaders) throws NoSuchProviderException, NoSuchAlgorithmException
    {
        String username = httpHeaders.getHeaderString(HTTPHeaderNames.LOGIN);
        String email = httpHeaders.getHeaderString(HTTPHeaderNames.EMAIL);
        String repeatEmail = httpHeaders.getHeaderString(HTTPHeaderNames.REPEAT_EMAIL);
        String password = httpHeaders.getHeaderString(HTTPHeaderNames.PASSWORD);
        String repeatPassword = httpHeaders.getHeaderString(HTTPHeaderNames.REPEAT_PASSWORD);


        return registrationHelper.registerUser(username, email, repeatEmail, password, repeatPassword).build();


    }


}
