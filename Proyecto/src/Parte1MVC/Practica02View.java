package Parte1MVC;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionListener;

public class Practica02View extends JFrame {
    public JButton bSalir = new JButton("Haz clic para Salir");
    public JTextField campoTexto = new JTextField();
    public JPasswordField campoPassword = new JPasswordField();
    public JTextArea areaTexto = new JTextArea();
    public JFormattedTextField campoFormateado = new JFormattedTextField(12345.67);
    public JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
    public JSlider slider = new JSlider(0, 100, 50);
    public JComboBox<String> comboBox = new JComboBox<>(new String[]{"Opción 1", "Opción 2", "Opción 3"});

    public Practica02View() {
        setTitle("Entrada de Datos MVC");
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 440, 400);
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);

        // Configuración rápida de posiciones
        addComponent(panel, new JLabel("JButton:"), bSalir, 10);
        addComponent(panel, new JLabel("JTextField:"), campoTexto, 50);
        addComponent(panel, new JLabel("JPasswordField:"), campoPassword, 90);
        
        JScrollPane sp = new JScrollPane(areaTexto);
        sp.setBounds(180, 130, 170, 40);
        JLabel lArea = new JLabel("JTextArea:");
        lArea.setBounds(10, 130, 150, 30);
        panel.add(lArea); panel.add(sp);

        addComponent(panel, new JLabel("JFormatted:"), campoFormateado, 180);
        addComponent(panel, new JLabel("JSpinner:"), spinner, 220);
        addComponent(panel, new JLabel("JSlider:"), slider, 260);
        addComponent(panel, new JLabel("JComboBox:"), comboBox, 310);

        add(panel);
    }

    private void addComponent(JPanel p, JLabel lbl, JComponent comp, int y) {
        lbl.setBounds(10, y, 150, 30);
        comp.setBounds(180, y, 170, 30);
        p.add(lbl); p.add(comp);
    }

    public void setEscuchador(ActionListener al) {
        bSalir.addActionListener(al);
    }
}