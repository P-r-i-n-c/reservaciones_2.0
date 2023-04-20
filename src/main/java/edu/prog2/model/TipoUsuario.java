package edu.prog2.model;


public enum TipoUsuario {

    CLIENTE("CLIENTE"),
    AUXILIAR("AUXILIAR"),
    ADMINISTRADOR("ADMINISTRADOR");

    private String value;

    private TipoUsuario(String value) {
        this.value = value;
    }

    public static TipoUsuario getEnum(String value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        for (TipoUsuario v : values()) {
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