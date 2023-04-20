package edu.prog2.helpers;

import java.io.Console;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.EnumSet;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Keyboard {

    private static Console con = System.console();
    public static Scanner sc = new Scanner(con.reader())
            .useDelimiter("[\n]+|[\r\n]+");

    public static String readString(String message) {
        message = String.format("%s%s%s", Utils.BLUE, message, Utils.RESET);
        System.out.print(message);
        return sc.nextLine();

    }

    public static int readInt(String message) {
        message = String.format("%s%s%s", Utils.BLUE, message, Utils.RESET);
        boolean ok;
        int value = Integer.MIN_VALUE;
        System.out.print(message);

        do {
            try {
                ok = true;
                value = sc.nextInt();
            } catch (InputMismatchException e) {
                ok = false;
                System.out.print(">> Valor erróneo. " + message);
            } finally {
                sc.nextLine();
            }
        } while (!ok);

        return value;
    }

    public static int readInt(int from, int to, String mensaje) {
        int value;
        int tmp = Math.min(from, to);
        if (tmp == to) {
            to = from;
            from = tmp;
        }

        do {
            value = readInt(mensaje);
            if (value == 0) {

                break;
            }
            if (value < from || value > to) {
                System.out.printf("%sRango inválido. %s", Utils.RED, Utils.RESET);
            }
        } while (value < from || value > to);
        return value;
    }

    public static String readString(int from, int to, String message) {

        String value;
        int tmp = Math.min(from, to);

        if (tmp == to) {
            to = from;
            from = tmp;
        }
        do {
            value = readString(message);
            if (value.length() == 0) {

                break;
            }
            if (value.length() < from || value.length() > to) {
                System.out.printf("%sRango inválido. %s", Utils.RED, Utils.RESET);
            }
        } while (value.length() < from || value.length() > to);

        return value;

    }

    public static long readLong(String message) {

        message = String.format("%s%s%s", Utils.BLUE, message, Utils.RESET);
        boolean ok;
        long value = Long.MIN_VALUE;
        System.out.print(message);

        do {
            try {
                ok = true;
                value = sc.nextInt();
            } catch (InputMismatchException e) {
                ok = false;
                System.out.print(">> Valor erróneo. " + message);
            } finally {
                sc.nextLong();
            }
        } while (!ok);

        return value;

    }

    public static long readLong(long from, long to, String mensaje) {

        long value;
        long tmp = Math.min(from, to);
        if (tmp == to) {
            to = from;
            from = tmp;
        }

        do {
            value = readLong(mensaje);
            if (value == 0) {

                break;
            }
            if (value < from || value > to) {
                System.out.printf("%sRango inválido. %s", Utils.RED, Utils.RESET);
            }
        } while (value < from || value > to);
        return value;
    }

    public static double readDouble(String message) {

        message = String.format("%s%s%s", Utils.BLUE, message, Utils.RESET);
        boolean ok;
        double value = Double.NaN;
        System.out.print(message);

        do {
            try {
                ok = true;
                value = sc.nextDouble();
            } catch (InputMismatchException e) {
                ok = false;
                System.out.print(">> Valor erróneo. " + message);
            } finally {
                sc.nextLine();
            }
        } while (!ok);

        return value;

    }

    public static double readDouble(double from, double to, String mensaje) {

        double value;
        double tmp = Math.min(from, to);
        if (tmp == to) {
            to = from;
            from = tmp;
        }

        do {
            value = readDouble(mensaje);
            if (value == 0) {

                break;
            }
            if (value < from || value > to) {
                System.out.printf("%sRango inválido. %s", Utils.RED, Utils.RESET);
            }
        } while (value < from || value > to);
        return value;
    }

    public static boolean readBoolean(String message) {

        message = String.format("%s%s%s", Utils.BLUE, message, Utils.RESET);
        boolean ok;
        boolean value = false;
        System.out.print(message);

        do {
            try {
                ok = true;
                String str = ' ' + sc.nextLine().toLowerCase().trim() + ' ';
                if (" si s true t yes y ".contains(str)) {
                    value = true;
                } else if (" no n false f not ".contains(str)) {
                    value = false;
                } else {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                ok = false;
                System.out.printf(
                        "%s>> Se esperaba [si|s|true|t|yes|y|no|not|n|false|f]%s %s",
                        Utils.RED, Utils.RESET, message);
            }
        } while (!ok);

        return value;
    }

    public static LocalDate readDate(String message) {

        message = String.format("%s%s%s", Utils.BLUE, message, Utils.RESET);
        boolean ok;
        LocalDate date = java.time.LocalDate.now();
        System.out.print(message);

        do {
            try {
                ok = true;
                String strDate = sc.nextLine().trim().toLowerCase();
                if (!"hoy|now".contains(strDate)) {
                    date = LocalDate.parse(strDate);
                }
            } catch (DateTimeParseException dtpe) {
                ok = false;
                System.out.printf(
                        ">> %sFecha errónea%s. %s", Utils.RED, Utils.RESET, message);
            }

        } while (!ok);

        return date;
    }

    public static LocalDateTime readDateTime(String message) {
        message = String.format("%s%s%s", Utils.BLUE, message, Utils.RESET);
        boolean ok;
        LocalDateTime dateTime = LocalDateTime.now();
        System.out.print(message);

        do {
            try {
                ok = true;
                String strDateTime = sc.nextLine().trim().toLowerCase();
                if (!"ahora|now".contains(strDateTime)) {
                    dateTime = LocalDateTime.parse(strDateTime.replace(" ", "T"));
                }
            } catch (DateTimeParseException dtpe) {
                ok = false;
                System.out.printf(
                        ">> %sFecha y hora errónea%s. %s", Utils.RED, Utils.RESET, message);
            }

        } while (!ok);

        return dateTime;
    }

    public static LocalDateTime readDateTime(LocalDateTime parse, LocalDateTime parse2, String message) {
        message = String.format("%s%s%s", Utils.BLUE, message, Utils.RESET);
        boolean ok;
        LocalDateTime dateTime = LocalDateTime.now();
        System.out.print(message);

        do {
            try {
                ok = true;
                String strDateTime = sc.nextLine().trim().toLowerCase();
                if (!"ahora|now".contains(strDateTime)) {
                    dateTime = LocalDateTime.parse(strDateTime.replace(" ", "T"));
                }
            } catch (DateTimeParseException dtpe) {
                ok = false;
                System.out.printf(
                        ">> %sFecha y hora errónea%s. %s", Utils.RED, Utils.RESET, message);
            }

        } while (!ok);

        return dateTime;
    }

    public static LocalDate readDate(String from, String to, String mensaje) {

        LocalDate value;
        LocalDate dateFrom = LocalDate.parse(from);
        LocalDate dateTo = LocalDate.parse(to);
        if (dateFrom.isAfter(dateTo)) {
            LocalDate newDateFrom = dateTo;
            dateTo = dateFrom;
            dateFrom = newDateFrom;
        }
        do {
            value = readDate(mensaje);
            if (value.isBefore(dateFrom) || value.isAfter(dateTo)) {
                System.out.printf("%sRango inválido. %s", Utils.RED, Utils.RESET);
            }
        } while (value.isBefore(dateFrom) || value.isAfter(dateTo));

        return value;

    }

    public static Duration toDuration(String strDuration) {
        String[] aDuration = strDuration.trim().split(":");
        if (aDuration.length != 2) {
            throw new IllegalArgumentException("Se esperaba HH:MM");
        }

        return Duration.parse(
                String.format("PT%sH%sM", aDuration[0].trim(), aDuration[1].trim()));
    }

    public static Duration readDuration(String message) {
        message = String.format("%s%s%s", Utils.BLUE, message, Utils.RESET);
        boolean ok;
        Duration duration = Duration.ZERO;
        System.out.print(message);

        do {
            try {
                ok = true;
                String strDuration = sc.nextLine();
                duration = toDuration(strDuration);
            } catch (Exception e) {
                ok = false;
                System.out.printf(
                        ">> %sDuración errónea%s. %s", Utils.RED, Utils.RESET, message);
            }

        } while (!ok);

        return duration;
    }

    public static Duration readDuration(String from, String to, String mensaje) {

        Duration value;
        int valFrom;
        int valTo;
        Duration inicio = toDuration(from);
        Duration fin = toDuration(to);

        int val = inicio.compareTo(fin);
        if (val > 0) {

            Duration newInicio = fin;
            fin = inicio;
            inicio = newInicio;
        }

        do {
            value = readDuration(mensaje);
            valFrom = value.compareTo(inicio);
            valTo = value.compareTo(fin);
            if (valFrom < 0 || valTo > 0) {
                System.out.printf("%sRango inválido. %s", Utils.RED, Utils.RESET);
            }

        } while (valFrom < 0 || valTo > 0);

        return value;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> T readEnum(Class<T> c, String message) {
        message = String.format("%s%s%s", Utils.BLUE, message, Utils.RESET);
        Object[] allItems = (EnumSet.allOf(c)).toArray();

        int i;
        for (i = 0; i < allItems.length; i++) {
            message += String.format("%n%3d - %s", i + 1, allItems[i]);
        }

        message = String.format(
                "%s%nElija una opción entre 1 y %d: ", message, allItems.length);

        do {
            i = readInt(message);
            System.out.println();
        } while (i < 1 || i > allItems.length);

        return (T) allItems[i - 1];
    }

}
