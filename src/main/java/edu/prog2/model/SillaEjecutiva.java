package edu.prog2.model;

import org.json.JSONObject;


public class SillaEjecutiva extends Silla {

    protected Menu menu;
    protected Licor licor;

    public SillaEjecutiva(String id, Avion avion, int fila, char columna, Menu menu, Licor licor) {
        super(id, avion, fila, columna);
        this.menu = menu;
        this.licor = licor;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Licor getLicor() {
        return licor;
    }

    public void setLicor(Licor licor) {
        this.licor = licor;
    }

    public SillaEjecutiva(SillaEjecutiva silla) {
        this(silla.getId(), silla.getAvion(), silla.getFila(), silla.getColumna(), silla.getMenu(),
                silla.getLicor());
        
    }

    public SillaEjecutiva(JSONObject json) {
        this(
                json.getString("id"),
                new Avion(json.getJSONObject("avion")),
                json.getInt("fila"),
                json.getString("columna").charAt(0),
                json.getEnum(Menu.class, "menu"),
                json.getEnum(Licor.class, "licor"));

    }

    public JSONObject toJSONObject() {
        return new JSONObject(this);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", super.toString(), menu, licor);
    }

    @Override
    public String toCSV() {
        return String.format("%s;%s;%s;%s;%s;%s;%s;%s%n",
                id, avion.getMatricula(), fila, columna, ubicacion, getDisponible(), menu, licor);
    }

}