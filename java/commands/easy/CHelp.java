package commands.easy;

import commands.interfaces.Easy;

/**
 * Команда Help
 */
public class CHelp implements Easy {
    @Override
    public String execute() {
        StringBuilder sb = new StringBuilder();
        sb.append("команда info выводит в стандартный поток вывода информацию о коллекции").append("; ");
        sb.append("команда show выводит в стандартный поток все элементы коллекции в строковом представлении").append("; ");
        sb.append("команда insert добавляет новый элемент с заданным ключом").append("; ");
        sb.append("команда update обновляет значение элемента коллекции, id которого равен заданному").append("; ");
        sb.append("команда remove_key удаляет элемент из коллекции по его ключу").append("; ");
        sb.append("команда clear очищает коллекцию").append("; ");
        sb.append("команда save сохраняет коллекцию в файл").append("; ");
        sb.append("команда execute_script считывает и исполняет скрипт из указанного файла").append("; ");
        sb.append("команда exit завершает программу (без сохранения в файл)").append("; ");
        sb.append("команда history выводит последние 5 команд (без их аргументов)").append("; ");
        sb.append("команда replace_if_lowe заменяет значение по ключу, если новое значение меньше старого").append("; ");
        sb.append("команда remove_greater_key удаляет из коллекции все элементы, ключ которых превышает заданный").append("; ");
        sb.append("команда remove_all_by_head удаляет из коллекции все элементы, значение поля head которых эквивалентно заданному ").append("; ");
        sb.append("команда filter_less_than_speaking выводит элементы, значение поля speaking которых меньше заданного").append("; ");
        sb.append("команда print_descending выводит элементы коллекции в порядке убывания").append(".");
        return sb.toString();
    }
}
