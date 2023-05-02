package commands.hard;

import commands.CommandsWorker;
import commands.interfaces.Hard;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
/**
 * Команда Execute_Script
 */
public class CExecute_Script implements Hard {
    CommandsWorker cw;

    public CExecute_Script(CommandsWorker cw) {
        this.cw = cw;
    }

    @Override
    public String execute(String[] arguments) {
        String result = "";
        String address = arguments[1];
        Path path = Paths.get(arguments[1]);
        String absolute = path.toAbsolutePath().toString();
        if (CommandsWorker.listOfUsedFiles.contains(absolute)) {
            result = "Зацикливание программы. Выполнение сценария остановлено.";
            CommandsWorker.commandsFromFile.clear();
            CommandsWorker.listOfUsedFiles.clear();
        } else if (Files.exists(Path.of(address))) {
            CommandsWorker.listOfUsedFiles.add(absolute);
            DataInputStream dis;
            try {
                dis = new DataInputStream(new BufferedInputStream(new FileInputStream(address)));
                byte[] b = dis.readAllBytes();
                int temp;
                ByteArrayInputStream bais = new ByteArrayInputStream(b);
                while ((temp = bais.read()) != -1) {
                    result += (char) temp;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (CommandsWorker.commandsFromFile.isEmpty()) {
                adder(CommandsWorker.commandsFromFile, result);
            } else {
                ArrayList<String> list = new ArrayList<>();
                adder(list, result);
                for (int i = 0; i < CommandsWorker.commandsFromFile.size(); i++) {
                    list.add(CommandsWorker.commandsFromFile.get(i));
                }
                CommandsWorker.commandsFromFile = list;
            }
        } else {
            System.out.println("Некорректный адрес скрипта");
        }
        return result.equals("Зацикливание программы. Выполнение сценария остановлено.")? result : "";
    }

    private void adder(ArrayList<String> list, String s) {
        Collections.addAll(list, s.split("\\s"));
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals("")) {
                list.remove(i--);
            }
        }

    }
}
