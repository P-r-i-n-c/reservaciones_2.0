package edu.prog2.services;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.prog2.helpers.Utils;
import edu.prog2.model.Trayecto;

public class TrayectosService {

    private List<Trayecto> trayectos;
    private String fileName;

    public TrayectosService() throws Exception {
        trayectos = new ArrayList<>();
        fileName = Utils.PATH + "trayectos";

        if (Utils.fileExists(fileName + ".csv")) {
            loadCSV();
        } else if (Utils.fileExists(fileName + ".json")) {
            loadJSON();
        } else {
            System.out.println("Aún no se ha creado un archivo: " + fileName);
        }
    }

    public List<Trayecto> loadCSV() throws Exception {
        String text = Utils.readText(fileName + ".csv");

        try (Scanner sc = new Scanner(text).useDelimiter(";|[\n]+|[\r\n]+")) {
            while (sc.hasNext()) {
                String id = sc.next();
                String origen = sc.next();
                String destino = sc.next();
                double costo = Double.parseDouble(sc.next());
                Duration duracion = Duration.parse(sc.next());

                trayectos.add(new Trayecto(id, origen, destino, costo, duracion));
            }
        }

        return trayectos;
    }

    public List<Trayecto> loadJSON() throws Exception {
        trayectos = new ArrayList<>();

        String data = Utils.readText(fileName + ".json");
        JSONArray jsonArr = new JSONArray(data);

        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            trayectos.add(new Trayecto(jsonObj));
        }

        return trayectos;
    }

    public boolean contains(Trayecto trayecto) {
        return trayectos.contains(trayecto);
    }

    public boolean add(Trayecto trayecto) throws Exception {
        if (contains(trayecto)) {
            throw new IOException(String.format(
                    "No agregado, el trayecto con ID %s ya existe", trayecto.getId()));
        }

        boolean ok = trayectos.add(trayecto);
        Utils.writeData(trayectos, fileName);
        return ok;
    }

    public List<Trayecto> getList() {
        return trayectos;
    }

    public Trayecto get(int index) {
        return trayectos.get(index);
    }

    public Trayecto get(Trayecto trayecto) {
        int index = trayectos.indexOf(trayecto);
        trayecto = index > -1 ? trayectos.get(index) : null;

        if (trayecto == null) {
            throw new NullPointerException("No se encontró el trayecto");
        }

        return trayecto;
    }

    public JSONObject getJSON(int index) {
        return trayectos.get(index).toJSONObject();
    }

    public JSONObject getJSON(Trayecto trayecto) {
        int index = trayectos.indexOf(trayecto);
        return index > -1 ? getJSON(index) : null;
    }

    public JSONObject get(String id) throws Exception {
        return getJSON(new Trayecto(id));
    }

    public JSONArray getJSONArray() throws Exception {
        return new JSONArray(Utils.readText(fileName + ".json"));
    }

    public JSONObject set(String id, JSONObject json) throws Exception {
        Trayecto trayecto = get(new Trayecto(id));

        if (trayecto == null) {
            throw new NullPointerException("No se encontró el trayecto " + id);
        } else {
            trayecto.setId(id);
        }

        trayecto.setOrigen(json.getString("origen"));
        trayecto.setDestino(json.getString("destino"));
        trayecto.setCosto(json.getDouble("costo"));
        String duracionStr = json.getString("duracion");
        Duration duracion = Duration.parse(duracionStr);

        trayecto.setDuracion(duracion);

        Utils.writeData(trayectos, fileName);
        return new JSONObject(trayecto);
    }

    public void remove(String id) throws Exception {
        Trayecto trayecto = new Trayecto(id);

        if (Utils.exists(Utils.PATH + "vuelos", "trayecto", trayecto)) {
            throw new Exception(String.format(
                    "No eliminado. El trayecto %s tiene un vuelo", id));
        }

        if (!trayectos.remove(trayecto)) {
            throw new Exception(
                    String.format(
                            "No se encontró el trayecto con identificación %s", id));
        }

        Utils.writeData(trayectos, fileName);
    }

    public void update() throws Exception {
        trayectos = new ArrayList<>();
        loadCSV();
        Utils.writeJSON(trayectos, fileName + ".json");
    }
}
