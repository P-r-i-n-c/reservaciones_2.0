package edu.prog2.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.json.JSONObject;

import edu.prog2.helpers.Utils;

public class Vuelo implements IFormatCSV {

    private String id;
    private LocalDateTime fechaHora; // fecha y hora del vuelo
    private Trayecto trayecto;
    private Estado estado;
    private Avion avion;

    public Vuelo(String id, LocalDateTime fechaHora, Trayecto trayecto, Avion avion) {
        this.id = id;
        this.fechaHora = fechaHora.truncatedTo(ChronoUnit.MINUTES);
        this.trayecto = new Trayecto(trayecto); // composición
        this.avion = avion; // agregación
        this.estado = Estado.PROGRAMADO;
    }

    public Vuelo() {
        this(Utils.getRandomKey(5), LocalDateTime.now(), new Trayecto(), new Avion());
    }

    public Vuelo(Vuelo v) {
        this(v.id, v.fechaHora, v.trayecto, v.avion);
    }

    public Vuelo(String id) {
        this(id, LocalDateTime.now(), new Trayecto(), new Avion());
    }

    public String strFechaHora() {
        return Utils.strDateTime(fechaHora);
    }

    public String strDuracion() {
        Duration duracion = Duration.between(fechaHora, fechaHora.plus(trayecto.getDuracion()));
        long horas = duracion.toHours();
        long minutos = duracion.minusHours(horas).toMinutes();
        return horas + "h " + minutos + "m";
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
        this.fechaHora = fechaHora.truncatedTo(ChronoUnit.MINUTES);
    }

    public Trayecto getTrayecto() {
        return trayecto;
    }

    public void setTrayecto(Trayecto trayecto) {
        this.trayecto = trayecto;
    }

    public Avion getAvion() {
        return avion;
    }

    public void setAvion(Avion avion) {
        this.avion = avion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return String.format(
                "%-5s %-16s %-14s %-11s %-13s %-16s %-18s %-10s%n", id, strFechaHora(), avion.getMatricula(),
                trayecto.getOrigen(),
                trayecto.getDestino(), strDuracion(), trayecto.getCosto(), estado);
    }

    @Override
    public String toCSV() {
        return String.format(
                "%s;%s;%s;%s;%s%n", id, fechaHora, trayecto.getId(), avion.getMatricula(), estado);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Vuelo)) {
            return false;
        }

        Vuelo vuelo = (Vuelo) obj;
        return this.id.equals(vuelo.id) ||
                this.fechaHora.equals(vuelo.fechaHora) &&
                        this.trayecto.equals(vuelo.trayecto) &&
                        this.avion.equals(vuelo.avion);
    }

    public Vuelo(JSONObject json) {
        this(json.getString("id"),
                LocalDateTime.parse(json.getString("fechaHora")),
                new Trayecto(json.getJSONObject("trayecto")),
                new Avion(json.getJSONObject("avion")));
    }

    public JSONObject toJSONObject() {
        return new JSONObject(this);
    }

}