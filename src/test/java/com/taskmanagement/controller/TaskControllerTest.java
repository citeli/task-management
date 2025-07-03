package com.taskmanagement.controller;

import com.taskmanagement.domain.enums.Status;
import com.taskmanagement.dto.TaskRequestDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class TaskControllerTest {

    private static final String BASE_PATH = "/tasks";
    private static final String AUTH = "admin";
    private static final String PASS = "admin123";

    @Test
    void testCreateAndGetById() {
        //creating a new task
        int id = given()
                .auth().basic(AUTH, PASS)
                .contentType(ContentType.JSON)
                .body(new TaskRequestDTO("test title", "desc test", Status.ToDo))
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(201)
                .body("title", is("test title"))
                .extract().path("id");

        //finding by id
        given()
                .auth().basic(AUTH, PASS)
                .when()
                .get(BASE_PATH + "/" + id)
                .then()
                .statusCode(200)
                .body("id", is(id));
    }

    @Test
    void testUpdateTask() {
        int id = given()
                .auth().basic(AUTH, PASS)
                .contentType(ContentType.JSON)
                .body(new TaskRequestDTO("Original", "To be updated", Status.ToDo))
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(201)
                .extract().path("id");

        String updatedJson = """
            {
              "id": %d,
              "title": "Updated",
              "description": "Desc updated",
              "status": "Completed"
            }
            """.formatted(id);

        given()
                .auth().basic(AUTH, PASS)
                .contentType(ContentType.JSON)
                .body(updatedJson)
                .when()
                .put(BASE_PATH + "/" + id)
                .then()
                .statusCode(200)
                .body("title", is("Updated"))
                .body("status", is("Completed"));
    }

    @Test
    void testListAllAndCompleted() {
        given()
                .auth().basic(AUTH, PASS)
                .contentType(ContentType.JSON)
                .body(new TaskRequestDTO("Completed task", "Description", Status.Completed))
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(201);

        given()
                .auth().basic(AUTH, PASS)
                .when()
                .get(BASE_PATH)
                .then()
                .statusCode(200)
                .body("$", not(empty()));

        given()
                .auth().basic(AUTH, PASS)
                .when()
                .get(BASE_PATH + "/completed")
                .then()
                .statusCode(200)
                .body("status", everyItem(equalTo("Completed")));
    }

    @Test
    void testDeleteTask() {
        int id = given()
                .auth().basic(AUTH, PASS)
                .contentType(ContentType.JSON)
                .body(new TaskRequestDTO("deleted", "deleted task", Status.ToDo))
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(201)
                .extract().path("id");

        given()
                .auth().basic(AUTH, PASS)
                .when()
                .delete(BASE_PATH + "/" + id)
                .then()
                .statusCode(204);

        given()
                .auth().basic(AUTH, PASS)
                .when()
                .get(BASE_PATH + "/" + id)
                .then()
                .statusCode(404);
    }
}