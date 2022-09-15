import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

public class HelloWorldTest {

    @Test
    public void testGetStatusCode(){
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        System.out.println(response.get("messages[1].message").toString());
    }
}
