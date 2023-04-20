package edu.prog2;

import static spark.Spark.get;
import static spark.Spark.put;

import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.prog2.helpers.StandardResponse;
import edu.prog2.services.AvionesService;
import edu.prog2.services.ReservasService;
import edu.prog2.services.SillasService;
import edu.prog2.services.TrayectosService;
import edu.prog2.services.UsuariosService;
import edu.prog2.services.VuelosService;

public class Rest {
    public static void main(String[] args) throws Exception {

        Locale.setDefault(new Locale("es_CO"));

        UsuariosService usuarios = new UsuariosService();
        AvionesService aviones = new AvionesService();
        TrayectosService trayectos = new TrayectosService();
        VuelosService vuelos = new VuelosService(trayectos, aviones);
        SillasService sillas = new SillasService(aviones);
        ReservasService reservas = new ReservasService(usuarios, vuelos, sillas);

        get("/usuarios", (req, res) -> usuarios.getJSONArray().toString(2));
        get("/aviones", (req, res) -> aviones.getJSONArray().toString(2));
        get("/trayectos", (req, res) -> trayectos.getJSONArray().toString(2));
        get("/vuelos", (req, res) -> vuelos.getJSONArray().toString(2));
        get("/sillas", (req, res) -> sillas.getJSONArray().toString(2));
        get("/reservas", (req, res) -> reservas.getJSONArray().toString(2));

        get("/hola", (req, res) -> {
            String nombre = "Carlos";
            return String.format("¡Hola %s!", nombre);
        });

        get("/prueba1", (request, response) -> {
            return new StandardResponse(response, 200, "Prueba exitosa");
        });

        get("/prueba2", (request, response) -> {
            String message = "Hola Carlos Cuesta";
            return new StandardResponse(response, message);
        });

        get("/prueba3", (request, response) -> {
            try {
                double x = Double.parseDouble("23,5");
                double y = 0;
                double z = x / y;
                return new StandardResponse(response, "Resultado = " + z);
            } catch (Exception e) {
                return new StandardResponse(response, 404, "Error: " + e);
            }
        });

        get("/prueba4", (request, response) -> { // manejo de excepciones
            // StandardResponse(Response response, int status, Exception e)
            double x = Double.parseDouble("23,5");
            double y = 1;
            try {
                double z = x / y;
                return new StandardResponse(response, "Resultado = " + z);
            } catch (Exception e) {
                return new StandardResponse(response, e);
            }
        });

        get("/prueba5/:matricula", (request, response) -> {
            String matricula = request.params(":matricula");
            JSONObject json = aviones.get(matricula);
            return new StandardResponse(response, "ok", json);
        });

        get("/prueba5", (request, response) -> {
            JSONArray json = aviones.getJSONArray();
            return new StandardResponse(response, "ok", json);
        });

        get("/prueba6", (request, response) -> {
            JSONObject json = new JSONObject()
                    .put("matricula", "hk2333")
                    .put("modelo", "air bus 20020");

            return new StandardResponse(response, "no se pudo actualizar", json);
        });

        get("/prueba7", (request, response) -> {
            JSONObject json = new JSONObject()
                    .put("matricula", "hk2333")
                    .put("modelo", "air bus 20020");

            return new StandardResponse(response, "no se pudo actualizar", json);
        });

        get("/prueba8", (request, response) -> {
            JSONObject json = aviones.get("HK2023");

            return new StandardResponse(response, "no se pudo actualizar", json);
        });

        get("/prueba8/:matricula", (request, response) -> {
            String matricula = request.params(":matricula");

            return new StandardResponse(response, "ok", aviones.get(matricula));
        });

        get("/prueba9", (request, response) -> {

            return new StandardResponse(response, "ok", aviones.getJSONArray());
        });

        get("/usuarios/:identificacion", (request, response) -> {
            String identificacion = request.params(":identificacion");
            JSONObject json = usuarios.get(identificacion);
            return new StandardResponse(response, "ok", json);
        });

        get("/reservas/:id", (request, response) -> {
            String id = request.params(":id");
            JSONObject json = reservas.get(id);
            return new StandardResponse(response, "ok", json);
        });

        put("/reservas/:id", (request, response) -> {
            String id = request.params(":id");
            JSONObject jsonBody = new JSONObject(request.body());
            JSONObject json = reservas.set(id, jsonBody);
            return new StandardResponse(response, "ok", json);
        });

    }

}