package commands.easy;

import commands.CommandsWorker;
import dragon.Dragon;
import commands.interfaces.Easy;

import java.util.HashMap;

/**
 * Команда Clear
 */
public class CClear implements Easy {
    private HashMap<Long, Dragon> dragons = CommandsWorker.dragons;

    @Override
    public String execute() {
        dragons.clear();
        CommandsWorker.setId();
        System.out.println("Cleared");
        return "Cleared!";
    }
}
