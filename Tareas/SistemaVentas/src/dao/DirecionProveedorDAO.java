package dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import dao.interfaces.MetodosDao;
import entidades.DireccionProveedor;
import entidades.Proveedor;
import main.Acceso;

public class DirecionProveedorDAO implements MetodosDao<DireccionProveedor> {

    private final List<DireccionProveedor> lista;
    private Metodos<DireccionProveedor> metodos;
    private final String ruta = "direccionProveedor.txt";
    private boolean resp;
    private DireccionProveedor direccion;
    private final ProveedorDAO DATOS;

    public DirecionProveedorDAO() {
        lista = new ArrayList<>();
        metodos = new Metodos<>(lista);
        DATOS = new ProveedorDAO();
        cargarLista();
    }

    private void cargarLista() {
        DireccionProveedor direc;
        Proveedor proveedor;
        int rut;
        for (String dato : Acceso.cargarArchivo(ruta)) {
            direc = new DireccionProveedor();
            StringTokenizer st = new StringTokenizer(dato, ",");

            rut = Integer.parseInt(st.nextToken());
            direc.setCalle(st.nextToken());
            direc.setNumero(st.nextToken());
            direc.setCiudad(st.nextToken());
            direc.setComuna(st.nextToken());

            proveedor = DATOS.getObjeto(rut);
            direc.setProveedor(proveedor);

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
            if (codigo == metodos.obtenerRegistro(i).getProveedor().getRut()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public List listar() {
        List<DireccionProveedor> registros = new ArrayList<>();
        DireccionProveedor direc;
        Proveedor proveedor;
        int rut;
        try {
            for (String dato : Acceso.cargarArchivo(ruta)) {
                direc = new DireccionProveedor();
                StringTokenizer st = new StringTokenizer(dato, ",");

                rut = Integer.parseInt(st.nextToken());
                direc.setCalle(st.nextToken());
                direc.setNumero(st.nextToken());
                direc.setCiudad(st.nextToken());
                direc.setComuna(st.nextToken());

                proveedor = DATOS.getObjeto(rut);
                direc.setProveedor(proveedor);

                registros.add(direc);

            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al listar DireccionProveedor: " + e.getMessage());
        }
        return registros;
    }

    @Override
    public boolean insertar(DireccionProveedor obj) {
        resp = false;
        PrintWriter pw;
        FileWriter fw;
        try {
            fw = new FileWriter("Archivos/" + ruta);
            pw = new PrintWriter(fw);

            obj = new DireccionProveedor(obj.getProveedor(), obj.getCalle(), obj.getNumero(), obj.getCiudad(), obj.getComuna());
            metodos.agregarRegistro(obj);

            for (int i = 0; i < metodos.cantidadRegistro(); i++) {

                direccion = metodos.obtenerRegistro(i);
                pw.println(String.valueOf(direccion.getProveedor().getRut() + "," + direccion.getCalle() + "," + direccion.getNumero() + "," + direccion.getCiudad() + "," + direccion.getComuna()));

            }
            pw.close();
            resp = true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar DireccionProveedor: " + e.getMessage());
        }
        return resp;
    }

    @Override
    public boolean actualizar(DireccionProveedor obj) {
        resp = false;
        PrintWriter pw;
        FileWriter fw;
        try {
            fw = new FileWriter("Archivos/" + ruta);
            pw = new PrintWriter(fw);
            obj = new DireccionProveedor(obj.getProveedor(), obj.getCalle(), obj.getNumero(), obj.getCiudad(), obj.getComuna());
            int codigo = buscaCodigo(obj.getProveedor().getRut());
            if (codigo == -1) {
                metodos.agregarRegistro(obj);
            } else {
                metodos.modificar(codigo, obj);
            }

            for (int i = 0; i < metodos.cantidadRegistro(); i++) {
                direccion = metodos.obtenerRegistro(i);
                pw.println(String.valueOf(direccion.getProveedor().getRut() + "," + direccion.getCalle() + "," + direccion.getNumero() + "," + direccion.getCiudad() + "," + direccion.getComuna()));
            }
            pw.close();
            resp = true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar Proveedor: " + e.getMessage());
        }
        return resp;
    }

    @Override
    public DireccionProveedor getObjeto(int codigo) {
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
