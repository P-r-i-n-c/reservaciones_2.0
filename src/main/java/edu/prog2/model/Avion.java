package edu.prog2.model;

import org.json.JSONObject;

public class Avion implements IFormatCSV {
    String matricula;
    String modelo;

    public Avion() {
        this("", "");
    }

    public Avion(String matricula, String modelo) {
        this.matricula = matricula;
        this.modelo = modelo;
    }

    public Avion(Avion a) {
        this(a.matricula, a.modelo);
    }

    public Avion(String matricula) {
        this(matricula, "");
    }

    public Avion(JSONObject json) {
        this(json.getString("matricula"), json.getString("modelo"));
    }

    public String getMatricula() {
        return matricula;
    }

    public String getModelo() {
        return modelo;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public JSONObject toJSONObject() {
        return new JSONObject(this);
    }

    @Override
    public String toCSV() {
        return String.format("%s;%s%n", matricula, modelo);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Avion)) {
            return false;
        }

        Avion avion = (Avion) obj;
        return this.matricula.equals(avion.matricula);
    }

    @Override
    public String toString() {
        return String.format("%-17s%-20s", matricula, modelo);
    }

    public boolean add(Avion avion) {
        return false;
    }

    public Object getId() {
        return null;
    }

}