package br.ce.diegosouza.rest.refac;

import br.ce.diegosouza.rest.core.BaseTest;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class AccountsTest extends BaseTest {

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

    @Test
    public void shouldIncludeAccount(){
        given()
                .body("{\"nome\": \"Conta Inserida\"}")
                .when()
                .post("/contas")
                .then()
                .statusCode(201);
    }

    @Test
    public void shouldChangeAccount(){
        Integer ACCOUNT_ID = getIdAccountByName("Conta para alterar");
        given()
                .body("{\"nome\": \"Conta alterada\"}")
                .pathParam("id", ACCOUNT_ID)
                .when()
                .put("/contas/{id}")
                .then()
                .statusCode(200).body("nome", is("Conta alterada"));
    }

    @Test
    public void shouldDoNotIncludeAccountWithSameName(){

        given()
                .body("{\"nome\": \"Conta mesmo nome\"}")
                .when()
                .post("/contas")
                .then()
                .statusCode(400).body("error", is("Já existe uma conta com esse nome!"));
    }

    public Integer getIdAccountByName(String name){
       return RestAssured.get("/contas?nome="+name).then().extract().path("id[0]");
    }

}
