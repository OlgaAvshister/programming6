package commands.hard;

import commands.CommandsWorker;
import commands.StaticWorker;
import commands.interfaces.Hard;
import dragon.Dragon;

import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Команда Remove_Key
 */
public class CRemove_Key implements Hard {
    private HashMap<Long, Dragon> dragons = CommandsWorker.dragons;


    @Override
    public String execute(String[] arguments) {
        String result;
        Long key = StaticWorker.checkEnteredLong(arguments[1]);
//        dragons.remove(key);
        if (CommandsWorker.dragons.isEmpty()) {
            result = "Драконов нет!";
        } else {
            CommandsWorker.dragons = (HashMap<Long, Dragon>) dragons.entrySet().stream().filter(e -> e.getKey() != key).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
            result = "Исполнено!";
        }
        return result;
    }
}
