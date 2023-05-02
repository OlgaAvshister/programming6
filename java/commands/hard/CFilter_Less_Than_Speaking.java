package commands.hard;

import commands.CommandsWorker;
import commands.interfaces.Hard;
import dragon.Dragon;

import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Команда Filter_Less_Than_Speaking
 */
public class CFilter_Less_Than_Speaking implements Hard {
    private HashMap<Long, Dragon> dragons = CommandsWorker.dragons;


    @Override
    public String execute(String[] arguments) {
        String result;
        if (!dragons.isEmpty()) {
//                ArrayList<Long> keys = new ArrayList<>();
            boolean isNotSpeaking = arguments[1].equals("0") || arguments[1].equals("false");
//                for (Map.Entry<Long, Dragon> entry : dragons.entrySet()) {
//                    Dragon temp = dragons.get(entry.getKey());
//                    if (temp.getSpeaking() && !isNotSpeaking) {
//                        keys.add(entry.getKey());
//                    }
//                    if (!temp.getSpeaking() && isNotSpeaking) {
//                        keys.add(entry.getKey());
//                    }
//                }
//                for (int i = 0; i < keys.size(); i++) {
//                    this.dragons.remove(keys.get(i));
//                }
            CommandsWorker.dragons = (HashMap<Long, Dragon>) dragons.entrySet().stream().filter(e -> e.getValue().getSpeaking() == isNotSpeaking).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
//            this.dragons = commands.CommandsWorker.dragons;
            result = "Исполнено!";
        } else {
            result = "Список драконов пуст!";
        }
        return result;
    }
}