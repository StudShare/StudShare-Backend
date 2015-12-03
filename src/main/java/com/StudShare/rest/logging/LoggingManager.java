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
@ComponentScan(basePackageClasses = Authenticator.class)
public class LoggingManager
{

    @Autowired
    @Qualifier("authenticator")
    Authenticator authenticator;

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Context HttpHeaders httpHeaders)
            throws NoSuchProviderException, NoSuchAlgorithmException, JsonProcessingException
    {

        Map<String, String> map = new TreeMap<String, String>();
        ObjectMapper mapper = new ObjectMapper();


        try
        {
            String username = httpHeaders.getHeaderString(HTTPHeaderNames.USERNAME);
            String password = httpHeaders.getHeaderString(HTTPHeaderNames.PASSWORD);

            String authToken = authenticator.login(username, password);

            map.put("username", username);
            map.put("auth_token", authToken);

            String jsonResponse = mapper.writeValueAsString(map);

            return getNoCacheResponseBuilder(Response.Status.OK).entity(jsonResponse).build();
        }
        catch (final LoginException ex)
        {
            map.put("message", "Problem matching service key, username and password");
            String jsonResponse = mapper.writeValueAsString(map);
            return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity(jsonResponse).build();
        }

    }


    @POST
    @Path("/logout")
    public Response logout(@Context HttpHeaders httpHeaders) throws GeneralSecurityException
    {
        try
        {

            String username = httpHeaders.getHeaderString(HTTPHeaderNames.USERNAME);
            String authToken = httpHeaders.getHeaderString(HTTPHeaderNames.AUTH_TOKEN);


            authenticator.logout(username, authToken);

            return getNoCacheResponseBuilder(Response.Status.NO_CONTENT).build();

        }
        catch (final GeneralSecurityException ex)
        {
            return getNoCacheResponseBuilder(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
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
