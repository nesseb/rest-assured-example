package pl.nesseb.endpoints.pet.post

import io.restassured.builder.RequestSpecBuilder
import io.restassured.builder.ResponseSpecBuilder
import io.restassured.specification.RequestSpecification
import io.restassured.specification.ResponseSpecification
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import pl.nesseb.model.Pet

import static pl.nesseb.utils.PetUtils.pet
import static pl.nesseb.utils.PetUtils.removePet
import static io.restassured.RestAssured.baseURI
import static io.restassured.RestAssured.given

class PostPetEndpointsTest {
    private static RequestSpecification requestSpecification
    private static ResponseSpecification responseSpecification

    private static Pet pet

    @BeforeAll
    static void setup() {
        baseURI = 'https://petstore.swagger.io/v2/'
        requestSpecification =
                new RequestSpecBuilder().
                        addHeader('Accept', 'application/json').
                        addHeader('Content-Type', 'application/json').
                        build()

        responseSpecification =
                new ResponseSpecBuilder().
                        expectHeader('Content-Type', 'application/json').
                        build()
    }

    @AfterEach
    void cleanUp() {
        removePet(pet.id)
    }

    @Test
    void 'should create new pet'() {
        pet =  pet()

        given().
                spec(requestSpecification).
                body(pet).
        when().
                post('pet').
        then().
                log().ifValidationFails().
                and().
                statusCode(200).
                and().
                spec(responseSpecification)
    }
}
