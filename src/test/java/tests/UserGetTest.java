package tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.BaseTestCase;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestCase {

    private ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testAuthAndGetAnotherData(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response response = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        JsonPath response1 = apiCoreRequests
                .makeGetJSONRequest("https://playground.learnqa.ru/api/user/1");

        Assertions.assertEquals(response1.get("username"), "Lana");
        }
}
