package dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import dao.interfaces.MetodosDao;
import entidades.Proveedor;
import main.Acceso;

public class ProveedorDAO implements MetodosDao<Proveedor> {

    private final List<Proveedor> lista;
    private final Metodos<Proveedor> metodos;
    private final String ruta = "proveedor.txt";
    private boolean resp;
    private Proveedor proveedor;

    public ProveedorDAO() {
        lista = new ArrayList<>();
        metodos = new Metodos<>(lista);
        cargarLista();
    }

    private void cargarLista() {
        Proveedor proveedor_cargar; //1, david
        for (String dato : Acceso.cargarArchivo(ruta)) {
            StringTokenizer st = new StringTokenizer(dato, ",");
            proveedor_cargar = new Proveedor(Integer.parseInt(st.nextToken()), st.nextToken(), st.nextToken(), st.nextToken());
            metodos.agregarRegistro(proveedor_cargar);
        }
    }

    public List seleccionar() {
        List<Proveedor> registros = new ArrayList<>();
        try {
            for (String dato : Acceso.cargarArchivo(ruta)) {
                StringTokenizer st = new StringTokenizer((dato), ",");
                registros.add(new Proveedor(Integer.parseInt(st.nextToken()), st.nextToken(), st.nextToken(), st.nextToken()));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error en Seleccionar proveedor: " + e.getMessage());
        }
        return registros;
    }

    public int buscaNombre(String nombre) {
        for (int i = 0; i < metodos.cantidadRegistro(); i++) {
            if (metodos.obtenerRegistro(i).getNombre().equalsIgnoreCase(nombre)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public List listar() {
        List<Proveedor> registros = new ArrayList<>();
        try {
            for (String dato : Acceso.cargarArchivo(ruta)) {
                StringTokenizer st = new StringTokenizer(dato, ",");
                proveedor = new Proveedor(Integer.parseInt(st.nextToken()), st.nextToken(), st.nextToken(), st.nextToken());
                registros.add(proveedor);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al listar Proveedores: " + e.getMessage());
        }
        return registros;
    }

    @Override
    public boolean insertar(Proveedor obj) {
        resp = false;
        PrintWriter pw;
        FileWriter fw;
        try {
            fw = new FileWriter("Archivos/" + ruta);
            pw = new PrintWriter(fw);
            obj = new Proveedor(obj.getRut(), obj.getNombre(), obj.getTelefono(), obj.getPaginaWeb());
            metodos.agregarRegistro(obj);
            for (int i = 0; i < metodos.cantidadRegistro(); i++) {
                proveedor = metodos.obtenerRegistro(i);
                pw.println(String.valueOf(proveedor.getRut() + "," + proveedor.getNombre() + "," + proveedor.getTelefono() + "," + obj.getPaginaWeb()));
            }
            pw.close();
            resp = true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar Proveedor: " + e.getMessage());
        }
        return resp;
    }

    @Override
    public boolean actualizar(Proveedor obj) {

        resp = false;
        PrintWriter pw;
        FileWriter fw;
        try {
            fw = new FileWriter("Archivos/" + ruta);
            pw = new PrintWriter(fw);
            obj = new Proveedor(obj.getRut(), obj.getNombre(), obj.getTelefono(), obj.getPaginaWeb());

            int codigo = buscaCodigo(obj.getRut());
            if (codigo == -1) {
                metodos.agregarRegistro(obj);
            } else {
                metodos.modificar(codigo, obj);
            }

            for (int i = 0; i < metodos.cantidadRegistro(); i++) {
                proveedor = metodos.obtenerRegistro(i);
                pw.println(String.valueOf(proveedor.getRut() + "," + proveedor.getNombre() + "," + proveedor.getTelefono() + "," + proveedor.getPaginaWeb()));
            }
            pw.close();
            resp = true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar Proveedor: " + e.getMessage());
        }
        return resp;
    }

    @Override
    public int buscaCodigo(int codigo) {
        for (int i = 0; i < metodos.cantidadRegistro(); i++) {
            if (codigo == metodos.obtenerRegistro(i).getRut()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Proveedor getObjeto(int codigo) {
        Proveedor proveedor_get = null;
        for (int i = 0; i < metodos.cantidadRegistro(); i++) {
            proveedor_get = metodos.obtenerRegistro(i);
            if (proveedor_get.getRut() == codigo) {
                return proveedor_get;
            }
        }
        return proveedor_get;
    }

}
