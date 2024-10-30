/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author PABLORICARDOHERNANDE
 */
public class Elementos extends JFrame implements ActionListener{
    JTextField txtDestino, txtOrigen;
    JButton cmdCambiarTexto, cmdBoton1, cmdBoton2, cmdBoton3, cmdBoton4;
    public Elementos()
    {
        super();
        
        config();
    }

    private void config() {
      setTitle("Ejemplos de Eventos");
      setSize(200,150);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setLocationRelativeTo(null);
      setLayout(null);
      
      //JButton cmdCambiarTexto;
     /* cmdCambiarTexto = new JButton("Cambia");
      cmdCambiarTexto.setSize(100,40);
      cmdCambiarTexto.setLocation(0,50);
      cmdCambiarTexto.addActionListener((e)->onClickCambiarTexto());
      add(cmdCambiarTexto);
      
      //JTextField txtOrigen;
      txtOrigen = new JTextField();
      txtOrigen.setBounds(0,0,100,40);
      add(txtOrigen);
      
      //JTextField txtDestino;
      txtDestino = new JTextField();
      txtDestino.setBounds(0,110,100,40);
      add(txtDestino);*/
      
      JLabel titulo;
      titulo = new JLabel("Menu de programas con Interfaz");
      //titulo.setBounds(0,200,200,2);
      add(titulo);
     
      cmdBoton1 = new JButton("Ordenamiento");
      cmdBoton1.setBounds(0,60,140,80);
      cmdBoton1.addActionListener(this);
      add(cmdBoton1);
      cmdBoton2 = new JButton("Kaprekar");
      cmdBoton2.setBounds(0,120,140,80);
      cmdBoton2.addActionListener(this);
      add(cmdBoton2);
      cmdBoton3 = new JButton("Comprension");
      cmdBoton3.setBounds(0,180,140,80);
      cmdBoton3.addActionListener(this);
      add(cmdBoton3);
      cmdBoton4 = new JButton("Fracciones con letra");
      cmdBoton4.setBounds(0,240,140,80);
      cmdBoton4.addActionListener(this);
      add(cmdBoton4);
    }
    private void onClickCambiarTexto()
    {
       String texto;
       texto = txtOrigen.getText();
       txtDestino.setText(texto);
    }

    @Override
    /*public void actionPerformed(ActionEvent e) {
        //JOptionPane.showMessageDialog(null,"Hola");
        if(e.getSource() == cmdBoton1)
            metodo1();
        else if(e.getSource()==cmdBoton2)
            metodo2();
         else if(e.getSource()==cmdBoton3)
            metodo3();
        else if(e.getSource()==cmdBoton4)
            metodo4();
    }*/
     public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cmdBoton1) {
            ejecutarProgramaCombi();
        } else if (e.getSource() == cmdBoton2) {
            ejecutarProgramaKapre();
        } else if (e.getSource() == cmdBoton3) {
            ejecutarProgramaCompre();
        } else if (e.getSource() == cmdBoton4) {
            ejecutarProgramaFracc();
        }
    }
     
    private void ejecutarProgramaCombi() {
         /*String rutaCarpeta = "C:\\Users\\Usuario\\Documents\\NetBeansProjects\\Combinaciones\\build\\classes";
    // Clase principal con el nombre completo del paquete
    String clasePrincipal = "combinaciones.Combinaciones";  // Cambia esto por tu clase principal

    try {
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", rutaCarpeta, clasePrincipal);
        pb.start();
    } catch (IOException ex) {
        ex.printStackTrace();
    }*/
     //String rutaCarpeta = "C:\\Users\\Usuario\\Documents\\NetBeansProjects\\Combinaciones\\build\\classes";
    //String clasePrincipal = "combinaciones.Combinaciones";
    
    try {
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", "C:\\Users\\PABLORICARDOHERNANDE\\Documents\\NetBeansProjects\\Combinaciones\\build\\classes", "combinaciones.Combinaciones");
        Process process = pb.start();
        
        // Captura la salida estándar y los errores del proceso
        InputStreamReader isr = new InputStreamReader(process.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line); // Muestra la salida del proceso
        }
        
        InputStreamReader esr = new InputStreamReader(process.getErrorStream());
        BufferedReader ebr = new BufferedReader(esr);
        while ((line = ebr.readLine()) != null) {
            System.err.println(line); // Muestra los errores del proceso
        }
    } catch (IOException ex) {
        ex.printStackTrace();
    }
    }
    
    private void ejecutarProgramaKapre() {
        // String rutaCarpeta = "C:\\Users\\Usuario\\Documents\\NetBeansProjects\\Ordenamiento\\build\\classes";
    // Clase principal con el nombre completo del paquete
    //String clasePrincipal = "ordenamiento.Ordenamiento";  // Cambia esto por tu clase principal

    try {
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", "C:\\Users\\PABLORICARDOHERNANDE\\Documents\\NetBeansProjects\\Ordenamiento\\build\\classes", "ordenamiento.Ordenamiento");
        Process process = pb.start();
        
        // Captura la salida estándar y los errores del proceso
        InputStreamReader isr = new InputStreamReader(process.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line); // Muestra la salida del proceso
        }
        
        InputStreamReader esr = new InputStreamReader(process.getErrorStream());
        BufferedReader ebr = new BufferedReader(esr);
        while ((line = ebr.readLine()) != null) {
            System.err.println(line); // Muestra los errores del proceso
        }
    } catch (IOException ex) {
        ex.printStackTrace();
    }
    }
    
    private void ejecutarProgramaCompre() {
         //String rutaCarpeta = "C:\\Users\\Usuario\\Documents\\NetBeansProjects\\Encriptador\\build\\classes";
    // Clase principal con el nombre completo del paquete
    //String clasePrincipal = "compresionydescompresion.Encriptador";  // Cambia esto por tu clase principal

   try {
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", "C:\\Users\\PABLORICARDOHERNANDE\\Documents\\NetBeansProjects\\CompresionyDescompresion\\build\\classes", "compresionydescompresion.Encriptador");
        Process process = pb.start();
        
        // Captura la salida estándar y los errores del proceso
        InputStreamReader isr = new InputStreamReader(process.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line); // Muestra la salida del proceso
        }
        
        InputStreamReader esr = new InputStreamReader(process.getErrorStream());
        BufferedReader ebr = new BufferedReader(esr);
        while ((line = ebr.readLine()) != null) {
            System.err.println(line); // Muestra los errores del proceso
        }
    } catch (IOException ex) {
        ex.printStackTrace();
    }
    }
    
   private void ejecutarProgramaFracc() {
    // Ruta a la carpeta 'build/classes' de tu proyecto Fracciones
    //String rutaCarpeta = "C:\\Users\\Usuario\\Documents\\NetBeansProjects\\Fracciones\\build\\classes";
    // Clase principal con el nombre completo del paquete
    //String clasePrincipal = "fracciones.Fracciones";  // Cambia esto por tu clase principal

     try {
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", "C:\\Users\\PABLORICARDOHERNANDE\\Documents\\NetBeansProjects\\Fracciones\\build\\classes", "fracciones.Fracciones");
        Process process = pb.start();
        
        // Captura la salida estándar y los errores del proceso
        InputStreamReader isr = new InputStreamReader(process.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line); // Muestra la salida del proceso
        }
        
        InputStreamReader esr = new InputStreamReader(process.getErrorStream());
        BufferedReader ebr = new BufferedReader(esr);
        while ((line = ebr.readLine()) != null) {
            System.err.println(line); // Muestra los errores del proceso
        }
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}

    /*private void metodo2() {
        JOptionPane.showMessageDialog(null,"Kaprekar");
    }

    private void metodo1() {
        JOptionPane.showMessageDialog(null,"Ordenamineto");
    }
    
    private void metodo3() {
        JOptionPane.showMessageDialog(null,"Comprension");
    }
    
     private void metodo4() {
        JOptionPane.showMessageDialog(null,"Fracciones de Letras");
    }*/
}
