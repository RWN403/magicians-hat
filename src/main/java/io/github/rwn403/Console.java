package io.github.rwn403;

import java.util.Scanner;

/**
 * Format and output console messages.
 * @author RWN403
 * @version 1.0
 */
public class Console {

    // Declare ANSI colour codes.
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";

    // Output a console message sent by the app.
    public static void app(String msg) {
        System.out.print("[" + ANSI_YELLOW + "APP" + ANSI_RESET + "]: " + msg + "\n");
    }

    // Output a console message sent by the database.
    public static void database(String msg) {
        System.out.print("[" + ANSI_PURPLE + "DATABASE" + ANSI_RESET + "]: " + msg + "\n");
    }

    // Output a console message caused by an error.
    public static void error(String msg) {
        System.out.print("[" + ANSI_RED + "ERROR" + ANSI_RESET + "]: " + msg + "\n");
    }

    // Output a console message describing an exception.
    public static void exception(Exception e) {
        System.out.print(
            "[" + ANSI_RED + "EXCEPTION" + ANSI_RESET + "]: Encountered an exception." + "\n"
        );
        System.out.println();
        e.printStackTrace();
        System.out.println();
    }

    // Get user input.
    public static String input(String msg) {
        Scanner sc = new Scanner(System.in);
        console(msg + " " + ANSI_CYAN);
        String input = sc.nextLine();
        System.out.print(ANSI_RESET);
        sc.close();
        return input;
    }

    // Output a console message sent by the console.
    private static void console(String msg) {
        System.out.print("[" + ANSI_BLUE + "CONSOLE" + ANSI_RESET + "]: " + msg);
    }
}
