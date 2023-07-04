package tests;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.requests.CreateCourierRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.GenerateData;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierTest extends BaseTest {

    private String courierLogin;
    private String courierPassword;
    private String courierName;

    public static final String ENDPOINT = "/api/v1/courier";

    @Before
    public void prepareTestData() {
        courierLogin = faker.name().firstName() + faker.number().digits(3);
        courierPassword = faker.number().digits(8);
        courierName = faker.name().firstName();
    }

    @Test
    @DisplayName("Создание курьера с передачей всех параметров в тело запроса")
    public void createCourierWithAllParameters() {
        CreateCourierRequest courier = new CreateCourierRequest(courierLogin, courierPassword, courierName);
        sendRequestCreateCourier(courier);
        response.then()
                .statusCode(201)
                .assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание курьера с передачей только обязательных параметров в тело запроса")
    public void createCourierWithRequiredParameters() {
        CreateCourierRequest courier = new CreateCourierRequest(courierLogin, courierPassword);
        sendRequestCreateCourier(courier);
        response.then()
                .statusCode(201)
                .assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Нельзя создать двух курьеров с одинаковыми данными - логин, пароль и имя")
    public void canNotCreateTwoSameCouriers() {
        CreateCourierRequest courier = new CreateCourierRequest(courierLogin, courierPassword, courierName);
        sendRequestCreateCourier(courier);
        response.then().statusCode(201);

        sendRequestCreateCourier(courier);
        response.then()
                .statusCode(409)
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Нельзя создать двух курьеров с одинаковыми логинами")
    public void canNotCreateTwoCouriersWithSameLogins() {
        CreateCourierRequest courier = new CreateCourierRequest(courierLogin, courierPassword, courierName);
        sendRequestCreateCourier(courier);
        response.then().statusCode(201);

        courier.setPassword(faker.dog().name());
        courier.setFirstName(faker.name().firstName());

        sendRequestCreateCourier(courier);
        response.then()
                .statusCode(409)
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Нельзя создать курьера без передачи пароля в тело запроса")
    public void canNotCreateCourierWithoutPassword() {
        CreateCourierRequest courier = new CreateCourierRequest(courierLogin);
        sendRequestCreateCourier(courier);

        response.then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Нельзя создать курьера без передачи логина в тело запроса")
    public void canNotCreateCourierWithoutLogin() {
        CreateCourierRequest courier = new CreateCourierRequest();
        courier.setPassword(courierPassword);
        sendRequestCreateCourier(courier);

        response.then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step("Отправили запрос на создание курьера")
    public void sendRequestCreateCourier(CreateCourierRequest courier) {
        response = given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(courier)
            .when()
            .post(ENDPOINT);
    }

    @After
    public void deleteCourier() {
        Courier.deleteCourier(courierLogin, courierPassword);
    }

}
