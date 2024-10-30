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
public class Descompresion 
{
    public static String descomprimir(List<Integer> compressedBytes) {
        StringBuilder result = new StringBuilder();

        int index = 0;
        while (index < compressedBytes.size()) {
            int offsetLengthByte = compressedBytes.get(index);
            int offset = (offsetLengthByte >> 4) & 0xF;
            int length = offsetLengthByte & 0xF;
            index++; // Avanzar al siguiente byte

            if (index >= compressedBytes.size()) {
                break; // Si no hay suficiente espacio para el siguiente byte, salir del bucle
            }

            int valorascii = compressedBytes.get(index);
            index++; // Avanzar al siguiente byte

            // Convertir ASCII value a char
            char nextChar = (char) valorascii;

            // Reconstruir el resultado y agregarlo
            if (length > 0) {
                int startIndex = result.length() - offset;
                int endIndex = startIndex + length;
                for (int i = startIndex; i < endIndex; i++) {
                    char ch = (i >= 0 && i < result.length()) ? result.charAt(i) : '\0';
                    result.append(ch);
                }
            }
            if (nextChar != '\0') {
                result.append(nextChar);
            }
        }

        return result.toString();
    }
}


