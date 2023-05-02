package commands.easy;

import com.fasterxml.jackson.databind.ObjectMapper;
import commands.CommandsWorker;
import dragon.Dragon;
import commands.interfaces.Easy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Команда Save
 */
public class CSave implements Easy {
    private String file;
    private HashMap<Long, Dragon> dragons = CommandsWorker.dragons;

    public CSave(File file) {
        this.file = String.valueOf(file);
    }

    @Override
    public String execute() {
        try (FileWriter fw = new FileWriter(file)) {
            ObjectMapper om = new ObjectMapper(); // запись в файл при помощи ObjectMapper (с использованием геттеров)
            String json = om.writeValueAsString(dragons);
            fw.write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "Done";
    }
}