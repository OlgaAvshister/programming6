package dragon;

import java.util.Random;

/**
 * Класс, отвечающий за создание головы дракона
 */
public class DragonHead {
    private int size;
    private Integer eyesCount; //Поле не может быть null, Значение поля должно быть больше 0
    private Integer toothCount; //Поле может быть null, Значение поля должно быть больше 0

    /**
     * Конструктор, содержащий данные о голове дракона
     * @param size
     */
    public DragonHead(int size){
        this.size = size;
        Random random = new Random();
        this.eyesCount = random.nextInt(7)+1;
        this.toothCount = random.nextInt(70)+30;
    }

    public int getSize(){
        return this.size;
    }

    public Integer getEyesCount() {
        return eyesCount;
    }

    public Integer getToothCount() {
        return toothCount;
    }
}