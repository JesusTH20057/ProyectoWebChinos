/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ordenamiento;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JTextArea;
/**
 *
 * @author PABLORICARDOHERNANDE
 */
public class Kaprekar{
    /*private static final Set<Integer> forbiddenNumbers = new HashSet<>(Arrays.asList(
            0, 1111, 2222, 3333, 4444, 5555, 6666, 7777, 8888, 9999
    ));

    public boolean isForbiddenNumber(int number) {
        return forbiddenNumbers.contains(number);
    }
    
    public boolean isValidInput(String input) {
        return input.matches("\\d{1,4}");
    }
    
    
    public void performKaprekarRoutine(int number) {
        int result = number;

        do {
            result = kaprekarStep(result);
        } while (result != 6174);
    }

    private int kaprekarStep(int number) {
        int[] digits = getDigits(number);

        Arrays.sort(digits);
        int minNumber = formNumber(digits);

        reverseArray(digits);
        int maxNumber = formNumber(digits);

        int result = maxNumber - minNumber;

       
        System.out.println(maxNumber + "-" + minNumber + "=" + result);

        return result;
    }

    private int[] getDigits(int number) {
        int[] digits = new int[4];
        for (int i = 3; i >= 0; i--) {
            digits[i] = number % 10;
            number /= 10;
        }
        return digits;
    }

    private int formNumber(int[] digits) {
        int number = 0;
        for (int digit : digits) {
            number = number * 10 + digit;
        }
        return number;
    }

    private void reverseArray(int[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            int temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
    }*/
    private static final Set<Integer> forbiddenNumbers = new HashSet<>(Arrays.asList(
        0, 1111, 2222, 3333, 4444, 5555, 6666, 7777, 8888, 9999
    ));

    public boolean isForbiddenNumber(int number) {
        return forbiddenNumbers.contains(number);
    }

    public boolean isValidInput(String input) {
        return input.matches("\\d{1,4}");
    }

    public void performKaprekarRoutine(int number, JTextArea resultArea) {
        int result = number;

        do {
            result = kaprekarStep(result, resultArea);
        } while (result != 6174);
    }

    private int kaprekarStep(int number, JTextArea resultArea) {
        int[] digits = getDigits(number);

        Arrays.sort(digits);
        int minNumber = formNumber(digits);

        reverseArray(digits);
        int maxNumber = formNumber(digits);

        int result = maxNumber - minNumber;

        // Mostrar el resultado en el JTextArea
        resultArea.append(maxNumber + " - " + minNumber + " = " + result + "\n");

        return result;
    }

    private int[] getDigits(int number) {
        int[] digits = new int[4];
        for (int i = 3; i >= 0; i--) {
            digits[i] = number % 10;
            number /= 10;
        }
        return digits;
    }

    private int formNumber(int[] digits) {
        int number = 0;
        for (int digit : digits) {
            number = number * 10 + digit;
        }
        return number;
    }

    private void reverseArray(int[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            int temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
    }
}
