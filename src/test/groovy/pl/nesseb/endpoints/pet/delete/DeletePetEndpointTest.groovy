package pl.nesseb.endpoints.pet.delete

import io.restassured.builder.RequestSpecBuilder
import io.restassured.builder.ResponseSpecBuilder
import io.restassured.specification.RequestSpecification
import io.restassured.specification.ResponseSpecification
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

import static io.restassured.RestAssured.baseURI
import static io.restassured.RestAssured.given
import static pl.nesseb.utils.PetUtils.createPet
import static pl.nesseb.utils.PetUtils.removePet

class DeletePetEndpointTest {
    private static RequestSpecification requestSpecification
    private static ResponseSpecification responseSpecification

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
    }

    @Test
    void 'should remove a pet'() {
        createdPetId = createPet()

        given().
                spec(requestSpecification).
                pathParam('petId', createdPetId).
        when().
                delete("pet/{petId}").
        then().
                log().ifValidationFails().
                and().
                statusCode(200).
                and().
                spec(responseSpecification)
    }

    @Test
    void 'should not found a pet'() {
        createdPetId = createPet()
        removePet(createdPetId)

        given().
                spec(requestSpecification).
                pathParam('petId', createdPetId).
        when().
                delete("pet/{petId}").
        then().
                log().ifValidationFails().
                and().
                statusCode(404)
    }
}
