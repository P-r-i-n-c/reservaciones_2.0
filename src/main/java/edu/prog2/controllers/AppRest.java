package edu.prog2.controllers;

import static spark.Spark.exception;

import java.util.Locale;

import edu.prog2.helpers.StandardResponse;
import edu.prog2.services.UsuariosService;

public class AppRest {
    public static void main(String[] args) throws Exception {

        Locale.setDefault(new Locale("es_CO"));

        StandardResponse.DEBUGGIN = true;

        exception(Exception.class, (exception, request, response) -> response
                .body((new StandardResponse(response, exception)).toString()));

        UsuariosService usuariosService = new UsuariosService();
        new UsuariosController(usuariosService);
        // espacio para insertar código en pasos posteriores

    }
}