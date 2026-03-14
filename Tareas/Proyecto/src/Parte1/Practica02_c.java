package Parte1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;

public class Practica02_c extends JFrame implements ActionListener {

    String[] opciones = {"Opción 1", "Opción 2", "Opción 3"};
    JComboBox<String> comboBox;
    JPanel panel;
    JButton Bsalir;
    JTextField campoTexto;
    JPasswordField campoPassword;
    JTextArea areaTexto;
    JFormattedTextField campoFormateado;
    JSpinner spinner;
    JSlider slider;

    public Practica02_c() {
        
        setTitle("Ejemplo de componentes de entrada de texto");
        setSize(442, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setBounds(0, 0, 403, 365);
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);

        JLabel etiqueta1 = new JLabel("JButton:");
        etiqueta1.setBounds(0, 3, 178, 36);
        Bsalir = new JButton("Haz clic para Salir");
        Bsalir.setBounds(188, 3, 172, 36);
        Bsalir.addActionListener(this);
        panel.add(etiqueta1);
        panel.add(Bsalir); 
        
        JLabel etiqueta2 = new JLabel("JTextField:");
        etiqueta2.setBounds(0, 49, 148, 36);
        campoTexto = new JTextField();
        campoTexto.setBounds(188, 50, 172, 36);
        panel.add(etiqueta2);
        panel.add(campoTexto);

        JLabel etiqueta3 = new JLabel("JPasswordField:");
        etiqueta3.setBounds(0, 95, 148, 36);
        campoPassword = new JPasswordField();
        campoPassword.setBounds(188, 97, 172, 36);
        panel.add(etiqueta3);
        panel.add(campoPassword);

        JLabel etiqueta4 = new JLabel("JTextArea:");
        etiqueta4.setBounds(0, 141, 148, 36);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(188, 141, 172, 36);
        panel.add(etiqueta4);
        panel.add(scrollPane);
        
        areaTexto = new JTextArea();
        scrollPane.setViewportView(areaTexto); 
        
        JLabel etiqueta5 = new JLabel("JFormattedTextField:");
        etiqueta5.setBounds(0, 187, 141, 36);
        campoFormateado = new JFormattedTextField();
        campoFormateado.setBounds(188, 187, 172, 36);
        campoFormateado.setValue(12345.67);
        panel.add(etiqueta5);
        panel.add(campoFormateado);

        JLabel etiqueta6 = new JLabel("JSpinner:");
        etiqueta6.setBounds(0, 233, 141, 36);
        SpinnerNumberModel modeloSpinner = new SpinnerNumberModel(0, 0, 100, 1);
        spinner = new JSpinner(modeloSpinner);
        spinner.setBounds(188, 233, 172, 36);
        panel.add(etiqueta6);
        panel.add(spinner);

        JLabel etiqueta7 = new JLabel("JSlider:");
        etiqueta7.setBounds(0, 279, 148, 36);
        slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        slider.setBounds(188, 280, 178, 36);
        slider.setMajorTickSpacing(20);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        panel.add(etiqueta7);
        panel.add(slider);

        JLabel etiqueta8 = new JLabel("JComboBox:");
        etiqueta8.setBounds(0, 325, 148, 36);
        comboBox = new JComboBox<>(opciones);
        comboBox.setBounds(188, 325, 172, 36);
        panel.add(etiqueta8);
        panel.add(comboBox); 
        
        getContentPane().setLayout(null);
        getContentPane().add(panel);
        
        setVisible(true);
    }

    public static void main(String[] args) {
        Practica02_c ventana = new Practica02_c();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.Bsalir) {
            String cadena = " valor de Jtextfield " + this.campoTexto.getText().trim();
            
            cadena = cadena + "\n" + " valor de JPasswordField " + new String(this.campoPassword.getPassword()).trim();
            
            cadena = cadena + "\n" + " valor de Jtextarea " + this.areaTexto.getText();
            cadena = cadena + "\n" + " valor de Jformattedtextfiel " + this.campoFormateado.getText().trim();
            cadena = cadena + "\n" + " valor de spinner " + this.spinner.getValue().toString();
            cadena = cadena + "\n" + " valor de slidder " + String.valueOf(this.slider.getValue());
            
            if (comboBox.getSelectedIndex() > -1) {
                cadena = cadena + "\n" + " valor de combo es " + this.comboBox.getSelectedItem().toString();
            }

            JOptionPane.showMessageDialog(this, cadena);
            
            System.exit(0); 
        }
    }
}