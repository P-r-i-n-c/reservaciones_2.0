package edu.prog2.model;

import org.json.JSONObject;

import edu.prog2.helpers.Utils;

public class Silla implements IFormatCSV {
    protected String id;
    protected int fila;
    protected char columna;
    protected boolean disponible;
    protected Avion avion;
    protected Ubicacion ubicacion;

    public Silla(String id, Avion avion, int fila, char columna) {
        this.id = id;
        this.fila = fila;
        this.columna = columna;
        this.avion = avion;
        this.disponible = true;

        if (columna == 'A' || columna == 'F') {
            this.ubicacion = Ubicacion.VENTANA;
        }

        if (columna == 'B' || columna == 'E') {
            this.ubicacion = Ubicacion.CENTRAL;
        }

        if (columna == 'C' || columna == 'D') {
            this.ubicacion = Ubicacion.PASILLO;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public char getColumna() {
        return columna;
    }

    public void setColumna(char columna) {
        this.columna = columna;
    }

    public boolean getDisponible() {
        return this.disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public Avion getAvion() {
        return avion;
    }

    public void setAvion(Avion avion) {
        this.avion = avion;
    }

    public String getPosicion() {
        return String.format("%02d%c", fila, columna);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", ubicacion, columna, getDisponible());
    }

    @Override
    public String toCSV() {
        return String.format("%s;%s;%s;%s;%s;%s%n", id, avion.getMatricula(), fila, columna, ubicacion,
                getDisponible());
    }

    public Silla() {
        this(Utils.getRandomKey(5), new Avion(), 1, 'A');
    }

    public Silla(Silla s) {
        this(s.id, s.avion, s.fila, s.columna);
    }

    public Silla(String id) {
        this(id, new Avion(), 0, '0'); // Valores por defecto para fila y columna
    }

    public Silla(JSONObject json) {
        this(
                json.getString("id"),
                new Avion(json.getJSONObject("avion")),
                json.getInt("fila"),
                json.getString("columna").charAt(0));

    }

    public JSONObject toJSONObject() {
        return new JSONObject(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Silla)) {
            return false;
        }

        Silla silla = (Silla) obj;
        return this.id.equals(silla.id)
                || this.avion.equals(silla.getAvion()) && this.getPosicion().equals(silla.getPosicion());
    }
}