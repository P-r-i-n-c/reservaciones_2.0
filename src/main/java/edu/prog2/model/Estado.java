package edu.prog2.model;

public enum Estado {
    A_TIEMPO("A Tiempo"),
    RETRASADO("Retrasado"),
    CANCELADO("Cancelado"),
    PROGRAMADO("Programado");

    private String value;

    private Estado(String value) {
        this.value = value;
    }

    public static Estado getEnum(String value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        for (Estado v : values()) {
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
