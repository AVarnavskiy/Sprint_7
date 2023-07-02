package tests;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class Orders {

    public static void cancelOrder(int trackId) {
        given()
            .log().all()
            .contentType(ContentType.JSON)
            .queryParam("track", String.valueOf(trackId))
            .when()
            .put("/api/v1/orders/cancel")
            .then()
            .statusCode(200);
    }
}
