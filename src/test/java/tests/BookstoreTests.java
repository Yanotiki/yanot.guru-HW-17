package tests;

import helpers.CustomAllureListener;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class BookstoreTests {
    @BeforeAll
    @Tag("booktest")
    static void setup() {
        RestAssured.baseURI = "https://demoqa.com";
    }

    @Test
    @DisplayName("Проверка книги по номеру ISBN")
    @Story("API тесты для demoqa.com/bookstore")
    @Tag("booktest")
    void getBooksTest() {
        given()
                .filter(withCustomTemplates())
                .params("ISBN", "9781449325862")
                .log().uri()
                .log().body()
                .when()
                .get("/BookStore/v1/Book")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("subTitle", is("A Working Introduction"))
                .body("author", is("Richard E. Silverman"))
                .body("pages", is(234));
    }

    @Test
    @DisplayName("Проверка всего списка книг")
    @Story("API тесты для demoqa.com/bookstore")
    @Tag("booktest")
    void getAllBooksTest() {
        given()
                .filter(withCustomTemplates())
                .when()
                .log().all()
                .get("/BookStore/v1/Books")
                .then()
                .log().body()
                .statusCode(200)
                .body("books", hasSize(greaterThan(0)))
                .body("books.title[0]", is("Git Pocket Guide"))
                .body("books.author[1]", is("Addy Osmani"));
    }

    @Test
    void getBooksWithAllLogsTest() {
        given()
                .log().uri()
                .log().body()
                .when()
                .get("/BookStore/v1/Books")
                .then()
                .log().status()
                .log().body()
                .body("books", hasSize(greaterThan(0)));
    }

    @Test
    @DisplayName("Проверка генерации токена пользователя")
    @Story("API тесты для demoqa.com/bookstore")
    @Tag("booktest")
    void generateTokenTest() {
        String data = "{ \"userName\": \"alex\", " +
                "\"password\": \"asdsad#frew_DFS2\" }";
        given()
                .contentType(JSON)
                .body(data)
                .log().uri()
                .log().body()
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."))
                .body("token.size()", greaterThan(10));
    }

    @Test
    @DisplayName("Проверка генерации токена пользователя с помощью AllureListener")
    @Story("API тесты для demoqa.com/bookstore")
    @Tag("booktest")
    void generateTokenTestWithAllureListener() {
        String data = "{ \"userName\": \"alex\", " +
                "\"password\": \"asdsad#frew_DFS2\" }";
        given()
                .filter(new AllureRestAssured())
                .contentType(JSON)
                .body(data)
                .log().uri()
                .log().body()
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."))
                .body("token.size()", greaterThan(10));
    }

    @Test
    @DisplayName("Проверка генерации токена пользователя с помощью CustomAllureListener")
    @Story("API тесты для demoqa.com/bookstore")
    @Tag("bookstoretest")
    void generateTokenTestWithCustomAllureListener() {
        String data = "{ \"userName\": \"alex\", " +
                "\"password\": \"asdsad#frew_DFS2\" }";
        given()
                .filter(withCustomTemplates())
                .contentType(JSON)
                .body(data)
                .log().uri()
                .log().body()
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/generateToken_response_shema.json"))
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."))
                .body("token.size()", greaterThan(10));
    }
}

