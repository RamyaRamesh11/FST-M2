package liveProject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;


@ExtendWith(PactConsumerTestExt.class)
    public class ConsumerTest {
        //Headers
        Map<String,String> headers = new HashMap<>();

        //set the resource path
        String resourcePath = "/api/users";

        //Create the contract
        @Pact(consumer= "UserConsumer", provider = "UserProvider")
        public RequestResponsePact createPact(PactDslWithProvider builder)
        {
            //set the headers
            headers.put("Content-Type", "application/json");
            //create the body
            DslPart requestResponseBody = new PactDslJsonBody()
                    .numberType("id")
                    .stringType("firstName")
                    .stringType("lastName")
                    .stringType("email","ramya@example.com");

            //Record interaction pact
            return builder.given("A request to create a user")
                    .uponReceiving("A request to create a user")
                    .method("POST")
                    .path(resourcePath)
                    .headers(headers)
                    .body(requestResponseBody)
                    .willRespondWith()
                    .status(201)
                    .body(requestResponseBody)
                    .toPact();

        }

        @Test
        @PactTestFor(providerName = "UserProvider", port = "8282")
        public void consumerTest()
        {
            //Base URI
            String baseURI = "http://localhost:8282"+resourcePath;

            //Request Body
            Map<String,Object> reqBody = new HashMap<>();
            reqBody.put("id",568);
            reqBody.put("firstName", "Ramya");
            reqBody.put("lastName", "N");
            reqBody.put("email", "ramya@example.com");

            //Generate Response
            given().headers(headers).body(reqBody).log().all().
                    when().post(baseURI).
                    then().statusCode(201).log().all();



        }

    }

