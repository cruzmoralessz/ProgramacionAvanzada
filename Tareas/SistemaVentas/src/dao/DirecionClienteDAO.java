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
import entidades.DireccionCliente;
import main.Acceso;

public class DirecionClienteDAO implements MetodosDao<DireccionCliente> {

    private final List<DireccionCliente> lista;
    private Metodos<DireccionCliente> metodos;
    private final String ruta = "direccionCliente.txt";
    private boolean resp;
    private DireccionCliente direccion;
    private final ClienteDAO DATOS;

    public DirecionClienteDAO() {
        lista = new ArrayList<>();
        metodos = new Metodos<>(lista);
        DATOS = new ClienteDAO();
        cargarLista();
    }

    private void cargarLista() {
        DireccionCliente direc;
        Cliente cliente;
        int rut;
        for (String dato : Acceso.cargarArchivo(ruta)) {
            direc = new DireccionCliente();
            StringTokenizer st = new StringTokenizer(dato, ",");

            rut = Integer.parseInt(st.nextToken());
            direc.setCalle(st.nextToken());
            direc.setNumero(st.nextToken());
            direc.setCiudad(st.nextToken());
            direc.setComuna(st.nextToken());

            cliente = DATOS.getObjeto(rut);
            direc.setCliente(cliente);

            metodos.agregarRegistro(direc);

        }
    }

//    public int buscaNombre(String nombre) {
//        for (int i = 0; i < metodos.cantidadRegistro(); i++) {
//            if (metodos.obtenerRegistro(i).getNombre().equalsIgnoreCase(nombre)) {
//                return i;
//            }
//        }
//        return -1;
//    }
    @Override
    public int buscaCodigo(int codigo) {
        for (int i = 0; i < metodos.cantidadRegistro(); i++) {
            if (codigo == metodos.obtenerRegistro(i).getCliente().getRut()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public List listar() {
        List<DireccionCliente> registros = new ArrayList<>();
        DireccionCliente direc;
        Cliente cliente;
        int rut;
        try {
            for (String dato : Acceso.cargarArchivo(ruta)) {
                direc = new DireccionCliente();
                StringTokenizer st = new StringTokenizer(dato, ",");

                rut = Integer.parseInt(st.nextToken());
                direc.setCalle(st.nextToken());
                direc.setNumero(st.nextToken());
                direc.setCiudad(st.nextToken());
                direc.setComuna(st.nextToken());

                cliente = DATOS.getObjeto(rut);
                direc.setCliente(cliente);

                registros.add(direc);

            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al listar DireccionClientes: " + e.getMessage());
        }
        return registros;
    }

    @Override
    public boolean insertar(DireccionCliente obj) {
        resp = false;
        PrintWriter pw;
        FileWriter fw;
        try {
            fw = new FileWriter("Archivos/" + ruta);
            pw = new PrintWriter(fw);

            obj = new DireccionCliente(obj.getCliente(), obj.getCalle(), obj.getNumero(), obj.getCiudad(), obj.getComuna());
            metodos.agregarRegistro(obj);

            for (int i = 0; i < metodos.cantidadRegistro(); i++) {

                direccion = metodos.obtenerRegistro(i);
                pw.println(String.valueOf(direccion.getCliente().getRut() + "," + direccion.getCalle() + "," + direccion.getNumero() + "," + direccion.getCiudad() + "," + direccion.getComuna()));

            }
            pw.close();
            resp = true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar DireccionClientes: " + e.getMessage());
        }
        return resp;
    }

    @Override
    public boolean actualizar(DireccionCliente obj) {
        resp = false;
        PrintWriter pw;
        FileWriter fw;
        try {
            fw = new FileWriter("Archivos/" + ruta);
            pw = new PrintWriter(fw);
            obj = new DireccionCliente(obj.getCliente(), obj.getCalle(), obj.getNumero(), obj.getCiudad(), obj.getComuna());
            int codigo = buscaCodigo(obj.getCliente().getRut());
            if (codigo == -1) {
                metodos.agregarRegistro(obj);
            } else {
                metodos.modificar(codigo, obj);
            }

            for (int i = 0; i < metodos.cantidadRegistro(); i++) {
                direccion = metodos.obtenerRegistro(i);
                pw.println(String.valueOf(direccion.getCliente().getRut() + "," + direccion.getCalle() + "," + direccion.getNumero() + "," + direccion.getCiudad() + "," + direccion.getComuna()));
            }
            pw.close();
            resp = true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar Clientes: " + e.getMessage());
        }
        return resp;
    }

    @Override
    public DireccionCliente getObjeto(int codigo) {
//        Cliente cliente = null;
//        for (int i = 0; i < metodos.cantidadRegistro(); i++) {
//            cliente = metodos.obtenerRegistro(i);
//            if (cliente.getRut() == codigo) {
//                return cliente;
//            }
//        }
//        return cliente;
        return null;
    }

}
