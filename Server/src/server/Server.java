/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package server;

/**
 *
 * @author PABLORICARDOHERNANDE
 */

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 12345;
    private static ExecutorService pool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        try (ServerSocket listener = new ServerSocket(PORT)) {
            System.out.println("Servidor del juego del gato iniciado en el puerto " + PORT);
            while (true) {
                Socket client = listener.accept();
                pool.execute(new GameSession(client));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class GameSession implements Runnable {
    private Socket client;
    private GameBot bot;
    private char[][] board;
    private char currentTurn;
    private boolean isFirstMove = true;

    public GameSession(Socket client) {
        this.client = client;
        this.board = new char[3][3];
        this.currentTurn = Math.random() < 0.5 ? 'X' : 'O'; // Turno inicial aleatorio
        this.bot = new GameBot();
    }

    /*public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            out.println("Conexión establecida. Tú eres " + (currentTurn == 'X' ? 'O' : 'X'));
            
            if (currentTurn == 'O') { // Si el servidor (bot) inicia
                botMove(out, true);
                printBoard(out);
                isFirstMove = false;
            }
            
            while (true) {
                if (currentTurn == 'O') {
                    botMove(out, false);
                } else {
                    playerMove(in, out);
                }
                printBoard(out);

                if (checkWin(out)) {
                    break;
                }
                
                currentTurn = (currentTurn == 'X') ? 'O' : 'X';
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    
    public void run() {
    try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
         PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
        out.println("Conexión establecida. Tú eres " + (currentTurn == 'X' ? 'O' : 'X'));
        
        // Iniciar con el servidor si currentTurn es 'O'
        if (currentTurn == 'O') { // Si el servidor (bot) inicia
            botMove(out, true);
            printBoard(out);
            isFirstMove = false;
            currentTurn = 'X'; // Cambiar el turno al cliente después de la primera jugada del servidor
        }
        
        // Ciclo de juego
        while (true) {
            if (currentTurn == 'O') {
                botMove(out, false);
            } else {
                playerMove(in, out);
            }
            printBoard(out);

            if (checkWin(out)) {
                break;
            }
            
            // Cambiar el turno
            currentTurn = (currentTurn == 'X') ? 'O' : 'X';
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    private void botMove(PrintWriter out, boolean isInitialMove) {
        int[] move;
        if (isInitialMove) {
            move = bot.getRandomMove(board); // Movimiento inicial aleatorio
        } else {
            move = bot.getMove(board); // Movimientos posteriores
        }
        board[move[0]][move[1]] = 'O';
        out.println("BOT:JUGADA," + move[0] + "," + move[1]);
    }

    private void playerMove(BufferedReader in, PrintWriter out) throws IOException {
        while (true) {
            out.println("JUGADOR:TURNO");
            String move = in.readLine();
            String[] parts = move.split(",");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            if (board[row][col] == '\0') {
                board[row][col] = 'X';
                out.println("JUGADOR:JUGADA," + row + "," + col);
                break;
            } else {
                out.println("JUGADOR:POSICION_OCUPADA");
            }
        }
    }

    private boolean checkWin(PrintWriter out) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != '\0' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                out.println("RESULTADO:GANADOR," + board[i][0]);
                return true;
            }
            if (board[0][i] != '\0' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                out.println("RESULTADO:GANADOR," + board[0][i]);
                return true;
            }
        }
        if (board[0][0] != '\0' && board[0][0] == board[1][1] && board[1][1] == board[2][2] ||
            board[0][2] != '\0' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            out.println("RESULTADO:GANADOR," + board[1][1]);
            return true;
        }
        if (isBoardFull()) {
            out.println("RESULTADO:EMPATE");
            return true;
        }
        return false;
    }

    private boolean isBoardFull() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == '\0') {
                    return false;
                }
            }
        }
        return true;
    }

    private void printBoard(PrintWriter out) {
        for (char[] row : board) {
            for (char cell : row) {
                out.print(cell == '\0' ? "." : cell);
                out.print(" ");
            }
            out.println();
        }
    }
}

class GameBot {
    public int[] getMove(char[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') {
                    return new int[]{i, j}; // Primera posición libre
                }
            }
        }
        return null;
    }

    public int[] getRandomMove(char[][] board) {
        while (true) {
            int row = (int) (Math.random() * 3);
            int col = (int) (Math.random() * 3);
            if (board[row][col] == '\0') {
                return new int[]{row, col}; // Retorna una posición aleatoria libre
            }
        }
    }
}
