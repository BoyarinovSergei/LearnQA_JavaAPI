package oldExercises;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

public class GuessPass {

    @Test
    public void guessPass(){
        var listOfPass = parseTheThirdTable();
        var authorizationStatus = "You are NOT authorized";
        var counter = 0;

        while(authorizationStatus.equals("You are NOT authorized")){
            Response response1 = RestAssured
                    .given()
                    .param("login","super_admin")
                    .formParam("password", listOfPass.get(counter))
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();

            Response response2 = RestAssured
                    .given()
                    .cookies("auth_cookie", response1.getCookie("auth_cookie"))
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();

            if(response2.asString().equals("You are authorized"))
                System.out.println("Dude, your forgotten password is: "
                        + listOfPass.get(counter) +"\n" + response2.asString());

            authorizationStatus = response2.asString();
            counter++;
        }
    }

    public ArrayList<String> parseTheThirdTable(){
        Document document;
        var lst  = new ArrayList<String>();

        try {document = Jsoup.connect(
                    "https://en.wikipedia.org/wiki/List_of_the_most_common_passwords").get();}
        catch (IOException e) {throw new RuntimeException(e);}

        for (int i = 2; i <= 26; i++){
            for(int y = 2; y <= 10; y++){
                lst.add(Objects.requireNonNull(document.selectXpath(
                                "//div[@id='mw-content-text']//table[3]//tr[" + i + "]/td[" + y + "]")
                        .first()).text());
            }
        }
        return lst;
    }
}
