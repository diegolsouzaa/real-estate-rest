package br.ce.diegosouza.rest.core;

import io.restassured.http.ContentType;

public interface Constants {

    String APP_BASE_URL = "http://barrigarest.wcaquino.me";
    Integer APP_PORT = 80;
    String APP_BASE_PATH = "";
    ContentType APP_CONTENT_TYPE = ContentType.JSON;
    Long MAX_TIMEOUT = 3000L;

}
