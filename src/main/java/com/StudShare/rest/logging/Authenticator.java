package com.StudShare.rest.logging;


import com.StudShare.domain.SiteUser;
import com.StudShare.domain.Token;
import com.StudShare.service.UserManagerDao;


import com.StudShare.service.UserManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.security.*;
import java.util.UUID;
import javax.security.auth.login.LoginException;

@Component("authenticator")
@ComponentScan(basePackageClasses = UserManagerDao.class)
public final class Authenticator
{

    @Autowired
    @Qualifier("userManager")
    private UserManagerDao userManager;

    public UserManagerDao getUserManager()
    {
        return userManager;
    }

    public void setUserManager(UserManagerDao userManager)
    {
        this.userManager = userManager;
    }

    public String login(String username, String password) throws LoginException
    {

        SiteUser siteUser = userManager.findUserByUsername(username);

        if (siteUser != null)
        {
            String passwordToCheck = getSecurePassword(password, siteUser.getSalt());

            if (siteUser.getHash().equals(passwordToCheck))
            {
                Token token = new Token();
                token.setSiteUser(siteUser);
                String authToken;
                Token tokenExist;

                do
                {
                    authToken = UUID.randomUUID().toString();
                    tokenExist = userManager.findTokenBySSID(authToken);
                }
                while (tokenExist != null);

                token.setToken(authToken);
                userManager.addToken(token);
                return authToken;
            }
        }

        throw new LoginException("Don't Come Here Again!");
    }

    /**
     * The method that pre-validates if the client which invokes the REST API is
     * from a authorized and authenticated source.
     *
     * @param username The username
     * @param ssid     The authorization token generated after login
     * @return TRUE for acceptance and FALSE for denied.
     */
    public boolean isAuthTokenValid(String username, String ssid)
    {
        SiteUser siteUser = userManager.findUserByUsername(username);
        Token token = userManager.findTokenBySSID(ssid);

        if (token != null && siteUser != null)
        {
            if (token.getSiteUser().getIdSiteUser() == siteUser.getIdSiteUser())
            {
                return true;
            }
        }
        return false;
    }


    public void logout(String username, String ssid) throws GeneralSecurityException
    {

        SiteUser siteUser = userManager.findUserByUsername(username);
        Token token = userManager.findTokenBySSID(ssid);

        if (token != null && siteUser != null)
        {
            if (token.getSiteUser().getIdSiteUser() == siteUser.getIdSiteUser())
            {
                userManager.deleteToken(token);
                return;
            }

        }

        throw new GeneralSecurityException("Invalid username and authorization token match.");
    }


    public String getSecurePassword(String passwordToHash, String salt)
    {
        String generatedPassword = null;
        try
        {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            //Add password bytes to digest
            md.update(salt.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest(passwordToHash.getBytes());
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    //Add salt
    public String generateSalt() throws NoSuchAlgorithmException, NoSuchProviderException
    {
        //Always use a SecureRandom generator
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        //Create array for salt
        byte[] salt = new byte[64];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        return salt.toString();
    }
}