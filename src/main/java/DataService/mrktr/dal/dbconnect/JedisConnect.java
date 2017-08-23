/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataService.mrktr.dal.dbconnect;

import DataService.mrktr.modules.AuthUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.NotAuthorizedException;
import redis.clients.jedis.Jedis;

/**
 *
 * @author KooMasha
 */
public class JedisConnect {

    private static final Jedis JEDIS = new Jedis("104.154.226.78", 6379);
    private static final Gson GSON = new Gson();

    public static String getKey(String key) {
        return JEDIS.get(key);
    }

    public static void setKey(String key, String value) {
        JEDIS.set(key, value);
    }

    public static void setExpire(String key, String expire) {
        JEDIS.expire(key, Integer.parseInt(expire));
    }

    public static String generateToken(HashMap<String, String> props, String tokenExpire) {
        String token = UUID.randomUUID().toString();
        setKey("token/" + token, GSON.toJson(props));
        setExpire("token/" + token, tokenExpire);
        return token;
    }

    private static Map<String, String> getTokenData(String key) throws NotAuthorizedException{
        String value = getKey("token/" + key);
        if (value == null) {
            throw new NotAuthorizedException("Please aquire a valid token");
        }
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> result = GSON.fromJson(value, type);
        return result;
    }

    public static AuthUser getAuthenticatedUser(String token) {
        Map<String, String> data = getTokenData(token);
        String fbToken = data.get("fbToken");
        String userID = data.get("userID");
        return new AuthUser(token, Integer.parseInt(userID), fbToken, "User");
    }

}
