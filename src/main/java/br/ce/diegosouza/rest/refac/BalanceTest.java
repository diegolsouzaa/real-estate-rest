package br.ce.diegosouza.rest.refac;

import br.ce.diegosouza.rest.Utils.RealStateUtils;
import br.ce.diegosouza.rest.core.BaseTest;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class BalanceTest extends BaseTest {


    @Test
    public void shouldCalculateBalanceAccount(){
        Integer ACCOUNT_ID = RealStateUtils.getIdAccountByName("Conta para saldo");

        given()
                .when()
                .get("/saldo")
                .then().log().all()
                .statusCode(200)
                .body("find{it.conta_id == "+ACCOUNT_ID+"}.saldo", is("534.00"));
    }

}
