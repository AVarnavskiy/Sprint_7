package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;

public class BaseTest {
    Response response;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
}
