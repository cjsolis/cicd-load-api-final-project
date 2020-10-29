import io.restassured.RestAssured;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

public class APITest {

    private String authUser = "testuser";
    private String authPass = "testpass";

    @Test
    public void Status_Endpoint() {

        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";

        RestAssured.when()
                .get("/v1/examen/status")
                .then()
                .statusCode(200)
                .body("status", equalTo("Listos para el examen"));
    }

    @Test
    public void PutName_NoBody() {

        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";

        RestAssured.when()
                .put("/v1/examen/updateName")
                .then()
                .statusCode(406)
                .body("message", equalTo("Name was not provided"));
    }

    @Test
    public void RandomName_NoAuth() {

        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";

        RestAssured.when()
                .get("/v1/examen/randomName")
                .then()
                .statusCode(401)
                .body("message", equalTo("Please login first"));
    }

    @Test
    public void RandomName_SameName() {

        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";

        String name = RestAssured.given().auth()
                .basic(authUser, authPass)
                .when()
                .get("/v1/examen/randomName")
                .then()
                .statusCode(200)
                .extract()
                .response().path("name");

        String sameNameBody = String.format("{ \"name\":\"%s\"}", name);

        RestAssured.given()
                .body(sameNameBody)
                .when()
                .post("/v1/examen/sameName")
                .then()
                .statusCode(200)
                .body("name", equalTo(name));
    }
}