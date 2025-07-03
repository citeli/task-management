# 🗂️ Task Management API

A lightweight task management REST service built with Quarkus, offering CRUD functionality, basic security, an in-memory H2 database, OpenAPI documentation, and startup data seeding.

## 🔧 System Requirements

- **Java**: AdoptOpenJDK or OpenJDK 21 (or higher)
- **Maven**: Included via Maven Wrapper (`./mvnw`)
- **IDE Used**: IntelliJ IDEA Community

## ⚙️ Setup & Local Run

1. Clone the repository:
    ```bash
    git clone https://github.com/citeli/task-management.git
    cd task-management
    ```

2. Ensure `JAVA_HOME` points to a JDK 21+ installation:
    ```bash
    echo %JAVA_HOME%        # Windows
    echo $JAVA_HOME         # macOS/Linux
    ```

3. Build the project:
    ```bash
    ./mvnw clean install
    ```

4. Run the application in development mode:
    ```bash
    ./mvnw quarkus:dev
    ```
## 🚀 What the Project Does

- Exposes REST endpoints to **create**, **read**, **update**, and **delete** tasks.
- Task fields: `title`, `description`, and `status` (`ToDo`, `InProgress`, `Completed`).
- Uses **Quarkus**, **Hibernate + Panache**, and **H2 in-memory database**.
- A startup **data seeder** populates initial tasks if the database is empty.
- **Basic Auth** protects listing and get completed tasks routes.
- Input is validated via DTO (`@Valid`), and enum values are checked.

## 🔐 Authentication

Protected endpoints require **HTTP Basic Authentication** with the following credentials:
```text
Username: admin
Password: admin123
```
Use this header in your requests: 
```text
Authorization: Basic YWRtaW46YWRtaW4xMjM=
```

(The value above is the Base64 encoding of `admin:admin123`)

## 📦 API Endpoints

| Method | Endpoint             | Description                                | Response                      | Auth Required |
|--------|----------------------|--------------------------------------------|-------------------------------|---------------|
| POST   | `/tasks`             | Create a new task                          | `201 Created` + Task object   | ❌             |
| GET    | `/tasks`             | List all tasks                             | `200 OK` + array of tasks     | ✅             |
| GET    | `/tasks/completed`   | List tasks with status = Completed         | `200 OK` + filtered tasks     | ✅             |
| GET    | `/tasks/{id}`        | Get a task by its ID                       | `200 OK` or `404 Not Found`   | ❌             |
| PUT    | `/tasks/{id}`        | Update a task                              | `200 OK` or `404 Not Found`   | ❌             |
| DELETE | `/tasks/{id}`        | Delete a task by ID                        | `204 No Content` or `404`     | ❌             |

## 📄 Request Body — TaskRequestDTO

Use the following JSON structure when creating or updating a task:

```json
{
  "title": "Example Task",
  "description": "This is a sample description.",
  "status": "ToDo"
}
```

Allowed values for status:
- ToDo
- InProgress
- Completed

## ✅ Validation & Error Handling

- Uses `@Valid` to enforce required fields such as `title` and `status`.
- The `description` field must not exceed 500 characters.
- If an invalid enum value is passed (e.g., `"status": "INVALID"`), the API returns `400 Bad Request`.
- A custom `JsonExceptionMapper` provides clear messages when invalid enum values are received.

## 🧪 Tests

The project includes both unit and integration tests using JUnit and RestAssured.

### Run all tests
```bash
./mvnw test
./mvnw test -Dtest=TaskServiceTest
./mvnw test -Dtest=TaskControllerTest
```
Test coverage includes:
- Business logic in TaskService
- REST API endpoints in TaskController
- Repository filtering methods in TaskRepository
- Validation and error scenarios

## 🌐 API Documentation (Swagger / OpenAPI)

Interactive documentation is available at:
http://localhost:8080/q/swagger-ui

This interface provides:

- A complete overview of all available endpoints
- Request and response schemas
- The ability to test API calls directly from the browser

![Swagger UI Screenshot](https://raw.githubusercontent.com/citeli/task-management/refs/heads/dev/assets/swagger.png)


> Note: Authentication is required for protected endpoints — use the "Authorize" button in the UI.

## ⚙️ Configuration & Seeder

- The application uses an **in-memory H2 database**, configured via `application.properties` with:
```text
quarkus.datasource.jdbc.url=jdbc:h2:mem:tasks;DB_CLOSE_DELAY=-1
quarkus.hibernate-orm.database.generation=drop-and-create
```


- On startup, a **seeder class** checks if the database is empty. If so, it populates it with 3 predefined tasks:
- One with status `InProgress`
- Two with status `Completed`

- This ensures the API has usable data for development or demonstration after each restart.

> You can disable or control this behavior by modifying the `TaskDataSeeder` class or using configuration profiles.
