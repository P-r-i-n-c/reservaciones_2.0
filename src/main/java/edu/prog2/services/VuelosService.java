package edu.prog2.services;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.prog2.helpers.Utils;
import edu.prog2.model.Avion;
import edu.prog2.model.Trayecto;
import edu.prog2.model.Vuelo;

public class VuelosService {

    private List<Vuelo> vuelos;
    private TrayectosService trayectos;
    private AvionesService aviones;
    private String fileName;

    public VuelosService(TrayectosService trayectos, AvionesService aviones)
            throws Exception {
        this.trayectos = trayectos;
        this.aviones = aviones;
        vuelos = new ArrayList<>();
        fileName = Utils.PATH + "vuelos";

        if (Utils.fileExists(fileName + ".csv")) {
            loadCSV();
        } else if (Utils.fileExists(fileName + ".json")) {
            loadJSON();
        } else {
            System.out.println("Aún no se ha creado un archivo: " + fileName);
        }
    }

    public List<Vuelo> loadCSV() throws Exception {
        String text = Utils.readText(fileName + ".csv");

        try (Scanner sc = new Scanner(text).useDelimiter(";|[\n]+|[\r\n]+")) {
            while (sc.hasNext()) {
                String id = sc.next();
                LocalDateTime fechaHora = LocalDateTime.parse(sc.next());
                Trayecto trayecto = trayectos.get(new Trayecto(sc.next()));
                Avion avion = aviones.get(new Avion(sc.next()));

                vuelos.add(new Vuelo(id, fechaHora, trayecto, avion));
                sc.nextLine();
            }
        }

        return vuelos;
    }

    public List<Vuelo> loadJSON() throws Exception {
        vuelos = new ArrayList<>();

        String data = Utils.readText(fileName + ".json");
        JSONArray jsonArr = new JSONArray(data);

        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            vuelos.add(new Vuelo(jsonObj));
        }

        return vuelos;
    }

    public boolean add(Vuelo vuelo) throws Exception {
        if (contains(vuelo)) {
            throw new IOException(String.format(
                    "No agregado, el vuelo con identificación %s ya existe", vuelo.getId()));
        }
        boolean ok = vuelos.add(vuelo);
        Utils.writeData(vuelos, fileName);
        return ok;
    }

    public void add(JSONObject json) throws Exception {
        String id = Utils.getRandomKey(5);
        LocalDateTime fechaHora = LocalDateTime.parse(json.getString("fechaHora"));
        Trayecto trayecto = trayectos.get(new Trayecto(json.getString("idTrayecto")));
        Avion avion = aviones.get(new Avion(json.getString("matriculaAvion")));
        add(new Vuelo(id, fechaHora, trayecto, avion));
    }

    public boolean contains(Vuelo vuelo) {

        return vuelos.contains(vuelo);
    }

    public List<Vuelo> getList() {
        return vuelos;
    }

    public Vuelo get(int index) {
        return vuelos.get(index);
    }

    public Vuelo get(Vuelo vuelo) {
        int index = vuelos.indexOf(vuelo);
        vuelo = index > -1 ? vuelos.get(index) : null;

        if (vuelo == null) {
            throw new NullPointerException("No se encontró el vuelo ");
        }

        return vuelo;
    }

    public JSONObject getJSON(int index) {
        return vuelos.get(index).toJSONObject();
    }

    public JSONObject getJSON(Vuelo vuelo) {
        int index = vuelos.indexOf(vuelo);
        return index > -1 ? getJSON(index) : null;
    }

    public JSONObject get(String id) throws Exception {
        return getJSON(new Vuelo(id));
    }

    public JSONArray getJSONArray() throws Exception {
        return new JSONArray(Utils.readText(fileName + ".json"));
    }

    public JSONObject set(String id, JSONObject json) throws Exception {
        Vuelo vuelo = new Vuelo(json);
        vuelo.setId(id);
        int index = vuelos.indexOf(vuelo);

        if (index < 0) {
            throw new NullPointerException("No se encontró el vuelo con identificación " + id);
        }

        vuelos.set(index, vuelo);
        Utils.writeData(vuelos, fileName);
        return new JSONObject(vuelo);
    }

    public void remove(String id) throws Exception {
        Vuelo vuelo = new Vuelo(id);
        // – pendiente aquí una verificación importante…
        if (!vuelos.remove(vuelo)) {
            throw new Exception(
                    String.format(
                            "No se encontró el vuelo con identificación %s", id));
        }
        Utils.writeData(vuelos, fileName);
    }

    public AvionesService getAviones() {
        return aviones;
    }

    public TrayectosService getTrayectos() {
        return trayectos;
    }

    public JSONArray select(String paramsVuelos) throws Exception {
        JSONObject json = Utils.paramsToJson(paramsVuelos);
        Trayecto trayecto = trayectos.get(
                new Trayecto(
                        "", json.getString("origen"), json.getString("destino"), 0.0, Duration.ZERO));

        LocalDateTime fechaHora = LocalDateTime.parse(json.getString("fechaHora"));

        JSONArray array = new JSONArray();
        for (Vuelo v : vuelos) {
            if (v.getTrayecto().equals(trayecto) && v.getFechaHora().isAfter(fechaHora)) {
                array.put(new JSONObject(v));
            }
        }

        return array;
    }

    public void update() throws Exception {
        vuelos = new ArrayList<>();
        loadCSV();
        Utils.writeJSON(vuelos, fileName + ".json");
    }

}