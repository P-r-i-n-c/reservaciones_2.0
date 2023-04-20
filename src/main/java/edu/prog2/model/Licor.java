package edu.prog2.model;

public enum Licor {

    WHISKEY("Whiskey"),
    OPORTO("Oporto"),
    VINO("Vino"),
    NINGUNO("Ninguno");

    private String value;

    private Licor(String value) {
        this.value = value;
    }

    public static Licor getEnum(String value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        for (Licor v : values()) {
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
