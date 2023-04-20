package edu.prog2.model;

import java.time.LocalDateTime;

import org.json.JSONException;
import org.json.JSONObject;

import edu.prog2.helpers.Utils;

public class Reserva implements IFormatCSV {
    private String id;
    private LocalDateTime fechaHora;
    private double costo;
    private boolean checkIn;
    private boolean cancelada;
    private Usuario usuario;
    private Vuelo vuelo;
    private Silla silla;

    public Reserva() throws Exception {
        this(Utils.getRandomKey(5), new Usuario(), new Vuelo(), new Silla());
    }

    public Reserva(String id, Usuario usuario, Vuelo vuelo, Silla silla) {
        if (!silla.getAvion().equals(vuelo.getAvion())) {
            throw new IllegalArgumentException();
        }
        this.id = id;
        this.fechaHora = LocalDateTime.now();
        this.costo = vuelo.getTrayecto().getCosto();
        this.checkIn = true;
        this.cancelada = false;
        this.usuario = usuario;
        this.vuelo = vuelo;
        this.silla = silla instanceof SillaEjecutiva ? new SillaEjecutiva((SillaEjecutiva) silla) : new Silla(silla);

    }

    public Reserva(String id) throws Exception {
        this(id, new Usuario(), new Vuelo(), new Silla());
    }

    public Reserva(JSONObject json) throws JSONException, Exception {
        this(json.getString("id"), new Usuario(json.getJSONObject("usuario")), new Vuelo(json.getJSONObject("vuelo")),
                json.getJSONObject("silla").has("menu") ? new SillaEjecutiva(json.getJSONObject("silla"))
                        : new Silla(json.getJSONObject("silla")));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public boolean isCheckIn() {
        return checkIn;
    }

    public void setCheckIn(boolean checkIn) {
        this.checkIn = checkIn;
    }

    public boolean isCancelada() {
        return cancelada;
    }

    public void setCancelada(boolean cancelada) {
        this.cancelada = cancelada;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Vuelo getVuelo() {
        return vuelo;
    }

    public void setVuelo(Vuelo vuelo) {
        this.vuelo = vuelo;
    }

    public Silla getSilla() {
        return silla;
    }

    public void setSilla(Silla silla) {
        this.silla = silla;
    }

    public JSONObject toJSONObject() {
        return new JSONObject(this);
    }

    public String strFechaHora() {
        return Utils.strDateTime(fechaHora);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Reserva)) {
            return false;
        }

        Reserva reserva = (Reserva) obj;
        return this.id.equals(reserva.id) || (this.usuario.equals(reserva.usuario)
                && this.vuelo.equals(reserva.getVuelo()) && this.silla.equals(reserva.silla));
    }

    @Override
    public String toString() {
        String cancelada = this.cancelada ? "Cancelado" : "Vigente";
        String chekIn = this.checkIn ? "Checkeado" : "Pendiente";
        
        String tipoSilla;
        if (silla instanceof SillaEjecutiva) {
            tipoSilla = "ejecutiva";
            SillaEjecutiva silla = (SillaEjecutiva) this.silla;
            return String.format(
                    "Reserva %s - Pasajero: %s %s\n\tFecha y hora: %s - Estado: %s - CheckIn: %s\n\tAvion: %s - %s, Silla: %s - %s, menu: %s, licor: %s ",
                    id, usuario.getNombres(), usuario.getApellidos(), strFechaHora(), cancelada, chekIn,
                    vuelo.getAvion().getMatricula(), vuelo.getAvion().getModelo(), silla.getPosicion(), tipoSilla,
                    silla.getMenu(), silla.getLicor());
        } else {
            tipoSilla = "economica";
            return String.format(
                    "Reserva %s - Pasajero: %s %s\n\tFecha y hora: %s - Estado: %s - CheckIn: %s\n\tAvion: %s - %s, Silla: %s - %s",
                    id, usuario.getNombres(), usuario.getApellidos(), strFechaHora(), cancelada, chekIn,
                    vuelo.getAvion().getMatricula(), vuelo.getAvion().getModelo(), silla.getPosicion(), tipoSilla);
        }
    }

    @Override
    public String toCSV() {
        if (silla instanceof SillaEjecutiva) {
            SillaEjecutiva silla = (SillaEjecutiva) this.silla;
            return String.format("%s;%.1f;%s;%s;%s;%b;%b;%s;%s%n", id, vuelo.getTrayecto().getCosto(),
                    usuario.getIdentificacion(), vuelo.getId(), silla.getId(), silla.getDisponible(), checkIn,
                    silla.getMenu(), silla.getLicor());
        } else {
            return String.format("%s;%.1f;%s;%s;%s;%b;%b%n", id, vuelo.getTrayecto().getCosto(),
                    usuario.getIdentificacion(), vuelo.getId(), silla.getId(), silla.getDisponible(), checkIn);
        }
    }

}
