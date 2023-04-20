package edu.prog2.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.prog2.helpers.Utils;
import edu.prog2.model.Avion;

public class AvionesService {
    private List<Avion> aviones;
    private String fileName;

    public AvionesService() throws Exception {
        aviones = new ArrayList<>();
        fileName = Utils.PATH + "aviones";

        if (Utils.fileExists(fileName + ".csv")) {
            loadCSV();
        } else if (Utils.fileExists(fileName + ".json")) {
            loadJSON();
        } else {
            System.out.println("Aún no se ha creado un archivo: " + fileName);
        }
    }

    public List<Avion> loadCSV() throws Exception {
        String text = Utils.readText(fileName + ".csv");

        try (Scanner sc = new Scanner(text).useDelimiter(";|[\n]+|[\r\n]+")) {
            while (sc.hasNext()) {
                String matricula = sc.next();
                String modelo = sc.next();

                aviones.add(new Avion(matricula, modelo));
            }
        }

        return aviones;
    }

    public List<Avion> loadJSON() throws Exception {
        aviones = new ArrayList<>();

        String data = Utils.readText(fileName + ".json");
        JSONArray jsonArr = new JSONArray(data);

        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            aviones.add(new Avion(jsonObj));
        }

        return aviones;
    }

    public boolean contains(Avion avion) {
        return aviones.contains(avion);
    }

    public boolean add(Avion avion) throws Exception {
        if (contains(avion)) {
            throw new IOException(String.format(
                    "No agregado, el avion %s ya existe", avion.getMatricula()));
        }
        boolean ok = aviones.add(avion);
        Utils.writeData(aviones, fileName);
        return ok;
    }

    public List<Avion> getList() {
        return aviones;
    }

    public Avion get(int index) {
        return aviones.get(index);
    }

    public Avion get(Avion avion) {
        int index = aviones.indexOf(avion);
        avion = index > -1 ? aviones.get(index) : null;

        if (avion == null) {
            throw new NullPointerException("No se encontró el avion ");
        }

        return avion;
    }

    public JSONObject getJSON(int index) {
        return aviones.get(index).toJSONObject();
    }

    public JSONObject getJSON(Avion avion) {
        int index = aviones.indexOf(avion);
        return index > -1 ? getJSON(index) : null;
    }

    public JSONObject get(String matricula) throws Exception {
        return getJSON(new Avion(matricula));
    }

    public JSONArray getJSONArray() throws Exception {
        return new JSONArray(Utils.readText(fileName + ".json"));
    }

    public JSONObject set(String matricula, JSONObject json) throws Exception {
        Avion avion = new Avion(json);
        avion.setMatricula(matricula);
        int index = aviones.indexOf(avion);

        if (index < 0) {
            throw new NullPointerException("No se encontró el avion con matricula %s" + matricula);
        }

        aviones.set(index, avion);
        Utils.writeData(aviones, fileName);
        return new JSONObject(avion);
    }

    public void remove(String matricula) throws Exception {
        Avion avion = new Avion(matricula);

        if (!aviones.remove(avion)) {
            throw new Exception(
                    String.format(
                            "No se encontró el avion con matricula %s", matricula));
        }
        Utils.writeData(aviones, fileName);
    }

    public void update() throws Exception {
        aviones = new ArrayList<>();
        loadCSV();
        Utils.writeJSON(aviones, fileName + ".json");
    }

}
