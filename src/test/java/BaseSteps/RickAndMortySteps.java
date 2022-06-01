package BaseSteps;

import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import static Utils.Configuration.getConfigurationValue;
import static io.restassured.RestAssured.given;

public class RickAndMortySteps {
    public String charId;
    public String mortyLoc;
    public String mortyRace;
    public String lastCharRace;
    public String lastCharLoc;
    public int lastEpisode;
    public int lastChar;

    @Когда("^узнаем информацию о персонаже c id")
    public void gettingCharacter() {
        Response gettingCharacter = given()
                .header("Content-Type", "application/json")
                .header("Charset", "UTF-8")
                .contentType(ContentType.JSON)
                .baseUri(getConfigurationValue("baseUriMortyApi"))
                .when()
                .get("/character/" + getConfigurationValue("id"))
                .then()
                .statusCode(200)
                .extract()
                .response();
        charId = new JSONObject(gettingCharacter.getBody().asString()).get("id").toString();
        mortyLoc = new JSONObject(gettingCharacter.getBody().asString()).getJSONObject("location").get("name").toString();
        mortyRace = new JSONObject(gettingCharacter.getBody().asString()).get("species").toString();
    }

    @И("^получаем номер последнего эпизода с участием этого персонажа")
    public void gettingLastEpisode() {
        Response gettingLastEpisode = given()
                .header("Content-Type", "application/json")
                .header("Charset", "UTF-8")
                .contentType(ContentType.JSON)
                .baseUri(getConfigurationValue("baseUriMortyApi"))
                .when()
                .get("/character/" + charId)
                .then()
                .statusCode(200)
                .extract()
                .response();
        int lastEpisodeIndex = (new JSONObject(gettingLastEpisode.getBody().asString()).getJSONArray("episode").length() - 1);
        lastEpisode = Integer.parseInt(new JSONObject(gettingLastEpisode.getBody().asString()).getJSONArray("episode")
                .get(lastEpisodeIndex).toString().replaceAll("\\D", ""));
    }

    @И("^получаем последнего персонажа в этом эпизоде")
    public void gettingLastCharacter() {
        Response gettingLastChar = given()
                .header("Content-Type", "application/json")
                .header("Charset", "UTF-8")
                .contentType(ContentType.JSON)
                .baseUri(getConfigurationValue("baseUriMortyApi"))
                .when()
                .get("/episode/" + lastEpisode)
                .then()
                .statusCode(200)
                .extract()
                .response();
        int lastCharIndex = (new JSONObject(gettingLastChar.getBody().asString()).getJSONArray("characters").length() - 1);
        lastChar = Integer.parseInt(new JSONObject(gettingLastChar.getBody().asString()).getJSONArray("characters")
                .get(lastCharIndex).toString().replaceAll("\\D", ""));
    }

    @И("^узнаем информацию о последнем персонаже")
    public void gettingLastCharInfo() {
        Response gettingLastCharInfo = given()
                .header("Content-Type", "application/json")
                .header("Charset", "UTF-8")
                .contentType(ContentType.JSON)
                .baseUri(getConfigurationValue("baseUriMortyApi"))
                .when()
                .get("/character/" + lastChar)
                .then()
                .statusCode(200)
                .extract()
                .response();
        lastCharRace = new JSONObject(gettingLastCharInfo.getBody().asString()).get("species").toString();
        lastCharLoc = new JSONObject(gettingLastCharInfo.getBody().asString()).getJSONObject("location").get("name").toString();
    }

    @Тогда("^сравниваем расу обоих персонажей")
    public void raceAssert() {
        Assertions.assertEquals(mortyRace, lastCharRace, "Персонажи не одной расы!");
    }

    @И("^сравниваем местонахождение обоих персонажей")
    public void locAssert() {
        Assertions.assertEquals(mortyLoc, lastCharLoc, "Персонажи находятся в разных местах!");
    }
}
