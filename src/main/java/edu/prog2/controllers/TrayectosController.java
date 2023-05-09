package edu.prog2.controllers;

import spark.Request;
import spark.Response;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.put;

import java.util.UUID;

import org.json.JSONObject;

import edu.prog2.helpers.StandardResponse;
import edu.prog2.model.Trayecto;
import edu.prog2.services.TrayectosService;

public class TrayectosController {

    public TrayectosController(final TrayectosService trayectosService) {

        path("/trayectos", () -> {

            get("", (request, response) -> new StandardResponse(response, "ok", trayectosService.getJSONArray()));

            get("/:id", (Request request, Response response) -> {
                String id = request.params(":id");
                JSONObject json = trayectosService.get(id);
                return new StandardResponse(response, "ok", json);
            });

            post("", (request, response) -> {
                JSONObject requestBody = new JSONObject(request.body());

                //usa un identificador unico, generando un id de 5 caracteres con letras mayuscula
                String generador = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
                requestBody.put("id", generador);

                Trayecto trayecto = new Trayecto(requestBody);
                trayectosService.add(trayecto);

                return new StandardResponse(response, "ok");
            });

            put("/:id", (request, response) -> {
                String id = request.params(":id");
                JSONObject json = new JSONObject(request.body());
                json = trayectosService.set(id, json);
                return new StandardResponse(response, "ok", json);
            });

            delete("/:id", (request, response) -> {
                String id = request.params(":id");
                trayectosService.remove(id);
                return new StandardResponse(response, "ok");
            });

        });

    }

}