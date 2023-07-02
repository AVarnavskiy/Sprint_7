package tests;

import io.restassured.http.ContentType;
import models.responses.CreateOrdersResponse;
import models.responses.OrdersResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

public class OrdersTest extends BaseTest {

    private int track;
    public static final String ENDPOINT = "/api/v1/orders";

    @Test
    public void getOrdersList() {
        createOrder(); // Создаем заказ на случай отсутствия заказов в БД

        response = given()
            .log().all()
            .when()
            .get(ENDPOINT);

        response.then().statusCode(200);
        Object[] ordersArray = response.as(OrdersResponse.class).getOrders();
        // Проверям, что в списке заказов есть минимум 1 заказ
        Assert.assertTrue(ordersArray.length >= 1);
    }

    private void createOrder() {
        CreateOrdersTest order = new CreateOrdersTest("Иванов", "Иван", "Вавилова 12", "Ленинский проспект", "+79874231234", 5, "2023-07-05", "Везите с кайфом", new String[] { "BLACK" });

        response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(ENDPOINT);

        response.then()
                .statusCode(201)
                .assertThat().body("track", is(instanceOf(Integer.class)));

        track = response.as(CreateOrdersResponse.class).getTrack();
    }

    @After
    public void cancelOrder() {
        Orders.cancelOrder(track);
    }
}
