/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataService.mrktr.dal;

import DataService.mrktr.common.TypeParser;
import DataService.mrktr.dal.dbconnect.GraphConnection;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.DatatypeConverter;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;

/**
 *
 * @author KooMasha
 */
public class UserDAL {

    private final GraphConnection _conn;

    public UserDAL(GraphConnection conn) {
        _conn = conn;
    }

    public int createNewUser(Map<String, Object> userMainData, Map<String, Object> userExtraData) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("user", userMainData);
        params.put("data", userExtraData);

        StatementResult userResult = _conn.run("CREATE (u:User {user})  "
                + "CREATE  (d:fbUser {data}) "
                + "CREATE  (u)-[:DETAILS]->(d) "
                + "RETURN (id(u)) as id", params);
        userResult.hasNext();
        Record user = userResult.single();
        int userID = user.get("id").asInt();
        return userID;
    }

    public void updateUserFriendsRelations(String myFbID, String[] friendIDs) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("myFbID", myFbID);
        params.put("friendIDs", friendIDs);
        _conn.run("MATCH (a:User { fbID: {myFbID} }), (b:User) WHERE  b.fbID in {friendIDs} MERGE (a)-[:FRIEND]-(b)", params);
    }

    public void updateUserExtraData(Map<String, Object> userData, String fbID) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("data", userData);
        params.put("fbID", fbID);
        _conn.run("MATCH (u:User {fbID: {fbID}})-[:DETAILS]->(d) SET d = {data}", params);
    }

    public int getUserIDByFbID(String fbID) {
        int userID = -1;
        HashMap<String, Object> params = new HashMap<>();
        params.put("fbID", fbID);
        StatementResult userResult = _conn.run("MATCH (n) WHERE n.fbID = {fbID}  RETURN max(id(n)) as id", params);
        userResult.hasNext();
        Record user = userResult.single();
        if (!user.get("id").isNull()) {
            userID = user.get("id").asInt();
        }
        return userID;
    }

    public Map<String, Object> getUserByID(int userID) {
        HashMap<String, Object> params = new HashMap<>();
        Map<String, Object> user = new HashMap<>();
        params.put("userID", userID);
        StatementResult queryResult = _conn.run("MATCH (u) WHERE id(u) = {userID}  RETURN properties(u) as user", params);
        queryResult.hasNext();
        Record userRecord = queryResult.single();
        if (!userRecord.get("user").isNull()) {
            user = userRecord.get("user").asMap();
        }
        return user;
    }

    public List<Map<String, Object>> getUserFriends(int userID) {
        HashMap<String, Object> params = new HashMap<>();
        List<Map<String, Object>> friends = new ArrayList<>();
        params.put("userID", userID);
        StatementResult queryResult = _conn.run("MATCH (u)-[:FRIEND]-(f) WHERE id(u) = {userID}  RETURN properties(f) as friend", params);
        while (queryResult.hasNext()) {
            Record friendRecord = queryResult.next();
            if (!friendRecord.get("friend").isNull()) {
                friends.add(friendRecord.get("friend").asMap());
            }
        }
        return friends;
    }

    public List<String> getUserLocationsIDs(String fbID) {
        List<String> result = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("fbID", fbID);
        StatementResult locationsResult = _conn.run("MATCH (u:User {fbID: {fbID}})-[:LOCATED]-(l:Location) RETURN l.id AS id", params);
        while (locationsResult.hasNext()) {
            Record location = locationsResult.next();
            if (!location.get("id").isNull()) {
                result.add(location.get("id").asString());
            }
        }
        return result;
    }

    public void upsertUserLocation(String fbID, Map<String, Object> location) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("fbID", fbID);
        params.put("timestamp", DatatypeConverter.printDateTime(new GregorianCalendar()));
        params.put("location", location);
        params.put("locationID", location.get("id").toString());
        _conn.run("MERGE (l:Location {id: {locationID}}) SET l = {location}", params);
        _conn.run("MATCH (u:User {fbID: {fbID}}), (l:Location {id: {locationID}}) CREATE  (u)-[:LOCATED {timestamp: {timestamp}}]->(l)", params);
    }
}
