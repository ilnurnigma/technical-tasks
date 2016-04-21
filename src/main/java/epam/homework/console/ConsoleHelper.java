package epam.homework.console;

import java.io.Console;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConsoleHelper {
    public static final String PROMPT_RESET = "reset";
    public static final String PROMPT_$CWD = "$cwd";
    public static final String PROMPT_DEFAULT = "$";
    public static final String SHELL_NAME = "[MyShell]";
    public static final String CMD_PROMPT = "prompt";
    public static final String CMD_DIR = "dir";
    public static final String CMD_TREE = "tree";
    public static final String CMD_EXIT = "exit";
    public static final String CMD_CD = "cd";
    public static final String CMD_EMPTY = "";
    public static final String INDENTATION_CHARACTER = "-";
    public static final String CD_PARENT = "..";

    private Console console;
    private String currentDirectory = Paths.get("").toAbsolutePath().toString();


    public ConsoleHelper(Console console) {
        this.console = console;
    }

    public void start() throws IOException {
        String promptCommand = PROMPT_RESET;
        String cmd;
        do {
            System.out.print(SHELL_NAME + " " + getPrompt(promptCommand, getCurrentDirectory()) + ">");
            cmd = console.readLine();
            String[] commandArray = cmd.trim().split("\\s+", 2);

            switch (commandArray[0]) {
                case CMD_PROMPT:
                    promptCommand = getPromptCommand(commandArray);
                    break;

                case CMD_DIR:
                    displayDirContent(getCurrentDirectory());
                    break;

                case CMD_TREE:
                    displayDirTree(getCurrentDirectory());
                    break;

                case CMD_CD:
                    if (commandArray.length > 1) {
                        changeDirectory(commandArray[1]);
                    }
                    break;

                case CMD_CD + CD_PARENT:
                    changeDirectory(CD_PARENT);
                    break;

                case CMD_EXIT:
                    break;

                case CMD_EMPTY:
                    break;

                default:
                    System.out.println(commandArray[0] + " : unknown command");
            }

        } while (!CMD_EXIT.equals(cmd));

        System.out.println("Bye");
    }

    private void changeDirectory(String command) {
        if (CD_PARENT.equals(command)) {
            Path path = Paths.get(currentDirectory).getParent();
            currentDirectory = path.toString();
        } else {
            Path path = Paths.get(currentDirectory, command);
            if (Files.exists(path)) {
                currentDirectory = path.toString();
            } else {
                System.out.println(path + " : does not exist");
            }
        }
    }

    private void displayDirContent(String currentPath) throws IOException {
        System.out.println("Content of " + currentPath);

        try (DirectoryStream<Path> paths = Files.newDirectoryStream(Paths.get(currentPath))) {
            for (Path path : paths) {
                if (Files.isRegularFile(path)) {
                    System.out.print("FILE   ");
                } else if (Files.isDirectory(path)) {
                    System.out.print("DIR    ");
                }
                System.out.println(path.getFileName());
            }
        }
    }

    private void displayDirTree(String path) throws IOException {
        int startLevel = Paths.get(path).getNameCount();

        Files.walk(Paths.get(path)).forEach(filePath -> {
            if (Files.isDirectory(filePath)) {
                System.out.println(getIndentationString(filePath.getNameCount() - startLevel) + filePath.getFileName());
            }
        });
    }

    private String getPromptCommand(String[] commandArray) {
        if (commandArray.length > 1) {
            return commandArray[1];
        }

        return "";
    }

    private String getPrompt(String promptCommand, String currentPath) {
        switch (promptCommand) {
            case PROMPT_$CWD:
                return currentPath;

            case PROMPT_RESET:
                return PROMPT_DEFAULT;
        }

        return promptCommand;
    }

    private String getIndentationString(int level) {
        String indentChar = "";
        for (int i = 0; i < level; i++) {
            indentChar += INDENTATION_CHARACTER;
        }
        return indentChar;
    }

    private String getCurrentDirectory() {
        return currentDirectory;
    }
}
