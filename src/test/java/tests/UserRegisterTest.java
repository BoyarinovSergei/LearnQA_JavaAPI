package tests;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest extends BaseTestCase {
    private ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testCreateUserWithWrongEmail(){
        Map<String, String> map = new HashMap<>();
        map.put("username", "string");
        map.put ("firstName", "string");
        map.put ("lastName", "string");
        map.put ("email", "qweryandex.ru");
        map.put ("password", "string");

        Response response = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", map);

        response.asString();
        Assertions.assertEquals(response.asString(), "Invalid email format");
    }
}
