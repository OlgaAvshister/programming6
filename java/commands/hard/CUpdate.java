package commands.hard;

import commands.CommandsWorker;
import commands.StaticWorker;
import commands.interfaces.Hard;
import dragon.Coordinates;
import dragon.Dragon;

import java.util.HashMap;

/**
 * Команда Update
 */
public class CUpdate implements Hard {

    private HashMap<Long, Dragon> dragons = CommandsWorker.dragons;

    @Override
    public String execute(String[] arguments) {
        String result = "Исполнено";
        if (CommandsWorker.dragons.containsKey(Long.parseLong(arguments[1]))) {
            System.out.println();
            if (StaticWorker.checkCommsForDragon(CommandsWorker.commandsFromFile)) {
//            Dragon dragon = new Dragon(new Scanner(System.in), key, commands.CommandsWorker.commandsFromFile);
                Dragon dragon = new Dragon(Long.parseLong(arguments[1]), arguments[2], new Coordinates(Integer.parseInt(arguments[3]),
                        Float.parseFloat(arguments[4])), Long.parseLong(arguments[5]), Integer.parseInt(arguments[6]),
                        Boolean.getBoolean(arguments[7]), Integer.parseInt(arguments[8]));

                dragons.remove(arguments[1]);
                dragons.put(dragon.getId(), dragon);
            } else {
                StaticWorker.setExternalExit(true);
                result = "Ошибка исполнения скрипта.";
            }
        } else {
            result = "Дракон c запрошенным id не найден";
        }
        return result;
    }
}