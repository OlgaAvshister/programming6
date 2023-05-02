
import com.google.gson.Gson;
import commands.CommandsWorker;
import commands.StaticWorker;
import dragon.Dragon;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;
// класс клиента, который будет работать с сервером
public class Client {
    public static void main(String[] args) throws InterruptedException {
        ArrayList<PacketForSend> packets = new ArrayList<>(); // создаём пакеты команд, которые будут отрабатывать в случае execute_script
        ArrayList<Path> usedAdds = new ArrayList<>(); // здесь будут лежать использованные адреса
        Socket s = null; // создаём сокет, который будет работать на сервере
        try {
            boolean fin = false;
            while (!fin) {
                s = new Socket("localhost", 40003);
                PrintWriter pw = new PrintWriter(s.getOutputStream()); // активизируем систему записи информации в сокет
                Scanner send = new Scanner(System.in);
                Scanner in = new Scanner(s.getInputStream());
                if (packets.size() == 0) {
                    System.out.print("You: ");
                }
                String words;
                boolean fromScript = false;
                boolean valid = false;
                try {
                    do {
                        if (packets.size() == 0) {
                            words = send.nextLine();
                            fromScript = false;
                        } else {
                            words = packets.remove(0).open();
                            fromScript = true;
                        }
                        if (words.startsWith("insert")) {
                            words = insertWorking(words, fromScript);
                        }
                        if (Pattern.matches("update [0-9]*", words)) {
                            words = insertWorking(words, fromScript);
                        }
                        if (words.startsWith("execute_script ")) {
                            valid = scriptWork(words, packets, usedAdds, 0);
                            if (packets.size() != 0) {
                                fromScript = true;
                                words = packets.remove(0).open();
                            }
                            if (!valid) {
                                words = "";
                            }
                        }
//                    if (valid) {
                        valid = validate(words, fromScript);
//                    }
                        if (!valid) {
                            System.out.println("Введены некорректные данные.");
                        }
                    } while (!valid);
                    String json = new Gson().toJson(new PacketForSend(words));
                    pw.println(json);
                    pw.flush();
                    String info = in.nextLine();
                    System.out.println("Server: " + info);
                    fin = info.equalsIgnoreCase("Спасибо за пользование программой");
                } catch (NoSuchElementException nsee) {
                    System.exit(1);
                }
            }
        } catch (IOException e) {
            System.out.println("Нет доступа к серверу.");
        }
    }

    private static boolean scriptWork(String words, ArrayList<PacketForSend> packets, ArrayList<Path> usedAdds, int position) throws IOException {
        boolean valid = true;
        words = words.substring(15).trim();
        Path path = Path.of(words);
        if (Files.exists(path) && !usedAdds.contains(path.toAbsolutePath())) {
            usedAdds.add(path.toAbsolutePath());
            String command = new String(Files.readAllBytes(path));
            boolean isAnotherExecuteScript = false;
            if (words.startsWith("execute_script ")) {
                valid = scriptWork(command, packets, usedAdds, 0);
                isAnotherExecuteScript = true;
            }
            String[] commands = command.split("[\n\r]+");
            if (isAnotherExecuteScript) {
                commands = cutUnused(commands);
            }
            if (valid) {
                while (commands.length != 0) {
                    String[] temp = prepareFullCommand(commands);
                    if (temp != null) {
                        commands = cutUnused(commands);
                        String commandsInString = prepareString(temp);
                        if (validate(commandsInString, true)) {
                            packets.add(position++, new PacketForSend(commandsInString));
                        } else {
                            valid = false;
                        }
                    } else {
                        valid = false;
                        break;
                    }
                }
            }
        } else {
            valid = false;
        }
        return valid;
    }

    private static String prepareString(String[] temp) {
        String result = "";
        for (int j = 0; j < temp.length; j++) {
            result += temp[j] + " ";
        }
        return result.trim();
    }

    private static String[] cutUnused(String[] commands) {
        String[] result = new String[commands.length - 1];
        for (int i = 0; i < commands.length - 1; i++) {
            result[i] = commands[i + 1];
        }
        return result;
    }

    private static String[] prepareFullCommand(String[] commands) {
//        int ceil = Math.min(commands.length, 10);
        String[] temp = commands[0].split(" ");
//        for (int i = 0; i <= 10; i++) {
//            String[] temp = new String[i];
//            for (int j = 0; j < i; j++) {
//                temp[j] = commands[j];
//            }
        String test = prepareString(temp);
        if (validate(test, true)) {
            return temp;
        }
//        }
        return null;
    }

    private static boolean validate(String command, boolean fromScript) {
        boolean result = false;
        String[] arguments = command.split(" ");
        if (arguments.length == 1) {
            result = arguments[0].equalsIgnoreCase("clear") ||
                    arguments[0].equalsIgnoreCase("exit") ||
                    arguments[0].equalsIgnoreCase("help") ||
                    arguments[0].equalsIgnoreCase("history") ||
                    arguments[0].equalsIgnoreCase("info") ||
                    (arguments[0].equalsIgnoreCase("insert") && !fromScript) ||
                    arguments[0].equalsIgnoreCase("print_descending") ||
                    arguments[0].equalsIgnoreCase("show");
        }
        if (arguments.length == 2) {
            if (arguments[0].equalsIgnoreCase("filter_less_than_speaking")) {
                result = StaticWorker.checkLegalArgumentFromLessThanSpeaking(arguments[1]);
            }
            if (arguments[0].equalsIgnoreCase("remove_all_by_head") ||
                    arguments[0].equalsIgnoreCase("remove_greater_key") ||
                    arguments[0].equalsIgnoreCase("remove_key") ||
                    arguments[0].equalsIgnoreCase("update")) {
                result = StaticWorker.checkEnteredLong(arguments[1]) > 0;
            }
            if (arguments[0].equalsIgnoreCase("execute_script")) {
                result = true;
            }
        }
        if (arguments.length == 3) {
            if (arguments[0].equalsIgnoreCase("replace_if_lowe")) {
                result = Pattern.matches("[0-9]", arguments[1]) && Pattern.matches("[0-9]", arguments[2]);
            }
        }
        if (arguments[0].equalsIgnoreCase("insert") && arguments.length == 8 && fromScript) {
            result = true;
        }
        if (arguments[0].equalsIgnoreCase("update") && arguments.length == 9) {
            result = true;
        }
        return result;
    }

    private static String insertWorking(String command, boolean fromScript) {
        long res = -1;
        StringBuilder sb = new StringBuilder();
        if (Pattern.matches("update [0-9]*", command)) {
            res = Long.parseLong(command.trim().split(" ")[1]);
        }
        if (!fromScript) {
            Dragon temp = new Dragon(new Scanner(System.in), CommandsWorker.getNewId(), CommandsWorker.commandsFromFile);
            String tempName = temp.getName();
            int tempX = temp.getCoordinates().getX();
            float tempY = temp.getCoordinates().getY();
            long tempAge = temp.getAge();
            int tempWeight = temp.getWeight();
            boolean tempSay = temp.getSpeaking();
            int tempSize = temp.getHead().getSize();
            sb.append(command).append(" ").append(tempName).append(" ").append(tempX).append(" ").append(tempY).append(" ")
                    .append(tempAge).append(" ").append(tempWeight).append(" ").append(tempSay).append(" ").append(tempSize);
        } else {
            sb.append(command);
        }
        return sb.toString();
    }
}