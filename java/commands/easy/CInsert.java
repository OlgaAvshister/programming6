package commands.easy;

import commands.CommandsWorker;
import commands.StaticWorker;
import dragon.Coordinates;
import dragon.Dragon;
import commands.interfaces.Easy;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Команда Insert
 */
public class CInsert implements Easy {
    private HashMap<Long, Dragon> dragons = CommandsWorker.dragons;
    private Scanner scanner = new Scanner(System.in);


    @Override
    public String execute() {
        boolean workFromScript = false;
        String result = "Исполнено!";
        String[] comms;
        if (CommandsWorker.commandsFromFile.isEmpty()) {
            comms = CommandsWorker.receivedCommand.split(" ");
        } else {
            workFromScript = true;
            comms = new String[8];
            if (CommandsWorker.commandsFromFile.size() >= 7) {
                for (int i = 1; i < comms.length; i++) {
                    if (!CommandsWorker.commandsFromFile.isEmpty()) {
                        comms[i] = CommandsWorker.commandsFromFile.remove(0);
                    } else {
                        StaticWorker.setExternalExit(true);
                    }
                }
            } else {
                StaticWorker.setExternalExit(true);
            }
        }

        if (!StaticWorker.isExternalExit() &&
                ((!workFromScript && StaticWorker.checkCommsForDragon(CommandsWorker.commandsFromFile)) || workFromScript)) {
            Dragon dragon = new Dragon(comms[1], new Coordinates(Integer.parseInt(comms[2]), Float.parseFloat(comms[3])),
                    Long.parseLong(comms[4]), Integer.parseInt(comms[5]), Boolean.getBoolean(comms[6]), Integer.parseInt(comms[7]));
//            Dragon dragon = new Dragon(scanner, CommandsWorker.getNewId(), CommandsWorker.commandsFromFile);
            dragons.put(dragon.getId(), dragon);
        } else {
            StaticWorker.setExternalExit(true);
            result = "Ошибка исполнения скрипта.";
        }
        return result;
    }
}
