package principal;

import controlador.ControladorPrincipal;
import controlador.GestorExcel;
import controlador.GestorJSON;
import vista.VistaPrincipal;

import com.formdev.flatlaf.FlatDarkLaf; //black

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        
        // flatlaf
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("ERROR inicializando flatlaf, usaremos el predeterminado");
        }

        SwingUtilities.invokeLater(() -> {
            
            // instanciar v y m
            VistaPrincipal vista = new VistaPrincipal();
            
            GestorExcel gestorExcel = new GestorExcel();
            GestorJSON gestorJSON = new GestorJSON();
            
            // unir todo
            ControladorPrincipal controlador = new ControladorPrincipal(vista, gestorExcel, gestorJSON);
            
            controlador.iniciar();
            vista.setVisible(true);
            
        });
    }
}