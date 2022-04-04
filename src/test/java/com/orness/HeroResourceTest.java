package com.orness;

import com.orness.web.HeroResource;
import com.orness.web.requests.HeroCreationRequest;
import com.orness.web.responses.ErrorResponse;
import com.orness.web.responses.HeroResponse;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(HeroResource.class)
public class HeroResourceTest {

    @Test
    public void testGetHeroEndpoint() {
        Response response = given()
          .when().get("{mail}", "steve.rogers@marvel.com")
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
          .when().get("{mail}", "wrong")
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
                            "lastname": "Starks",
                            "mail": "tony.starks@marvel.com",
                            "age": 35
                        }
                        """)
                .contentType(ContentType.JSON)
                .when().post("")
                .then()
                .statusCode(204);
    }


    @ParameterizedTest
    @CsvSource(quoteCharacter = '"', textBlock = """
            # FIRSTNAME,       LASTNAME,     MAIL,                       AGE
            Tony,                Stark,      wrong,                      35
            Tony,                Starks2,    wrong@marvel.com,           35
            Tony,                Starks,     steve.rogers@marvel.com,    35
            Tony,                Starks,     steve.rogers@marvel.com,    200
            ,                    Starks ,    steve.rogers@marvel.com,    35
            """)
    @TestTransaction
    public void testCreateHeroWrongEmail(String firstname, String lastname, String mail, int age) {

        given()
                .body(getCreateHeroPayload(firstname, lastname, mail, age))
                .contentType(ContentType.JSON)
                .when().post()
                .then()
                .statusCode(400);
    }

    private HeroCreationRequest getCreateHeroPayload(String firstname, String lastname, String mail, int age) {
        return new HeroCreationRequest(firstname, lastname, mail, age);
    }

}