import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.collection.IsMapContaining.hasKey;

public class HelloWorldTest {

    @Test
    @Disabled
    public void testGetStatusCode(){
        Response response;
        String code = "301";
        String url = "https://playground.learnqa.ru/api/long_redirect";

        while(!code.equals("200")){
            response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .get(url)
                    .andReturn();
            code = String.valueOf(response.getStatusCode());
            url = response.getHeader("Location");
        }
    }

    @Test
    public void extractToken(){
        JsonPath response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        String token = response.getJsonObject("token").toString();

        JsonPath response2 = RestAssured
                .given()
                .param("token",token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        Assertions.assertEquals(response2.getJsonObject(
                "status").toString(), "Job is NOT ready");

        try {Thread.sleep(Long.parseLong(
                String.format("%d000", (int) response.getJsonObject("seconds"))));
        } catch (InterruptedException e) {throw new RuntimeException(e);}

        JsonPath response3 = RestAssured
                .given()
                .param("token",token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .then()
                .body("$", hasKey("result"))
                .and().extract().jsonPath();
        Assertions.assertEquals(response3.getJsonObject(
                "status").toString(), "Job is ready");
    }
}
