package Parte1;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.border.EmptyBorder;

public class Practica01_02 extends JWindow {
    
    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Practica01_02 frame = new Practica01_02();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } // Llaves de cierre agregadas
        });
    }

    public Practica01_02() {
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
    }
} // Llave final de la clase agregada