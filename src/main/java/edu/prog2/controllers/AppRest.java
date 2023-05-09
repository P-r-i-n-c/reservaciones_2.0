package edu.prog2.controllers;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

import edu.prog2.controllers.*;
import edu.prog2.helpers.StandardResponse;
import edu.prog2.services.*;

import spark.Spark;
import static spark.Spark.*;

public class AppRest {

    public static void main(String[] args) throws Exception {
        System.out.println(
                "Envíe un argumento true o false para habilitar o deshabilitar la traza de errores en la terminal");

        boolean debug = true;
        if (args.length > 0) {
            debug = Boolean.parseBoolean(args[0]);
        }
        StandardResponse.DEBUGGIN = debug;

        // La forma documentada NO carga el idioma correctamente
        // Locale locale = new
        // Locale.Builder().setLanguage("es").setRegion("CO").build();
        // Locale.setDefault(locale); // usar punto como separador de decimales

        // La forma obsoleta funcina correctamente:
        Locale.setDefault(new Locale("es_CO"));

        exception(Exception.class,
                (exception, request, response) -> response
                        .body((new StandardResponse(response, exception)).toString()));

        enableCORS();

        UsuariosService usuariosService = new UsuariosService();
        new UsuariosController(usuariosService);

        TrayectosService trayectosService = new TrayectosService();
        new TrayectosController(trayectosService);

        AvionesService avionesService = new AvionesService();
        new AvionesController(avionesService);

        SillasService sillasService = new SillasService(avionesService);
        new SillasController(sillasService);

        // VuelosService vuelosService = new VuelosService(trayectosService, avionesService);
        // new VuelosController(vuelosService);

        // ReservasService reservasVuelosService = new ReservasService(usuariosService, vuelosService, sillasService);
        // new ReservasController(reservasVuelosService);

        // // luego de un request PUT de usuarios, trayectos o aviones actualizar los
        // // archivos JSON relacionados
        // after("/*/:param", (request, response) -> {
        //     if (request.requestMethod().equals("PUT")) {
        //         String path = request.pathInfo().split("/")[1];
        //         if (path.equals("aviones")) {
        //             sillasService.update();
        //         }
        //         if ("usuarios|trayectos|aviones".contains(path)) {
        //             vuelosService.update();
        //             reservasVuelosService.update();
        //         }
        //     }
        // });

        get("/favicon.ico", (request, response) -> {
            response.type("image/ico");
            return Files.readAllBytes(Paths.get("./data/favicon.ico"));
        });

    }

    private static void enableCORS() {
        Spark.staticFiles.header("Access-Control-Allow-Origin", "*");

        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "ok";
        });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
    }

}