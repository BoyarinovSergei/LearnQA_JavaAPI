package tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.BaseTestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;

import static lib.DataGenerator.getRandomEmail;
import static lib.DataGenerator.getRegistrationData;

public class UserEditTest  extends BaseTestCase {
    private ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testChangeDataWithoutAuth(){
        Map<String, String> data = new HashMap<>();
        data.put("email", getRandomEmail());

        JsonPath response = apiCoreRequests
                .makePostJSONRequestWithParams(
                        "https://playground.learnqa.ru/api/user/", getRegistrationData(data));

        String userID = response.getJsonObject("id");

        Map<String, String> forLogIn = new HashMap<>();
        forLogIn.put("email", data.get("email"));
        forLogIn.put("password", data.get("password"));

        Response response2 = apiCoreRequests
                .makePutRequestWithParams(
                        "https://playground.learnqa.ru/api/user/" + userID, forLogIn);

        Assertions.assertEquals("Auth token not supplied", response2.getBody().asString());
    }

    @Test
    public void testChangeDataBeingAuthorizedAsAnotherUser(){
        Map<String, String> dataForFirstUser = getRegistrationData();

        JsonPath firstUser = apiCoreRequests
                .makePostJSONRequestWithParams(
                        "https://playground.learnqa.ru/api/user/", getRegistrationData(dataForFirstUser));


        Map<String, String> dataForSecondUser = getRegistrationData();
        JsonPath secondUser = apiCoreRequests
                .makePostJSONRequestWithParams(
                        "https://playground.learnqa.ru/api/user/", getRegistrationData(dataForSecondUser));

        String secondUserID = secondUser.getJsonObject("id");

        Map<String, String> data2 = new HashMap<>();
        data2.put("email", dataForFirstUser.get("email"));
        data2.put("password", dataForFirstUser.get("password"));

        Response response1 = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api/user/login", data2);

        Response response2 = apiCoreRequests.
                makePutRequestWithParams(
                        "https://playground.learnqa.ru/api/user/" + secondUserID, dataForSecondUser);

        Assertions.assertEquals("Auth token not supplied", response2.getBody().asString());
    }

    @Test
    public void testChangeUserEmail(){
        Map<String, String> data = getRegistrationData();

        JsonPath firstUser = apiCoreRequests.makePostJSONRequestWithParams(
                "https://playground.learnqa.ru/api/user/", getRegistrationData(data));

        String userId = firstUser.getString("id");

        Map<String, String> authData = new HashMap<>();
        authData.put("email", data.get("email"));
        authData.put("password", data.get("password"));

        Response responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login", authData);

        Map<String, String> editData = new HashMap<>();
        editData.put("email", "asdferyandex.ru");

        Response responseEditUser = apiCoreRequests.
                makePutRequestWithTokenCookieBody(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"),
                        editData);

        Assert.assertEquals("Invalid email format", responseEditUser.asString());

        JsonPath responseUserData = apiCoreRequests.
                makeGetRequestWithTokenCookieJSON(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        Assert.assertEquals(data.get("email"), responseUserData.getString("email"));
    }

    @Test
    public void testChangeFirstName(){
        Map<String, String> data = getRegistrationData();

        JsonPath firstUser = apiCoreRequests
                .makePostJSONRequestWithParams("https://playground.learnqa.ru/api/user/", getRegistrationData(data));

        String userId = firstUser.getString("id");

        Map<String, String> authData = new HashMap<>();
        authData.put("email", data.get("email"));
        authData.put("password", data.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", "F");

        JsonPath responseEditUser = apiCoreRequests.
                makePutRequestWithTokenCookieBodyJsonPath(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"),
                        editData);

        Assert.assertEquals("Too short value for field firstName", responseEditUser.getString("error"));

        JsonPath responseUserData = apiCoreRequests
                .makeGetRequestWithTokenCookieJSON(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        Assert.assertEquals(data.get("firstName"), responseUserData.getString("firstName"));
    }
}
