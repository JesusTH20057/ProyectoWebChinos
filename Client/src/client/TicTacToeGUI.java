/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

/**
 *
 * @author PABLORICARDOHERNANDE
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;

public class TicTacToeGUI extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private PrintWriter out;

    public TicTacToeGUI(PrintWriter out) {
        this.out = out;
        setTitle("Juego del Gato");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 3));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int row = i;
                final int col = j;
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[i][j].addActionListener(e -> handleMove(row, col));
                add(buttons[i][j]);
            }
        }
    }

    private void handleMove(int row, int col) {
        out.println(row + "," + col);
    }

    public void updateBoard(String message) {
        if (message.startsWith("BOT:JUGADA")) {
            String[] parts = message.split(",");
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);
            buttons[row][col].setText("O");
        } else if (message.startsWith("JUGADOR:JUGADA")) {
            String[] parts = message.split(",");
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);
            buttons[row][col].setText("X");
        } else if (message.equals("JUGADOR:POSICION_OCUPADA")) {
            JOptionPane.showMessageDialog(this, "Posición ocupada. Intenta nuevamente.");
        } else if (message.startsWith("RESULTADO:GANADOR")) {
            char ganador = message.charAt(message.length() - 1);
            String resultado = ganador == 'X' ? "¡Ganaste!" : "¡Perdiste!";
            JOptionPane.showMessageDialog(this, resultado);
        } else if (message.equals("RESULTADO:EMPATE")) {
            JOptionPane.showMessageDialog(this, "¡Empate!");
        }
    }
}