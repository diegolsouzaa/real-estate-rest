package br.ce.diegosouza.rest.refac.suite;


import br.ce.diegosouza.rest.core.BaseTest;
import br.ce.diegosouza.rest.refac.AccountsTest;
import br.ce.diegosouza.rest.refac.AuthTest;
import br.ce.diegosouza.rest.refac.BalanceTest;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.runners.Suite.SuiteClasses;

@RunWith(org.junit.runners.Suite.class)
@SuiteClasses({
        AccountsTest.class,
        //MovementTest.class,
        BalanceTest.class,
        AuthTest.class
})
public class Suite extends BaseTest {

    @BeforeClass
    public static void login(){
        Map<String, String> login = new HashMap<String, String>();
        login.put("email", "diego@lauriano");
        login.put("senha", "1234");

        String token =
                given()
                        .body(login)
                        .when()
                        .post("/signin")
                        .then()
                        .statusCode(200)
                        .extract().path("token");

        RestAssured.requestSpecification.header("Authorization", "JWT " + token);

        RestAssured.get("/reset").then().statusCode(200);
    }

}
