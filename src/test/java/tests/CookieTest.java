package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CookieTest {

    @Test
    public void checkCookie(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        Assertions.assertEquals(response.getCookie("HomeWork"), "hw_value");
    }

    @Test
    public void checkHeader(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
        Assertions.assertEquals(response.getHeader("x-secret-homework-header"), "Some secret value");
    }
}
