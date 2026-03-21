package main;

import archivo.GestorJSON;
import controlador.ControladorProducto;
import modelo.GestionProductos;
import modelo.Producto;
import vista.VentanaPrincipal;
import java.util.ArrayList;

public class Principal {
    public static void main(String[] args) {
        VentanaPrincipal vista = new VentanaPrincipal();
        
        GestionProductos modelo = new GestionProductos();
        
        GestorJSON persistencia = new GestorJSON();
        
        // cargar datos de json al iniciar
        ArrayList<Producto> listaRecuperada = persistencia.importarJSON();
        for (Producto p : listaRecuperada) {
            modelo.insertar(p);
        }
        
        // unnir todo
        ControladorProducto controlador = new ControladorProducto(vista, modelo, persistencia);
        
        vista.setVisible(true);
    }
}