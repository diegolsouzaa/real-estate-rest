package br.ce.diegosouza.rest.test;

import br.ce.diegosouza.rest.core.BaseTest;
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

    private Movimentation getMovimetationValid(){
        Movimentation movimentation = new Movimentation();
        movimentation.setConta_id(331859);
        //movimentation.setUsuario_id(88);
        movimentation.setDescricao("descricao da movimentacao");
        movimentation.setEnvolvido("Envolvido na movimentacao");
        movimentation.setTipo("REC");
        movimentation.setData_transacao("01/01/2000");
        movimentation.setData_pagamento("10/05/2010");
        movimentation.setValor(150f);
        movimentation.setStatus(true);

        return movimentation;

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

    @Test
    public void shouldIncludeMovimentation(){
        Movimentation mov = getMovimetationValid();

        given()
                .header("Authorization", "JWT " + token)
                .body(mov)
                .when()
                .post("/transacoes")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    public void shouldValidateRequiredFieldsInMovimentation(){
        given()
                .header("Authorization", "JWT " + token)
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
    public void shouldDoNotIncludeMovimentationWithFutureDate(){

        Movimentation mov = getMovimetationValid();
        mov.setData_transacao("25/12/2020");

        given()
                .header("Authorization", "JWT " + token)
                .body(mov)
                .when()
                .post("/transacoes")
                .then().log().all()
                .statusCode(400)
                .body("$", hasSize(1))
                .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"));
    }

    @Test
    public void shouldDoNotRemoveMovimentation(){
        given()
                .header("Authorization", "JWT " + token)
                .when()
                .delete("/contas/331859")
                .then().log().all()
                .statusCode(500)
                .body("constraint", is("transacoes_conta_id_foreign"));
    }

    @Test
    public void shouldCalculateBalanceAccount(){
        given()
                .header("Authorization", "JWT " + token)
                .when()
                .get("/saldo")
                .then().log().all()
                .statusCode(200)
                .body("find{it.conta_id == 331859}.saldo", is("150.00"));
    }

    //323630
    @Test
    public void shouldRemoveMovement(){

        given()
                .header("Authorization", "JWT " + token)
                .when()
                .delete("/transacoes/323630")
                .then().log().all()
                .statusCode(204);
    }
}
