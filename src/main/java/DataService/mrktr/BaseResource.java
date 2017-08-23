/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataService.mrktr;

import DataService.mrktr.auth.AuthUserPrincipal;
import DataService.mrktr.modules.AuthUser;
import com.google.gson.Gson;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author KooMasha
 */

public class BaseResource {

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo context;

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseResource.class);
    protected static final Gson GSON = new Gson();

    public UriInfo getContext() {
        return context;
    }

    public AuthUser getAuthUser() {
        return ((AuthUserPrincipal) securityContext.getUserPrincipal()).getAuthUser();
    }   
}
