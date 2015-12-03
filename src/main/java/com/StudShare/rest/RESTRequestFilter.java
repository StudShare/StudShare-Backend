package com.StudShare.rest;

import com.StudShare.rest.logging.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.util.logging.Logger;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
@ComponentScan(basePackageClasses = Authenticator.class)
public class RESTRequestFilter implements ContainerRequestFilter
{

    @Autowired
    @Qualifier("authenticator")
    Authenticator authenticator;


    private final static Logger log = Logger.getLogger(RESTRequestFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext requestCtx) throws IOException
    {

        String path = requestCtx.getUriInfo().getPath();
        log.info("Filtering request path: " + path);

        // IMPORTANT!!! First, Acknowledge any pre-flight test from browsers for this case before validating the headers (CORS stuff)
        if (requestCtx.getRequest().getMethod().equals("OPTIONS"))
        {
            requestCtx.abortWith(Response.status(Response.Status.OK).build());
            return;
        }

        // For any pther methods besides login, the authToken must be verified
        if (path.startsWith("user/logout"))
        {
            String authToken = requestCtx.getHeaderString(HTTPHeaderNames.AUTH_TOKEN);
            String username = requestCtx.getHeaderString(HTTPHeaderNames.USERNAME);
            // if it isn't valid, just kick them out.
            if (!authenticator.isAuthTokenValid(username, authToken))
            {
                requestCtx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        }
    }
}