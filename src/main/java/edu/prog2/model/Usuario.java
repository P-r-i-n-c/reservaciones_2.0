
package edu.prog2.model;

import org.json.JSONException;
import org.json.JSONObject;

import edu.prog2.helpers.Utils;

public class Usuario implements IFormatCSV {
    private String identificacion;
    private String nombres;
    private String apellidos;
    private TipoUsuario tipoUsuario;
    private String contraseña;

    public Usuario(String identificacion, String nombres, String apellidos, TipoUsuario tipoUsuario,
            String contraseña) throws Exception {
        this.identificacion = identificacion;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.tipoUsuario = tipoUsuario;
        int num = 123;
        String contraseñaString = String.valueOf(num);
        this.contraseña = Utils.MD5(contraseñaString);

    }

    public Usuario(Usuario u) throws Exception {
        this(u.identificacion, u.nombres, u.apellidos, u.tipoUsuario, u.contraseña);
    }

    public Usuario(String identificacion) throws Exception {
        this(identificacion, "", "", TipoUsuario.CLIENTE, ""); // tipo de usuario cliente predeterminado
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Usuario)) {
            return false;
        }

        Usuario usuario = (Usuario) obj;
        return this.identificacion.equals(usuario.identificacion);
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    @Override
    public String toString() {
        return String.format("%-10s%-15s%-15s%-15s %-15s", identificacion, nombres, apellidos, tipoUsuario, contraseña);
    }

    @Override
    public String toCSV() {
        return String.format("%s;%s;%s;%s;%s%n",
                identificacion, nombres, apellidos, tipoUsuario, contraseña);
    }

    public Usuario(JSONObject json) throws JSONException, Exception {
        this(json.getString(
                "identificacion"),
                json.getString("nombres"),
                json.getString("apellidos"),
                json.getEnum(TipoUsuario.class, "tipoUsuario"),
                json.getString("contraseña"));
    }

    public Usuario() throws Exception {
        this("", "", "", TipoUsuario.CLIENTE, "");
    }

    public JSONObject toJSONObject() {
        return new JSONObject(this);
    }

}
