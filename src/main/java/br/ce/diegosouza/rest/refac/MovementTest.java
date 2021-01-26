//package br.ce.diegosouza.rest.refac;
//
//import br.ce.diegosouza.rest.Utils.DataUtils;
//import br.ce.diegosouza.rest.core.BaseTest;
//import br.ce.diegosouza.rest.test.Movimentation;
//import io.restassured.RestAssured;
//import org.junit.Test;
//
//import static io.restassured.RestAssured.given;
//
//public class MovementTest extends BaseTest {
//
//
//    @Test
//    public void shouldIncludeMovimentation(){
//
//        Movimentation mov = getMovimetationValid();
//        given()
//                .body(mov)
//                .when()
//                .post("/transacoes")
//                .then().log().all()
//                .statusCode(201);
//    }
//
//    private Movimentation getMovimetationValid(){
//        Movimentation movimentation = new Movimentation();
//        movimentation.setConta_id(getIdContaPeloNome("Conta para movimentacoes"));
//        //movimentation.setUsuario_id(88);
//        movimentation.setDescricao("descricao da movimentacao");
//        movimentation.setEnvolvido("Envolvido na movimentacao");
//        movimentation.setTipo("REC");
//        movimentation.setData_transacao(DataUtils.getDataFuture(-1));
//        movimentation.setData_pagamento(DataUtils.getDataFuture(5));
//        movimentation.setValor(150f);
//        movimentation.setStatus(true);
//
//        return movimentation;
//
//    }
//
//    public Integer getIdContaPeloNome(String nome){
//        return RestAssured.get("/contas?nome="+nome).then().extract().path("id[0]");
//    }
//
//
//
//
//
//
//
//}
