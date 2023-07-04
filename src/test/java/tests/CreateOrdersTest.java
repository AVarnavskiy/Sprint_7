package tests;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import models.requests.CreateOrdersRequest;
import models.responses.CreateOrdersResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@RunWith(Parameterized.class)
public class CreateOrdersTest extends BaseTest {
    private final String[] color;
    private int track;
    public static final String ENDPOINT = "/api/v1/orders";

    public CreateOrdersTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][] {
                { new String[] { "BLACK" } },
                { new String[] { "GRAY" } },
                { new String[] { "BLACK", "GRAY" } },
                { new String[] { } },
                { null }
        };
    }

    @Test
    @DisplayName("Создание заказа с указанием разных данных в параметре color")
    public void createOrders() {
        sendRequestCreateOrder();

        response.then()
            .statusCode(201)
            .assertThat().body("track", is(instanceOf(Integer.class)));
    }

    @Step("Отправили запрос на создание заказа")
    public void sendRequestCreateOrder() {
        response = Orders.createOrder(color);
    }

    @After
    public void cancelOrder() {
        track = response.as(CreateOrdersResponse.class).getTrack();
        Orders.cancelOrder(track);
    }

}
