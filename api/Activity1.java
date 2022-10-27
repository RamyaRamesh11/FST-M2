package activities;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Activity1 {
    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    int petId;

    @BeforeClass
    public void setUp() {
        //Request Specification
        requestSpec = new RequestSpecBuilder().
                setBaseUri("https://petstore.swagger.io").
                setContentType(ContentType.JSON).build();

        //Response Specification
        responseSpec = new ResponseSpecBuilder().
                build();
    }

    @Test(priority=1)
    public void postPet()
    {
        //Request Body
        Map<String,Object> reqBody = new HashMap<>();
        reqBody.put("id", "77232");
        reqBody.put("name", "Riley");
        reqBody.put("status", "alive");


        //Generate Response
        Response response = given().log().all().spec(requestSpec).body(reqBody).when().post("/v2/pet");


        //Extract the id
        petId = response.then().extract().path("id");

        //Assertions
        response.then().body("id", equalTo(77232));
        response.then().body("name", equalTo("Riley"));
        response.then().body("status", equalTo("alive"));

    }

    @Test(priority=2)
    public void getPet()
    {
        given().log().all().spec(requestSpec).pathParam("petId", petId).
                when().get("/v2/pet/{petId}").
                then().log().all().spec(responseSpec).body("id", equalTo(77232)).body("name", equalTo("Riley")).body("status", equalTo("alive"));
    }

    @Test(priority=3)
    public void deletePet()
    {
        given().log().all().spec(requestSpec).pathParam("petId", petId).
                when().delete("/v2/pet/{petId}").
                then().log().all().spec(responseSpec).body("code", equalTo(200)).body("message", equalTo("" + petId));
    }


}
