/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compresionydescompresion;

/**
 *
 * @author PABLORICARDOHERNANDE
 */
public class Resultado 
{
    int offset;  // Desplazamiento hacia atrás
    int length;  // Longitud del patrón repetido
    char newchar;  // Siguiente carácter no repetido después del patrón

    public Resultado(int offset, int length, char nextChar) {
        this.offset = offset;
        this.length = length;
        this.newchar = nextChar;
    }
}
