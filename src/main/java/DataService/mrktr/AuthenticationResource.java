/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataService.mrktr;

import DataService.mrktr.bl.UserBL;
import DataService.mrktr.facebook.FacebookSession;
import java.util.HashMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author KooMasha
 */
@Path("authentication")
public class AuthenticationResource extends BaseResource{
    /**
     * Creates a new instance of AuthenticationResource
     */
    public AuthenticationResource() {
    }

    @POST
    @Path("getaccestoken")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createToken(String code) {
        try {

            FacebookSession fb = new FacebookSession(code);
            UserBL user = new UserBL();
            HashMap<String, String> result = user.facebookLogin(fb);
            LOGGER.info("hello world");
            return GSON.toJson(result);
        } catch (Exception e) {
            String msg = e.getMessage();
            return "{\"error\":\"" + msg + "\"}";
        }
    }

    /**
     * POST method for updating or creating an instance of
     * AuthenticationResource
     *
     * @return
     */
    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public String getRedirectURL() {
        try {
            String url = FacebookSession.getLoginRedirectURL();
            return "{\"url\":\"" + url + "\"}";
        } catch (Exception e) {
            String msg = e.getMessage();
            return "{\"error\":\"" + msg + "\"}";
        }
    }

}
