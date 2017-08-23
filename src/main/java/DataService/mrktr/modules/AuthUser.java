/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataService.mrktr.modules;

/**
 *
 * @author KooMasha
 */
public class AuthUser {
    private String _token;
    private int _userID;
    private String _fbAccessToken;
    private String _role;

    public AuthUser() {
    }

    
    public AuthUser(String _token, int _userID, String _fbAccessToken, String _role) {
        this._token = _token;
        this._userID = _userID;
        this._fbAccessToken = _fbAccessToken;
        this._role = _role;
    }

    public String getToken() {
        return _token;
    }

    public int getUserID() {
        return _userID;
    }

    public String getFbAccessToken() {
        return _fbAccessToken;
    }

    public String getRole() {
        return _role;
    }
    
    
}
