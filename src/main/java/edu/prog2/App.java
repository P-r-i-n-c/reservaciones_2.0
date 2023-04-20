package edu.prog2;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import edu.prog2.helpers.Keyboard;
import edu.prog2.helpers.Utils;
import edu.prog2.model.Avion;
import edu.prog2.model.Licor;
import edu.prog2.model.Menu;
import edu.prog2.model.Reserva;
import edu.prog2.model.Silla;
import edu.prog2.model.SillaEjecutiva;
import edu.prog2.model.TipoUsuario;
import edu.prog2.model.Trayecto;
import edu.prog2.model.Usuario;
import edu.prog2.model.Vuelo;
import edu.prog2.services.AvionesService;
import edu.prog2.services.ReservasService;
import edu.prog2.services.SillasService;
import edu.prog2.services.TrayectosService;
import edu.prog2.services.UsuariosService;
import edu.prog2.services.VuelosService;

import static spark.Spark.*;

public class App {

    static UsuariosService usuarios;
    static TrayectosService trayectos;
    static VuelosService vuelos;
    static AvionesService aviones;
    static SillasService sillas;
    static ReservasService reservas;

    public static void main(String[] args) {
        menu();
    }

    private static void menu() {

        try {
            inicializar();
        } catch (Exception e) {
            e.printStackTrace();
        }

        do {
            try {
                int opcion = leerOpcion();
                switch (opcion) {
                    case 1:
                        crearUsuarios();
                        break;
                    case 2:
                        crearAviones();
                        break;
                    case 3:
                        crearTrayectos();
                        break;
                    case 4:
                        crearVuelos();
                        break;
                    case 5:
                        crearVuelo();
                        break;
                    case 6:
                        listarUsuarios();
                        break;
                    case 7:
                        listarAviones();
                        break;
                    case 8:
                        listarTrayectos();
                        break;
                    case 9:
                        listarVuelos();
                        break;
                    case 10:
                        removerSilla();
                        break;
                    case 11:
                        buscarUsuarioPorIndice();
                        break;
                    case 12:
                        buscarUsuarioPorId();
                        break;
                    case 13:
                        buscarUsuarioPorIndice2();
                        break;
                    case 14:
                        buscarUsuarioPorId2();
                        System.out.println(usuarios.getJSONArray().toString(2));
                        break;
                    case 15:
                        System.out.println(sillas.aircraftWithNumberSeats().toString(2));
                        break;
                    case 16:
                        actualizarUsuario();
                        break;
                    case 17:
                        removerUsuario();
                        break;
                    case 18:
                        listarSillas(); // pt 54
                        break;
                    case 19:
                        listarVuelos();
                        break;
                    case 20:
                        listarVuelosCiudades();
                    case 21:
                        pruebaUpdateVuelos();
                        break;
                    case 22:
                        crearReservas();
                        break;
                    case 99:
                        get("/hello", (req, res) -> usuarios.getJSONArray().toString(2));
                        break;
                    // … más casos …
                    // …
                    case 0:
                        salir();
                        break;
                    default:
                        System.out.println("Opción inválida");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (true);
    }

    private static void inicializar() throws Exception {
        System.out.print("\033[H\033[2J"); // limpiar la consola
        Locale.setDefault(new Locale("es_CO"));
        usuarios = new UsuariosService(); // crear la instancia de usuarios
        aviones = new AvionesService();
        trayectos = new TrayectosService();
        vuelos = new VuelosService(trayectos, aviones);
        sillas = new SillasService(aviones);
        reservas = new ReservasService(usuarios, vuelos, sillas);

    }

    private static void salir() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.exit(0);
    }

    static int leerOpcion() {
        String opciones = String.format(
                "\n%sMenú de opciones:%s\n", Utils.GREEN, Utils.RESET)
                + "  1 - Crear usuarios                 17 - Remover usuario                      \n"
                + "  2 - Crear aviones con sillas       18 - Listar sillas                        \n"
                + "  3 - Crear trayectos                19 - Buscar usuario por indice 2          \n"
                + "  4 - Crear vuelos                   20 - Buscar usuario por id 2              \n"
                + "  5 - Crear vuelo                    21 - Prueba update vuelos                 \n"
                + "  6 - Listar usuarios                22 - Crear Reservas                       \n"
                + "  7 - Listar aviones con sillas         - ---------------------------          \n"
                + "  8 - Listar trayectos                  - ---------------------------          \n"
                + "  9 - Listar vuelos                     - ---------------------------          \n"
                + " 10 - Remover silla                     - ---------------------------          \n"
                + " 11 - Buscar usuario por indice         - ---------------------------          \n"
                + " 12 - Buscar usuario por id             - ---------------------------          \n"
                + " 13 - Buscar usuario por indice 2       - ---------------------------          \n"
                + " 14 - Buscar usuario por id 2           - ---------------------------          \n"
                + " 15 - aircraftNumberWithSeats           - ---------------------------          \n"
                + " 16 - Actualizar usuario                - ---------------------------          \n"

                + String.format("  %s0 - Salir%s\n", Utils.RED, Utils.RESET)
                + String.format(
                        "\nElija una opción (%s0 para salir%s) > ",
                        Utils.RED, Utils.RESET);
        int opcion = Keyboard.readInt(opciones);
        System.out.println();
        return opcion;
    }

    private static void listarSillas() throws Exception {
        String matricula = Keyboard.readString("Listar sillas del avion con matricula: ");
        System.out.println(sillas.select(matricula).toString(2));
    }

    private static void pruebaUpdateVuelos() throws Exception {
        vuelos.update();
    }

    private static void listarVuelosCiudades() throws Exception {
        String s = "fechaHora=2023-03-01T00:00&origen=Manizales&destino=Medellin";
        System.out.println(vuelos.select(s).toString(2));
    }

    private static void crearUsuarios() throws Exception {
        Usuario usuario;

        System.out.println("Ingreso de usuarios\n");

        do {
            String identificacion = Keyboard.readString("Identificacion (Intro termina): ");

            if (identificacion.length() == 0) {
                return;
            }

            String nombres = Keyboard.readString(1, 25, "Nombres: ");
            String apellidos = Keyboard.readString("Apellidos: ");

            TipoUsuario tipoUsuario = Keyboard.readEnum(TipoUsuario.class,
                    "Seleccione un tipo de usuario (CLIENTE, AUXILIAR o ADMINISTRADOR): ");

            String contraseña = Keyboard.readString("cree su contraseña: ");

            usuario = new Usuario(identificacion, nombres, apellidos, tipoUsuario, contraseña);
            boolean ok = usuarios.add(usuario);

            if (ok) {
                System.out.printf("Se agregó el usuario con identificacion: %s%n%n", identificacion);
            }

        } while (true);

    }

    private static void listarUsuarios() {
        System.out.println("-".repeat(69));
        System.out.println("ID        NOMBRES        APELLIDOS        TIPO        CONTRASEÑA");
        System.out.println("-".repeat(69));

        for (Usuario usuario : usuarios.getList()) {
            System.out.println(usuario);
        }
        System.out.println("-".repeat(69));
    }

    private static void crearTrayectos() throws Exception {

        Trayecto trayecto;
        System.out.println("Ingreso de trayecto\n");

        do {
            String id = Utils.getRandomKey(4);

            String origen = Keyboard.readString(2, 25, "Origen(Intro termina): ");

            if (origen.length() == 0) {
                return;
            }

            String destino = Keyboard.readString(2, 25, "Destino: ");

            double costo = Keyboard.readDouble("Precio: ");

            Duration duracion = Keyboard.readDuration("Duracion (HH:MM)");

            trayecto = new Trayecto(id, origen, destino, costo, duracion);
            boolean ok = trayectos.add(trayecto);

            if (ok) {
                System.out.printf(
                        "Se agregó el trayecto con id: %s, origen %s, destino %s, costo %.2f y duracion %s%n",
                        id, origen, destino, costo, trayecto.duracionStr());
            }

        } while (true);
    }

    private static void listarAviones() {
        System.out.println("-".repeat(50));
        System.out.println("MATRICULA        MODELO");
        System.out.println("-".repeat(50));

        for (Avion avion : vuelos.getAviones().getList()) {
            System.out.println(avion);
        }
        System.out.println("-".repeat(50));
    }

    private static void listarTrayectos() {
        System.out.println("-".repeat(50));
        System.out.println("ORIGEN        DESTINO        PRECIO        DURACION");
        System.out.println("-".repeat(50));

        for (Trayecto trayecto : trayectos.getList()) {
            System.out.println(trayecto);
        }
        System.out.println("-".repeat(50));
    }

    private static Avion elegirAvion() {
        Avion avion;
        int i;
        int size = aviones.getList().size();

        do {
            System.out.println("Aviones elegibles: ");

            for (i = 0; i < size; i++) {
                avion = aviones.get(i);
                System.out.printf(
                        "%2d - %s %s%n", (i + 1), avion.getMatricula(), avion.getModelo());
            }

            i = Keyboard.readInt(String.format(
                    "Ingrese un índice entre 1 y %d (0 - No elegir): ", size));
            System.out.println();

        } while (i < 0 || i > size);

        return (i == 0) ? null : aviones.get(i - 1);
    }

    private static Trayecto elegirTrayecto() {
        Trayecto trayecto;
        int i;
        int size = trayectos.getList().size();

        do {
            System.out.println("Trayectos elegibles: ");

            for (i = 0; i < size; i++) {
                trayecto = trayectos.get(i);
                System.out.printf(
                        "%2d - %s - %s $%.2f%n", (i + 1), trayecto.getOrigen(), trayecto.getDestino(),
                        trayecto.getCosto());
            }

            i = Keyboard.readInt(String.format(
                    "Ingrese un índice entre 1 y %d (0 - No elegir): ", size));
            System.out.println();

        } while (i < 0 || i > size);

        return (i == 0) ? null : trayectos.get(i - 1);
    }

    private static void crearVuelos() throws Exception {
        System.out.println("Ingreso de vuelos\nElija 0 en trayectos o aviones para terminar\n");

        do {
            String id = Utils.getRandomKey(5);

            Trayecto trayecto = elegirTrayecto();
            if (trayecto == null) {
                return;
            }

            Avion avion = elegirAvion();
            if (avion == null) {
                return;
            }

            LocalDateTime desde = LocalDateTime.now();
            LocalDateTime hasta = desde.plusYears(1);

            String mensaje = String.format(
                    "Fecha y hora entre %s y %s: ", Utils.strDateTime(desde), Utils.strDateTime(hasta));
            LocalDateTime fechaHora = Keyboard.readDateTime(desde, hasta, mensaje);

            boolean ok = vuelos.add(new Vuelo(id, fechaHora, trayecto, avion));

            if (ok) {
                System.out.printf("\nSe agregó el vuelo %s%n%n", id);
            }

        } while (true);

    }

    private static void listarVuelos() {
        System.out.println("-".repeat(110));
        System.out.println(
                "ID    FECHA Y HORA        AVION        ORIGEN        DESTINO        TIEMPO        VALOR        CANC.");
        System.out.println("-".repeat(110));

        for (Vuelo vuelo : vuelos.getList()) {
            System.out.println(vuelo);
        }
        System.out.println("-".repeat(110));
    }

    private static void buscarUsuarioPorId() throws Exception {
        String id = Keyboard.readString("Identificación del usuario: ");
        Usuario usuario = usuarios.get(new Usuario(id));
        System.out.println(usuario.toJSONObject().toString(2));
    }

    private static void buscarUsuarioPorIndice() {
        int i = Keyboard.readInt("ïndice de usuario a buscar: ");
        Usuario usuario = usuarios.get(i);
        System.out.println(usuario.toJSONObject().toString(2));
    }

    private static void buscarUsuarioPorIndice2() {
        int i = Keyboard.readInt("Índice del usuario a buscar: ");
        JSONObject usuario = usuarios.getJSON(i);
        System.out.println(usuario.toString(2));
    }

    private static void buscarUsuarioPorId2() throws Exception {
        String id = Keyboard.readString("Identificación del usuario: ");
        JSONObject usuario = usuarios.get(id);
        System.out.println(usuario.toString(2));
    }

    private static void actualizarUsuario() throws Exception {
        String id = Keyboard.readString("Identificación del usuario a modificar: ");

        JSONObject json = new JSONObject()
                .put("identificacion", "no interesa")
                .put("nombres", Keyboard.readString("Nuevo nombre: "))
                .put("apellidos", Keyboard.readString("Nuevos apellidos: "))
                .put("tipoUsuario", Keyboard.readEnum(TipoUsuario.class,
                        "Seleccione un tipo de usuario (CLIENTE, AUXILIAR o ADMINISTRADOR): "))
                .put("contraseña", Keyboard.readString("Nueva contraseña: "));

        System.out.printf("Nuevos datos del usuario %s:%n", id);
        System.out.println(usuarios.set(id, json).toString(2));
    }

    private static void removerUsuario() throws Exception {
        String id = Keyboard.readString("Identificación del usuario a eliminar: ");
        usuarios.remove(id);
        System.out.println("Usuario eliminado");
    }

    private static void crearVuelo() throws Exception {
        LocalDateTime fechaHora = LocalDateTime.now();
        vuelos.add(
                new JSONObject()
                        .put("fechaHora", fechaHora.toString())
                        .put("idTrayecto", "8KM6S")
                        .put("matriculaAvion", "HK3005"));
    }

    private static void crearAviones() throws Exception {
        System.out.println("Ingreso de aviones\n");

        do {
            String matricula = Keyboard.readString("Matrícula (Intro termina): ");

            if (matricula.length() == 0) {
                return;
            }

            String modelo = Keyboard.readString(3, 25, "Modelo: ");
            int totalSillas = Keyboard.readInt("Cuántas sillas: ");
            int filasEjecutivas = Keyboard.readInt("Cuántas filas de económicas: ");

            boolean ok = aviones.add(new Avion(matricula, modelo));

            System.out.println(sillas.toString());
            if (ok) {
                System.out.printf("Se agregó el avión con matrícula: %s%n%n", matricula);
            }

            if (ok) {
                sillas.create(matricula, filasEjecutivas, totalSillas);
                System.out.printf("Se agregaron las sillas al avión %s%n%n", matricula);
            }
        } while (true);
    }

    private static void removerSilla() throws Exception {
        String id = Keyboard.readString("Matricula del avion a eliminar: ");
        aviones.remove(id);
        sillas.removeAll(id);
        System.out.println("avion eliminado");
    }

    private static List<Silla> getAvailableSeats(Vuelo vuelo) throws Exception {

        List<Silla> disponibles = reservas.seatsAvailableOnFlight(vuelo);

        System.out.printf(
                "%nListado de sillas disponibles en el vuelo %s: %s - %s - %s",
                vuelo.getAvion().getMatricula(), vuelo.getTrayecto().getOrigen(),
                vuelo.getTrayecto().getDestino(), vuelo.strFechaHora());

        for (int i = 0; i < disponibles.size(); i++) {
            if (i % 10 == 0) {
                System.out.println();
            }

            Silla s = disponibles.get(i);
            char tipo = s instanceof SillaEjecutiva ? 'E' : 'S';
            System.out.printf(
                    "%s%4d%s-%s-%c", Utils.CYAN, i + 1, Utils.RESET, s.getPosicion(), tipo);
        }

        System.out.println("\n");
        return disponibles;
    }

    private static Silla chooseAvailableSeat(Vuelo vuelo) throws Exception {
        List<Silla> sillas = getAvailableSeats(vuelo);

        int i;
        int max = sillas.size();
        do {
            i = Keyboard.readInt(String.format("Ingresa un numero entre 1 y %d: ", max));

            if (i < 1 || i > max) {
                System.out.println("El numero ingresado esta fuera de rango ");
            }
        } while (i < 1 || i > max);

        Silla silla = sillas.get(i - 1);
        silla.setDisponible(false); // cambio de disponibilidad a silla original

        if (silla instanceof SillaEjecutiva) {

            SillaEjecutiva aux = (SillaEjecutiva) silla;
            aux.setMenu(Keyboard.readEnum(Menu.class, "Opciones de menu"));
            aux.setLicor(Keyboard.readEnum(Licor.class, "Opciones de licor"));
            silla = aux;
        }

        return silla;
    }

    private static Vuelo elegirVuelo() {
        Vuelo vuelo;
        int i;
        int size = vuelos.getList().size();
        Avion avion;
        Trayecto trayecto;

        do {
            System.out.println("Vuelos elegibles: ");

            for (i = 0; i < size; i++) {
                vuelo = vuelos.get(i);
                avion = aviones.get(i);
                trayecto = trayectos.get(i);
                System.out.printf(
                        "%2d - %s - %s - %s - %s ($%.0f)%n", (i + 1), vuelo.strFechaHora(), avion.getMatricula(),
                        trayecto.getOrigen(), trayecto.getDestino(), trayecto.getCosto());
            }

            i = Keyboard.readInt(String.format(
                    "Ingrese un índice entre 1 y %d (0 - No elegir): ", size));
            System.out.println();

        } while (i < 0 || i > size);

        return (i == 0) ? null : vuelos.get(i - 1);
    }

    private static Usuario elegirUsuario() {
        Usuario usuario;
        int i;
        int size = usuarios.getList().size();

        do {
            System.out.println("Usuarios elegibles: ");

            for (i = 0; i < size; i++) {
                usuario = usuarios.get(i);
                System.out.printf(
                        "%2d. %s - %s - %s%n", (i + 1), usuario.getNombres(), usuario.getApellidos(),
                        usuario.getTipoUsuario());
            }

            i = Keyboard.readInt(String.format(
                    "Ingrese un índice entre 1 y %d (0 - No elegir): ", size));
            System.out.println();

        } while (i < 0 || i > size);

        return (i == 0) ? null : usuarios.get(i - 1);
    }

    private static void crearReservas() throws Exception {
        do {
            Usuario usuario = elegirUsuario();

            if (usuario == null) {
                return;
            }
            Vuelo vuelo = elegirVuelo();

            if (vuelo == null) {
                System.out.println("reserva descartada");
                break;
            }

            Silla silla = chooseAvailableSeat(vuelo);

            String id = Utils.getRandomKey(5);
            Reserva reserva = new Reserva(id, usuario, vuelo, silla);
            reserva.getSilla().setDisponible(false); // cambio de disponibilidad a copia
            reservas.add(reserva);
            System.out.println("\nSe ha registrado la reserva " + id);
        } while (true);
    }

    
 
 
}