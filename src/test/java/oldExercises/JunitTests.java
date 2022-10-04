package oldExercises;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JunitTests {

    @ParameterizedTest
    @ValueSource(strings = {"123", "234", "345"})
    public void testRest(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/map")
                .andReturn();
        System.out.println(response.getStatusCode());
        assertEquals(200, response.getStatusCode(), "Actual status is " + response.getStatusCode());
    }
}
