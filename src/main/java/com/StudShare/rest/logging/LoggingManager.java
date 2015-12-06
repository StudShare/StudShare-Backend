package com.StudShare.rest.logging;

import com.StudShare.rest.HTTPHeaderNames;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Map;
import java.util.TreeMap;
import javax.security.auth.login.LoginException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;


@Path("/user")
@ComponentScan(basePackageClasses = AuthenticatorLogin.class)
public class LoggingManager
{

    @Autowired
    AuthenticatorLogin authenticatorLogin;

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Context HttpHeaders httpHeaders)
            throws NoSuchProviderException, NoSuchAlgorithmException, JsonProcessingException
    {
        String username = httpHeaders.getHeaderString(HTTPHeaderNames.USERNAME);
        String password = httpHeaders.getHeaderString(HTTPHeaderNames.PASSWORD);

        return authenticatorLogin.login(username, password).build();


    }


    @POST
    @Path("/logout")
    public Response logout(@Context HttpHeaders httpHeaders) throws GeneralSecurityException
    {

        String username = httpHeaders.getHeaderString(HTTPHeaderNames.USERNAME);
        String authToken = httpHeaders.getHeaderString(HTTPHeaderNames.AUTH_TOKEN);

        return authenticatorLogin.logout(username, authToken).build();


    }


}
