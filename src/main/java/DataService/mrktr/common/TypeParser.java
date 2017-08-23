/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataService.mrktr.common;

import com.google.gson.Gson;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author KooMasha
 */
public class TypeParser {

    private static Gson GSON = new Gson();

    public static Map<String, Object> jsonObjectToMap(JsonObject jo) {
        Map<String, Object> result = new HashMap<>();
        JsonArray propertyNames = jo.names();
        String key;
        Object value;
        for (int i = 0; i < propertyNames.length(); i++) {
            key = propertyNames.getString(i);
            value = jo.get(key);
            value = parseObject(value);
            result.put(key, value);
        }
        return result;
    }

    public static List<Map<String, Object>> jsonArrayToList(JsonArray ja) {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> value;
        for (int i = 0; i < ja.length(); i++) {
            value = (Map<String, Object >)parseObject(ja.get(i));
            result.add(value);
        }
        return result;
    }

    public static Map<String, Object> mapValuesToJson(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>();
        for (String key : map.keySet()) {
            if (!(map.get(key) instanceof String)) {
                result.put(key, GSON.toJson(map.get(key)));
            }
            else {
                result.put(key, map.get(key));
            }
        }
        return result;
    }

    public static List<Map<String, Object>> listValuesToJson(List<Map<String, Object>> list) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> map : list) {
            result.add(mapValuesToJson(map));
        }
        return result;
    }

    private static Object parseObject(Object obj) {
        String test = "";
        Object newObj = null;
        if (obj instanceof JsonObject) {
            newObj = jsonObjectToMap((JsonObject) obj);
        }
        else if (obj instanceof JsonArray) {
            newObj = jsonArrayToList((JsonArray) obj);
        }
        else if (obj instanceof Integer || obj instanceof Double) {
            newObj = String.valueOf(obj);
        }
        else if (obj instanceof String) {
            newObj = obj;
        }
        else if (!(obj instanceof String)) {
            test = "Other";
        }
        return newObj;
    }

}
