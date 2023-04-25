package edu.prog2.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.prog2.helpers.Utils;
import edu.prog2.model.TipoUsuario;
import edu.prog2.model.Usuario;

public class UsuariosService {

    private List<Usuario> usuarios;
    private String fileName;

    public UsuariosService() throws Exception {
        usuarios = new ArrayList<>();
        fileName = Utils.PATH + "usuarios";

        if (Utils.fileExists(fileName + ".csv")) {
            loadCSV();
        } else if (Utils.fileExists(fileName + ".json")) {
            loadJSON();
        } else {
            System.out.println("Aún no se ha creado un archivo: " + fileName);
        }
    }

    public List<Usuario> loadCSV() throws Exception {
        String text = Utils.readText(fileName + ".csv");

        try (Scanner sc = new Scanner(text).useDelimiter(";|[\n]+|[\r\n]+")) {
            while (sc.hasNext()) {
                String identificacion = sc.next();
                String nombres = sc.next();
                String apellidos = sc.next();
                TipoUsuario tipoUsuario = TipoUsuario.getEnum(sc.next());
                String contrasenia = sc.next();

                usuarios.add(new Usuario(identificacion, nombres, apellidos, tipoUsuario, contrasenia));
            }
        }

        return usuarios;
    }

    public List<Usuario> loadJSON() throws Exception {
        usuarios = new ArrayList<>();

        String data = Utils.readText(fileName + ".json");
        JSONArray jsonArr = new JSONArray(data);

        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            usuarios.add(new Usuario(jsonObj));
        }

        return usuarios;
    }

    public boolean contains(Usuario usuario) {
        return usuarios.contains(usuario);
    }

    public boolean add(Usuario usuario) throws Exception {
        if (contains(usuario)) {
            throw new IOException(String.format(
                    "No agregado, el usuario %s ya existe", usuario.getIdentificacion()));
        }
        // usuario.setPassword(Utils.MD5(usuario.getPassword())); 🧐
        boolean ok = usuarios.add(usuario);
        Utils.writeData(usuarios, fileName);
        return ok;
    }

    public List<Usuario> getList() {
        return usuarios;
    }

    public Usuario get(int index) {
        return usuarios.get(index);
    }

    public Usuario get(Usuario usuario) {
        int index = usuarios.indexOf(usuario);
        usuario = index > -1 ? usuarios.get(index) : null;

        if (usuario == null) {
            throw new NullPointerException("No se encontró el usuario ");
        }

        return usuario;
    }

    public JSONObject getJSON(int index) {
        return usuarios.get(index).toJSONObject();
    }

    public JSONObject getJSON(Usuario usuario) {
        int index = usuarios.indexOf(usuario);
        return index > -1 ? getJSON(index) : null;
    }

    public JSONObject get(String id) throws Exception {
        return getJSON(new Usuario(id));
    }

    public JSONArray getJSONArray() throws Exception {
        return new JSONArray(Utils.readText(fileName + ".json"));
    }

    public JSONObject set(String identificacion, JSONObject json) throws Exception {
        Usuario usuario = get(new Usuario(json));
        usuario.setIdentificacion(identificacion);
        int index = usuarios.indexOf(usuario);

        if (index < 0) {
            throw new NullPointerException("No se encontró el usuario " + identificacion);
        }

        usuarios.set(index, usuario);
        Utils.writeData(usuarios, fileName);
        return new JSONObject(usuario);
    }

    public void remove(String identificacion) throws Exception {
        Usuario usuario = new Usuario(identificacion);
        // – pendiente aquí una verificación importante…
        if (!usuarios.remove(usuario)) {
            throw new Exception(
                    String.format(
                            "No se encontró el usuario con identificación %s", identificacion));
        }
        Utils.writeData(usuarios, fileName);
    }

    public void update() throws Exception {
        usuarios = new ArrayList<>();
        loadCSV();
        Utils.writeJSON(usuarios, fileName + ".json");
    }
}
