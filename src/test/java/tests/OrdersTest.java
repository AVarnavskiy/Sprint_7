package tests;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.responses.CreateOrdersResponse;
import models.responses.OrdersResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class OrdersTest extends BaseTest {

    private Response responseCreateOrder;
    private int track;
    public static final String ENDPOINT = "/api/v1/orders";

    @Before
    public void createOrder() {
        responseCreateOrder = Orders.createOrder(new String[] { "BLACK" });
    }

    @Test
    @DisplayName("Получили список заказов в виде массива")
    public void getOrdersList() {
        sendRequestGetOrders();
        response.then().statusCode(200);

        Object[] ordersArray = response.as(OrdersResponse.class).getOrders();
        // Проверям, что в списке заказов есть минимум 1 заказ
        Assert.assertTrue(ordersArray.length >= 1);
    }

    @Step("Отправили запрос для получения списка заказов")
    public void sendRequestGetOrders() {
        response = given()
                .log().all()
                .when()
                .get(ENDPOINT);
    }

    @After
    public void cancelOrder() {
        track = responseCreateOrder.as(CreateOrdersResponse.class).getTrack();
        Orders.cancelOrder(track);
    }
}
