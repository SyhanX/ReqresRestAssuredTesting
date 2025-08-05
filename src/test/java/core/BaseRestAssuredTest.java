package core;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseRestAssuredTest {

    @BeforeAll
    public static void setUp() {
        RestAssured.filters(new AllureRestAssured());
    }
}
