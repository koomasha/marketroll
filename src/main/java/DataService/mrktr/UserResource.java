/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataService.mrktr;

import DataService.mrktr.auth.Secured;
import DataService.mrktr.bl.UserBL;
import DataService.mrktr.common.HttpRequest;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
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
@Path("user")
public class UserResource extends BaseResource {

    /**
     * Creates a new instance of UserResource
     */
    public UserResource() {
    }

    @POST
    @Path("getuser")
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getUser(String userID) {
        try {
            UserBL user = new UserBL();
            int ID = Integer.parseInt(userID);
            return GSON.toJson(user.getUserByID(ID));
        }
        catch (Exception e) {
            String msg = e.getMessage();
            HashMap<String, String> error = new HashMap<>();
            error.put("error", msg);
            return GSON.toJson(error);
        }
    }

    @POST
    @Path("getmyuser")
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getMyUser() {
        try {
            UserBL user = new UserBL();
            int ID = getAuthUser().getUserID();
            return GSON.toJson(user.getUserByID(ID));
        }
        catch (Exception e) {
            String msg = e.getMessage();
            HashMap<String, String> error = new HashMap<>();
            error.put("error", msg);
            return GSON.toJson(error);
        }
    }

    @POST
    @Path("getuserfriends")
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserFriends(String userID) {
        try {
            UserBL user = new UserBL();
            int ID = Integer.parseInt(userID);
            return GSON.toJson(user.getUserFriends(ID));
        }
        catch (Exception e) {
            String msg = e.getMessage();
            HashMap<String, String> error = new HashMap<>();
            error.put("error", msg);
            return GSON.toJson(error);
        }
    }

    @POST
    @Path("getmyfriends")
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getMyFriends() {
        try {
            UserBL user = new UserBL();
            int ID = getAuthUser().getUserID();
            return GSON.toJson(user.getUserFriends(ID));
        }
        catch (Exception e) {
            String msg = e.getMessage();
            HashMap<String, String> error = new HashMap<>();
            error.put("error", msg);
            return GSON.toJson(error);
        }
    }

}
