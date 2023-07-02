package tests;

import io.restassured.http.ContentType;
import models.requests.CreateCourierRequest;
import org.junit.Before;
import org.junit.Test;
import utils.GenerateData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierTest extends BaseTest {

    private String courierLogin;
    private String courierPassword;
    private String courierName;

    public static final String ENDPOINT = "/api/v1/courier";

    @Before
    public void prepareTestData() {
        courierLogin = GenerateData.generateCourierLogin();
        courierPassword = "qwerty123";
        courierName = "Курьер для теста";
    }

    @Test
    public void createCourierWithAllParameters() {
        CreateCourierRequest courier = new CreateCourierRequest(courierLogin, courierPassword, courierName);

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(courier)
            .when()
            .post(ENDPOINT)
            .then()
            .statusCode(201)
            .assertThat().body("ok", equalTo(true));

        Courier.deleteCourier(courierLogin, courierPassword);
    }

    @Test
    public void createCourierWithRequiredParameters() {
        CreateCourierRequest courier = new CreateCourierRequest(courierLogin, courierPassword);

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(courier)
            .when()
            .post(ENDPOINT)
            .then()
            .statusCode(201)
            .assertThat().body("ok", equalTo(true));

        Courier.deleteCourier(courierLogin, courierPassword);
    }

    @Test
    public void canNotCreateTwoSameCouriers() {
        CreateCourierRequest courier = new CreateCourierRequest(courierLogin, courierPassword, courierName);

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(courier)
            .when()
            .post(ENDPOINT)
            .then()
            .statusCode(201);

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(courier)
            .when()
            .post(ENDPOINT)
            .then()
            .statusCode(409)
            .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        Courier.deleteCourier(courierLogin, courierPassword);
    }

    @Test
    public void canNotCreateTwoCouriersWithSameLogins() {
        CreateCourierRequest courier = new CreateCourierRequest(courierLogin, courierPassword, courierName);

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(courier)
            .when()
            .post(ENDPOINT)
            .then()
            .statusCode(201);

        courier.setPassword("qwerty123456789");
        courier.setFirstName("Курьер Тестовый 123");

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(courier)
            .when()
            .post(ENDPOINT)
            .then()
            .statusCode(409)
            .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        Courier.deleteCourier(courierLogin, courierPassword);
    }

    @Test
    public void canNotCreateCourierWithoutPassword() {
        CreateCourierRequest courier = new CreateCourierRequest(courierLogin);

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(courier)
            .when()
            .post(ENDPOINT)
            .then()
            .statusCode(400)
            .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void canNotCreateCourierWithoutLogin() {
        String body = "{\"password\": \"qwerty\"}";

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(body)
            .when()
            .post(ENDPOINT)
            .then()
            .statusCode(400)
            .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

}
