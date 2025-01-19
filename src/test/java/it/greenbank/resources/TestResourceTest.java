package it.greenbank.resources;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class TestResourceTest {
    
    @Test
    void testTestEndpoint() {
        given()
          .when().get("/test")
          .then()
             .statusCode(200)
             .body(is("TEST-OK"));
    }

}