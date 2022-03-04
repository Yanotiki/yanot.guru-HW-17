package tests;

import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.is;

public class ReqresTests {

    @Test
    void successfulCreate() {
        String data = "{ \"name\": \"morpheus\", " +
                "\"job\": \"leader\" }";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"));
    }

    @Test
    void successfulUpdate() {
        String data = "{ \"name\": \"morpheus\", " +
                "\"job\": \"zion resident\" }";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("name", is("morpheus"))
                .body("job", is("zion resident"));
    }


    @Test
    void singleResource() {
        given()
                .get("https://reqres.in/api/unknown/23")
                .then()
                .statusCode(404);
    }

    @Test
    void successfulSingleUser() {
        given()
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("data.id", is(2))
                .body("data.email", is("janet.weaver@reqres.in"))
                .body("support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test
    void registerUnsuccessful() {
        String data = "{ \"email\": \"sydney@fife\"}";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
}
