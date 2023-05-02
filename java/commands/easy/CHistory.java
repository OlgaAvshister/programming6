package commands.easy;

import commands.interfaces.Easy;

/**
 * Команда History
 */
public class CHistory implements Easy {
    String[] history;

    public CHistory(String[] history) {
        this.history = history;
    }

    @Override
    public String execute() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < history.length; i++) {
            if (!history[i].equals("")) {
                sb.append(history[i]).append(" ");
            }
        }
        System.out.println(sb);
        return sb.toString();
    }
}
