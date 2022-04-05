package com.orness;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.orness.data.HeroEntity;
import com.orness.data.HeroRepository;
import com.orness.web.HeroResource;
import com.orness.web.requests.HeroCreationRequest;
import com.orness.web.responses.ErrorResponse;
import com.orness.web.responses.HeroResponse;
import com.orness.web.responses.PageResponse;
import io.agroal.api.AgroalDataSource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.anyRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(value = WireMockAgify.class, restrictToAnnotatedClass = true)
@TestHTTPEndpoint(HeroResource.class)
public class HeroResourceTest {

    @InjectAgifyMock
    WireMockServer wireMockServer;

    @Inject
    HeroRepository heroRepository;

    @Inject
    AgroalDataSource dataSource;

    @BeforeEach
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    // look here https://github.com/quarkusio/quarkus/issues/5523
    void setUp() {
        wireMockServer.resetAll();
        WireMock.configureFor(wireMockServer.port());
        final String truncate = "TRUNCATE TABLE heroEntity";
        try (final Connection con = dataSource.getConnection();
             final Statement stmt = con.createStatement()) {
            stmt.executeUpdate(truncate);
            stmt.executeUpdate(new String(getClass().getClassLoader().getResourceAsStream("import.sql").readAllBytes()));
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

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
    public void testGetHeroesEndpoint() {
        Response response = given()
          .when().get("")
          .then()
             .statusCode(200)
             .contentType(ContentType.JSON)
             .extract().response();
        TypeRef<PageResponse<HeroResponse>> typeRef
                = new TypeRef<>() {};
        PageResponse<HeroResponse> body = response.jsonPath().getObject("$", typeRef);
        Assertions.assertEquals(2, body.count());
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
        verify(exactly(0), anyRequestedFor(anyUrl()));
        Optional<HeroEntity> hero = heroRepository.findByMail("tony.starks@marvel.com");
        Assertions.assertTrue(hero.isPresent());
        Assertions.assertEquals(35, hero.get().getAge());
        Assertions.assertEquals("tony.starks@marvel.com", hero.get().getMail());
    }

    @Test
    public void testCreateHeroWithoutAge() {
        mockAgifyCall("tony", 55);
        given()
                .body("""
                        {
                            "firstname": "Tony",
                            "lastname": "Starks",
                            "mail": "tony.starks@marvel.com"
                        }
                        """)
                .contentType(ContentType.JSON)
                .when().post("")
                .then()
                .statusCode(204);
        Optional<HeroEntity> hero = heroRepository.findByMail("tony.starks@marvel.com");
        Assertions.assertTrue(hero.isPresent());
        Assertions.assertEquals(55, hero.get().getAge());
        Assertions.assertEquals("tony.starks@marvel.com", hero.get().getMail());
    }

    @Test
    public void testCreateHeroWithFailedAgifyCall() {

        stubFor(get(urlPathEqualTo("/")).withQueryParam("name", equalTo("tony")).willReturn(
                aResponse().withStatus(500)
        ));
        Response response = given()
                .body("""
                        {
                            "firstname": "Tony",
                            "lastname": "Starks",
                            "mail": "tony.starks@marvel.com"
                        }
                        """)
                .contentType(ContentType.JSON)
                .when().post("")
                .then()
                .statusCode(500)
                .extract().response();
        ErrorResponse error = response.jsonPath().getObject("$", ErrorResponse.class);

        Assertions.assertEquals("Internal Error", error.title());
        Assertions.assertNotNull(error.stacktraceId());

        Optional<HeroEntity> hero = heroRepository.findByMail("tony.starks@marvel.com");
        Assertions.assertTrue(hero.isEmpty());
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

    private void mockAgifyCall(String firstname, int age) {
        String body = MessageFormat.format("""
                '{'
                    "name": "{0}",
                    "age": {1},
                    "count": 1
                '}'
                """, firstname, Integer.toString(age));
        stubFor(get(urlPathEqualTo("/")).withQueryParam("name", equalTo(firstname)).willReturn(
                okJson(body)
        ));
    }

}