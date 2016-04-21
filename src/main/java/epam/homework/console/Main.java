package epam.homework.console;

import java.io.Console;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Console console = System.console();
        if (console == null) {
            System.out.println("Console object was not found.");
            return;
        }

        ConsoleHelper consoleHelper = new ConsoleHelper(console);
        consoleHelper.start();
    }
}
