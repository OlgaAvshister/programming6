package commands.hard;

import commands.CommandsWorker;
import commands.StaticWorker;
import commands.interfaces.Hard;
import dragon.Dragon;

import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Команда Remove_All_By_Head
 */
public class CRemove_All_By_Head implements Hard {
    private HashMap<Long, Dragon> dragons = CommandsWorker.dragons;


    @Override
    public String execute(String[] arguments) {
        String result;
        long size = StaticWorker.checkEnteredLong(arguments[1]);
//            ArrayList<Long> keys = new ArrayList<>();
//            for (Map.Entry<Long, Dragon> entry : dragons.entrySet()) {
//                Dragon temp = dragons.get(entry.getKey());
//                if (size == temp.getHead().getSize()) {
//                    keys.add(entry.getKey());
//                }
//            }
//            for (int i = 0; i < keys.size(); i++) {
//                this.dragons.remove(keys.get(i));
//            }
        if (CommandsWorker.dragons.isEmpty()) {
            result = "Драконов нет!";
        } else {
            CommandsWorker.dragons = (HashMap<Long, Dragon>) dragons.entrySet().stream().filter(e -> e.getValue().getHead().getSize() != size).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
            result = "Исполнено!";
        }
        return result;
    }

}
