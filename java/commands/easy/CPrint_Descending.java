package commands.easy;

import commands.CommandsWorker;
import commands.interfaces.Easy;
import dragon.Dragon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Команда Print_descending
 */
public class CPrint_Descending implements Easy {
    private HashMap<Long, Dragon> dragons = CommandsWorker.dragons;


    @Override
    public String execute() {
        ArrayList<Dragon> list = new ArrayList<>();
        for (Map.Entry<Long, Dragon> entry : dragons.entrySet()) {
            list.add(entry.getValue());
        }
        Collections.sort(list);
        StringBuilder sb = new StringBuilder();
        list.stream().forEach(s -> sb.append(s).append(" "));
        return sb.toString();
    }
}