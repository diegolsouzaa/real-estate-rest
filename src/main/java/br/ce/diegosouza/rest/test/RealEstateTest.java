package br.ce.diegosouza.rest.test;

import br.ce.diegosouza.rest.Utils.DataUtils;
import br.ce.diegosouza.rest.core.BaseTest;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RealEstateTest extends BaseTest {

    private static String ACCOUNT_NAME = "Conta" + System.nanoTime();
    private static Integer ACCOUNT_ID;
    private static Integer MOV_ID;

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
    }

    private Movimentation getMovimetationValid(){
        Movimentation movimentation = new Movimentation();
        movimentation.setConta_id(ACCOUNT_ID);
        //movimentation.setUsuario_id(88);
        movimentation.setDescricao("descricao da movimentacao");
        movimentation.setEnvolvido("Envolvido na movimentacao");
        movimentation.setTipo("REC");
        movimentation.setData_transacao(DataUtils.getDataFuture(-1));
        movimentation.setData_pagamento(DataUtils.getDataFuture(5));
        movimentation.setValor(150f);
        movimentation.setStatus(true);

        return movimentation;

    }



    @Test
    public void t02_shouldIncludeAccount(){

        ACCOUNT_ID = given()
               .body("{\"nome\": \""+ACCOUNT_NAME+"\"}")
               .when()
               .post("/contas")
               .then()
               .statusCode(201).extract().path("id");
    }

    @Test
    public void t03_shouldChangeAccount(){
        given()
                .body("{\"nome\": \""+ACCOUNT_NAME+" alterada\"}")
                .pathParam("id", ACCOUNT_ID)
                .when()
                .put("/contas/{id}")
                .then().log().all()
                .statusCode(200).body("nome", is(ACCOUNT_NAME+" alterada"));
    }

    @Test
    public void t04_shouldDoNotIncludeAccountWithSameName(){

        given()
                .body("{\"nome\": \""+ACCOUNT_NAME+" alterada\"}")
                .when()
                .post("/contas")
                .then()
                .statusCode(400).body("error", is("Já existe uma conta com esse nome!"));
    }

    @Test
    public void t05_shouldIncludeMovimentation(){
        Movimentation mov = getMovimetationValid();

        MOV_ID = given()
                .body(mov)
                .when()
                .post("/transacoes")
                .then().log().all()
                .statusCode(201).extract().path("id");
    }

    @Test
    public void t06_shouldValidateRequiredFieldsInMovimentation(){
        given()
                .body("{}")
                .when()
                .post("/transacoes")
                .then()
                .statusCode(400).body("$", hasSize(8))
                .body("msg", hasItems(
                        "Data da Movimentação é obrigatório",
                        "Data do pagamento é obrigatório",
                        "Descrição é obrigatório",
                        "Interessado é obrigatório",
                        "Valor é obrigatório",
                        "Valor deve ser um número",
                        "Conta é obrigatório",
                        "Situação é obrigatório"
                ));

    }

    @Test
    public void t07_shouldDoNotIncludeMovimentationWithFutureDate(){

        Movimentation mov = getMovimetationValid();
        mov.setData_transacao(DataUtils.getDataFuture(2));

        given()
                .body(mov)
                .when()
                .post("/transacoes")
                .then().log().all()
                .statusCode(400)
                .body("$", hasSize(1))
                .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"));
    }

    @Test
    public void t08_shouldDoNotRemoveMovimentation(){
        given()
                .pathParam("id", ACCOUNT_ID)
                .when()
                .delete("/contas/{id}")
                .then().log().all()
                .statusCode(500)
                .body("constraint", is("transacoes_conta_id_foreign"));
    }

    @Test
    public void t09_shouldCalculateBalanceAccount(){
        given()
                .when()
                .get("/saldo")
                .then().log().all()
                .statusCode(200)
                .body("find{it.conta_id == "+ACCOUNT_ID+"}.saldo", is("150.00"));
    }

    //323630
    @Test
    public void t10_shouldRemoveMovement(){

        given()
                .pathParam("id", MOV_ID)
                .when()
                .delete("/transacoes/{id}")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    public void t11_shouldDoNotAcessApiWithoutToken(){
        FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
        req.removeHeader("Authorization");
        given()
                .when()
                .get("/contas")
                .then()
                .statusCode(401);
    }


}
