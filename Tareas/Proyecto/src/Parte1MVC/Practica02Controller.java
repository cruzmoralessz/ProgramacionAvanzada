package Parte1MVC;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Practica02Controller implements ActionListener {
    private Practica02View vista;
    private Practica02Model modelo;

    public Practica02Controller(Practica02View vista, Practica02Model modelo) {
        this.vista = vista;
        this.modelo = modelo;
        this.vista.setEscuchador(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.bSalir) {
            
            modelo.texto = vista.campoTexto.getText();
            modelo.password = new String(vista.campoPassword.getPassword());
            modelo.area = vista.areaTexto.getText();
            modelo.formateado = vista.campoFormateado.getText();
            modelo.spinner = (int) vista.spinner.getValue();
            modelo.slider = vista.slider.getValue();
            modelo.combo = (String) vista.comboBox.getSelectedItem();

            JOptionPane.showMessageDialog(vista, modelo.generarResumen());
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        Practica02View v = new Practica02View();
        Practica02Model m = new Practica02Model();
        new Practica02Controller(v, m);
        v.setVisible(true);
    }
}