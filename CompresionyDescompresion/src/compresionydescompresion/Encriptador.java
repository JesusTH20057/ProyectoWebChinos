/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package compresionydescompresion;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author PABLORICARDOHERNANDE
 */
public class Encriptador 
{

    /**
     * @param args the command line arguments
     */
   /* public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Mensaje de bienvenida
        
        while (true) {
            // Mostrar el menú de inicio
            System.out.println("\nSeleccione una opción:");
            System.out.println("1. Comprimir");
            System.out.println("2. Descomprimir");
            System.out.println("3. Salir");

            // Leer la opción del usuario
            System.out.print("Opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    // Opción para comprimir
                    System.out.print("Ingrese la frase a comprimir: ");
                    String input = scanner.nextLine();
                    List<Resultado> resultComprimido = Compresion.comprimir(input);
                    for (Resultado result : resultComprimido) {
                        String offsetBits = String.format("%4s", Integer.toBinaryString(result.offset)).replace(' ', '0');
                        String lengthBits = String.format("%4s", Integer.toBinaryString(result.length)).replace(' ', '0');
                        String combinedBits = offsetBits + lengthBits;
                        int combinedByte = Integer.parseInt(combinedBits, 2);
                        int nextCharByte = (int) result.newchar;
                        System.out.print(combinedByte + ", " + nextCharByte + ", ");
                    }
                    break;

                case 2:
                    // Opción para descomprimir
                    System.out.print("Ingrese el codigo para descomprimir: ");
                    String inputBytes = scanner.nextLine();
                    String[] byteStrings = inputBytes.split(",");
                    List<Integer> compressedBytes = new ArrayList<>();
                    for (String byteString : byteStrings) {
                        compressedBytes.add(Integer.parseInt(byteString.trim()));
                    }
                    String stringDescomprimido = Descompresion.descomprimir(compressedBytes);
                    System.out.println("Frase descomprimida: " + stringDescomprimido);
                    break;

                case 3:
                    // Opción para salir
                    System.out.println("Saliendo...");
                    System.exit(0);

                default:
                    System.out.println("Opción no válida.");
                    break;
            }
        }
    }*/
    private JFrame frame;
    private JTextArea inputArea;
    private JTextArea outputArea;

    public Encriptador() {
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Compresor y Descompresor de Texto");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Área de entrada
        inputArea = new JTextArea();
        JScrollPane inputScrollPane = new JScrollPane(inputArea);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        frame.add(inputScrollPane, BorderLayout.CENTER);

        // Área de salida
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        frame.add(outputScrollPane, BorderLayout.SOUTH);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        frame.add(buttonPanel, BorderLayout.NORTH);

        JButton compressButton = new JButton("Comprimir");
        compressButton.addActionListener(e -> compressText());
        buttonPanel.add(compressButton);

        JButton decompressButton = new JButton("Descomprimir");
        decompressButton.addActionListener(e -> decompressText());
        buttonPanel.add(decompressButton);

        JButton clearButton = new JButton("Limpiar");
        clearButton.addActionListener(e -> clearText());
        buttonPanel.add(clearButton);

        frame.setVisible(true);
    }

    private void compressText() {
        String inputText = inputArea.getText().trim();
        if (inputText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Por favor ingrese texto para comprimir.");
            return;
        }

        List<Resultado> compressed = Compresion.comprimir(inputText);
        StringBuilder output = new StringBuilder("Texto comprimido: ");
        for (Resultado result : compressed) {
            String offsetBits = String.format("%4s", Integer.toBinaryString(result.offset)).replace(' ', '0');
            String lengthBits = String.format("%4s", Integer.toBinaryString(result.length)).replace(' ', '0');
            String combinedBits = offsetBits + lengthBits;
            int combinedByte = Integer.parseInt(combinedBits, 2);
            int nextCharByte = (int) result.newchar;
            output.append(combinedByte).append(", ").append(nextCharByte).append(", ");
        }

        outputArea.setText(output.toString());
    }

    private void decompressText() {
        String inputText = inputArea.getText().trim();
        if (inputText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Por favor ingrese los bytes comprimidos para descomprimir.");
            return;
        }

        try {
            String[] byteStrings = inputText.split(",");
            List<Integer> compressedBytes = new ArrayList<>();
            for (String byteString : byteStrings) {
                compressedBytes.add(Integer.parseInt(byteString.trim()));
            }

            String decompressedText = Descompresion.descomprimir(compressedBytes);
            outputArea.setText("Texto descomprimido: " + decompressedText);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Formato incorrecto de bytes. Asegúrese de ingresar números separados por comas.");
        }
    }

    private void clearText() {
        inputArea.setText("");
        outputArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Encriptador::new);
    }
}

