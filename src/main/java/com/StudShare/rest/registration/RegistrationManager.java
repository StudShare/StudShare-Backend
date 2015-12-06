package com.StudShare.rest.registration;

import com.StudShare.rest.HTTPHeaderNames;
import com.StudShare.rest.logging.AuthenticatorLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Path("/user")
@ComponentScan(basePackageClasses = AuthenticatorLogin.class)
public class RegistrationManager
{
    @Autowired
    AuthenticatorRegistration authenticatorRegistration;

    @POST
    @Path("/registration")
    public Response registration(@Context HttpHeaders httpHeaders) throws NoSuchProviderException, NoSuchAlgorithmException
    {
        String username = httpHeaders.getHeaderString(HTTPHeaderNames.USERNAME);
        String email = httpHeaders.getHeaderString(HTTPHeaderNames.EMAIL);
        String repeatEmail = httpHeaders.getHeaderString(HTTPHeaderNames.REPEAT_EMAIL);
        String password = httpHeaders.getHeaderString(HTTPHeaderNames.PASSWORD);
        String repeatPassword = httpHeaders.getHeaderString(HTTPHeaderNames.REPEAT_PASSWORD);


        return authenticatorRegistration.registration(username, email, repeatEmail, password, repeatPassword).build();


    }


}
