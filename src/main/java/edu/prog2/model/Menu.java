package edu.prog2.model;

public enum Menu {

    POLLO_A_LA_NARANJA("Pollo a la naranja"),
    VEGETARIANO("Vegetariano"),
    FILETE_DE_PESCADO("Filete de pescado"),
    INDEFINIDO("Indefinido");

    private String value;

    private Menu(String value) {
        this.value = value;
    }

    public static Menu getEnum(String value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        for (Menu v : values()) {
            if (value.equalsIgnoreCase(v.value)) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        return value;
    }

}
