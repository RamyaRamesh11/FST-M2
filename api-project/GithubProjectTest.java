package liveProject;

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


public class GithubProjectTest {
    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;
    String sshKey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQChYqZIBNKWFnwCIGcdUJVB0ydYvu/TIszLhtv3MDMCn036OiyKoed+0fqSRrzhvHeG5UDJVeTct31liug75ttELwZ5yMfvw0fgeSvhuezVWpGWMPYQif5I8tOlbenG2LXBehlpZXoaRITQQfsQgy2PiETs4HB/Ofw7I8DqXzBg2b4mDfv4S7BIMJxS7QYtCHWJ5LEggcUPCfUFWBa7BNpzUnQLnAxVTfLlspwx3tGe90eDT1C66gPAs0diPE/FHRivi+TNFKFC75MVQ8RSBwK8lcJsy7ZAU5erUwWgrbdaMjQG1w55EGizow710NgWrlNDYZwnK/fRLxg6mgjAKnLh";
    int id;

    @BeforeClass
    public void setUp() {
        //Request Specification
        requestSpec = new RequestSpecBuilder().
                setBaseUri("https://api.github.com").
                setContentType(ContentType.JSON).addHeader("Authorization", "token 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").
                build();

        //Response Specification
        responseSpec = new ResponseSpecBuilder().
                build();
    }

        @Test(priority=1)
        public void addSshKey()
        {
            //Request Body
            Map<String,Object> reqBody = new HashMap<>();
            reqBody.put("title", "TestAPIKey");
            reqBody.put("key", sshKey);


            //Generate Response
            Response response = given().log().all().spec(requestSpec).body(reqBody).when().post("/user/keys");


            //Extract the id
            id = response.then().extract().path("id");
            System.out.println(id);


            //Assertions
            response.then().log().all().spec(responseSpec).statusCode(201);
        }

        @Test(priority = 2)
        public void getSshKey()
        {
            given().log().all().spec(requestSpec).pathParam("id", id).
                    when().get("/user/keys/{id}").
                    then().log().all().spec(responseSpec).statusCode(200);

        }

        @Test(priority = 3)
        public void deleteSshKey()
        {
            given().log().all().spec(requestSpec).pathParam("id", id).
                    when().delete(" /user/keys/{id}").
                    then().log().all().spec(responseSpec).statusCode(204);
        }




    }

