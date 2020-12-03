package br.ce.diegosouza.rest.test;

import br.ce.diegosouza.rest.core.BaseTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RealEstateTest extends BaseTest {

    @Test
    public void shouldDoNotAcessApiWithoutToken(){
        given()
        .when()
            .get("/contas")
        .then()
            .statusCode(401)
        ;
    }

    @Test
    public void shouldIncludeAccount(){
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
            .extract().path("token")
        ;

        given()
               .header("Authorization", "JWT " + token)
               .body("{\"nome\": \"conta qualquer2\"}")
               .when()
               .post("/contas")
                .then()
               .statusCode(201)
        ;
    }

    @Test
    public void shouldChangeAccount(){


    }
}
