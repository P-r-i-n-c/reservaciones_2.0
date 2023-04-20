package edu.prog2.model;

import java.time.Duration;

import org.json.JSONObject;

public class Trayecto implements IFormatCSV {

    private String id;
    private String origen;
    private String destino;
    private double costo;
    private Duration duracion;

    public Trayecto(String id, String origen, String destino, double costo, Duration duracion) {
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.costo = costo;
        this.duracion = duracion;
    }

    public Trayecto() {
        this("", "", "", 0.0, Duration.ZERO);
    }

    public Trayecto(Trayecto t) {
        this(t.id, t.origen, t.destino, t.costo, t.duracion);
    }

    public Trayecto(String id) {
        this(id, "", "", 0.0, Duration.ZERO); // tipo de usuario cliente predeterminado

    }

    public String duracionStr() {
        long horas = duracion.toHours();
        long minutos = duracion.toMinutes() % 60;
        String duracionStr = String.format("%02d:%02d", horas, minutos);

        return duracionStr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public Duration getDuracion() {
        return duracion;
    }

    public void setDuracion(Duration duracion) {
        this.duracion = duracion;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Trayecto)) {
            return false;
        }

        Trayecto trayecto = (Trayecto) obj;
        return this.id.equals(trayecto.id) ||
                this.origen.equals(trayecto.origen) &&
                        this.destino.equals(trayecto.destino);
    }

    @Override
    public String toString() {
        return String.format("%-13s %-14s %-13.2f %-9s ", origen, destino, costo, duracionStr());
    }

    @Override
    public String toCSV() {
        return String.format("%s;%s;%s;%s;%s%n", id, origen, destino, costo, duracion);
    }

    public Trayecto(JSONObject json) {
        this(
                json.getString("id"),
                json.getString("origen"),
                json.getString("destino"),
                json.getDouble("costo"),
                Duration.parse(json.getString("duracion"))

        );
    }

    public JSONObject toJSONObject() {
        return new JSONObject(this);
    }
}
