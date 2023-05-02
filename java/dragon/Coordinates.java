package dragon;
/**
 * Метод, отвечающий за определение координат дракона
 */
public class Coordinates {
    private int x;
    private float y;

    /**
     * Конструктор, определяющий координаты дракона
     * @param x
     * @param y
     */

    public Coordinates(int x, float y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
