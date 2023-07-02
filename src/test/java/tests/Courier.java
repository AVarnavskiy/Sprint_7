package tests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.requests.LoginCourierRequest;
import models.responses.LoginCourierResponse;

import static io.restassured.RestAssured.given;

// Вынес в отдельный класс методы, которые используются в нескольких классов для тестов, чтобы не дублировать код
public class Courier {

    public static void deleteCourier(String courierLogin, String courierPassword) {
        int id = getCourierId(courierLogin, courierPassword);
        given()
        .log().all()
        .when()
        .delete("/api/v1/courier/" + id)
        .then()
        .statusCode(200);
    }

    private static int getCourierId(String courierLogin, String courierPassword) {
        LoginCourierRequest courier = new LoginCourierRequest(courierLogin, courierPassword);
        Response response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(LoginCourierTest.ENDPOINT);

        return response.as(LoginCourierResponse.class).getId();
    }
}
