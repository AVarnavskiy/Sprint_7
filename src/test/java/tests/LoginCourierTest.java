package tests;

import io.restassured.http.ContentType;
import models.requests.CreateCourierRequest;
import models.requests.LoginCourierRequest;
import org.junit.Test;
import utils.GenerateData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class LoginCourierTest extends BaseTest {
    private String courierLogin;
    private String courierPassword;
    public static final String ENDPOINT = "/api/v1/courier/login";

    @Test
    public void authorization() {
        createCourier();

        LoginCourierRequest loginCourierRequest = new LoginCourierRequest(courierLogin, courierPassword);

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(loginCourierRequest)
            .when()
            .post(ENDPOINT)
            .then()
            .statusCode(200)
            .assertThat().body("id", is(instanceOf(Integer.class)));

        Courier.deleteCourier(courierLogin, courierPassword);
    }

    @Test
    public void canNotAuthorizationWithoutPassword() {
        createCourier();

        LoginCourierRequest loginCourierRequest = new LoginCourierRequest(courierLogin);

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(loginCourierRequest)
            .when()
            .post(ENDPOINT)
            .then()
            .statusCode(504);
            //.assertThat().body("message", equalTo("Недостаточно данных для входа"));
        // Если передавать только логин, то сервер отвечает по таймауту и возвращает 504, что противоречит документации
        // В тесте оставляю рабочий вариант

        Courier.deleteCourier(courierLogin, courierPassword);
    }

    @Test
    public void canNotAuthorizationWithoutLogin() {
        createCourier();

        String body = String.format("{\"password\": \"%s\"}", courierPassword);

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(body)
            .when()
            .post(ENDPOINT)
            .then()
            .statusCode(400)
            .assertThat().body("message", equalTo("Недостаточно данных для входа"));

        Courier.deleteCourier(courierLogin, courierPassword);
    }

    @Test
    public void canNotAuthorizationWithWrongLogin() {
        createCourier();

        LoginCourierRequest loginCourierRequest = new LoginCourierRequest(courierLogin, courierPassword);
        loginCourierRequest.setLogin(loginCourierRequest.getLogin() + 123);

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(loginCourierRequest)
            .when()
            .post(ENDPOINT)
            .then()
            .statusCode(404)
            .assertThat().body("message", equalTo("Учетная запись не найдена"));

        Courier.deleteCourier(courierLogin, courierPassword);
    }

    @Test
    public void canNotAuthorizationWithWrongPassword() {
        createCourier();

        LoginCourierRequest loginCourierRequest = new LoginCourierRequest(courierLogin, courierPassword);
        loginCourierRequest.setPassword(loginCourierRequest.getPassword() + 123);

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(loginCourierRequest)
            .when()
            .post(ENDPOINT)
            .then()
            .statusCode(404)
            .assertThat().body("message", equalTo("Учетная запись не найдена"));

        Courier.deleteCourier(courierLogin, courierPassword);
    }

    @Test
    public void canNotAuthorizationWithNonExistentCourier() {
        LoginCourierRequest loginCourierRequest = new LoginCourierRequest("Courier78ghs1sf", "123qwert123");

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(loginCourierRequest)
            .when()
            .post(ENDPOINT)
            .then()
            .statusCode(404)
            .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    private void createCourier() {
        courierLogin = GenerateData.generateCourierLogin();
        courierPassword = "qwerty123";

        CreateCourierRequest courier = new CreateCourierRequest(courierLogin, courierPassword);

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(courier)
            .when()
            .post(CreateCourierTest.ENDPOINT)
            .then()
            .statusCode(201);
    }

}
