package edu.prog2.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.prog2.helpers.Utils;
import edu.prog2.model.Licor;
import edu.prog2.model.Menu;
import edu.prog2.model.Reserva;
import edu.prog2.model.Silla;
import edu.prog2.model.SillaEjecutiva;
import edu.prog2.model.Usuario;
import edu.prog2.model.Vuelo;

public class ReservasService {
    private List<Reserva> reservas;
    private String fileName;
    private UsuariosService usuarios;
    private VuelosService vuelos;
    private SillasService sillas;

    public ReservasService(
            UsuariosService usuarios, VuelosService vuelos, SillasService sillas) throws Exception {
        this.sillas = sillas;
        this.vuelos = vuelos;
        this.usuarios = usuarios;
        reservas = new ArrayList<>();
        fileName = Utils.PATH + "reservas";

        if (Utils.fileExists(fileName + ".csv")) {
            loadCSV();
        } else if (Utils.fileExists(fileName + ".json")) {
            loadJSON();
        } else {
            System.out.println("Aún no se ha creado un archivo: " + fileName);
        }
    }

    public boolean add(Reserva reserva) throws Exception {
        if (contains(reserva)) {
            throw new IOException(String.format(
                    "No agregado, la reserva con ID %s ya existe", reserva.getId()));
        }

        boolean ok = reservas.add(reserva);
        Utils.writeData(reservas, fileName);
        return ok;
    }

    public Reserva get(int index) {
        return reservas.get(index);
    }

    public Reserva get(Reserva reserva) {
        int index = reservas.indexOf(reserva);
        reserva = index > -1 ? reservas.get(index) : null;

        if (reserva == null) {
            throw new NullPointerException("No se encontró el trayecto");
        }

        return reserva;
    }

    public JSONObject get(String id) throws Exception {
        return getJSON(new Reserva(id));
    }

    public List<Reserva> getList() {
        return reservas;
    }

    public boolean contains(Reserva reserva) {
        return reservas.contains(reserva);
    }

    public List<Reserva> loadJSON() throws Exception {
        reservas = new ArrayList<>();

        String data = Utils.readText(fileName + ".json");
        JSONArray jsonArr = new JSONArray(data);

        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            reservas.add(new Reserva(jsonObj));
        }

        return reservas;
    }

    public JSONObject getJSON(int index) {
        return reservas.get(index).toJSONObject();

    }

    public JSONObject getJSON(Reserva reserva) {
        int index = reservas.indexOf(reserva);
        return index > -1 ? getJSON(index) : null;
    }

    public void remove(String id) throws Exception {
        Reserva reserva = new Reserva(id);
        // – pendiente aquí una verificación importante…
        if (!reservas.remove(reserva)) {
            throw new Exception(
                    String.format(
                            "No se encontró la reserva con identificación %s", id));
        }
        Utils.writeData(reservas, fileName);
    }

    public void update() throws Exception {
        reservas = new ArrayList<>();
        loadCSV();
        Utils.writeJSON(reservas, fileName + ".json");
    }

    public void loadCSV() throws Exception {
        String linea;

        try (BufferedReader archivo = Files.newBufferedReader(Paths.get(fileName + ".csv"))) {
            while ((linea = archivo.readLine()) != null) {
                String data[] = linea.split(";");// Cree un array data con los datos de la línea
                String id = data[0];
                double costo = Double.parseDouble(data[1]);
                Usuario usuario = usuarios.get(new Usuario(data[2]));
                Vuelo vuelo = vuelos.get(new Vuelo(data[3]));
                Silla silla = sillas.get(new Silla(data[4]));

                boolean disponible = Boolean.parseBoolean(data[5]);

                boolean checkIn = Boolean.parseBoolean(data[6]);

                if (silla instanceof SillaEjecutiva) {
                    SillaEjecutiva aux = (SillaEjecutiva) silla;
                    aux.setMenu(Menu.getEnum(data[7]));
                    aux.setLicor(Licor.getEnum(data[8]));
                    silla = aux;
                }

                // asignar el costo original al costo del trayecto del vuelo:
                vuelo.getTrayecto().setCosto(costo);
                // reconstruir la reserva y agregarla al listado:
                Reserva reserva = new Reserva(id, usuario, vuelo, silla);
                reserva.getSilla().setDisponible(disponible);
                reserva.setCheckIn(checkIn);
                reservas.add(reserva);
            }
        }
    }

    public JSONArray getJSONArray() throws Exception {
        JSONArray reservados = new JSONArray();
        for (Usuario u : usuarios.getList()) {
            JSONObject json = new JSONObject(u).put("vuelos", new JSONArray());
            boolean tieneReservas = false;
            for (Reserva vr : reservas) {
                if (u.equals(vr.getUsuario())) {
                    tieneReservas = true;
                    json.getJSONArray("vuelos").put(new JSONObject(vr));
                }
            }
            if (tieneReservas) {
                reservados.put(json);
            }
        }
        return reservados;
    }

    private void asignarSilla(JSONObject body, Reserva reserva, Silla silla) {

        if (silla instanceof SillaEjecutiva) {
            SillaEjecutiva ejecutiva = (SillaEjecutiva) silla;
            if (body.has("menu")) {
                ejecutiva.setMenu(body.getEnum(Menu.class, "menu"));
            }
            if (body.has("licor")) {
                ejecutiva.setLicor(body.getEnum(Licor.class, "licor"));
            }
            silla = ejecutiva;
        }

        reserva.getSilla().setDisponible(true);
        silla.setDisponible(false);
        reserva.setSilla(silla);

    }

    public JSONObject set(String id, JSONObject body) throws Exception {
        Reserva reserva = get(new Reserva(id));

        if (reserva == null) {
            throw new NullPointerException("No se encontro la reserva");
        }

        if (body.has("idVuelo")) {

            Vuelo vuelo = vuelos.get(new Vuelo(body.getString("idVuelo"))); // nueva instancia

            if (vuelo == null) {
                throw new Exception("El vuelo no existe, esta cancelado o la fecha no coincide");
            }

            if (vuelo.getFechaHora().compareTo(LocalDateTime.now()) >= 0) {
                reserva.setVuelo(vuelo);
            }else {
                throw new Exception("La fecha esta en el pasado :(");
            }

            if (body.has("idSilla")) {

                Silla silla = sillas.get(new Silla(body.getString("idSilla")));

                if (silla != null && seatAvailableOnFlight(silla, vuelo)) {

                    asignarSilla(body, reserva, silla);
                    reserva.setVuelo(vuelo);

                } else {
                    throw new Exception("La silla no esta disponible o no existe");
                }

            } else {

                List<Silla> sillasDisponibles = seatsAvailableOnFlight(vuelo);

                if (sillasDisponibles.isEmpty()) {

                    throw new Exception("El vuelo no tiene sillas disponibles");
                }

                Class<?> claseSilla = reserva.getSilla().getClass();

                for (Silla silla : sillasDisponibles) {
                    if (claseSilla.equals(silla.getClass())) {
                        asignarSilla(body, reserva, silla);
                        reserva.setVuelo(vuelo);
                        break;
                    }
                }

            }

        } else {
            if (body.has("idSilla")) {

                Silla silla = sillas.get(new Silla(body.getString("idSilla")));

                if (silla != null && seatAvailableOnFlight(silla, reserva.getVuelo())) {

                    asignarSilla(body, reserva, silla);

                } else {
                    throw new Exception("La silla no existe o no esta disponible en el vuelo actual");
                }

            }
        }

        Utils.writeData(reservas, fileName);
        return new JSONObject(reserva);
    }

    public int totalFlightsBooked(Usuario usuario) {

        int totalReservas = 0;

        for (Reserva reserva : reservas) {
            if (reserva.getUsuario().equals(usuario)) {
                totalReservas++;
            }
        }

        return totalReservas;

    }

    private List<Silla> reservedSeatsOnFlight(Vuelo vuelo) throws Exception {

        List<Silla> reservadas = new ArrayList<>();

        for (Reserva reserva : reservas) {
            if (reserva.getVuelo().getAvion().equals(vuelo.getAvion()) && !reserva.getSilla().getDisponible()) {

                reservadas.add(reserva.getSilla());

            }
        }
        return reservadas;
    }

    private boolean seatAvailableOnFlight(Silla silla, Vuelo vuelo) throws Exception {

        List<Silla> reservadas = reservedSeatsOnFlight(vuelo);

        for (Silla reservada : reservadas) {
            if (reservada.equals(silla)) {
                return false;
            }
        }
        return true;
    }

    public List<Silla> seatsAvailableOnFlight(Vuelo vuelo) throws Exception {

        List<Silla> disponibles = new ArrayList<>();

        for (Silla silla : sillas.getList()) {
            if (silla.getAvion().equals(vuelo.getAvion())) {
                if (seatAvailableOnFlight(silla, vuelo)) {
                    disponibles.add(silla);
                }
            }
        }
        return disponibles;
    }

    public JSONArray seatsAvailableOnFlight(String id) throws Exception {

        Vuelo vuelo = vuelos.get(new Vuelo(id));

        return new JSONArray(seatsAvailableOnFlight(vuelo));

    }
}
