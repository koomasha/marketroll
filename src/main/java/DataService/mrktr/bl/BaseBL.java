/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataService.mrktr.bl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 *
 * @author KooMasha
 */
public class BaseBL {
    protected static Gson GSON = new Gson();
    
    public Type getListOfMapsType(){
        return new TypeToken<List<Map<String, Object>>>() {}.getType();
    }
    
        public Type getMapType(){
        return new TypeToken<Map<String, Object>>() {}.getType();
    }
}
