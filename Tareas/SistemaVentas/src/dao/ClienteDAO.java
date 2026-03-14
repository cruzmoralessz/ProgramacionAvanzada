package dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import dao.interfaces.MetodosDao;
import entidades.Cliente;
import main.Acceso;

public class ClienteDAO implements MetodosDao<Cliente> {

    private final List<Cliente> lista;
    private final Metodos<Cliente> metodos;
    private final String ruta = "cliente.txt";
    private boolean resp;
    private Cliente cliente;

    public ClienteDAO() {
        lista = new ArrayList<>();
        metodos = new Metodos<>(lista);
        cargarLista();
    }

    private void cargarLista() {
        Cliente cliente_cargar; //1, david
        for (String dato : Acceso.cargarArchivo(ruta)) {
            StringTokenizer st = new StringTokenizer(dato, ",");
            cliente_cargar = new Cliente(Integer.parseInt(st.nextToken()), st.nextToken());
            metodos.agregarRegistro(cliente_cargar);
        }
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
        List<Cliente> registros = new ArrayList<>();
        try {
            for (String dato : Acceso.cargarArchivo(ruta)) {
                StringTokenizer st = new StringTokenizer(dato, ",");
                cliente = new Cliente(Integer.parseInt(st.nextToken()), st.nextToken());
                registros.add(cliente);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al listar Clientes: " + e.getMessage());
        }
        return registros;
    }

    @Override
    public boolean insertar(Cliente obj) {
        resp = false;
        PrintWriter pw;
        FileWriter fw;
        try {
            fw = new FileWriter("Archivos/" + ruta);
            pw = new PrintWriter(fw);
            obj = new Cliente(obj.getRut(), obj.getNombre());
            metodos.agregarRegistro(obj);
            for (int i = 0; i < metodos.cantidadRegistro(); i++) {
                cliente = metodos.obtenerRegistro(i);
                pw.println(String.valueOf(cliente.getRut() + "," + cliente.getNombre()));
            }
            pw.close();
            resp = true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar Clientes: " + e.getMessage());
        }
        return resp;
    }

    @Override
    public boolean actualizar(Cliente obj) {
        resp = false;
        PrintWriter pw;
        FileWriter fw;
        try {
            fw = new FileWriter("Archivos/" + ruta);
            pw = new PrintWriter(fw);
            obj = new Cliente(obj.getRut(), obj.getNombre());
            int codigo = buscaCodigo(obj.getRut());
            if (codigo == -1) {
                metodos.agregarRegistro(obj);
            } else {
                metodos.modificar(codigo, obj);
            }

            for (int i = 0; i < metodos.cantidadRegistro(); i++) {
                cliente = metodos.obtenerRegistro(i);
                pw.println(String.valueOf(cliente.getRut() + "," + cliente.getNombre()));
            }
            pw.close();
            resp = true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar Clientes: " + e.getMessage());
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
    public Cliente getObjeto(int codigo) {
        Cliente cliente = null;
        for (int i = 0; i < metodos.cantidadRegistro(); i++) {
            cliente = metodos.obtenerRegistro(i);
            if (cliente.getRut() == codigo) {
                return cliente;
            }
        }
        return cliente;
    }

    public List seleccionar() {
        List<Cliente> registros = new ArrayList<>();
        try {
            for (String dato : Acceso.cargarArchivo(ruta)) {
                StringTokenizer st = new StringTokenizer(dato, ",");
                registros.add(new Cliente(Integer.parseInt(st.nextToken()), st.nextToken()));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al seleccionar Cliente!");
        }
        return registros;
    }
}
