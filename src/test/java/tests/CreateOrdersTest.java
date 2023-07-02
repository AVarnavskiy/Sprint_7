package tests;

import io.restassured.http.ContentType;
import models.responses.CreateOrdersResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@RunWith(Parameterized.class)
public class CreateOrdersTest extends BaseTest {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int renTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;

    private int track;

    public static final String ENDPOINT = "/api/v1/orders";

    public CreateOrdersTest(String firstName, String lastName, String address, String metroStation, String phone, int renTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.renTime = renTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][] {
                { "Иванов", "Иван", "Вавилова 12", "Ленинский проспект", "+79874231234", 5, "2023-07-05", "Везите с кайфом", new String[] { "BLACK" } },
                { "Иванов", "Иван", "Вавилова 12", "Ленинский проспект", "+79874231234", 5, "2023-07-05", "Везите с кайфом", new String[] { "GRAY" } },
                { "Иванов", "Иван", "Вавилова 12", "Ленинский проспект", "+79874231234", 5, "2023-07-05", "Везите с кайфом", new String[] { "BLACK", "GRAY" } },
                { "Иванов", "Иван", "Вавилова 12", "Ленинский проспект", "+79874231234", 5, "2023-07-05", "Везите с кайфом", new String[] { } },
                { "Иванов", "Иван", "Вавилова 12", "Ленинский проспект", "+79874231234", 5, "2023-07-05", "Везите с кайфом", null }
        };
    }

    @Test
    public void createOrders() {
        CreateOrdersTest order = new CreateOrdersTest(firstName, lastName, address, metroStation, phone, renTime, deliveryDate, comment, color);

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
