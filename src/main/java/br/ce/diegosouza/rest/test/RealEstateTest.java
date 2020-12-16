package br.ce.diegosouza.rest.test;

import br.ce.diegosouza.rest.core.BaseTest;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RealEstateTest extends BaseTest {

    private String token;

    @Before
    public void login(){

        Map<String, String> login = new HashMap<String, String>();
        login.put("email", "diego@lauriano");
        login.put("senha", "1234");

        token =
                given()
                        .body(login)
                        .when()
                        .post("/signin")
                        .then()
                        .statusCode(200)
                        .extract().path("token");
    }

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
        given()
                .header("Authorization", "JWT " + token)
                .body("{\"nome\": \"conta alterada\"}")
                .when()
                .put("/contas/331859")
                .then().log().all()
                .statusCode(200).body("nome", is("conta alterada"));
    }

    @Test
    public void shouldDoNotIncludeAccountWithSameName(){

        given()
                .header("Authorization", "JWT " + token)
                .body("{\"nome\": \"conta alterada\"}")
                .when()
                .post("/contas")
                .then()
                .statusCode(400).body("error", is("Já existe uma conta com esse nome!"))
        ;
    }
}
