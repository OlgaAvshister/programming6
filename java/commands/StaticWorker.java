package commands;

import java.util.ArrayList;

/**
 * Класс, отвечающий за проверки данных
 */

public class StaticWorker {
    private static boolean externalExit = false;

    public static void setExternalExit(boolean externalExit) {
        StaticWorker.externalExit = externalExit;
    }

    public static boolean isExternalExit() {
        return externalExit;
    }

    public static long checkEnteredLong(String argument) {
        boolean incorrect = false;
        long result = -1;
        try {
            result = Long.valueOf(argument);
        } catch (NumberFormatException nfe) {
            System.out.println("Введены неправильные данные");
            incorrect = true;
        }
        if (!incorrect && result <= 0) {
            System.out.println("Размер не может быть ниже 0");
        }
        return result;
    }

    public static boolean checkLegalArgumentFromLessThanSpeaking(String s) {
        return s.equals("0") || s.equals("1") || s.equals("true") || s.equals("false");
    }

    /**
     * Метод, проверяющий, является ли введенное значение целым числом
     * @param value
     * @return
     */
    public static String checkIfInt(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return "Введите правильное значение, пожалуйста";
        }
        return value;
    }

    /**
     * Метод, проверяющий, является ли введенное значение целым положительным числом
     * @param value
     * @return
     */
    public static String checkIfIntWeightOrHead(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return "Введите правильное значение, пожалуйста";
        }
        if (Integer.parseInt(value) <= 0){
            return "Введите правильное значение, пожалуйста";
        }
        return value;
    }

    /**
     * Метод, проверяющий, является ли введенное значение дробным числом
     * @param value
     * @return
     */
    public static String checkIfFloat(String value) {
        try {
            Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return "Введите правильное значение, пожалуйста";
        }
        return value;
    }

    /**
     * Метод, проверяющий, является ли введенное значение дробным числом
     * @param value
     * @return
     */
    public static String checkIfLong(String value) {
        try {
            Long.parseLong(value);
        } catch (NumberFormatException e) {
            return "Введите правильное значение, пожалуйста";
        }
        if (Long.parseLong(value) <= 0){
            return "Введите правильное значение, пожалуйста";
        }
        return value;
    }

    /**
     * Метод, проверяющий скрипт
     * @param comms
     * @return
     */
    public static boolean checkCommsForDragon(ArrayList<String> comms) {
        return comms.isEmpty() ||
                (comms.size() > 6 &&
                        !comms.get(0).matches(" *") &&
                        !checkIfInt(comms.get(1)).equals("Введите правильное значение, пожалуйста") &&
                        !checkIfInt(comms.get(2)).equals("Введите правильное значение, пожалуйста") &&
                        !checkIfLong(comms.get(3)).equals("Введите правильное значение, пожалуйста") &&
                        !checkIfIntWeightOrHead(comms.get(4)).equals("Введите правильное значение, пожалуйста") &&
                        (comms.get(5).equals("yes") || comms.get(5).equals("no")) &&
                        !checkIfIntWeightOrHead(comms.get(6)).equals("Введите правильное значение, пожалуйста"));
    }


}
