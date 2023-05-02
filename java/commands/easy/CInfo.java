package commands.easy;

import commands.CommandsWorker;
import dragon.Dragon;
import commands.interfaces.Easy;

import java.time.LocalDate;
import java.util.HashMap;

/**
 * Команда Info
 */
public class CInfo implements Easy {
    private HashMap<Long, Dragon> dragons = CommandsWorker.dragons;
    private LocalDate start = LocalDate.now();


    @Override
    public String execute() {
        StringBuilder sb = new StringBuilder();
        sb.append(dragons.getClass()).append(", ").append(start).append(", ").append(dragons.size()).append(".");
        return sb.toString();
    }
}
