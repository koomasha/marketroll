/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataService.mrktr.auth;

import DataService.mrktr.modules.AuthUser;
import DataService.mrktr.dal.dbconnect.JedisConnect;
import java.security.Principal;

/**
 *
 * @author KooMasha
 */
public class AuthUserPrincipal implements Principal {

    AuthUser _authUser;

    public AuthUserPrincipal(String token) {
        _authUser = JedisConnect.getAuthenticatedUser(token);
    }

    @Override
    public String getName() {
        return _authUser.toString();
    }

    public AuthUser getAuthUser() {
        return _authUser;
    }

}

