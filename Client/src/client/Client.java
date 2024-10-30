/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package client;

/**
 *
 * @author PABLORICARDOHERNANDE
 */
// Client.java
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private TicTacToeGUI gui;

    public Client(String serverAddress) throws IOException {
        socket = new Socket(serverAddress, 12345);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        gui = new TicTacToeGUI(out);
        gui.setVisible(true);

        new Thread(new IncomingReader()).start();
    }

    private class IncomingReader implements Runnable {
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    gui.updateBoard(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String serverAddress = JOptionPane.showInputDialog(
                "Introduce la IP del servidor:");
        new Client(serverAddress);
    }
}