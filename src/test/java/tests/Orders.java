package tests;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.requests.CreateOrdersRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class Orders {

    static Faker faker = new Faker();

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

    public static Response createOrder(String[] color) {
        CreateOrdersRequest order = new CreateOrdersRequest(faker.name().firstName(),
                faker.name().lastName(),
                faker.address().fullAddress(),
                faker.funnyName().name(),
                faker.phoneNumber().phoneNumber(),
                new Random().nextInt(10),
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
                faker.funnyName().name(),
                color);

        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post("/api/v1/orders");
    }
}
