package com.StudShare.rest.registration;

import com.StudShare.rest.HTTPHeaderNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Path("/user")
@ComponentScan(basePackageClasses = {RegistrationHelper.class, ConfirmationRegistrationHelper.class})
public class RegistrationManager
{
    @Autowired
    RegistrationHelper registrationHelper;

    @Autowired
    ConfirmationRegistrationHelper confirmRegistrationHelper;

    @POST
    @Path("/register")
    public Response registration(@Context HttpHeaders httpHeaders,
                                 @Context UriInfo uriInfo) throws NoSuchProviderException, NoSuchAlgorithmException, URISyntaxException
    {

        String login = httpHeaders.getHeaderString(HTTPHeaderNames.LOGIN);
        String email = httpHeaders.getHeaderString(HTTPHeaderNames.EMAIL);
        String repeatEmail = httpHeaders.getHeaderString(HTTPHeaderNames.REPEAT_EMAIL);
        String password = httpHeaders.getHeaderString(HTTPHeaderNames.PASSWORD);
        String repeatPassword = httpHeaders.getHeaderString(HTTPHeaderNames.REPEAT_PASSWORD);

        return registrationHelper.registerUser(login, email, repeatEmail, password, repeatPassword, uriInfo).build();

    }

    @GET
    @Path("/register")
    public Response confirmationRegistration(@QueryParam("u") long idSiteUser,
                                             @QueryParam("activation_key") String activationKey,
                                             @Context HttpServletRequest httpServletRequest) throws URISyntaxException
    {

        StringBuilder url = new StringBuilder();
        url.append(httpServletRequest.getScheme())
                .append("://").append(httpServletRequest.getServerName());

        if (httpServletRequest.getServerPort() != 80 && httpServletRequest.getServerPort() != 443)
        {
            url.append(":").append(httpServletRequest.getServerPort());
        }


        return confirmRegistrationHelper.confirmRegisterUser(idSiteUser, activationKey, url.toString()).build();
    }


}
