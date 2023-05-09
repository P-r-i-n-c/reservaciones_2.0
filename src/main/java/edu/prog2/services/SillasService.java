package edu.prog2.services;

import edu.prog2.helpers.Utils;
import edu.prog2.model.Avion;
import edu.prog2.model.Licor;
import edu.prog2.model.Menu;
import edu.prog2.model.Silla;
import edu.prog2.model.SillaEjecutiva;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class SillasService {

    private List<Silla> sillas;
    private String fileName;
    private AvionesService aviones;

    public SillasService(AvionesService aviones) throws Exception {

        this.aviones = aviones;
        sillas = new ArrayList<>();
        fileName = Utils.PATH + "sillas";

        if (Utils.fileExists(fileName + ".csv")) {
            loadCSV();

        } else if (Utils.fileExists(fileName + ".json")) {
            loadJSON();
        } else {
            System.out.println("Aún no se ha creado un archivo: " + fileName);
        }
    }

    public boolean add(Silla silla) throws Exception {
        if (contains(silla)) {
            throw new IOException(
                    String.format(
                            "No agregada, la silla %s ya existe en el avión %s",
                            silla.getPosicion(), silla.getAvion().getMatricula()));
        }
        boolean ok = sillas.add(silla);
        return ok;
    }

    private boolean contains(Silla silla) {
        return sillas.contains(silla);
    }

    public List<Silla> getList() {
        return sillas;
    }

    public Silla get(int index) {
        return sillas.get(index);
    }

    public JSONObject get(String id) throws Exception {
        return getJSON(new Silla(id));
    }

    public Silla get(Silla silla) {
        int index = sillas.indexOf(silla);
        silla = index > -1 ? sillas.get(index) : null;

        if (silla == null) {
            throw new NullPointerException("No se encontró la silla ");
        }

        return silla;
    }

    public JSONObject getJSON(int index) {
        return sillas.get(index).toJSONObject();
    }

    public JSONObject getJSON(Silla silla) {
        int index = sillas.indexOf(silla);
        return index > -1 ? getJSON(index) : null;
    }

    public JSONArray getJSONArray() throws Exception {
        return new JSONArray(Utils.readText(fileName + ".json"));
    }

    public void update() throws Exception {
        sillas = new ArrayList<>();
        loadCSV();
        Utils.writeJSON(sillas, fileName + ".json");
    }

    public void remove(String id) throws Exception {
        Silla silla = new Silla(id);
        // – pendiente aquí una verificación importante…
        if (!sillas.remove(silla)) {
            throw new Exception(
                    String.format(
                            "No se encontró la silla con identificación %s", id));
        }
        Utils.writeData(sillas, fileName);
    }

    public JSONObject set(String id, JSONObject json) throws Exception {
        Silla silla = get(new Silla(id));
        if (silla == null) {
            throw new NullPointerException("No se encontró la silla con ID " + id);
        }

        silla.setFila(json.getInt("fila"));
        silla.setColumna(json.getString("columna").charAt(0));
        silla.setAvion(aviones.get(new Avion(json.getString("avion"))));
        silla.setDisponible(json.getBoolean("disponible"));

        if (silla instanceof SillaEjecutiva) {
            SillaEjecutiva aux = (SillaEjecutiva) silla;
            aux = (SillaEjecutiva) get(silla);
            aux.setMenu(Menu.valueOf(json.getString("menu")));
            aux.setLicor(Licor.valueOf(json.getString("licor")));
            silla = aux;
        }

        Utils.writeData(sillas, fileName);
        return new JSONObject(silla);
    }

    public AvionesService getAviones() {
        return aviones;
    }

    public void loadCSV() throws Exception {
        String linea;
        String matricula;
        int fila;
        char columna;
        try (BufferedReader archivo = Files.newBufferedReader(Paths.get(fileName + ".csv"))) {
            while ((linea = archivo.readLine()) != null) {
                String data[] = linea.split(";");
                String id = data[0];
                matricula = data[1];
                fila = Integer.parseInt(data[2]);
                columna = data[3].charAt(0);
                Avion avion = aviones.get(new Avion(matricula));

                if (data.length == 8) { // ejecutivas
                    Menu menu = Menu.getEnum(data[6]);
                    Licor licor = Licor.getEnum(data[7]);
                    sillas.add(new SillaEjecutiva(id, avion, fila, columna, menu, licor));
                } else if (data.length == 6) { // económicas
                    sillas.add(new Silla(id, avion, fila, columna));
                } else { // error
                    throw new IOException("Se esperaban 6 u 8 datos por línea");
                }
            }
        }
    }

    public List<Silla> loadJSON() throws Exception {
        sillas = new ArrayList<>();

        String data = Utils.readText(fileName + ".json");
        JSONArray jsonArr = new JSONArray(data);
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            if (jsonObj.has("licor")) {
                sillas.add(new SillaEjecutiva(jsonObj));
            } else {
                sillas.add(new Silla(jsonObj));
            }
        }
        return sillas;
    }

    public void create(String matriculaAvion, int filasEjecutivas, int totalSillas) throws IOException {

        Avion a = aviones.get(new Avion(matriculaAvion));
        int se = filasEjecutivas * 4;
        int sd = totalSillas - se;
        int filasE = (int) Math.ceil(sd / 6.);

        char[] columna = new char[] { 'A', 'C', 'D', 'F' };
        for (int i = 1; i <= filasEjecutivas; i++) {
            for (int j = 0; j < columna.length; j++) {
                sillas.add(new SillaEjecutiva(Utils.getRandomKey(5),
                        a, i, columna[j], Menu.INDEFINIDO, Licor.NINGUNO));
            }
        }

        columna = new char[] { 'A', 'B', 'C', 'D', 'E', 'F' };
        for (int i = filasEjecutivas + 1; i <= filasE + filasEjecutivas; i++) {
            for (int j = 0; j < columna.length; j++) {
                sillas.add(new Silla(Utils.getRandomKey(5),
                        a, i, columna[j]));
            }
        }

        Utils.writeData(sillas, fileName);

    }

    public void removeAll(String matricula) throws Exception {
        Avion avion = new Avion(matricula, "");

        // validación pendiente para evitar error de integridad referencial

        Iterator<Silla> it = sillas.iterator(); // importar de java.util
        while (it.hasNext()) {
            Silla s = it.next();
            if (s.getAvion().equals(avion)) {
                it.remove();
            }
        }
        Utils.writeData(sillas, fileName);
    }

    public JSONObject numberOfSeats(String matricula) throws Exception {
        if (aviones.get(matricula) == null) {
            throw new IOException("El avion no existe ");
        }

        if (sillas == null) {
            throw new Exception("La silla no existe ");
        }

        int sillasEjecutivas = 0;
        int sillasEconomicas = 0;

        for (Silla s : sillas) {
            if (s.getAvion().getMatricula().equals(matricula)) {
                if (s instanceof SillaEjecutiva) {
                    sillasEjecutivas++;
                } else {
                    sillasEconomicas++;
                }

            }
        }

        JSONObject json = new JSONObject()
                .put("totalSillas", new JSONObject()
                        .put("ejecutivas", sillasEjecutivas)
                        .put("economicas", sillasEconomicas))
                .put("matricula", matricula)
                .put("modelo", aviones.get(new Avion(matricula)).getModelo());

        return json;

    }

    public JSONArray aircraftWithNumberSeats() throws Exception {
        JSONArray array = new JSONArray();

        for (Avion a : aviones.getList()) {
            array.put(numberOfSeats(a.getMatricula()));
        }

        return array;

    }

    public JSONArray select(String matricula) throws Exception {
        if (aviones.get(new Avion(matricula)) == null) {
            throw new IOException("El avion no existe ");
        }

        JSONArray array = new JSONArray();

        for (Silla s : sillas) {
            if (s.getAvion().getMatricula().equals(matricula)) {
                array.put(s.toJSONObject());

            }
        }

        return array;

    }
}