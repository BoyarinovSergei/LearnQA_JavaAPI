package tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.BaseTestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static lib.DataGenerator.getRandomEmail;
import static lib.DataGenerator.getRegistrationData;

public class UserDeleteTest extends BaseTestCase {
    private ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testDeleteUserWithID2(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        Response response1 = apiCoreRequests
                .makeDeleteRequestWithTokenCookie(
                        "https://playground.learnqa.ru/api/user/2",
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        Assert.assertEquals("Please, do not delete test users with ID 1, 2, 3, 4 or 5.", response1.asString());
    }

    @Test
    public void testCreateAndDeleteUser(){
        Map<String, String> data = getRegistrationData();

        JsonPath firstUser = apiCoreRequests
                .makePostJSONRequestWithParams(
                        "https://playground.learnqa.ru/api/user/", getRegistrationData(data));

        String userId = firstUser.getString("id");

        Map<String, String> authData = new HashMap<>();
        authData.put("email", data.get("email"));
        authData.put("password", data.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        Response response1 = apiCoreRequests
                .makeDeleteRequestWithTokenCookie(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        Response response2 = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId, "", "");

        Assert.assertEquals("User not found", response2.asString());
    }

    @Test
    public void testDeleteUserBeingAuthorizedAsAnotherUser(){
        Map<String, String> dataForFirstUser = getRegistrationData();
        dataForFirstUser.put("email", getRandomEmail());

        JsonPath firstUser = apiCoreRequests
                .makePostJSONRequestWithParams(
                        "https://playground.learnqa.ru/api/user/", getRegistrationData(dataForFirstUser));

        String firstUserId = firstUser.getString("id");

        Map<String, String> dataForSecondUser = getRegistrationData();
        dataForSecondUser.put("email", getRandomEmail());

        JsonPath secondUser = apiCoreRequests
                .makePostJSONRequestWithParams(
                        "https://playground.learnqa.ru/api/user/", getRegistrationData(dataForSecondUser));

        String secondUserId = secondUser.getString("id");

        Map<String, String> authData = new HashMap<>();
        authData.put("email", dataForFirstUser.get("email"));
        authData.put("password", dataForFirstUser.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        Response response1 = apiCoreRequests
                .makeDeleteRequestWithTokenCookie(
                        "https://playground.learnqa.ru/api/user/" + secondUserId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        response1.prettyPrint();
    }
}
