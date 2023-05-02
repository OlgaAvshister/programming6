package commands.easy;

import commands.CommandsWorker;
import dragon.Dragon;
import commands.interfaces.Easy;

import java.util.HashMap;
import java.util.Map;

/**
 * Команда show
 */
public class CShow implements Easy {
    private HashMap<Long, Dragon> dragons = CommandsWorker.dragons;


    @Override
    public String execute() {
//        for (Map.Entry<Long, Dragon> entry : dragons.entrySet()) {
//            System.out.println(entry.getValue());
//        }
        StringBuilder sb = new StringBuilder();
        if (dragons.isEmpty()) {
            sb.append("Драконов нет!");
        } else {
            dragons.entrySet().stream().forEach(e -> sb.append(e.getValue()).append("|"));
        }
        return sb.toString();
    }

}
