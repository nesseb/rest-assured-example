package pl.nesseb.endpoints.pet.get

import io.restassured.builder.RequestSpecBuilder
import io.restassured.builder.ResponseSpecBuilder
import io.restassured.specification.RequestSpecification
import io.restassured.specification.ResponseSpecification
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

import static io.restassured.RestAssured.baseURI
import static io.restassured.RestAssured.given
import static io.restassured.RestAssured.when
import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.Matchers.lessThan
import static org.hamcrest.Matchers.greaterThan
import static pl.nesseb.utils.PetUtils.*

class GetPetEndpointsTest {
    private static RequestSpecification requestSpecification
    private static ResponseSpecification responseSpecification

    private List<Map<String, Object>> petsList
    private static int createdPetId

    @BeforeAll
    static void setup() {
        baseURI = 'https://petstore.swagger.io/v2/'
        requestSpecification =
                new RequestSpecBuilder().
                        addHeader('Accept', 'application/json').
                        build()

        responseSpecification =
                new ResponseSpecBuilder().
                        expectHeader('Content-Type', 'application/json').
                        build()
        createdPetId = createPet()
    }

    @AfterAll
    static void cleanUp() {
        removePet(createdPetId)
    }

    @Test
    void 'should find all pets with status available'() {
        petsList =
                given().
                        spec(requestSpecification).
                        queryParam('status', 'available').
                when().
                        get('pet/findByStatus').
                then().
                        log().ifValidationFails().
                        and().
                        statusCode(200).
                        extract().body().as(List.class)

        assertPetsStatus('available')
    }

    @Test
    void 'should find all pets with status sold'() {
        petsList =
                given().
                        spec(requestSpecification).
                        queryParam('status', 'sold').
                when().
                        get('pet/findByStatus').
                then().
                        log().ifValidationFails().
                        and().
                        statusCode(200).
                        extract().body().as(List.class)

        assertPetsStatus('sold')
    }

    @Test
    void 'should return a pet'() {
        given().
                spec(requestSpecification).
                pathParam('petId', createdPetId).
        when().
                get("pet/{petId}").
        then().
                log().ifValidationFails().
                and().
                statusCode(200).
                and().
                spec(responseSpecification)
    }

    @Test
    void 'should not found a pet'() {
        given().
                spec(requestSpecification).
                pathParam('petId', 0).
        when().
                get("pet/{petId}").
        then().
                log().ifStatusCodeMatches(lessThan(404)).
                and().
                log().ifStatusCodeMatches(greaterThan(404)).
                statusCode(404).
                body('message', equalTo('Pet not found')).
                spec(responseSpecification)
    }

    @Test
    void 'should not allowed declare empty parameter'() {
        when().
                get('pet').
        then().
                log().ifStatusCodeMatches(lessThan(405)).
                and().
                log().ifStatusCodeMatches(greaterThan(405)).
                statusCode(405)
    }

    private void assertPetsStatus(String expectedStatus) {
        !petsList.isEmpty()
        petsList.each {
            assert it['status'] == expectedStatus
        }
    }
}
