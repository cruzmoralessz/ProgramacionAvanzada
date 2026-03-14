package main;

import controlador.ControladorProducto;
import modelo.GestionProductos;
import vista.VentanaPrincipal;
import javax.swing.SwingUtilities;

import archivo.GestorCSV;

public class Principal {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GestorCSV gestorCSV = new GestorCSV();
            
            GestionProductos modelo = new GestionProductos();
            modelo.setListaProductos(gestorCSV.importarCSV());

            VentanaPrincipal vistaMDI = new VentanaPrincipal();

            // conectar todo
            new ControladorProducto(vistaMDI, modelo, gestorCSV);

            vistaMDI.setVisible(true);
        });
    }
}