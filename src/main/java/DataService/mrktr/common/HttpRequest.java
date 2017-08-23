/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataService.mrktr.common;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author KooMasha
 */
public class HttpRequest {

    private static final Client _client = ClientBuilder.newClient();

    // HTTP GET request
    public static String get(String url, HashMap<String, String> headers) throws Exception {
        Response response = invocationBuilder(url, headers).get(Response.class);
        checkResponseStatus(response);
        return response.readEntity(String.class);
    }

    // HTTP POST request
    public static String post(String url, String postData, HashMap<String, String> headers) throws Exception {
        Response response = invocationBuilder(url, headers).post(Entity.entity(postData, MediaType.APPLICATION_JSON), Response.class);
        checkResponseStatus(response);
        return response.readEntity(String.class);
    }

    private static Invocation.Builder invocationBuilder(String url, HashMap<String, String> headers) {
        WebTarget webTarget = _client.target(url);
        Invocation.Builder invocationBuilder = webTarget.request().accept(MediaType.APPLICATION_JSON);
        if(headers != null){
            for(String key : headers.keySet()){
                invocationBuilder.header(key, headers.get(key));
            } 
        }
        return invocationBuilder;
    }

    private static void checkResponseStatus(Response response) {
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
    }
}
