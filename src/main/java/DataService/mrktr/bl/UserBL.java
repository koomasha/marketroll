/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataService.mrktr.bl;

import DataService.mrktr.dal.UserDAL;
import DataService.mrktr.dal.dbconnect.GraphConnection;
import DataService.mrktr.dal.dbconnect.JedisConnect;
import DataService.mrktr.facebook.FacebookSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author KooMasha
 */
public class UserBL extends BaseBL {

    private final GraphConnection _conn = new GraphConnection();
    private final UserDAL _userDAL = new UserDAL(_conn);

    public UserBL() {
    }

    public HashMap<String, String> facebookLogin(FacebookSession fb) throws Exception {
        Map<String, Object> userData = fb.getObject("me", "age_range,birthday,currency,devices,education,email,favorite_athletes,favorite_teams,first_name,gender,hometown,id,inspirational_people,interested_in,languages,last_name,locale,location,middle_name,political,work,website,timezone,sports,significant_other,religion,relationship_status,name");
        Map<String, Object> friendsData = fb.getObject("me/friends", "id");
        try {
            _conn.beginTransaction();
            int userID = upsertUser(userData, friendsData);
            String inToken = generateToken(fb.getAccessToken(), fb.getTokenExpire(), userID);
            _conn.commit();

            HashMap<String, String> result = new HashMap<>();
            result.put("token", inToken);
            result.put("userID", String.valueOf(userID));
            return result;
        }
        catch (Exception e) {
            _conn.rollback();
            throw new Exception("Error while logging", e);
        }
    }

    public Map<String, Object> getUserByID(int userID) {
        return _userDAL.getUserByID(userID);
    }

    public List<Map<String, Object>> getUserFriends(int userID) {
        return _userDAL.getUserFriends(userID);
    }

    private String generateToken(String accessToken, String tokenExpire, int userID) {
        HashMap<String, String> props = new HashMap<>();
        props.put("fbToken", accessToken);
        props.put("userID", String.valueOf(userID));
        return JedisConnect.generateToken(props, tokenExpire);
    }

    private int upsertUser(Map<String, Object> userData, Map<String, Object> friendsData) {
        String fbID = userData.get("id").toString();
        int userID = _userDAL.getUserIDByFbID(fbID);
        if (userID < 0) {
            Map<String, Object> user = getMainUserDatObject(fbID, userData);
            userID = _userDAL.createNewUser(user, userData);
        }
        else {
            _userDAL.updateUserExtraData(userData, userData.get("id").toString());
        }
        updateUserFriendsRelations(fbID, (List<Map<String, Object>>) GSON.fromJson((String) friendsData.get("data"), getListOfMapsType()));
        if (userData.containsKey("location")) {
            Map<String, Object> location = (Map<String, Object>) GSON.fromJson((String) userData.get("location"), getMapType());
            upsertUserLocation(fbID, location);
        }

        return userID;
    }

    private Map<String, Object> getMainUserDatObject(String fbID, Map<String, Object> userData) {
        Map<String, Object> user = new HashMap<>();
        user.put("fbID", fbID);
        user.put("email", userData.get("email"));
        user.put("firstName", userData.get("first_name"));
        user.put("lastName", userData.get("last_name"));
        user.put("birthday", userData.containsKey("birthday") ? userData.get("birthday") : "");
        user.put("gender", userData.containsKey("gender") ? userData.get("gender") : "");
        user.put("locale", userData.containsKey("locale") ? userData.get("locale") : "");
        user.put("middle_name", userData.containsKey("middle_name") ? userData.get("middle_name") : "");
        user.put("timezone", userData.containsKey("timezone") ? userData.get("timezone") : "");
        user.put("relationship_status", userData.containsKey("relationship_status") ? userData.get("relationship_status") : "");
        return user;
    }

    private void updateUserFriendsRelations(String myFbID, List<Map<String, Object>> friendsData) {
        int numOfFriends = friendsData.size();
        String[] friendIDs = new String[numOfFriends];
        for (int i = 0; i < numOfFriends; i++) {
            friendIDs[i] = friendsData.get(i).get("id").toString();
        }
        _userDAL.updateUserFriendsRelations(myFbID, friendIDs);
    }

    private void upsertUserLocation(String fbID, Map<String, Object> location) {
        if (location != null) {
            List<String> userLocations = _userDAL.getUserLocationsIDs(fbID);
            if (userLocations.isEmpty() || !userLocations.contains(location.get("id").toString())) {
                _userDAL.upsertUserLocation(fbID, location);
            }
        }
    }

}
