package main;

import archivo.GestorJSON;
import controlador.ControladorProducto;
import controlador.ControladorProveedor;
import controlador.ControladorReportes;
import modelo.GestionProductos;
import modelo.GestionProveedores;
import modelo.Producto;
import modelo.Proveedor;
import vista.VentanaPrincipal;
import java.util.ArrayList;

public class Principal {
    public static void main(String[] args) {
        VentanaPrincipal vista = new VentanaPrincipal();
        
        GestionProductos modeloProd = new GestionProductos();
        GestionProveedores modeloProv = new GestionProveedores(); 
        
        GestorJSON persistencia = new GestorJSON();
        
        // cargar cosas del json
        ArrayList<Producto> listaRecuperada = persistencia.importarJSON();
        for (Producto p : listaRecuperada) {
            modeloProd.insertar(p);
        }
        
        ArrayList<Proveedor> proveedoresRecuperados = persistencia.importarProveedoresJSON();
        for (Proveedor prov : proveedoresRecuperados) {
            modeloProv.insertar(prov);
        }
        
        // unnir todo
        ControladorProducto controladorProd = new ControladorProducto(vista, modeloProd, persistencia);
        ControladorProveedor controladorProv = new ControladorProveedor(vista, modeloProv, persistencia);
        
        // instanciar
        ControladorReportes controladorReportes = new ControladorReportes(vista, modeloProd, modeloProv);
        
        vista.setVisible(true);
    }
}