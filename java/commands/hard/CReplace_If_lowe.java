package commands.hard;

import commands.CommandsWorker;
import commands.StaticWorker;
import commands.interfaces.Hard;
import dragon.Dragon;

import java.util.HashMap;

/**
 * Команда Replace_If_lowe
 */
public class CReplace_If_lowe implements Hard {
    private HashMap<Long, Dragon> dragons = CommandsWorker.dragons;


    @Override
    public String execute(String[] arguments) {
        String result;
        if (dragons.size() < 2) {
            result = "Слишком мало драконов!";
        } else if (!CommandsWorker.dragons.containsKey(Long.parseLong(arguments[1])) || !CommandsWorker.dragons.containsKey(Long.parseLong(arguments[2]))) {
            result = "Указаны некорректные id!";
        } else {
            Long idFirst = StaticWorker.checkEnteredLong(arguments[1]);
            Long idSecond = StaticWorker.checkEnteredLong(arguments[2]);
            if (idFirst != -1 && idSecond != -1) {
                Dragon dragonFirst = dragons.get(idFirst);
                Dragon dragonSecond = dragons.get(idSecond);
                if (dragonFirst.compareTo(dragonSecond) > 0) {
                    dragons.remove(dragonFirst.getId());
                    dragons.remove(dragonSecond.getId());
                    dragonSecond.setId(dragonFirst.getId());
                    dragons.put(dragonSecond.getId(), dragonSecond);
                }
            }
            result = "Исполнено!";
        }
        return result;
    }
}
