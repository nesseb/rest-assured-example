package pl.nesseb.utils

import pl.nesseb.model.Category
import pl.nesseb.model.Pet
import pl.nesseb.model.Tag

import static io.restassured.RestAssured.given
import static io.restassured.RestAssured.delete

class PetUtils {

    static int createPet() {
        Pet pet = given().contentType('application/json').body(pet()).
                when().post('pet').then().extract().body().as(Pet.class)
        return pet.id
    }

    static void removePet(int petId) {
        delete("pet/$petId")
    }

    static Pet pet() {
        int randomId = new Random().nextInt(999)
        return new Pet(
                id: randomId,
                category: new Category(id: 1, name: 'category'),
                name: 'Test name',
                photoUrls: ['some url'],
                tags: [new Tag(id: 1, name: 'tag1')],
                status: 'available')
    }
}
