package api.tests;

import api.models.*;
import core.BaseRestAssuredTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static api.config.Specs.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ReqresTest extends BaseRestAssuredTest {

    @Test
    @DisplayName("Make sure that received data is not empty and is correct")
    public void getExistingUser() {

        setupSpecs(
                requestSpec,
                responseSpec(STATUS_OK)
        );

        int userId = 2;

        given()
                .when()
                .get("api/users/" + userId)
                .then()
                .body("data", not(empty()))
                .body("data.email", endsWith("@reqres.in"))
                .body("data.id", equalTo(userId));
    }

    @DisplayName("Should return status code 404 not found")
    @Test
    public void getNonExistingUser() {
        setupSpecs(
                requestSpec,
                responseSpec(STATUS_NOT_FOUND)
        );

        int userId = 23;

        int actualStatusCode = given()
                .when()
                .get("api/users/" + userId)
                .then()
                .extract().statusCode();

        Assertions.assertEquals(STATUS_NOT_FOUND, actualStatusCode);
    }

    @Test
    @DisplayName("Should return status code 201 after user creation")
    public void createUser() {
        setupSpecs(
                requestSpec,
                responseSpec(STATUS_CREATED)
        );

        String userName = "morpheus";
        String userJob = "leader";
        UserCreationData newUser = new UserCreationData(userName, userJob);

        Response response = given()
                .body(newUser)
                .when()
                .post("api/users")
                .then()
                .extract().response();

        int actualStatusCode = response.statusCode();
        CreatedUser createdUser = response.as(CreatedUser.class);

        Assertions.assertEquals(STATUS_CREATED, actualStatusCode);
        Assertions.assertEquals(userName, createdUser.name());
        Assertions.assertEquals(userJob, createdUser.job());
    }

    @Test
    @DisplayName("Should return status code 200 with user id and token")
    public void registerUserWithSuccess() {
        setupSpecs(requestSpec, responseSpec(STATUS_OK));

        String email = "eve.holt@reqres.in";
        String password = "pistol";
        RegistrationData newUser = new RegistrationData(email, password);

        int expectedId = 4;
        String expectedToken = "QpwL5tke4Pnpja7X4";

        Response response = given()
                .body(newUser)
                .when()
                .post("api/register")
                .then()
                .extract().response();

        int actualStatusCode = response.statusCode();
        SuccessfulRegistration user = response.as(SuccessfulRegistration.class);

        Assertions.assertEquals(STATUS_OK, actualStatusCode);
        Assertions.assertEquals(expectedId, user.id());
        Assertions.assertEquals(expectedToken, user.token());
    }

    @Test
    @DisplayName("Should return status code 400 with error message")
    public void registerUserWithFailure() {
        setupSpecs(requestSpec, responseSpec(STATUS_ERR));

        String email = "eve.holt@reqres.in";
        RegistrationData newUser = new RegistrationData(email, null);

        String expectedErrorMessage = "Missing password";

        Response response = given()
                .body(newUser)
                .when()
                .post("api/register")
                .then()
                .extract().response();

        int actualStatusCode = response.statusCode();
        UnsuccessfulRegistration unsuccessfulReg = response.as(UnsuccessfulRegistration.class);

        Assertions.assertEquals(STATUS_ERR, actualStatusCode);
        Assertions.assertEquals(expectedErrorMessage, unsuccessfulReg.error());
    }
}
