package oldExercises;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DataProviderRunner.class)
public class CookieTest {

    @DataProvider
    public static Object[][] bunchOfData() {
        return new Object[][] {
                { "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
                        "Mobile", "No", "Android" },
                { "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1",
                        "Mobile", "Chrome", "iOS" },
                { "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
                        "Googlebot", "Unknown", "Unknown" },
                { "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0",
                        "Web", "Chrome", "No" },
                { "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1",
                        "Mobile", "No", "iPhone" }
        };
    }

    @Test
    @UseDataProvider("bunchOfData")
    public void checkUserAgent(String agentName, String platform, String browser, String device){
        JsonPath response = RestAssured
                .given().header("User-Agent", agentName)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .jsonPath();

        Assertions.assertEquals(response.get("platform"), platform, "platform " + agentName);
        Assertions.assertEquals(response.get("browser"), browser, "browser " + agentName);
        Assertions.assertEquals(response.get("device"), device, "device " + agentName);
    }

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
