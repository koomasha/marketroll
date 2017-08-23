/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataService.mrktr;

import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author KooMasha
 */
public class UserResourceTest extends BaseTest {

    public UserResourceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getUser method, of class UserResource.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetUser() throws Exception {
        String url = "user/getuser";
        String response = makePostRequest(url, "0");
        Map<String, String> result = responseToMap(response);
        assertNotNull(result);
        assertNull(result.get("error"));
    }

    /**
     * Test of getMyUser method, of class UserResource.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetMyUser() throws Exception {
        String url = "user/getmyuser";
        Map<String, String> result = responseToMap(makePostRequest(url, null));
        assertNotNull(result);
        assertNull(result.get("error"));
    }

    /**
     * Test of getUserFriends method, of class UserResource.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetUserFriends() throws Exception {
        String url = "user/getuserfriends";
        List<Map<String, String>> result = responseToList(makePostRequest(url, "0"));
        if (result.size() == 1) {
            assertNull(result.get(0).get("error"));
        }
    }

    /**
     * Test of getMyFriends method, of class UserResource.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetMyFriends() throws Exception {
        String url = "user/getmyfriends";
        List<Map<String, String>> result = responseToList(makePostRequest(url, null));
        if (result.size() == 1) {
            assertNull(result.get(0).get("error"));
        }
    }

}
