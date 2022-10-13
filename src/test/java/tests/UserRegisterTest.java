package tests;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.BaseTestCase;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

@RunWith(DataProviderRunner.class)
public class UserRegisterTest extends BaseTestCase {
    private ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @DataProvider
    public static Object[][] testData() {
        return new Object[][] {
                {null, "string", "string", "qwer@yandex.ru", "string", "username"},
                {"string", null, "string", "qwer@yandex.ru", "string", "firstName"},
                {"string", "string", null, "qwer@yandex.ru", "string", "lastName"},
                {"string", "string", "string", null, "string", "email"},
                {"string", "string", "string", "qwer@yandex.ru", null, "password"},
        };
    }
    private  Map<String, String> map = new HashMap<>(){
        {
            put("username","string"); put("firstName","string"); put("lastName","string");
            put("email","qwer@yandex.ru");put("password","string");
        }
    };

    @Test
    public void testCreateUserWithWrongEmail(){
       map.put("email", "qweryandex.ru");

        Response response = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", map);

        Assertions.assertEquals( "Invalid email format", response.asString());
    }

    @Test
    @UseDataProvider("testData")
    public void testCreateUserWithNoParam(String userName, String firstName,
                                          String lastName, String email, String password, String missedParam){
        Map<String, String> map1 = new HashMap<>();
        map1.put("username", userName);
        map1.put("firstName", firstName);
        map1.put("lastName", lastName);
        map1.put("email", email);
        map1.put("password", password);


        Response response = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", map1);

        Assertions.assertEquals("The following required params are missed: " + missedParam,
                response.asString(), "Значения не совпали");
    }

    @Test
    public void testCreateUserWithShortName(){
        map.put("username","1");

        Response response = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", map);

        Assertions.assertEquals( "The value of 'username' field is too short", response.asString());
    }

    @Test
    public void testCreateUserWithLongName(){
        map.put("username",generateString("qwerqe", 256));

        Response response = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", map);

        Assertions.assertEquals( "The value of 'username' field is too long", response.asString());
    }
}
