/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compresionydescompresion;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author PABLORICARDOHERNANDE
 */
public class Compresion 
{
    private static final int WHERE = 8;
    private static final int CUAL = 6;

    public static List<Resultado> comprimir(String input) {
        List<Resultado> res = new ArrayList<>();
        int index = 0;

        while (index < input.length()) {
            int bestMatchLength = 0;
            int bestMatchOffset = 0;
            char nextChar = input.charAt(index + bestMatchLength);

            // Buscar el mejor patrón repetido
            for (int offset = 1; offset <= WHERE && index - offset >= 0; offset++) {
                int matchLength = 0;
                while (matchLength < CUAL &&
                        index + matchLength < input.length() &&
                        input.charAt(index + matchLength) == input.charAt(index - offset + matchLength)) {
                    matchLength++;
                }
                if (matchLength > bestMatchLength) {
                    bestMatchLength = matchLength;
                    bestMatchOffset = offset;
                    nextChar = (index + matchLength < input.length()) ? input.charAt(index + matchLength) : '\0';
                }
            }

            // Dar resultado con el mejor patrón encontrado
            Resultado result = new Resultado(bestMatchOffset, bestMatchLength, nextChar);
            res.add(result);

            // Mover el índice al siguiente carácter después del patrón encontrado
            index += (bestMatchLength + 1);
        }

        return res;
    }

    public static void imprimirResultado(List<Resultado> resultComprimido) {
        System.out.println("Frase comprimida: ");
        for (Resultado result : resultComprimido) {
            System.out.println("(" + result.offset + ", " + result.length + ", " + result.newchar + ")");
        }
    }

    

}

