package WebHooks;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.BeforeClass;

public class Hooks {
    @BeforeClass
    public static void before(){
        RestAssured.filters(new AllureRestAssured());
    }
}
