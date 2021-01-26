package br.ce.diegosouza.rest.Utils;

import io.restassured.RestAssured;

public class RealStateUtils {

    public static Integer getIdAccountByName(String name){
        return RestAssured.get("/contas?nome="+name).then().extract().path("id[0]");
    }

    public static Integer getIdMovementByDescription(String desc){
        return RestAssured.get("/transacoes?descricao="+desc).then().extract().path("id[0]");
    }
}
