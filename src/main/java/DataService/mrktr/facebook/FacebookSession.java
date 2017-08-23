/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataService.mrktr.facebook;

import DataService.mrktr.common.HttpRequest;
import DataService.mrktr.common.TypeParser;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author KooMasha
 */
public class FacebookSession {

    private static final String SECRET = "e81696cc07900cee066b316908545d51";
    public static final String APPID = "1750889985165253";
    private static final String REDIRECTURL = "http://localhost:8080/DataService";
    public static final String PERMS = "email,user_friends,user_birthday,user_education_history,user_likes,user_hometown,user_relationship_details,user_location,user_religion_politics,user_work_history,user_website,user_relationships";

    private final String _accessToken;
    private final String _tokenExpire;
    private final FacebookClient _session;

    public FacebookSession(String code) throws Exception {
        String authData = HttpRequest.get(getAuthURL(code), null);
        _accessToken = parseToken(authData);
        _tokenExpire = parseExpirationDate(authData);
        _session = new DefaultFacebookClient(_accessToken, Version.VERSION_2_7);
    }

    public FacebookSession(String accessToken, String tokenExpire) {
        _accessToken = accessToken;
        _tokenExpire = tokenExpire;
        _session = new DefaultFacebookClient(_accessToken, Version.VERSION_2_7);
    }

    //Creating session for new test user
    public FacebookSession() throws Exception {
        String authData = HttpRequest.get(getTestAuthURL(), null);
        _accessToken =  parseToken(authData);
        _session = new DefaultFacebookClient(_accessToken, Version.VERSION_2_7);
        _tokenExpire = "6000";
    }

    public Map<String, Object> getObject(String method, String fields) {
        JsonObject data = _session.fetchObject(method, JsonObject.class, Parameter.with("fields", fields));
        return TypeParser.mapValuesToJson(TypeParser.jsonObjectToMap(data));
    }

    public List<Map<String, Object>> getList(String method, String fields) {
        Connection<JsonObject> userData = _session.fetchConnection(method, JsonObject.class);
        List<JsonObject> fbResult = userData.getData();
        List<Map<String, Object>> result = new ArrayList<>();
        for (JsonObject val : fbResult) {
            result.add(TypeParser.jsonObjectToMap(val));
        }
        return TypeParser.listValuesToJson(result);
    }

    public Map<String, Object> post(String method, Map<String, Object> params) {
        int i = 0;
        Parameter[] arr = new Parameter[params.size()];
        for (String key : params.keySet()) {
            arr[i] = Parameter.with(key, params.get(key));
            i++;
        }
        JsonObject response = _session.publish(method, JsonObject.class, arr);

        return TypeParser.mapValuesToJson(TypeParser.jsonObjectToMap(response));
    }

    public String getAccessToken() {
        return _accessToken;
    }

    public String getTokenExpire() {
        return _tokenExpire;
    }

    public static String getLoginRedirectURL() {
        return "https://graph.facebook.com/oauth/authorize?client_id="
                + APPID + "&display=page&redirect_uri="
                + REDIRECTURL + "&scope=" + PERMS;
    }

    private String getAuthURL(String authCode) {
        return "https://graph.facebook.com/oauth/access_token?client_id="
                + APPID + "&redirect_uri="
                + REDIRECTURL + "&client_secret=" + SECRET + "&code=" + authCode;
    }

    private String getTestAuthURL() {
        return "https://graph.facebook.com/oauth/access_token?client_id="
                + APPID + "&client_secret=" + SECRET + "&grant_type=client_credentials";
    }

    private String parseToken(String response) {
        String token = (response.split("&"))[0].replace("access_token=", "");
        return token;
    }

    private String parseExpirationDate(String response) {
        String exp = (response.split("&"))[1].replace("expires=", "");
        return exp;
    }
}
