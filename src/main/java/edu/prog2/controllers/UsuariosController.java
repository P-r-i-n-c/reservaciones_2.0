package edu.prog2.controllers;

import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.put;
import org.json.JSONObject;

import edu.prog2.helpers.StandardResponse;
import edu.prog2.model.Usuario;
import edu.prog2.services.UsuariosService;
import spark.Request;
import spark.Response;

public class UsuariosController {

    public UsuariosController(final UsuariosService usuariosService) {

        path("/usuarios", () -> {
            get("", (request, response) -> new StandardResponse(response, "ok", usuariosService.getJSONArray()));

            get("/:id", (Request request, Response response) -> {
                String id = request.params(":id");
                JSONObject json = usuariosService.get(id);
                return new StandardResponse(response, "ok", json);
            });

            post("", (request, response) -> {
                Usuario usuario = new Usuario(new JSONObject(request.body()));
                usuariosService.add(usuario);
                return new StandardResponse(response, "ok");
            });

            put("/:identificacion", (request, response) -> {
                String identificacion = request.params(":identificacion");
                JSONObject json = new JSONObject(request.body());
                json = usuariosService.set(identificacion, json);
                return new StandardResponse(response, "ok", json);
            });

            // bloque de la función para insertar código en pasos posteriores

        });

    }

}
