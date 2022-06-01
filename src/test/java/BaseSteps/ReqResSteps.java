package BaseSteps;

import io.cucumber.java.ru.Дано;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static Utils.Configuration.getConfigurationValue;
import static io.restassured.RestAssured.given;

public class ReqResSteps {
    @Дано("^json файл, производим post запрос с редактированием полей и проверкой тела")
    public void sendBody() throws IOException {
        JSONObject body = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/json/my.json"))));
        body.put("name", getConfigurationValue("newName"));
        body.put("job", getConfigurationValue("newJob"));
        Response postJson = given()
                .header("Content-Type", "application/json")
                .header("Charset", "UTF-8")
                .contentType(ContentType.JSON)
                .baseUri(getConfigurationValue("baseUriReqresApi"))
                .body(body.toString())
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract()
                .response();
        Assertions.assertEquals((new JSONObject(postJson.getBody().asString()).get("name")), (body.get("name")), "Поле name ошибочно!");
        Assertions.assertEquals((new JSONObject(postJson.getBody().asString()).get("job")), (body.get("job")), "Поле job ошибочно!");
    }
}
