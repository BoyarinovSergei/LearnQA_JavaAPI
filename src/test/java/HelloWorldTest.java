import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class HelloWorldTest {

    @Test
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
}
