/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package fracciones;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author PABLORICARDOHERNANDE
 */

public class Fracciones 
{
   private JFrame frame;
    private JTextField inputField;
    private JTextArea resultArea;    
//static Scanner scanner;
    public static Fraction numberOneF, numberTwoS;

    public static void main(String[] args) 
    {
        /*scanner = new Scanner(System.in).useDelimiter("\n");

        boolean salir = false;

        while (!salir) {
            System.out.println("Menú de Inicio:");
            System.out.println("1. Fracciones");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) 
            {
                case 1:
                    try 
                    {
                        OperadorP();
                    } 
                    catch (Exception ex) 
                    {
                        System.err.println(ex);
                    }
                    break;
                case 2:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
                    break;
            }
        }*/
        SwingUtilities.invokeLater(() -> {
            Fracciones gui = new Fracciones();
            gui.createAndShowGUI();
        });
    }
     private void createAndShowGUI() {
        frame = new JFrame("Calculadora de Fracciones");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Ingrese la expresión de la fracción:");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        inputField = new JTextField();
        resultArea = new JTextArea();
        resultArea.setEditable(false);

        JButton calculateButton = new JButton("Calcular");
        calculateButton.addActionListener(new CalculateAction());

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(titleLabel, BorderLayout.NORTH);
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(calculateButton, BorderLayout.SOUTH);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        frame.setVisible(true);
    }
    
     private class CalculateAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String input = inputField.getText().toLowerCase();
                var fracciones = input.split("(mas|menos|por|entre)");
                if (fracciones.length < 2) {
                    resultArea.setText("¡Falta ingresar un operador!");
                    return;
                }
                if (fracciones.length > 2) {
                    resultArea.setText("Solo debes de ingresar un operador.");
                    return;
                }

                numberOneF = new Fraction(fracciones[0].trim());
                numberTwoS = new Fraction(fracciones[1].trim());

                Fraction res = null;
                if (input.contains("mas")) { // Suma
                    res = numberOneF.Sumar(numberTwoS);
                } else if (input.contains("menos")) { // Resta
                    res = numberOneF.Restar(numberTwoS);
                } else if (input.contains("por")) { // Multiplicación
                    res = numberOneF.Multiplicar(numberTwoS);
                } else if (input.contains("entre")) { // División
                    res = numberOneF.Dividir(numberTwoS);
                }

                resultArea.setText("Resultado: " + res.toString());
            } catch (Exception ex) {
                resultArea.setText("Error: " + ex.getMessage());
            }
        }
    }
     
    /*public static void OperadorP() throws Exception 
    {
        System.out.println("");
        System.out.println("Ingrese la expresión de la fracción (por ejemplo, 'un tercio mas tres quintos'): ");
        String input = scanner.nextLine().toLowerCase();

        var fracciones = input.split("(mas|menos|por|entre)");
        if (fracciones.length < 2)
        {
            throw new Exception("Te falto ingresar un operador padre!");
        }
        if (fracciones.length > 2) 
        {
            throw new Exception("Solo debes de ingresar un solo operador");
        }

        numberOneF = new Fraction(fracciones[0]);
        numberTwoS = new Fraction(fracciones[1]);

        Fraction res = null;
        if (input.contains("mas")) //Suma
        {
            res = numberOneF.Sumar(numberTwoS);
        } 
        else if (input.contains("menos")) //Resta
        {
            res = numberOneF.Restar(numberTwoS);
        } 
        else if (input.contains("por")) //Multipicacion 
        {
            res = numberOneF.Multiplicar(numberTwoS);
        } 
        else if(input.contains("entre"))
        {
            res = numberOneF.Dividir(numberTwoS);
        }
        
        else 
        {
            res = numberOneF.Dividir(numberTwoS);
        }
        
        System.out.println("Resultado: " + res);
        System.out.println("");
    }*/
}

class Fraction
{

    int numerador1;
    int denominador2;

    public static String[] Pnumera = 
    {
        "cero", "un", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve",
        "diez", "once", "doce", "trece", "catorce", "quince", "dieciséis", "diecisiete", "dieciocho", "diecinueve",
        "veinte", "veintiuno", "veintidos", "veintitres", "noventaynueve"
    };


    public static String[] Sdenomina = 
    {
        "ceravo", "entero", "medio", "tercio", "cuarto", "quinto", "sexto", "septimo", "octavo", "noveno", "décimo",
        "noventaynueveavos"
    };

    public Fraction(int Numerador, int Denominador) throws Exception
    {
        if (Numerador > 19602) 
        {
            System.out.println("El numerador debe ser menor a mil");
        }
        
        if (Denominador > 9801) 
        {
            System.out.println("El denominador debe ser menor a mil");
        }  
        this.numerador1 = Numerador;
        this.denominador2 = Denominador;
    }
    
    public Fraction(String str) throws Exception
    {
       var words = str.toLowerCase().split(" ");
       if (words.length < 2) 
       {
            System.out.println("Las fracciones deben de contener dos palabras");
       }

    var Pnumerador = new StringBuilder();
    
    for (int i = 0; i < words.length - 1; ++i) 
    {
        Pnumerador.append(words[i]);
        Pnumerador.append(' ');
    }
    var Snumerador = Pnumerador.toString().trim();

    if (Snumerador.equals("noventaynueve")) 
    {
        this.numerador1 = 99;
    } 
    else 
    {
        boolean found = false;
        for (int a = 0; a < Pnumera.length; a++) 
        {
            if (Pnumera[a].equals(Snumerador)) 
            {
                this.numerador1 = a;
                found = true;
                break;
            }
        }

        if (!found) 
        {
            System.out.println("Numerador no conocido");
        }
    }

    var denominador = words[words.length - 1];
    if (denominador.equals("noventaynueveavos")) 
    {
        this.denominador2 = 99;
    } 
    else 
    {
        boolean found = false;
        for (int i = 0; i < Sdenomina.length; i++) {
            String denString;

            if (numerador1 == 1) 
            {
                denString = Sdenomina[i];
            } 
            else 
            {
                denString = Sdenomina[i] + 's';
            }

            if (denString.equals(denominador)) 
            {
                this.denominador2 = i;
                found = true;
                break;
            }
        }

        if (!found) 
        {
            System.out.println("Denominador no conocido");
        }
    }
    }


    private void PnumeradorST()
    {
        Pnumera = new String[9802];
        for (int a = 0; a < Pnumera.length; ++a) 
        {
            try 
            {
                Pnumera[a] = Mapa.toString(a);
            } 
            catch (Exception e) 
            {
                System.out.println("Fallo por la entarda dada");
            }
        }
    }

    private void SdenominadorST()
    {
        Sdenomina = new String[9802];
        for (int a = 0; a < Sdenomina.length; ++a) 
        {
            try 
            {
                Sdenomina[a] = Mapa2.toString(a);
            } 
            catch (Exception e) 
            {
               System.out.println("Fallo por la entarda dada");
            }
        }
    }

    @Override
    public String toString()
    {
        var AW = new StringBuilder();

        try 
        {
            AW.append(Mapa.toString(numerador1));
            AW.append(' ');
            AW.append(Mapa2.toString(denominador2));
            if (numerador1 != 1) AW.append('s');
        } 
        catch (Exception L) 
        {
            System.out.println(L);
            System.err.printf("La fraccion fallo con numerador: %d y denominador: %d\n",
                numerador1, denominador2
            );
            return "No";
        }

        return AW.toString();
    }

    public int RPnumerador()
    {
        return numerador1;
    }

    public int RPdenominador()
    {
        return denominador2;
    }

    public Fraction Sumar(Fraction other) throws Exception
    {
        int NuevoPnumerador = this.numerador1 * other.denominador2 + other.numerador1 * this.denominador2;

        int NuevoPdenominador = this.denominador2 * other.denominador2;

        if (NuevoPdenominador == 0) throw new Exception("El denominador no puede ser cero");

        return new Fraction(NuevoPnumerador, NuevoPdenominador);
    }
    
    public Fraction Restar(Fraction other) throws Exception
    {
        int NuevoPnumerador = this.numerador1 * other.denominador2 - other.numerador1 * this.denominador2;
        int NuevoPdenominador = this.denominador2 * other.denominador2;
        if (NuevoPdenominador == 0) throw new Exception("El denominador no puede ser cero");
        return new Fraction(NuevoPnumerador, NuevoPdenominador);
    }
    
    public Fraction Multiplicar(Fraction other) throws Exception
    {
        int NuevoPnumerador = this.numerador1 * other.numerador1;
        int NuevoPdenominador = this.denominador2 * other.denominador2;
        if (NuevoPdenominador == 0) throw new Exception("El denominador no puede ser cero");
        return new Fraction(NuevoPnumerador, NuevoPdenominador);
    }
    
    public Fraction Dividir(Fraction other) throws Exception
    {
        int NuevoPnumerador = this.numerador1 * other.denominador2;
        int NuevoPdenominador = this.denominador2 * other.numerador1;
        if (NuevoPdenominador == 0) throw new Exception("La operación es en una división por cero");
        return new Fraction(NuevoPnumerador, NuevoPdenominador);
    }
}

class Mapa
{

    public static String[] First = 
    {
        "cero", "uno", "dos", "tres",
        "cuatro", "cinco", "seis", "siete",
        "ocho", "nueve", "diez", "once",
        "doce", "trece", "catorce", "quince",
        "dieciséis", "diecisiete", "dieciocho", "diecinueve",
        "veinte", "noventaynueve", "noventaynueveavos"
    };

    public static String[] Second = 
    {
        "cero", "diez", "veinte", "treinta", "cuarenta",
        "cincuenta", "sesenta", "setenta", "ochenta", "noventa", "noventaynueve", "noventaynueveavos"
    };

    public static String[] Third = 
    {
        "cero", "cien", "doscientos", "trescientos", "cuatrocientos",
        "quinientos", "seiscientos", "setecientos", "ochocientos", "novecientos",
        "noventaynueve", "noventaynueveavos"
    };

    public static String toString(int m) throws Exception
    {
        if (m > 19602) 
        {
            var msg = String.format(
                "Número máximo es 19602, se intentó usar %d",
                m);
            throw new Exception(msg);
        }

        var answ = new StringBuilder();

        if (m < 0) 
        {
            answ.append("menos "); 
            m = -m;
        }

        if (m < 100 && m % 10 == 0) 
        {
            answ.append(Second[m / 10]);
            return answ.toString();
        }

        if (m <= 20) 
        {
            if (m == 1) answ.append("un");
            else answ.append(First[m]);
            return answ.toString();
        } 
        else if (m <= 30) 
        {
            answ.append("veinti");
            answ.append(First[m % 10]);
            return answ.toString();
        } 
        else if (m < 100) 
        {
            answ.append(Second[m / 10]);
            answ.append(" y ");
            answ.append(First[m % 10]);
            return answ.toString();
        } 
        else if (m < 1000) 
        {
            int cent = m / 100;
            int dec = (m / 10) % 10;
            int un = m % 10;

            answ.append(Third[cent]);
            if (cent == 1 && (dec != 0 || un != 0)) answ.append("to");
            if (!(dec == 0 && un == 0)) 
            {
                var next = toString(m % 100);
                if (answ.charAt(answ.length() - 1) == next.charAt(0)) {
                    answ.append(next.substring(1));
                } 
                else 
                {
                    answ.append(next);
                }
            }

            return answ.toString();
        } 
        else 
        {
            int mil = m / 1000;
            if (mil > 1) 
            {
                answ.append(First[mil]);
                answ.append(' ');
            }
            answ.append("mil ");
            var next = toString(m % 1000);
            answ.append(next);

            return answ.toString();
        }
    }
}

class Mapa2
{
    public static String[] First = 
    {
        "ceravo", "entero", "medio", "tercio",
        "cuarto", "quinto", "sexto", "séptimo",
        "octavo", "noveno", "décimo"
    }; 
    
    public static String toString(int m) throws Exception
    {
        if (m > 9801) 
        {
            System.out.println("Número máximo para el Mapa2 es 980");
        }
        
        if (m < 11) 
        {
            return First[m];
        }
        
        var CarMap1 = Mapa.toString(m);
        
        var answ2 = new StringBuilder();
        var words1 = CarMap1.split(" ");
        
        String Anterior = null;
        
        for (String word : words1) 
        {
            if (Anterior != null) 
            {
                if (Anterior.charAt(Anterior.length() - 1) == word.charAt(0)) {
                    answ2.append(word.substring(1));
                } 
                else 
                {
                    if (word.equals("y")) answ2.append("i");
                    else answ2.append(word);
                }
            } 
            else 
            {
                answ2.append(word);
            }
            Anterior = word;
        }

        if (answ2.charAt(answ2.length() - 1) != 'a') answ2.append('a');
        answ2.append("vo");
        
        return answ2.toString();
    }
}
