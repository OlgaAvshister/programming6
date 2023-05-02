package commands;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import commands.easy.*;
import commands.hard.*;
import commands.interfaces.Easy;
import commands.interfaces.Hard;
import dragon.Dragon;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

/**
 * Класс, отвечающий за работу всех команд
 */
public class CommandsWorker {
    public static Logger logger;
    public static long id = 0;
    public static String receivedCommand;
    public static ArrayList<String> commandsFromFile = new ArrayList<>();
    public static ArrayList<String> listOfUsedFiles = new ArrayList<>();

    private String[] commands = {"", "", "", "", ""};
    public static HashMap<Long, Dragon> dragons;
    private HashMap<String, Easy> easies = new HashMap<>();
    private HashMap<String, Hard> hards = new HashMap<>();

    private Scanner scanner = new Scanner(System.in);

    private File file;

    /**
     * Конструктор, проверяющий права и создающий коллекцию драконов
     *
     * @param filename
     * @throws IOException
     */
    public CommandsWorker(String filename) throws IOException {
        logger = LogManager.getLogger(CommandsWorker.class);
        logger.addAppender(new ConsoleAppender(new PatternLayout("%r [%t] %p %c %x - %m%n")));
        this.file = new File(filename);
        if (Files.notExists(Path.of(this.file.toURI()))) {
            System.out.println("Введен несуществующий файл, попробуйте снова");
            System.exit(1);
        }
        if (!this.file.canRead()) {
            System.out.println("У Вас нет прав на чтение");
            System.exit(1);
        }
        if (!this.file.canWrite()) {
            System.out.println("У Вас нет прав на запись");
            System.exit(1);
        }
        if (!this.file.canExecute()) {
            System.out.println("У Вас нет прав на исполнение");
            System.exit(1);
        }
        DataInputStream dos = new DataInputStream(new BufferedInputStream(new FileInputStream(this.file))); // открываем возможность побитового считывания файла
        byte[] b = dos.readAllBytes(); // считываем всё в массив байт
        int temp;
        String result = "";
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        while ((temp = bais.read()) != -1) {
            result += (char) temp; // побайтово записываем всё в строку
        }
        Type type = new TypeToken<HashMap<Long, Dragon>>() {
        }.getType(); // готовим тип данных, в который мы будем сохранять наш файл
        this.dragons = new Gson().fromJson(result, type); // записываем данные из формата GSON (библиотека GSON)
        if (this.dragons == null) { // если файл был пустой и ничего не записалось
            this.dragons = new HashMap<>(); // то инициализируем словарь драконов пустым словарём
        } else {
            for (Map.Entry<Long, Dragon> entry : dragons.entrySet()) {
                String[] s = dragons.get(entry.getKey()).getLocalDateToString().split("-"); //получаем дату создания дракона через сохранённую строковую переменную
                Dragon d = dragons.get(entry.getKey()); // получаем ссылку на дракона
                d.setCreationDate(LocalDate.of(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]))); // устанавливаем правильную дату создания дракона
            }
        }
        this.init();
        if (!this.checkDragons()) {
            System.out.println("Ошибка в вводном файле");
            System.exit(0);
        }

        setId();
    }

    public static void setId() {
        CommandsWorker.id = dragons.size() == 0 ? dragons.size() : dragons.entrySet().stream().max((a, b) -> (int) (a.getKey() - b.getKey())).get().getKey();
    }

    public void init() {
        easies.put("clear", new CClear());
        easies.put("help", new CHelp());
        easies.put("info", new CInfo());
        easies.put("insert", new CInsert());
        easies.put("print_descending", new CPrint_Descending());
        easies.put("show", new CShow());
        easies.put("history", new CHistory(this.commands));
        easies.put("exit", new CExit(this.file));
        hards.put("filter_less_than_speaking", new CFilter_Less_Than_Speaking());
        hards.put("remove_all_by_head", new CRemove_All_By_Head());
        hards.put("remove_greater_key", new CRemove_Greater_Key());
        hards.put("remove_key", new CRemove_Key());
        hards.put("replace_if_lowe", new CReplace_If_lowe());
        hards.put("update", new CUpdate());
        hards.put("execute_script", new CExecute_Script(this));
    }

    public static Long getNewId() {
        CommandsWorker.id++;
        return CommandsWorker.id;
    }

    /**
     * Метод, проверяющий правильность создания дракона
     *
     * @return
     */
    private boolean checkDragons() {
        boolean isCorrect = true;
        for (Map.Entry<Long, Dragon> entry : this.dragons.entrySet()) {
            Dragon temp = entry.getValue();
            isCorrect = checkDragon(temp);
            if (!isCorrect) {
                break;
            }
        }
        return isCorrect;
    }

    private boolean checkDragon(Dragon temp) {
        return temp.getId() > 0 &&
                !temp.getName().trim().equals("") &&
                temp.getCoordinates() != null &&
                temp.getLocalDateToString() != null &&
                temp.getAge() > 0 && temp.getWeight() > 0 &&
                temp.getColor() != null;
    }

    /**
     * Метод, считывающий и проверяющий введенные команды
     */
    public String workFromScript() {
        String result = "";
        while (!StaticWorker.isExternalExit() && commandsFromFile.size() != 0) {
            String commandLine = "";
            if (CommandsWorker.commandsFromFile.isEmpty()) {
                CommandsWorker.listOfUsedFiles.clear();
//                try {
//                    commandLine = scanner.nextLine().trim();
//                } catch (NoSuchElementException e) {
//                    System.exit(1);
//                }
            } else {
                commandLine = prepareFromCommands(CommandsWorker.commandsFromFile);
            }
            if (commandLine.equals("")) {

            } else {
                addInHistory(commandLine);
                String[] commands = commandLine.split("\\s");
                boolean hasCommand = false;
                if (commands.length == 1) {
                    if (easies.containsKey(commandLine)) {
                        hasCommand = true;
                        result += easies.get(commandLine).execute();
                    }
                } else {
                    if (hards.containsKey(commands[0])) {
                        hasCommand = true;
                        result += hards.get(commands[0]).execute(commands);
                    }
                }
                if (!hasCommand) {
                    result = "Ошибочная команда, попробуйте ещё раз";
                }
                if (result.contains("\r\n")) {
                    result = result.substring(0, result.lastIndexOf("\r\n")) + " | ";
                }
                init();
            }
        }
        return result;
    }

    public String work(String args) {
        CommandsWorker.receivedCommand = args;
        String commandLine = args;
        if (CommandsWorker.commandsFromFile.isEmpty()) {
            CommandsWorker.listOfUsedFiles.clear();
            try {
            } catch (NoSuchElementException e) {
                System.exit(1);
            }
        } else {
            commandLine = prepareFromCommands(CommandsWorker.commandsFromFile);
        }
        addInHistory(commandLine);
        String[] commands = commandLine.split("\\s");
        String result = "";
        if (commands.length == 1) {
            if (easies.containsKey(commandLine)) {
                result = easies.get(commandLine).execute();
            }
        } else {
            if (commands.length == 8 && commands[0].equalsIgnoreCase("insert")) {
                result = easies.get(commands[0]).execute();
            }
            if (commands.length == 9 && commands[0].equalsIgnoreCase("update")) {
                result = hards.get(commands[0]).execute(commands);
            }
            if (hards.containsKey(commands[0])) {
                result = hards.get(commands[0]).execute(commands);
                if (!commandsFromFile.isEmpty()) {
                    result += workFromScript();
                }
            }
        }
        init();
        return result;
    }

    private void addInHistory(String commandLine) {
        String[] comms = commandLine.split("\\s");
//        if (easies.containsKey(comms[0]) || hards.containsKey(comms[0])) { - если раскомментировать эту строку -
//        то в историю добавляться будут только зарегистрированные команды
        int count = 0;
        boolean isFull = false;
        while (!this.commands[count].equals("")) {
            count++;
            if (count == this.commands.length) {
                isFull = true;
                break;
            }
        }
        if (isFull) {
            for (int i = 1; i < this.commands.length; i++) {
                this.commands[i - 1] = this.commands[i];
            }
            this.commands[this.commands.length - 1] = comms[0];
        } else {
            this.commands[count] = comms[0];
        }
//        }
    }

    private String prepareFromCommands(ArrayList<String> commandsFromFile) {
        StringBuilder result = new StringBuilder();
        String command = commandsFromFile.remove(0);
        result.append(command);
        if (easies.keySet().contains(command)) {
            return result.toString();
        } else if (hards.keySet().contains(result.toString().trim())) {
            result.append(" ");
            result.append(commandsFromFile.remove(0));
            if (command.equals("replace_if_lowe")) {
                result.append(" ");
                result.append(commandsFromFile.remove(0));
            }
            return result.toString();
        } else {
            return "Ошибка";
        }
    }
}
