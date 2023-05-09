package edu.prog2.controllers;

import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.delete;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.prog2.helpers.StandardResponse;
import edu.prog2.model.Silla;
import edu.prog2.services.SillasService;
import spark.Request;
import spark.Response;

public class SillasController {
    public SillasController(final SillasService sillasService) {
        path("/sillas", () -> {
            get("", (request, response) -> new StandardResponse(response, "ok", sillasService.getJSONArray()));

            get("/:id", (Request request, Response response) -> {
                String id = request.params(":id");
                JSONObject json = sillasService.get(id);
                return new StandardResponse(response, "ok", json);
            });

            get("/select/:matricula", (Request req, Response res) -> {
                String matricula = req.params(":matricula".replace("+", " "));
                JSONArray json = sillasService.select(matricula);
                return new StandardResponse(res, "ok", json);

            });
            
            post("", (request, response) -> {
                Silla silla = new Silla(new JSONObject(request.body()));
                sillasService.add(silla);
                return new StandardResponse(response, "ok");
            });

            put("/:numero", (request, response) -> {
                String numero = request.params(":numero");
                JSONObject json = new JSONObject(request.body());
                json = sillasService.set(numero, json);
                return new StandardResponse(response, "ok", json);
            });

            delete("/:numero", (request, response) -> {
                String numero = request.params(":numero");
                sillasService.remove(numero);
                return new StandardResponse(response, "ok");
            });
        });
    }
}
