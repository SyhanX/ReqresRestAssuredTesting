package api.config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specs {

    private final static String BASE_URL = "https://reqres.in/";
    private final static String HEADER_NAME = "x-api-key";
    private final static String HEADER_VALUE = "reqres-free-v1";

    public final static int STATUS_OK = 200;
    public final static int STATUS_ERR = 400;
    public final static int STATUS_NOT_FOUND = 404;
    public final static int STATUS_CREATED = 201;

    public static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri(BASE_URL)
            .addHeader(HEADER_NAME, HEADER_VALUE)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static ResponseSpecification responseSpec(int code) {
        return new ResponseSpecBuilder()
                .expectStatusCode(code)
                .expectContentType(ContentType.JSON)
                .build();
    }

    public static void setupSpecs(
            RequestSpecification request,
            ResponseSpecification response
    ) {
        RestAssured.requestSpecification = request;
        RestAssured.responseSpecification = response;
    }
}

