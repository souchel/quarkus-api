package com.orness;

import com.orness.web.responses.ErrorResponse;
import com.orness.web.responses.HeroResponse;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class HeroResourceTest {

    @Test
    public void testGetHeroEndpoint() {
        Response response = given()
          .when().get("/heroes/{mail}", "steve.rogers@marvel.com")
          .then()
             .statusCode(200)
             .contentType(ContentType.JSON)
             .extract().response();
        HeroResponse body = response.jsonPath().getObject("$", HeroResponse.class);
        Assertions.assertEquals(new HeroResponse("Steve", "Rogers", 35, "steve.rogers@marvel.com"), body);
    }

    @Test
    public void testGetHeroEndpointNotFound() {
        Response response = given()
          .when().get("/heroes/{mail}", "wrong")
          .then()
             .statusCode(404)
             .contentType(ContentType.JSON)
             .extract().response();
        ErrorResponse body = response.jsonPath().getObject("$", ErrorResponse.class);
        Assertions.assertEquals("Not Found", body.title());
    }


    @Test
    @TestTransaction
    public void testCreateHero() {

        given()
                .body("""
                        {
                            "firstname": "Tony",
                            "lastname": "Straks",
                            "mail": "tony.starks@marvel.com",
                            "age": 35
                        }
                        """)
                .contentType(ContentType.JSON)
                .when().post("/heroes")
                .then()
                .statusCode(204);
    }

}