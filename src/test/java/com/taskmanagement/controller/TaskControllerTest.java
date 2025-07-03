package com.taskmanagement.controller;

import com.taskmanagement.domain.enums.Status;
import com.taskmanagement.domain.models.TaskModel;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@Transactional
class TaskControllerTest {

    @BeforeEach
    void setup() {
        RestAssured.basePath = "/tasks";
    }

    @Test
    void testCRUDFlow() {
        int id = given()
                .auth().basic("admin", "admin123")
                .contentType("application/json")
                .body(new TaskModel("Ctrl Test", "Created via test", Status.ToDo))
                .when()
                .post("/")
                .then()
                .statusCode(201)
                .body("title", is("Ctrl Test"))
                .extract().path("id");

        given()
                .auth().basic("admin", "admin123")
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .body("id", hasItem(id));

        given()
                .auth().basic("admin", "admin123")
                .when()
                .delete("/" + id)
                .then()
                .statusCode(204);

        given()
                .auth().basic("admin", "admin123")
                .when()
                .get("/" + id)
                .then()
                .statusCode(404);
    }
}
