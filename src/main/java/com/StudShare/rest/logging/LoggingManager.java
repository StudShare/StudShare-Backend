package com.StudShare.rest.logging;

import com.StudShare.rest.HTTPHeaderNames;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;


@Path("/user")
@ComponentScan(basePackageClasses = LoginAuthenticator.class)
public class LoggingManager
{

    @Autowired
    LoginAuthenticator authenticatorLogin;

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Context HttpHeaders httpHeaders)
            throws NoSuchProviderException, NoSuchAlgorithmException, JsonProcessingException
    {
        String username = httpHeaders.getHeaderString(HTTPHeaderNames.LOGIN);
        String password = httpHeaders.getHeaderString(HTTPHeaderNames.PASSWORD);
        return authenticatorLogin.login(username, password).build();


    }


    @POST
    @Path("/logout")
    public Response logout(@Context HttpHeaders httpHeaders) throws GeneralSecurityException
    {

        String username = httpHeaders.getHeaderString(HTTPHeaderNames.LOGIN);
        String authToken = httpHeaders.getHeaderString(HTTPHeaderNames.AUTH_TOKEN);

        return authenticatorLogin.logout(username, authToken).build();


    }


}
