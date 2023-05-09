package edu.prog2.controllers;

import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.delete;

import org.json.JSONObject;

import edu.prog2.helpers.StandardResponse;
import edu.prog2.model.Avion;
import edu.prog2.services.AvionesService;
import spark.Request;
import spark.Response;

public class AvionesController {

    public AvionesController(final AvionesService avionesService) {

        path("/aviones", () -> {
            get("", (request, response) -> new StandardResponse(response, "ok", avionesService.getJSONArray()));

            get("/:matricula", (Request request, Response response) -> {
                String matricula = request.params(":matricula");
                JSONObject json = avionesService.get(matricula);
                return new StandardResponse(response, "ok", json);
            });

            post("", (request, response) -> {
                Avion avion = new Avion(new JSONObject(request.body()));
                avionesService.add(avion);
                return new StandardResponse(response, "ok");
            });

            put("/:identificacion", (request, response) -> {
                String identificacion = request.params(":identificacion");
                JSONObject json = new JSONObject(request.body());
                json = avionesService.set(identificacion, json);
                return new StandardResponse(response, "ok", json);
            });

            delete("/:id", (request, response) -> {
                String id = request.params(":id");
                avionesService.remove(id);
                return new StandardResponse(response, "ok");
            });

            // bloque de la función para insertar código en pasos posteriores

        });

    }

}
