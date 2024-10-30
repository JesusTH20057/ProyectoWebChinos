/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ordenamiento;

import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author PABLORICARDOHERNANDE
 */
public class Ordenamiento {

    /**
     * @param args the command line arguments
     */
    /*public static void main(String[] args) {
        // TODO code application logic here
        
       Kaprekar kaprekar = new Kaprekar();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Ingresa un número de 4 dígitos: ");
            String input = scanner.next();

            // Validar que el input sea un número y que tenga 4 dígitos
            if (!kaprekar.isValidInput(input)) {
                System.out.println("Error: No se pueden numeros de mas de 4 digitos ni negativos o menores a 0");
            } 
            else {
                int number = Integer.parseInt(input);

                if (kaprekar.isForbiddenNumber(number)) {
                    System.out.println("Error: Número no permitido, por ser todos los digitos iguales. Por favor, ingresa otro número.");
                } else {
                    System.out.println("Secuencia de Kaprekar:");
                    kaprekar.performKaprekarRoutine(number);
                    break; // Salir del bucle si el número es válido
                }
            }
        }


        scanner.close();
    }*/
    public static void main(String[] args) {
        // Crear el marco de la ventana
        JFrame frame = new JFrame("Rutina de Kaprekar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        // Crear el panel para los campos de entrada y botón
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        // Crear campo de texto para ingresar el número
        JTextField numberField = new JTextField();
        panel.add(new JLabel("Ingrese un número de 4 dígitos:"));
        panel.add(numberField);

        // Botón para ejecutar la rutina de Kaprekar
        JButton executeButton = new JButton("Ejecutar Rutina de Kaprekar");
        panel.add(executeButton);

        // Agregar el panel a la ventana
        frame.add(panel, BorderLayout.NORTH);

        // Crear área de texto para mostrar los resultados
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Acción cuando se presiona el botón
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = numberField.getText().trim();
                Kaprekar kaprekar = new Kaprekar();

                // Validar la entrada
                if (!kaprekar.isValidInput(input)) {
                    JOptionPane.showMessageDialog(frame, 
                        "Error: El número debe tener 4 dígitos y que no sea negativo.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int number = Integer.parseInt(input);

                // Validar si es un número prohibido
                if (kaprekar.isForbiddenNumber(number)) {
                    JOptionPane.showMessageDialog(frame, 
                        "Error: No se pueden 4 digitos iguales.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Ejecutar la rutina de Kaprekar y mostrar los resultados
                resultArea.setText("Secuencia de Kaprekar:\n");
                kaprekar.performKaprekarRoutine(number, resultArea);
            }
        });

        // Hacer visible la ventana
        frame.setVisible(true);
    }
}
