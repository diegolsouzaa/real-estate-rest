package br.ce.diegosouza.rest.refac;

import br.ce.diegosouza.rest.Utils.RealStateUtils;
import br.ce.diegosouza.rest.core.BaseTest;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class AccountsTest extends BaseTest {

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
        Integer ACCOUNT_ID = RealStateUtils.getIdAccountByName("Conta para alterar");
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



}
