package dragon;

import commands.CommandsWorker;
import commands.StaticWorker;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

/**
 * Класс, отвечающий за создание дракона
 */

public class Dragon implements Comparable<Dragon>, Serializable {

    private Long id; //Поле не может быть null, Значение поля должно быть больше 0,
    // Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private transient LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long age; //Значение поля должно быть больше 0
    private Integer weight; //Значение поля должно быть больше 0
    private Boolean speaking; //Поле не может быть null
    private Color color; //Поле не может быть null
    private DragonHead head;

    private String localDateToString;

    public Dragon(String name, Coordinates coordinates, Long age, Integer weight, Boolean speaking, int headSize) {
        this.id = CommandsWorker.getNewId();
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDate.now();
        this.age = age;
        this.weight = weight;
        this.speaking = speaking;
        Random random = new Random();
        int number = random.nextInt(3);
        if (number == 0) {
            this.color = Color.BLACK;
        } else if (number == 1) {
            this.color = Color.ORANGE;
        } else {
            this.color = Color.WHITE;
        }
        this.head = new DragonHead(headSize);
        this.localDateToString = this.creationDate.toString();
    }


    public Dragon(long id, String name, Coordinates coordinates, Long age, Integer weight, Boolean speaking, int headSize) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDate.now();
        this.age = age;
        this.weight = weight;
        this.speaking = speaking;
        Random random = new Random();
        int number = random.nextInt(3);
        if (number == 0) {
            this.color = Color.BLACK;
        } else if (number == 1) {
            this.color = Color.ORANGE;
        } else {
            this.color = Color.WHITE;
        }
        this.head = new DragonHead(headSize);
        this.localDateToString = this.creationDate.toString();
    }

    /**
     * Конструктор, создающий дракона
     * @param id
     */
    public Dragon(Scanner scanner, Long id, ArrayList<String> comms) {
        this.id = id;
//        if (comms.isEmpty()){
//            System.out.println("Введите имя дракона");
//        }
        System.out.println("Введите имя дракона");
        String checkName = comms.isEmpty()? scanner.nextLine() : comms.remove(0); // каждый раз мы проверяем -
        // если массив команд пустой, то программа запрашиает команду у пользователя,
        // иначе - берёт следующую команду из массива команд и удаяет команду из массива команд
        while (checkName.matches(" *")){
            System.out.println("Введите правильное имя, пожалуйста");
            checkName = comms.isEmpty()? scanner.nextLine() : comms.remove(0);
        }
        this.name = checkName;

        System.out.println("Введите координаты дракона по оси x");
        String check = comms.isEmpty()? scanner.nextLine() : comms.remove(0);
        String value = StaticWorker.checkIfInt(check);
        if (value.equals("Введите правильное значение, пожалуйста")) {
            System.out.println(value);
        }
        while (!Objects.equals(value, check)){
            check = comms.isEmpty()? scanner.nextLine() : comms.remove(0);
            value = StaticWorker.checkIfInt(check);
            if (value.equals("Введите правильное значение, пожалуйста")) {
                System.out.println(value);
            }
        }
        int x = Integer.parseInt(value);

        System.out.println("Введите координаты дракона по оси y");
        check = comms.isEmpty()? scanner.nextLine() : comms.remove(0);
        value = StaticWorker.checkIfFloat(check);
        while (!Objects.equals(value, check)){
            check = comms.isEmpty()? scanner.nextLine() : comms.remove(0);
            value = StaticWorker.checkIfFloat(check);
            if (value.equals("Введите правильное значение, пожалуйста")) {
                System.out.println(value);
            }
        }
        float y = Float.parseFloat(value);
        this.coordinates = new Coordinates(x, y);
        this.creationDate = LocalDate.now();
        this.localDateToString = this.creationDate.toString();
        System.out.println("Введите возраст дракона");
        check = comms.isEmpty()? scanner.nextLine() : comms.remove(0);
        value = StaticWorker.checkIfLong(check);
        while (!Objects.equals(value, check) || Long.parseLong(value) <= 0){
            check = comms.isEmpty()? scanner.nextLine() : comms.remove(0);
            value = StaticWorker.checkIfLong(check);
            if (value.equals("Введите правильное значение, пожалуйста")) {
                System.out.println(value);
            }
        }
        this.age = Long.parseLong(value);

        System.out.println("Введите вес дракона");
        check = comms.isEmpty()? scanner.nextLine() : comms.remove(0);
        value = StaticWorker.checkIfIntWeightOrHead(check);
        while (!Objects.equals(value, check)){
            check = comms.isEmpty()? scanner.nextLine() : comms.remove(0);
            value = StaticWorker.checkIfIntWeightOrHead(check);
            if (value.equals("Введите правильное значение, пожалуйста")) {
                System.out.println(value);
            }
        }
        this.weight = Integer.parseInt(value);
        scanner = new Scanner(System.in); //очистить буфер
        System.out.println("Дракон разговаривает? (yes, no)");
        String answer = comms.isEmpty()? scanner.nextLine() : comms.remove(0);
        while (!answer.equals("yes") && !answer.equals("no")){
            System.out.println("Введите правильное значение, пожалуйста");
            answer = comms.isEmpty()? scanner.nextLine() : comms.remove(0);
        }
        this.speaking = answer.equals("yes");

        Random random = new Random();
        int number = random.nextInt(3);
        if (number == 0) {
            this.color = Color.BLACK;
        } else if (number == 1) {
            this.color = Color.ORANGE;
        } else {
            this.color = Color.WHITE;
        }
        System.out.println("Введите размер головы");
        check = comms.isEmpty()? scanner.nextLine() : comms.remove(0);
        value = StaticWorker.checkIfIntWeightOrHead(check);
        while (!Objects.equals(value, check)){
            check = comms.isEmpty()? scanner.nextLine() : comms.remove(0);
            value = StaticWorker.checkIfIntWeightOrHead(check);
            if (value.equals("Введите правильное значение, пожалуйста")) {
                System.out.println(value);
            }

        }
        int size = Integer.parseInt(value);
        this.head = new DragonHead(size);
    }

    public Long getAge() {
        return age;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }


    public Integer getWeight() {
        return weight;
    }

    public Color getColor() {
        return color;
    }
    //
    public DragonHead getHead(){
        return this.head;
    }

    public boolean getSpeaking(){
        return this.speaking;
    }
    public Long getId() {
        return this.id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getName() {
        return this.name;
    }
    public String getLocalDateToString() {
        return this.localDateToString;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName());
        sb.append(", age - ");
        sb.append(this.age);
        sb.append(", id - ");
        sb.append(this.id);
        return sb.toString();
    }

    @Override
    public int compareTo(Dragon o) {
        return (int) (this.age-o.age);
    }


}

