/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataService.mrktr;

import DataService.mrktr.bl.UserBL;
import DataService.mrktr.common.HttpRequest;
import DataService.mrktr.facebook.FacebookSession;
import static DataService.mrktr.facebook.FacebookSession.APPID;
import static DataService.mrktr.facebook.FacebookSession.PERMS;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author KooMasha
 */
public class BaseTest {
    
    protected static final String _url = "http://localhost:8080/DataService/mrktr/";
    protected static final Gson GSON = new Gson();
    protected static String _token = null;
    
    protected String makePostRequest(String url, String postData) throws Exception {
        HashMap<String, String> headers = new HashMap<>();
        if (_token == null) {
            setTokenWithPhantomJS("marketrolltest@gmail.com", "Aa9506176");
            System.out.println("TOKEN CREATED WITH PHANTOMJS");
        }
        headers.put("Authorization", "Bearer " + _token);
        String response = HttpRequest.post(_url + url, postData, headers);
        return response;
    }
    
    protected String createTestUser() throws Exception {
        FacebookSession fb = new FacebookSession();
        
        Map<String, Object> params = new HashMap<>();
        params.put("installed", true);
        params.put("permissions", fb.PERMS);
        params.put("name", "");
        params.put("owner_access_token", fb.getAccessToken());
        Map<String, Object> testUser = fb.post(fb.APPID + "/accounts/test-users", params);

        String accessToken = testUser.get("access_token").toString();
        fb = new FacebookSession(accessToken, "6000");
        UserBL instance = new UserBL();
        _token = instance.facebookLogin(fb).get("token");
        return _token;
    }
    
    protected void setTokenWithPhantomJS(String username, String password) throws Exception {
        String url = FacebookSession.getLoginRedirectURL();
        String cmd = "phantomjs src/test/java/DataService/mrktr/phantomjs/facebook.js ";
        cmd = cmd + username + " " + password + " \"" + url + "\"";
        Process process = Runtime.getRuntime().exec(cmd);
        
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        
        int exitStatus = process.waitFor();
        String lastLine = "";
        String currentLine = bufferedReader.readLine();
        StringBuilder stringBuilder = new StringBuilder();
        while (currentLine != null) {
            lastLine = currentLine;
            stringBuilder.append(currentLine);
            currentLine = bufferedReader.readLine();
        }
        _token = lastLine;
    }
    
    protected Map<String, String> responseToMap(String response) {
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        return GSON.fromJson(response, type);
    }
    
    protected List<Map<String, String>> responseToList(String response) {
        Type type = new TypeToken<List<Map<String, String>>>() {
        }.getType();
        return GSON.fromJson(response, type);
    }
}
